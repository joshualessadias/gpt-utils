package com.joshua.dias.gptutils.csv.service;

import com.joshua.dias.gptutils.csv.model.CsvProcessingRequest;
import com.joshua.dias.gptutils.csv.model.CsvProcessingResponse;
import com.joshua.dias.gptutils.csv.model.PropertyDTO;
import com.joshua.dias.gptutils.zapi.service.ZApiService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Default implementation of the CsvProcessingService.
 * This service processes CSV documents, renames columns, maps to JSON, and filters data.
 */
@ApplicationScoped
public class DefaultCsvProcessingService implements CsvProcessingService {

    private static final Logger LOG = Logger.getLogger(DefaultCsvProcessingService.class);
    // Column mapping from original to new names
    private static final Map<Integer, String> COLUMN_MAPPING = new HashMap<>();

    static {
        COLUMN_MAPPING.put(0, "num_imovel");
        COLUMN_MAPPING.put(1, "uf");
        COLUMN_MAPPING.put(2, "cidade");
        COLUMN_MAPPING.put(3, "bairro");
        COLUMN_MAPPING.put(4, "endereco");
        COLUMN_MAPPING.put(5, "preco");
        COLUMN_MAPPING.put(6, "valor_avaliacao");
        COLUMN_MAPPING.put(7, "desconto");
        COLUMN_MAPPING.put(8, "descricao");
        COLUMN_MAPPING.put(9, "modalidade_venda");
        COLUMN_MAPPING.put(10, "link_acesso");
    }

    private final ZApiService zApiService;
    private ExecutorService executorService;

    /**
     * Constructor that injects dependencies.
     */
    @Inject
    public DefaultCsvProcessingService(ZApiService zApiService) {
        this.zApiService = zApiService;
    }

    /**
     * Initializes the thread pool.
     */
    @PostConstruct
    void init() {
        LOG.info("Initializing DefaultCsvProcessingService");

        // Create a thread pool with bounded queue
        this.executorService = new ThreadPoolExecutor(
                5, // core pool size
                10, // max pool size
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy() // If queue is full, caller thread executes the task
        );
    }

