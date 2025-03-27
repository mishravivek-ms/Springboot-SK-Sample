package com.sk.controller;

import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.sk.model.MessageRequest;
import com.sk.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AIController {

	@Autowired
	AIService aiService;
	//Create sample RESR API method
	@GetMapping("/hello")
	public String getAI() {
		return "AI";
	}

	@PostMapping("/chapter2")
	public ResponseEntity<String> getchapter2(@RequestBody MessageRequest message) throws IOException, ServiceNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(aiService.extractChapter2Message(message.getMessage().trim()));
	}

	@PostMapping("/chapter3")
	public ResponseEntity<String> getchapter3(@RequestBody MessageRequest message) throws IOException, ServiceNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(aiService.extractChapter3Message(message.getMessage().trim()));
	}

	@PostMapping("/chapter4")
	public ResponseEntity<String> getchapter4(@RequestBody MessageRequest message) throws IOException, ServiceNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(aiService.extractChapter4Message(message.getMessage().trim()));
	}

	@PostMapping("/chapter5")
	public ResponseEntity<String> getchapter5(@RequestBody MessageRequest message) throws IOException, ServiceNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(aiService.extractChapter5Message(message.getMessage().trim()));
	}
}
