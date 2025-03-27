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
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Component;
        import org.springframework.web.client.RestTemplate;
        import org.springframework.web.context.annotation.SessionScope;

        import java.io.IOException;
        import java.nio.file.Path;
        import java.util.List;

        @Component
        @SessionScope
        public class chapter5 {

            private final kernelUtil kernelUtil;
            private final AzureAIConfig azureAIConfig;
            private final RestTemplate restTemplate;

            @Autowired
            public chapter5(kernelUtil kernelUtil, AzureAIConfig azureAIConfig, RestTemplate restTemplate) {
                this.kernelUtil = kernelUtil;
                this.azureAIConfig = azureAIConfig;
                this.restTemplate = restTemplate;
            }

            public String sendMessage(String input) {
                try {
                    // Challenge 03 for Create the DateTimePlugin
                    KernelPlugin foodplugin = KernelPluginFactory
                            .importPluginFromDirectory(Path.of("src/main/resources"),
                                    "promptconfig", null);

                    Kernel kernel = kernelUtil.kernelBuilder(List.of(foodplugin));

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
                } catch (IOException e) {
                    return "Error: I/O exception occurred - " + e.getMessage();
                } catch (ServiceNotFoundException e) {
                    return "Error: Service not found - " + e.getMessage();
                } catch (Exception e) {
                    return "Error: An unexpected error occurred - " + e.getMessage();
                }
            }
        }