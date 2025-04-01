package com.sk.chapters;

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
import com.sk.plugins.AISearchPlugin;
import com.sk.plugins.DateTimePlugin;
import com.sk.plugins.GeocodingPlugin;
import com.sk.plugins.WeatherPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Executes an OpenAI function with the given input.
 *
 * @param
 * @return The result of the OpenAI function execution as a string.
 * @throws IOException If an I/O error occurs.
 * @throws ServiceNotFoundException If the specified service is not found.
 */
@Component
@SessionScope
public class mainChapter {

    private final kernelUtil kernelUtil;
    private final ChatHistory chatHistory;
   private final AzureAIConfig config;
   private final RestTemplate restTemplate;

    @Autowired
    public mainChapter(kernelUtil kernelUtil, AzureAIConfig config, RestTemplate restTemplate) {
        this.kernelUtil = kernelUtil;
        this.chatHistory = new ChatHistory();
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public String SendMessage(String input) throws IOException, ServiceNotFoundException {

        //For Chapter 2, Uncomment the below line
        //Kernel kernel =kernelBuilder(null);

        //For Chapter 3, comment the previous line and Uncomment the below line
        Kernel kernel =AddPlugins();

        // Challenge 2 for Create chatCompletionService
        ChatCompletionService chatCompletionService = kernel.getService(ChatCompletionService.class);

        // Add user message to chat history
        chatHistory.addUserMessage(input);


        // Challenge 03 for Create the InvocationContext
        InvocationContext invocationContext = InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .build();

        List<ChatMessageContent<?>> response = chatCompletionService.getChatMessageContentsAsync(
                chatHistory,
                kernel,
                invocationContext
        ).block();

        if (response == null || response.isEmpty()) {
            throw new ServiceNotFoundException("No response from the service");
        }

        // Add AI response to chat history
        chatHistory.addAssistantMessage(response.get(0).getContent());

        return response.get(0).getContent();
    }

    public Kernel kernelBuilder(List<KernelPlugin> plugins) throws IOException {

        OpenAIAsyncClient clientasync = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(config.getOpenAiApiKey()))
                .endpoint(config.getOpenAiEndpoint())
                .buildAsyncClient();


        ChatCompletionService chatCompletion = OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(clientasync)
                .withModelId(config.getChatDeploymentName())
                .build();

        Kernel kernel = null;

        // Initialize the kernel with the chat completion service and the plugins
        Kernel.Builder kernelBuilder = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletion);
        if(plugins!=null) {
            for (KernelPlugin plugin : plugins) {
                kernelBuilder.withPlugin(plugin);
            }
        }
        return kernelBuilder.build();


    }

    private Kernel AddPlugins() throws IOException {
        // Challenge 03 START for Create the DateTimePlugin
        KernelPlugin dateTimePlugin = KernelPluginFactory
                .createFromObject(new DateTimePlugin(), "DateTimePlugin");

        KernelPlugin geoPlugin = KernelPluginFactory
                .createFromObject(new GeocodingPlugin(config,restTemplate), "GeocodingPlugin");

        KernelPlugin weatherPlugin = KernelPluginFactory
                .createFromObject(new WeatherPlugin(config,restTemplate), "WeatherPlugin");

        // Challenge 04, Uncomment bellow line for AI search plugin and add the plugin to the list
        KernelPlugin AISearch = KernelPluginFactory
                .createFromObject(new AISearchPlugin(config,kernelUtil), "AISearchPlugin");

        // Challenge 05, Uncomment bellow line for food plugin and add the plugin to the list
        KernelPlugin foodplugin = KernelPluginFactory
                .importPluginFromDirectory(Path.of("src/main/resources"),
                        "promptconfig", null);

        Kernel kernel = kernelBuilder(List.of(dateTimePlugin,geoPlugin,weatherPlugin,AISearch,foodplugin));
        // Challenge 03 END
        return kernel;
    }
}