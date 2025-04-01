package com.sk.controller;

import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.sk.model.MessageRequest;
import com.sk.service.AIService;
import jakarta.servlet.http.HttpSession;
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
		System.out.println("Message: " + message.getMessage());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(aiService.extractChapter2Message(message.getMessage().trim()));
	}

	@PostMapping("/reset-session")
	public String resetSession(HttpSession session) {
		System.out.println("Session reset-> new session id: " + session.getId());
		session.invalidate();  // ✅ This will destroy the existing session
		return "Session reset successfully";
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

	@PostMapping("/skChat")
	public ResponseEntity<String> getskChat(@RequestBody MessageRequest message) throws IOException, ServiceNotFoundException {
		System.out.println("Message: " + message.getMessage());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(aiService.chatResponse(message.getMessage().trim()));
	}
}
