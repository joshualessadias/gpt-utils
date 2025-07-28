package com.joshua.dias.gptutils.csv.model;

/**
 * Data Transfer Object (DTO) representing a property from the CSV data.
 * This class replaces the generic Map<String, Object> approach with a strongly-typed object.
 */
public class PropertyDTO {
    private String numImovel;
    private String uf;
    private String cidade;
    private String bairro;
    private String endereco;
    private String preco;
    private String valorAvaliacao;
    private String desconto;
    private String descricao;
    private String modalidadeVenda;
    private String linkAcesso;

    // Default constructor
    public PropertyDTO() {
    }

    // Getters and setters
    public String getNumImovel() {
        return numImovel;
    }

    public void setNumImovel(String numImovel) {
        this.numImovel = numImovel;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getValorAvaliacao() {
        return valorAvaliacao;
    }

    public void setValorAvaliacao(String valorAvaliacao) {
        this.valorAvaliacao = valorAvaliacao;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getModalidadeVenda() {
        return modalidadeVenda;
    }

    public void setModalidadeVenda(String modalidadeVenda) {
        this.modalidadeVenda = modalidadeVenda;
    }

    public String getLinkAcesso() {
        return linkAcesso;
    }

    public void setLinkAcesso(String linkAcesso) {
        this.linkAcesso = linkAcesso;
    }
}