package com.powerlogix.Handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.powerlogix.models.Websocket;
import com.powerlogix.repo.WebsocketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketHandler extends BinaryWebSocketHandler {

	@Autowired
	private  WebsocketRepository websocketRepository;
	private static List<WebSocketSession> sessions = new ArrayList<>();
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm'Z'");

	private ObjectMapper objectMapper = new ObjectMapper();
    public String remoteStartReqUniqueId;
    public String RemoteStopTransaction;
    public String getConfiguration;
    private Websocket websocket;
    private WebSocketSession session;
	    
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// Handle new connection establishment
		sessions.add(session);
		this.session=session;		
		System.out.println("WebSocket connection established");
	}

	
    	public void handleTextMessage(WebSocketSession session, TextMessage msg) {
	
		
		String receivedMessage = msg.getPayload();
		System.out.println("Received message: " + receivedMessage);

		
		// Get the current time and date
		LocalDateTime now = LocalDateTime.now();
		String timeAndDate = now.format(formatter);
//        String messageToSend = timeAndDate + ": " + receivedMessage;

        String payloadString;
		JSONArray jsonArray = new JSONArray(receivedMessage);

		int messageId = jsonArray.getInt(0);
		String uniqueId = jsonArray.getString(1);
		System.out.println("messageId : " + messageId);
		System.out.println("uniqueId : " + uniqueId);

		if (messageId == 2) 
		{
			String action = jsonArray.getString(2);
			System.out.println("action :" + action);
			String temp=null;

			if (action.equals("BootNotification")) 
			{
				
				String chargePointSerialNumber = jsonArray.getJSONObject(3).getString("chargePointSerialNumber");
				String chargePointVendor = jsonArray.getJSONObject(3).getString("chargePointVendor");
				String chargePointModel = jsonArray.getJSONObject(3).getString("chargePointModel");
				String firmwareVersion = jsonArray.getJSONObject(3).getString("firmwareVersion");

				System.out.println("chargePointSerialNumber : " + chargePointSerialNumber);
				System.out.println("chargePointVendor : " + chargePointVendor);
				System.out.println("chargePointModel : " + chargePointModel);
				System.out.println("firmwareVersion : " + firmwareVersion);
				

				 // Perform the necessary logic to determine the boot notification status
				temp = String.format("[3,\"%s\",{\"status\" : \"Accepted\", \"currentTime\":\"%s\",\"interval\":30}]",uniqueId,timeAndDate);

			}
			else if(action.equals("Authorize"))
			{
				
				 String idtag = websocket.getIdtag();
				    List<Websocket> allUser = websocketRepository.findAll();
				    boolean idTagExistsInDatabase = false;
				    for (Websocket currentUser : allUser) {
				        if (currentUser.getIdtag().equals(idtag)) {
				            idTagExistsInDatabase = true;
				            if (currentUser.getStatus().equals("Accepted")) {
				                temp = String.format("[3,\"%s\",{\"idTagInfo\" : {\"expiryDate\":\"%s\",\"status\" : \"Accepted\"}}]", uniqueId, timeAndDate);
				                break; // No need to continue searching once the ID Tag is found
				            } else if (currentUser.getStatus().equals("Rejected")) {
				                temp = String.format("[3,\"%s\",{\"idTagInfo\" : {\"expiryDate\":\"%s\",\"status\" : \"Rejected\"}}]", uniqueId, timeAndDate);
				                break; // No need to continue searching once the ID Tag is found
				            } else {
				                // Handle other status values if needed
				                temp = String.format("[3,\"%s\",{\"idTagInfo\" : {\"expiryDate\":\"%s\",\"status\" : \"%s\"}}]", uniqueId, timeAndDate, currentUser.getStatus());
				                break; // No need to continue searching once the ID Tag is found
				            }
				        }
				    }
				    if (!idTagExistsInDatabase) {
				        // Handle the case when the ID Tag is not found in the database
				        temp = String.format("[3,\"%s\",{\"idTagInfo\" : {\"expiryDate\":\"%s\",\"status\" : \"ID Tag not found in the database\"}}]", uniqueId, timeAndDate);
				    }
				
				
				
				
				
				
				
				// temp = String.format("[3,\"%s\",{\"idTagInfo\" : {\"expiryDate\":\"%s\",\"status\" : \"Accepted\"}}]",uniqueId,timeAndDate);
			}
			else if(action.equals("DataTransfer"))
			{
                temp = String.format("[3,\"%s\",{\"status\" : \"Accepted\", \"data\":\"string\"}]",uniqueId);
			}
			else if(action.equals("DiagnosticsStatusNotification"))
			{
				temp = String.format("[3,\"%s\",{}]",uniqueId);
			}
			else if(action.equals("FirmwareStatusNotification"))
			{
				temp = String.format("[3,\"%s\",{}]",uniqueId);                
			}
			else if(action.equals("Heartbeat"))                                                                                                                                                                                                                                                                                                                                                                                                                                               
			{

				temp = String.format("[3,\"%s\",{\"currentTime\":\"%s\"}]",uniqueId,timeAndDate);                

			}
			else if(action.equals("MeterValues"))
			{

				temp = String.format("[3,\"%s\",{}]",uniqueId);                

			}
			else if(action.equals("StartTransaction"))
			{
				temp = String.format("[3,\"%s\",{\"idTagInfo\" : {\"status\" : \"Accepted\"},\"transactionId\":12345}]",uniqueId);
			}
			else if(action.equals("StatusNotification"))
			{
				temp = String.format("[3,\"%s\",{}]",uniqueId);                
			}
			else if(action.equals("StopTransaction"))
			{
				temp = String.format("[3,\"%s\",{\"idTagInfo\" : {\"expiryDate\" : \"%s\", \"parentIdTag\":\"20\",\"status\" : \"Accepted\"}}]",uniqueId,timeAndDate);
    		}
		else if(action.equals("RemoteStartTransaction"))
		{
			temp = receivedMessage;	
			remoteStartReqUniqueId=uniqueId;
			System.out.println("remoteStartReqUniqueId inside RemoteStartTransaction:"+remoteStartReqUniqueId);
		}
		else if(action.equals("RemoteStopTransaction"))
		{
			temp = receivedMessage;	
			RemoteStopTransaction=uniqueId;
			System.out.println("remoteStartReqUniqueId inside RemoteStopTransaction:"+RemoteStopTransaction);
		}
		else if(action.equals("GetConfiguration"))
		{
			temp = receivedMessage;	
			getConfiguration=uniqueId;
			System.out.println("getConfiguration inside getConfiguration:"+getConfiguration);
		}
								
			
			
		for (WebSocketSession webSocketSession : sessions) {
			try {

				webSocketSession.sendMessage(new TextMessage(temp));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	else if(messageId == 3)
	{		

	      System.out.println("Verifying Unique id...!" );
	      System.out.println("remoteStartReqUniqueId :"+remoteStartReqUniqueId );
	      
	      if(uniqueId.equals(remoteStartReqUniqueId))
	      {
	    	  System.out.println("RemoteStartTrans UniqueId Matched!");
   	  		  String status = jsonArray.getJSONObject(2).getString("status");
			  System.out.println("status : " + status);
			  
	      }else
	      {
	    	System.out.println("RemoteStartTrans UniqueId Mis Matched!");  
	      }
	}
  }


	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// Handle connection closure
		System.out.println("WebSocket connection closed");
	}
}