package com.powerlogix.models;

import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User 
{
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long cId;
	   private String name;
	   private String email;
	   private String password;
	   private String role;
	   private String enabled;
	   
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Websocket> websockets = new ArrayList();

	public User(Long cId, String name, String email, String password, String role, String enabled,
			List<Websocket> websockets) {
		super();
		this.cId = cId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.websockets = websockets;
	}

	public Long getcId() {
		return cId;
	}

	public void setcId(Long cId) {
		this.cId = cId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public List<Websocket> getWebsockets() {
		return websockets;
	}

	public void setWebsockets(List<Websocket> websockets) {
		this.websockets = websockets;
	}

	@Override
	public String toString() {
		return "User [cId=" + cId + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", websockets=" + websockets + "]";
	}

	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}


	
		
}
