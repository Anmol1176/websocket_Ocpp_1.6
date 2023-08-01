package com.powerlogix.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.powerlogix.models.Websocket;
import com.powerlogix.repo.WebsocketRepository;


public class WebsocketService 
{
	
	@Autowired
	public WebsocketRepository websocketRepository;

	public List<Websocket> getAllUser() {
		List<Websocket> allUser = this.websocketRepository.findAll();

		return allUser;
	}

}
