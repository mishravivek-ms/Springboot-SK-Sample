package com.sk.chapters;

import com.microsoft.semantickernel.Kernel;
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

import java.io.IOException;
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
public class chapter4 {

    private final kernelUtil kernelUtil;
    private final AzureAIConfig azureAIConfig;

    private final RestTemplate restTemplate;

    @Autowired
    public chapter4(kernelUtil kernelUtil, AzureAIConfig azureAIConfig, RestTemplate restTemplate) {
        this.kernelUtil = kernelUtil;
        this.azureAIConfig = azureAIConfig;
        this.restTemplate = restTemplate;
    }


    public String sendMessage(String input) throws IOException, ServiceNotFoundException {

        // Challenge 03 for Create the DateTimePlugin
        KernelPlugin AISearch = KernelPluginFactory
                .createFromObject(new AISearchPlugin(azureAIConfig,kernelUtil), "AISearchPlugin");



        Kernel kernel = kernelUtil.kernelBuilder(List.of(AISearch));

        // Challenge 03 for Create the InvocationContext
        InvocationContext invocationContext = InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .build();

        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addUserMessage(input);
        ChatCompletionService chatCompletionService = kernel.getService(ChatCompletionService.class);

        // Invoke the chat completion service with the kernel and invocation context
        List<ChatMessageContent<?>> response = chatCompletionService.getChatMessageContentsAsync(
                chatHistory,
                kernel,
                invocationContext
        ).block();

        if (response == null || response.isEmpty()) {
            throw new ServiceNotFoundException("No response from the service");
        }
        return response.get(0).getContent();
    }
}