    /**
     * Shuts down the thread pool.
     */
    @PreDestroy
    void shutdown() {
        LOG.info("Shutting down DefaultCsvProcessingService");
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public CsvProcessingResponse process(CsvProcessingRequest request) {
        try {
            LOG.info("Processing CSV for phone: " + request.getPhoneNumber());

            // Download the CSV file
            String csvContent = downloadCsvFile(request.getDocumentUrl());

            // Process the CSV content
            List<PropertyDTO> propertyData = convertCsvToPropertyDTOs(csvContent);

            // Filter the data
            List<PropertyDTO> filteredData = filterData(propertyData);

            // Send notification with the filtered data
            sendNotification(request.getPhoneNumber(), filteredData);

            return new CsvProcessingResponse(request.getPhoneNumber(), filteredData);
        } catch (Exception e) {
            LOG.error("Error processing CSV: " + e.getMessage(), e);
            return new CsvProcessingResponse(request.getPhoneNumber(), "Error processing CSV: " + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<CsvProcessingResponse> processAsync(CsvProcessingRequest request) {
        LOG.info("Processing CSV asynchronously for phone: " + request.getPhoneNumber());
        return CompletableFuture.supplyAsync(() -> process(request), executorService);
    }

    /**
     * Downloads a CSV file from a URL.
     *
     * @param url The URL of the CSV file
     * @return The content of the CSV file as a string
     * @throws Exception If an error occurs during download
     */
    private String downloadCsvFile(String url) throws Exception {
        LOG.info("Downloading CSV file from URL: " + url);

        HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Converts CSV content to a list of PropertyDTO objects.
     * <p>
     * This method processes CSV data with the following format:
     * - First row: Title row with general information (e.g., "Lista de Imóveis da Caixa")
     * - Second row: Column headers (e.g., "Nº do imóvel;UF;Cidade;...")
     * - Subsequent rows: Data rows with property information
     * <p>
     * The CSV uses semicolons (;) as separators rather than commas.
     *
     * @param csvContent The CSV content as a string
     * @return A list of PropertyDTO objects representing the CSV data
     */
    private List<PropertyDTO> convertCsvToPropertyDTOs(String csvContent) {
        LOG.info("Converting CSV to PropertyDTO objects");

        List<PropertyDTO> propertyList = new ArrayList<>();
        String[] lines = csvContent.split("\n");

        // Skip the first two rows (title and header rows)
        int startLine = 4;

        for (int i = startLine; i < lines.length; i++) {
            String line = lines[i].trim();

            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }

            // Split by semicolons (the CSV uses ; as separator)
            String[] values = line.split(";");

            // Ensure we have enough values to process
            if (values.length < 2) {
                LOG.warn("Skipping line with insufficient data: " + line);
                continue;
            }

            PropertyDTO property = new PropertyDTO();

            // Map columns to PropertyDTO fields according to COLUMN_MAPPING
            for (int j = 0; j < values.length && j < COLUMN_MAPPING.size(); j++) {
                String columnName = COLUMN_MAPPING.get(j);
                String value = values[j].trim();

                // Set the appropriate field in the PropertyDTO
                switch (columnName) {
                    case "num_imovel":
                        property.setNumImovel(value);
                        break;
                    case "uf":
                        property.setUf(value);
                        break;
                    case "cidade":
                        property.setCidade(value);
                        break;
                    case "bairro":
                        property.setBairro(value);
                        break;
                    case "endereco":
                        property.setEndereco(value);
                        break;
                    case "preco":
                        property.setPreco(value);
                        break;
                    case "valor_avaliacao":
                        property.setValorAvaliacao(value);
                        break;
                    case "desconto":
                        property.setDesconto(value);
                        break;
                    case "descricao":
                        property.setDescricao(value);
                        break;
                    case "modalidade_venda":
                        property.setModalidadeVenda(value);
                        break;
                    case "link_acesso":
                        property.setLinkAcesso(value);
                        break;
                }
            }

            propertyList.add(property);
        }

        return propertyList;
    }

    /**
     * Filters the data based on specific criteria.
     *
     * @param propertyData The list of PropertyDTO objects to filter
     * @return The filtered data
     */
    private List<PropertyDTO> filterData(List<PropertyDTO> propertyData) {
        LOG.info("Filtering data");
        var res = new ArrayList<PropertyDTO>();

        var houses = propertyData.stream()
                .filter(property -> {
                    String cidade = property.getCidade();
                    String descricao = property.getDescricao();
                    return "MARINGA".equals(cidade) && descricao != null && descricao.contains("Casa");
                })
                .toList();
        var apartments = propertyData.stream()
                .filter(property -> {
                    String cidade = property.getCidade();
                    String descricao = property.getDescricao();
                    return "MARINGA".equals(cidade) && descricao != null && descricao.contains("Apartamento");
                })
                .toList();
        res.addAll(houses);
        res.addAll(apartments);
        return res;
    }

    /**
     * Sends a notification with the filtered data.
     *
     * @param phoneNumber  The phone number to send the notification to
     * @param filteredData The filtered data to include in the notification
     */
    private void sendNotification(String phoneNumber, List<PropertyDTO> filteredData) {
        LOG.info("Sending notification to phone: " + phoneNumber);

        if (filteredData.isEmpty()) {
            zApiService.sendMessage(phoneNumber, "Nenhum imóvel encontrado com os critérios especificados.");
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("*Imóveis encontrados:*\n\n");

        for (PropertyDTO property : filteredData) {
            var tipo = nonNull(property.getDescricao()) && !property.getDescricao().isEmpty() ?
                    property.getDescricao().split(",")[0] : "N/A";
            message.append("*").append(tipo).append("*\n");
            message.append("    Número do Imóvel: ").append(property.getNumImovel()).append("\n");
            message.append("    Cidade: ").append(property.getCidade()).append("\n");
            message.append("    *Bairro:* ").append(property.getBairro()).append("\n");
            message.append("    📍 *Endereço:* ").append(property.getEndereco()).append("\n");
            message.append("    💳 *Preço:* ").append(property.getPreco()).append("\n");
            message.append("    💰 *Valor de Avaliação:* ").append(property.getValorAvaliacao()).append("\n");
            message.append("    Desconto: ").append(property.getDesconto()).append("\n");
            message.append("    Descrição: ").append(property.getDescricao()).append("\n");
            message.append("    Modalidade de Venda: ").append(property.getModalidadeVenda()).append("\n");
            message.append("    Link de Acesso: ").append(property.getLinkAcesso()).append("\n\n");
            message.append("----------------------------\n\n");
        }

        zApiService.sendMessage(phoneNumber, message.toString());
    }
}