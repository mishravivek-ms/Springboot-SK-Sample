package com.sk.controller;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import com.sk.config.AzureAIConfig;
import com.sk.kernel.kernelUtil;
import com.sk.model.ChatRequest;
import com.sk.model.Message;
import com.sk.plugins.AISearchPlugin;
import com.sk.plugins.DateTimePlugin;
import com.sk.plugins.GeocodingPlugin;
import com.sk.plugins.WeatherPlugin;
import com.sk.service.AIService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AIController {

    @Autowired
    AIService aiService;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AzureAIConfig config;

    @Autowired
    kernelUtil kernelUtil;

    @GetMapping("/hello")
    public String getAI() {
        return "AI";
    }

    @PostMapping("/chapter2")
    public ResponseEntity<String> getchapter2(@RequestBody Message message) throws IOException, ServiceNotFoundException {
        System.out.println("Message: " + message.getContent());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(aiService.extractChapter2Message(message.getContent().trim()));
    }

    @PostMapping("/reset-session")
    public String resetSession(HttpSession session) {
        System.out.println("Session reset-> new session id: " + session.getId());
        session.invalidate();  // ✅ This will destroy the existing session
        return "Session reset successfully";
    }

    @PostMapping("/chapter3")
    public ResponseEntity<String> getchapter3(@RequestBody Message message) throws IOException, ServiceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(aiService.extractChapter3Message(message.getContent().trim()));
    }

    @PostMapping("/chapter4")
    public ResponseEntity<String> getchapter4(@RequestBody Message message) throws IOException, ServiceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(aiService.extractChapter4Message(message.getContent().trim()));
    }

    @PostMapping("/chapter5")
    public ResponseEntity<String> getchapter5(@RequestBody Message message) throws IOException, ServiceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(aiService.extractChapter5Message(message.getContent().trim()));
    }

    @PostMapping("/skChat")
    public ResponseEntity<List<ChatMessageContent<?>>> getskChat(@RequestBody ChatRequest chatRequest) throws IOException, ServiceNotFoundException {

        Kernel kernel = kernelBuilder();

        // challange 3, for adding the plugins
        kernel = AddPlugins(kernel);



		/* Challenge 2 for Create chatCompletionService
		 Initialize a new ChatHistory object to store the conversation history.
		 Retrieve the ChatCompletionService from the kernel to handle chat completions.
		 Iterate through the list of user messages from the ChatRequest object
		 and add each message to the chat history as a user message.
		*/
        ChatHistory chatHistory = new ChatHistory();
        ChatCompletionService chatCompletionService = kernel.getService(ChatCompletionService.class);
        for (ChatRequest.Message message : chatRequest.getMessages()) {
            chatHistory.addUserMessage(message.getContent());
        }

        InvocationContext invocationContext = null;

		/* Challenge 03 for Create the InvocationContext
		 Build an InvocationContext object to configure the behavior and return mode
		 for the invocation of the ChatCompletionService. This configuration allows
		 all kernel functions to be called and ensures the full conversation history
		 is returned as part of the response.*/
        invocationContext = InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true)) // Allow unrestricted kernel function calls
                .withReturnMode(InvocationReturnMode.FULL_HISTORY) // Return the full conversation history
                .build();

		/*
		 Challenge 2 for make the call to the chatCompletionService
		 Retrieve the list of chat message contents asynchronously from the ChatCompletionService.
		 This method takes the chat history, kernel, and invocation context as inputs and blocks
		 until the response is received. The response contains the AI-generated messages.
		*/
        List<ChatMessageContent<?>> responses = chatCompletionService.getChatMessageContentsAsync(
                chatHistory,
                kernel,
                invocationContext
        ).block();

        if (responses == null || responses.isEmpty()) {
            throw new ServiceNotFoundException("No response from the service");
        }
        // Add AI response to chat history
        chatHistory.addAll(responses);
        return ResponseEntity.ok(responses);
    }


	/*
	 * Adds plugins to the Kernel instance.
	 *
	 * This method creates and configures various plugins, including:
	 * - DateTimePlugin: Provides date and time-related functionalities.
	 * - GeocodingPlugin: Handles geocoding operations using the provided configuration and RestTemplate.
	 * - WeatherPlugin: Fetches weather-related data using the provided configuration and RestTemplate.
	 * - AISearchPlugin: Enables AI-based search functionalities using the provided configuration and kernel utility.
	 *
	 * Optionally, a food plugin can be added by uncommenting the relevant code.
	 *
	 * The method then builds a Kernel instance with the configured plugins and returns it.
	 *
	 * @return A Kernel instance with the configured plugins.
	 * @throws IOException If an error occurs during plugin creation or kernel building.
	 */
    private Kernel AddPlugins(Kernel kernel) throws IOException {
        // Challenge 03 START for Create the DateTimePlugin
        KernelPlugin dateTimePlugin = KernelPluginFactory
                .createFromObject(new DateTimePlugin(), "DateTimePlugin");

        KernelPlugin geoPlugin = KernelPluginFactory
                .createFromObject(new GeocodingPlugin(config, restTemplate), "GeocodingPlugin");

        KernelPlugin weatherPlugin = KernelPluginFactory
                .createFromObject(new WeatherPlugin(config, restTemplate), "WeatherPlugin");

        // Challenge 04, Uncomment bellow line for AI search plugin and add the plugin to the list
        KernelPlugin AISearch = KernelPluginFactory
                .createFromObject(new AISearchPlugin(config, kernelUtil), "AISearchPlugin");

        // Challenge 05, Uncomment bellow line for food plugin and add the plugin to the list
        KernelPlugin foodplugin = KernelPluginFactory
                .importPluginFromDirectory(Path.of("src/main/resources"),
                        "promptconfig", null);

        kernel = addPluginwithKernel(kernel,List.of(dateTimePlugin, geoPlugin, weatherPlugin, AISearch,foodplugin));

        return kernel;
    }

    public Kernel addPluginwithKernel(Kernel kernel, List<KernelPlugin> plugins) throws IOException {

        Kernel.Builder kernelBuilder = kernel.toBuilder();
        if (plugins != null) {
            for (KernelPlugin plugin : plugins) {
                kernelBuilder.withPlugin(plugin);
            }
        }
        return kernelBuilder.build();


    }

    public Kernel kernelBuilder() throws IOException {

        // Challenge 2 for Create the client
		/*
		 Create an OpenAIAsyncClient instance using the OpenAIClientBuilder.
		 This client is configured with the API key and endpoint from the AzureAIConfig.
		 It is used to interact with OpenAI services asynchronously.
		*/
        OpenAIAsyncClient clientasync = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(config.getOpenAiApiKey()))
                .endpoint(config.getOpenAiEndpoint())
                .buildAsyncClient();

        // Challenge 2 Create the chat completion service
		/*
		 Create a ChatCompletionService instance using the OpenAIChatCompletion builder.
		 This service is configured with the OpenAIAsyncClient and the model ID from the AzureAIConfig.
		 It is used to handle chat completions.
		*/
        ChatCompletionService chatCompletion = OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(clientasync)
                .withModelId(config.getChatDeploymentName())
                .build();

        Kernel kernel = null;
		/*
		 Build a Kernel instance using the Kernel builder.
		 Configure it with the ChatCompletionService and finalize the build process.
		*/
        kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletion)
                .build();

        return kernel;

    }
}
