### [< Previous Challenge](./Challenge-01.md) - [**Home**](../README.md) - [Next Challenge >](./Challenge-03.md)

# Challenge-02 - Semantic Kernel Fundamentals

## Introduction

The first step in understanding Semantic Kernel is to become familiar with the basics. Semantic Kernel is a lightweight, open-source development kit designed to help developers build AI-infused applications using the latest AI models. It supports languages like JAVA, Python, and .NET, making it versatile for various development environments. Semantic Kernel provides a simple and consistent API for developers to interact with several different AI models, including GPT-3.5, GPT-4, Meta-Llama, DALL·E, and many more. You can use Semantic Kernel to build applications that can generate text, images, sound, and even code. Models can be hosted locally or in the cloud, and you can use the same API to interact with them.

## Description

In this challenge, you will be provided with a starter application that will require you to complete the implementation of the chat feature using Semantic Kernel and the Azure AI Foundry GPT-4o model. The application is a simple spring boot API based application that allows users to interact with the AI model by using REST API tool like POSTMAN by typing a question or prompt. The AI model will then respond with an answer or completion to the prompt. The application uses the Semantic Kernel framework to interact with the AI model. You will need to complete the implementation of the chat API to send the user's prompt to the AI model and return the response to the user.

## Prerequisites

1. Complete the [Getting Familiar With the Reference Application](./Resources/Supporting%20Challenges/Challenge-02-Reference-App.md) beginner guide.

## Challenges

### Implementation Steps

