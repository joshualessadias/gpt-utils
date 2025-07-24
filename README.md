# GPT Utils - Message Receiving Service

A Java-based service for receiving and processing messages with audio content. This project provides a message receiving endpoint that validates incoming messages and processes audio content using OpenAI's whisper-1 model and enhanced post-processing with GPT-4.1 Nano.

## Features

- Message receiving endpoint with validation
- Audio processing using OpenAI's whisper-1 model
- Transcription processing using GPT-4.1 Nano via LangChain4j
- Notification system using Z-API for sending processed results
- Domain-based architecture for extensibility

## Quick Start

1. **Prerequisites**: Ensure you have Java 21+ and Gradle installed

2. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/gpt-utils.git
   cd gpt-utils
   ```

3. **Set your OpenAI API key**:
   ```bash
   export OPENAI_API_KEY=your-api-key-here
   ```

4. **Set your Z-API credentials** (if using the notification system):
   ```bash
   export ZAPI_INSTANCE_ID=your-instance-id
   export ZAPI_INSTANCE_TOKEN=your-instance-token
   export ZAPI_CLIENT_TOKEN=your-client-token
   ```

5. **Run the application**:
   ```bash
   ./gradlew quarkusDev
   ```

The application will be available at http://localhost:8912.

## Configuration

Set your OpenAI API key in one of the following ways:

1. Environment variable:
   ```bash
   export OPENAI_API_KEY=your-api-key-here
   ```

2. Update `application.yml`:
   ```yaml
   openai:
     api-key: your-api-key-here
     model: whisper-1
     timeout: 30
   ```

For Z-API integration (used for sending notifications), configure:

```yaml
zapi:
  instance-id: your-instance-id
  instance-token: your-instance-token
  client-token: your-client-token
```

## Running the Application

You can run the application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

The application will be available at http://localhost:8912.

## API Documentation

### Message Endpoint

- **POST /api/messages/receive**: Receive and process messages with audio content

## Technologies Used

- Java 21: Modern Java features for robust development
- Quarkus: Lightweight Java framework for cloud-native applications
- RESTEasy: JAX-RS implementation for RESTful services
- OpenAI API: For whisper-1 model integration
- Jackson: For JSON processing
- Z-API: For sending notifications via messaging platforms

## License

This project is licensed under the MIT License - see the LICENSE file for details.