package com.powerlogix.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.powerlogix.models.ReturnMessage;
import com.powerlogix.models.Websocket;
import com.powerlogix.repo.WebsocketRepository;

@RestController
public class WebsocketController 
{
	
	private final WebsocketRepository websocketRepository;
	private Set<String> idtags = new HashSet<>();

	@Autowired
	public WebsocketController(WebsocketRepository websocketRepository) {
		this.websocketRepository = websocketRepository;
	}

	   @MessageMapping("/message")
	    @SendTo("/topic/return-to")
	    public ReturnMessage  saveUserData(@RequestBody Websocket websocket) 
	    {
	    	ReturnMessage returnMessage = new ReturnMessage();
	        String idtag = websocket.getIdtag();

	        if (websocketRepository.existsByIdTag(idtag)) {
	            // ID Tag already exists in the database, add a custom error message
	            returnMessage.setError("ID Tag already exists !");
	            return returnMessage;
	        }

	        websocketRepository.save(websocket); // Save the user if the ID Tag does not exist

	        // Return a success message
	        returnMessage.setMessage("User data saved successfully.");
	        return returnMessage;
	    }

	@MessageMapping("/getData")
	@SendTo("/topic/userData")
	public List<Websocket> getData() {
		return websocketRepository.findAll();
	}

	@MessageMapping("/deleteUser")
	@SendTo("/topic/userData")
	public List<Websocket> deleteUser(Long userId) {
		// Delete the user data from the database
		websocketRepository.deleteById(userId);
		// Return the updated user data list
		return websocketRepository.findAll();
	}

	@MessageMapping("/getUserById/{userId}")
	@SendTo("/topic/userData")
	public void getUserById(@PathVariable("userId") Long userId) {
		// Find the user data by ID from the database
		Websocket userData = websocketRepository.findById(userId).orElse(null);
//		messagingTemplate.convertAndSend("/topic/singleUserData", userData);

		System.out.println(userId);
//		return userRepository.findUserById(userId);
	}

	@MessageMapping("/update/{userId}")
	@SendTo("/topic/updatedUserData")
	public void updateUser(@DestinationVariable  Long userId, Websocket websocket) {
		// Find the existing user data from the database
		Websocket UserData = websocketRepository.findById(userId).orElseThrow();

		// Update the user data fields
		UserData.setUsername(websocket.getUsername());
		UserData.setIdtag(websocket.getIdtag());
		UserData.setExpiryDate(websocket.getExpiryDate());
		UserData.setParentIdTag(websocket.getParentIdTag());
		UserData.setStatus(websocket.getStatus());
		// Save the updated user data in the database
		Websocket updated = websocketRepository.save(UserData);
		System.out.println(updated);
		
	}
		
}