1. Deploy a GTP-4o model using  [Azure AI Foundry Studio](https://ai.azure.com) `https://ai.azure.com`. The **Deployment name** should be something similar to ``` gpt-4o ```. This name will be needed next when configuring Semantic Kernel. :exclamation: Deployment type should be **Standard**. :exclamation:
2. Open the terminal or powershell. Navigate to code checkout directory and open the project in your favorite IDE. For example, if you are using Visual Studio Code, run the following commands:  
    ```bash
        cd .\Springboot-SK-Sample\
        code .
    ```
4. If you are using Visual Studio code or IntelliJ, open terminal Copy the TEMP file as PROPERTIES file
    ```bash
        cp .\src\main\resources\application.properties.temp .\src\main\resources\application.properties
    ```
3. Update the *application.properties* file with the Azure AI Foundry *Deployment name*, *Endpoint URL* and the *API Key*. These values can be found in the Azure AI Foundry Studio.

    :bulb: **The endpoint URL should be in the format** ```https://<deployment-name>.openai.azure.com```

    ```bash
        spring.ai.azure.openai.chat.deployment-name: "Replace with your AOI deployment name",
        spring.ai.azure.openai.endpoint: "Replace with your AOI endpoint",
        spring.ai.azure.openai.api-key: "Replace with your AOI API key",
        spring.application.name: "Replace with your application name",
    ```

4. Build and compile the application using the following command:

    ```bash
        mvn clean install package -DskipTests=true
    ```
5. Run the Spring Boot application using the following command:

    ```bash
        mvn spring-boot:run
    ```
   :pushpin: **OPTIONAL** If server is not starting due to port already in use, you can change the port in the `application.properties` file. For example, change the port to 8081.
    
    ```bash
        server.port=8081
    ```
6. Locate the file name "chapter2.java(src/main/java/com/sk/chapters/chapter2.java).
7. Search code comment `//  Chat Completion Service` in the `chapter2.java` file.

    :bulb: [Retrieving chat completion services](https://learn.microsoft.com/en-us/semantic-kernel/concepts/ai-services/chat-completion/?tabs=csharp-AzureOpenAI%2Cpython-AzureOpenAI%2Cjava-AzureOpenAI&pivots=programming-language-java#retrieving-chat-completion-services)

    :pushpin:  The kernel itself doesn't expose AI functionality directly - instead, it manages services that do. When we want to send messages to the AI, we need to obtain a reference to the chat service specifically from the kernel's dependency injection container (kernel.Services). That's why we use GetRequiredService to get an IChatCompletionService from the kernel, which we'll use to handle our actual AI conversations.

### Implement SendMessage Method

1. Implement the `SendMessage` Method. Navigate to kernelUtil class and navigate to kernelBuilder method.

    ```Java
    public Kernel kernelBuilder(List<KernelPlugin> plugins) throws IOException {
        // Challenge 2 for Create the client
        
        
        
    ```
    ```Java
    public String SendMessage(String input) throws IOException, ServiceNotFoundException {

        Kernel kernel = kernelUtil.kernelBuilder(null);
        
        
    ```
    1. Add the **user's message** to the chat history collection.

        In the `kernelUtil.java` file, find the `kernelBuilder` method. Below the comment `//Challenge 2 

       :pushpin: The chat history maintains a record of the conversation between user and AI. By adding each message to this history, we give the AI context about the ongoing conversation, allowing it to provide more relevant and coherent responses.

       :bulb: For a detailed explanation of Chat History, please refer to the documentation [here](https://learn.microsoft.com/en-us/semantic-kernel/concepts/ai-services/chat-completion/?tabs=csharp-AzureOpenAI%2Cpython-AzureOpenAI%2Cjava-AzureOpenAI&pivots=programming-language-java#non-streaming-chat-completion).

    1. Use the Chat Completion service to complete the implementation of the `SendMessage` method by sending the entire chat history, including the latest prompt, to the Azure AI Foundry chat service. Once the service processes this and generates a response, wait until the full response is received before sending it back to the client.

       :bulb: Refer to the Semantic Kernel documentation [here](https://learn.microsoft.com/en-us/semantic-kernel/concepts/ai-services/chat-completion/?tabs=csharp-AzureOpenAI%2Cpython-AzureOpenAI%2Cjava-AzureOpenAI&pivots=programming-language-java#non-streaming-chat-completion) for an example of how to call the chat completion service.

    1. Now add the **AI's Response** *Content* to the Chat History collection.

        Once you have the response back from the chat service, you'll need to add the text of this response to the UI so your user can see it. We've already wired up the UI to read the data from the Chat History object that you've added the **user's message** to above. We can add the AI's response to this same collection as an **Assistant** message to ensure the user knows the message came from the AI.

       :pushpin: We add both user messages and AI responses to the same chat history collection, but with different roles (User vs Assistant). This maintains the conversation context for future AI responses.

       :bulb: To see how the ChatHistory object works in detail, see the documentation [here](https://learn.microsoft.com/en-us/semantic-kernel/concepts/ai-services/chat-completion/chat-history?pivots=programming-language-java#creating-a-chat-history-object)

### Testing

1. Run the application and test the chat completion by submitting the prompt:
2. Use Postman or any API services. http://localhost:8080/api/chapter2
3. Use POST method and add the following JSON body:

    ```json
    {
	    "message": "Why Sun is RED"
    }
    ```
    :bulb: For more information on the Semantic Kernel, refer to the documentation [here](https://learn.microsoft.com/en-us/semantic-kernel/concepts/kernel?pivots=programming-language-java).

1. Test the Chat History by submitting the following prompt without refreshing the browsing window and clearing the chat history.

    ```text
    Why is it red?
    ```

    If the chat history is working, the AI will understand the context of the next question  and provide a relevant response.

    If you refresh the browser window or click `New Chat`, the AI will not have the context of the previous question and will not provide a relevant response. So then you would have to provide the context again by asking the question:

    ```text
    Why is the sky red?
    ```

    :bulb: For more information on Chat History, refer to the documentation [here](https://learn.microsoft.com/en-us/semantic-kernel/concepts/ai-services/chat-completion/chat-history?pivots=programming-language-csharp).

1. Test the application with a variety of prompts to ensure it responds appropriately.

## Success Criteria

- Configuration
  - [ ] Deployed GPT model in Azure AI Foundry Studio
  - [ ] Added deployment name, endpoint URL, and API key to appsettings.json
- Kernel Setup
  - [ ] Registered chat completion service with kernel builder
  - [ ] Built kernel instance
  - [ ] Retrieved chat completion service using GetRequiredService
- Message Handling
  - [ ] Implemented adding user messages to chat history
  - [ ] Successfully calling chat completion service
  - [ ] Adding AI responses to chat history
- Testing
  - [ ] "Why is the sky blue" returns a coherent response
  - [ ] "Why is it red" demonstrates chat history is working
  - [ ] AI responds appropriately to follow-up questions


## Learning Resources

[Understanding the kernel](https://learn.microsoft.com/en-us/semantic-kernel/concepts/kernel?pivots=programming-language-csharp)

[Chat completion](https://learn.microsoft.com/en-us/semantic-kernel/concepts/ai-services/chat-completion/?tabs=csharp-AzureOpenAI%2Cpython-AzureOpenAI%2Cjava-AzureOpenAI&pivots=programming-language-csharp)

[Chat history](https://learn.microsoft.com/en-us/semantic-kernel/concepts/ai-services/chat-completion/chat-history?pivots=programming-language-csharp)

[What is a Planner?](https://learn.microsoft.com/en-us/semantic-kernel/concepts/planning?pivots=programming-language-csharp)

### [< Previous Challenge](./Challenge-01.md) - **[Home](../README.md)** - [Next Challenge >](./Challenge-03.md)
