# CSV Processing Tool

This document describes the CSV processing tool that has been added to the project.

## Overview

The CSV processing tool is designed to:
1. Receive a CSV document via WhatsApp
2. Answer the webhook immediately
3. Process the CSV document asynchronously
4. Rename columns to a specific format
5. Map the CSV data to JSON
6. Filter the data based on specific criteria
7. Format the output using WhatsApp text formatting
8. Send the filtered data back to the user

## Implementation Details

### New Classes

1. **DocumentDTO**: Represents a document attachment in a WhatsApp message
   - Location: `com.joshua.dias.gptutils.message.model.DocumentDTO`
   - Purpose: Stores document metadata (URL, MIME type, file name, size)

2. **CsvProcessingRequest**: Represents a request to process a CSV document
   - Location: `com.joshua.dias.gptutils.csv.model.CsvProcessingRequest`
   - Purpose: Stores request parameters (phone number, document URL, message ID)

3. **CsvProcessingResponse**: Represents the response from CSV processing
   - Location: `com.joshua.dias.gptutils.csv.model.CsvProcessingResponse`
   - Purpose: Stores processing results (filtered data, success status, message)

4. **CsvProcessingService**: Interface for CSV processing
   - Location: `com.joshua.dias.gptutils.csv.service.CsvProcessingService`
   - Purpose: Defines the contract for CSV processing services

5. **DefaultCsvProcessingService**: Implementation of CsvProcessingService
   - Location: `com.joshua.dias.gptutils.csv.service.DefaultCsvProcessingService`
   - Purpose: Implements CSV processing logic (download, parse, filter, notify)

6. **CsvProcessingTool**: Implementation of the Tool interface for CSV processing
   - Location: `com.joshua.dias.gptutils.csv.service.CsvProcessingTool`
   - Purpose: Integrates CSV processing with the orchestration system

### Modified Classes

1. **ReceiveMessageDTO**: Added document field
   - Location: `com.joshua.dias.gptutils.message.model.ReceiveMessageDTO`
   - Changes: Added DocumentDTO field and getter/setter methods

2. **MessageResource**: Updated to handle document attachments
   - Location: `com.joshua.dias.gptutils.message.resource.MessageResource`
   - Changes: 
     - Added support for document content detection
     - Renamed `audioUrl` to `contentUrl` and added `contentType` variable
     - Updated parameters map to include content type

3. **DefaultPhoneToolMappingService**: Added mapping for CSV processing tool
   - Location: `com.joshua.dias.gptutils.message.service.DefaultPhoneToolMappingService`
   - Changes:
     - Added constants for CSV processing phone and tool
     - Added mapping for CSV processing tool

## CSV Processing Logic

The CSV processing logic is implemented in the `DefaultCsvProcessingService` class:

1. **Download**: Downloads the CSV file from the provided URL
2. **Parse**: Parses the CSV content and maps it to a list of JSON objects
3. **Rename Columns**: Renames columns to the specified format:
   - `num_imovel`
   - `uf`
   - `cidade`
   - `bairro`
   - `endereco`
   - `preco`
   - `valor_avaliacao`
   - `desconto`
   - `descricao`
   - `modalidade_venda`
   - `link_acesso`
4. **Filter**: Filters the data based on the following criteria:
   - `modalidade_venda` contains "Leilão"
   - `cidade` equals "MARINGA"
   - `descricao` contains "Casa"
5. **Format**: Formats the filtered data using WhatsApp text formatting:
   - Bold text using asterisks (`*text*`)
   - Line breaks using newlines (`\n`)
   - Separators using dashes (`----------------------------`)
6. **Notify**: Sends the formatted data back to the user via WhatsApp

## Usage

To use the CSV processing tool:

1. Send a CSV document to the WhatsApp number configured for CSV processing (120363419403036759-group)
2. The system will immediately acknowledge receipt of the document
3. The system will process the document asynchronously
4. Once processing is complete, the system will send the filtered data back to the user

## WhatsApp Text Formatting

The tool uses the following WhatsApp text formatting:

- **Bold**: `*text*`
- **Line Breaks**: `\n`
- **Separators**: `----------------------------`

This formatting makes the output more readable and user-friendly.