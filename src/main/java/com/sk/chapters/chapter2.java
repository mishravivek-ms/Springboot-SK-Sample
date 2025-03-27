package com.sk.chapters;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import com.sk.kernel.kernelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

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
@SessionScope
public class chapter2 {

    private final kernelUtil kernelUtil;
    private final ChatHistory chatHistory;

    @Autowired
    public chapter2(kernelUtil kernelUtil) {
        this.kernelUtil = kernelUtil;
        this.chatHistory = new ChatHistory();
    }

    public String SendMessage(String input) throws IOException, ServiceNotFoundException {

        Kernel kernel = kernelUtil.kernelBuilderWithoutPlugin();

        // Challenge 2 for Create chatCompletionService
        ChatCompletionService chatCompletionService = kernel.getService(ChatCompletionService.class);

        // Add user message to chat history
        chatHistory.addUserMessage(input);

        InvocationContext optionalInvocationContext = null;

        List<ChatMessageContent<?>> response = chatCompletionService.getChatMessageContentsAsync(
                chatHistory,
                kernel,
                optionalInvocationContext
        ).block();

        if (response == null || response.isEmpty()) {
            throw new ServiceNotFoundException("No response from the service");
        }

        // Add AI response to chat history
        chatHistory.addAssistantMessage(response.get(0).getContent());

        return response.get(0).getContent();
    }
}