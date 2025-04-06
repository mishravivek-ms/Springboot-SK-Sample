package com.sk.controller;

import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.sk.model.ChatRequest;
import com.sk.model.Message;
import com.sk.service.AIService;
import com.sk.model.Response;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public ResponseEntity<List<Response>> getskChat(@RequestBody ChatRequest chatRequest) throws IOException, ServiceNotFoundException {
		System.out.println("Message: " + chatRequest.getMessages().get(0).getContent());

		List<Response> responselist = new ArrayList<>();
		Response response = new Response();

		// Setting Role
		/*Response.Role role = new Response.Role();
		role.setLabel("Assistant");
		response.setRole(role);*/

		// Setting Items
		Response.Item item = new Response.Item();
		//item.set$type("TextContent");
		item.setText(aiService.chatResponse(chatRequest.getMessages().get(0).getContent()));
		response.setItems(List.of(item));

		// Setting ModelId
		//response.setModelId("gpt-4o");


		// Setting Metadata

		/*Response.Metadata metadata = new Response.Metadata();
		metadata.setId("chatcmpl-BI4EcluQGKRmOnQTODKmTDVe91gDD");
		metadata.setCreatedAt("2025-04-03T02:04:42+00:00");
		metadata.setSystemFingerprint("fp_ded0d14823");

		Response.Usage usage = new Response.Usage();
		usage.setOutputTokenCount(11);
		usage.setInputTokenCount(655);
		usage.setTotalTokenCount(666);
		usage.setOutputTokenDetails(Map.of(
			"ReasoningTokenCount", 0,
			"AudioTokenCount", 0,
			"AcceptedPredictionTokenCount", 0,
			"RejectedPredictionTokenCount", 0
		));
		usage.setInputTokenDetails(Map.of(
			"AudioTokenCount", 0,
			"CachedTokenCount", 0
		));
		metadata.setUsage(usage);

		metadata.setRefusal(null);
		metadata.setFinishReason("Stop");
		metadata.setContentTokenLogProbabilities(List.of());

		response.setMetadata(metadata);*/
		responselist.add(response);
		System.out.println("Response: " + response.getItems().toString());
		return ResponseEntity.ok(responselist);
	}
}
