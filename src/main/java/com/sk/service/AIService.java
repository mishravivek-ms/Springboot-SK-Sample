package com.sk.service;

import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import com.sk.chapters.chapter2;
import com.sk.chapters.chapter3;
import com.sk.chapters.chapter4;
import com.sk.chapters.chapter5;
import com.sk.chapters.mainChapter;

@Service
public class AIService {


    //private final AzureOpenAiChatModel chatModel;
    private final chapter2 chapter2;

    private final chapter3 chapter3;

    private final chapter4 chapter4;

    private final chapter5 chapter5;

    private final mainChapter mainChapter;


    @Autowired
    public AIService(chapter2 chapter2, chapter3 chapter3, chapter4 chapter4, chapter5 chapter5, mainChapter mainChapter) {
        //this.chatModel = chatModel;
        this.chapter2 = chapter2;
        this.chapter3 = chapter3;
        this.chapter4 = chapter4;
        this.chapter5 = chapter5;
        this.mainChapter = mainChapter;
    }


    public String extractChapter2Message(String message) throws IOException, ServiceNotFoundException {
        return chapter2.SendMessage(message);
    }

    public String extractChapter3Message(String message) throws IOException, ServiceNotFoundException {
        return chapter3.sendMessage(message);
    }

    public String extractChapter4Message(String message) throws IOException, ServiceNotFoundException {
        return chapter4.sendMessage(message);
    }

    public String extractChapter5Message(String message) throws IOException, ServiceNotFoundException {
        return chapter5.sendMessage(message);
    }

    public String chatResponse(String message) throws IOException, ServiceNotFoundException {
        return mainChapter.SendMessage(message);
    }
}
