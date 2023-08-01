package com.powerlogix.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Websocket 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;
	private String idtag;
	private String expiryDate;
	private String parentIdTag;
	private String status;
	public Websocket() {
		super();
		// TODO Auto-generated constructor stub
	}
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	public Websocket(Long id, String username, String idtag, String expiryDate, String parentIdTag, String status,
			User user) {
		super();
		this.id = id;
		this.username = username;
		this.idtag = idtag;
		this.expiryDate = expiryDate;
		this.parentIdTag = parentIdTag;
		this.status = status;
		this.user = user;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdtag() {
		return idtag;
	}
	public void setIdtag(String idtag) {
		this.idtag = idtag;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getParentIdTag() {
		return parentIdTag;
	}
	public void setParentIdTag(String parentIdTag) {
		this.parentIdTag = parentIdTag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Websocket [id=" + id + ", username=" + username + ", idtag=" + idtag + ", expiryDate=" + expiryDate
				+ ", parentIdTag=" + parentIdTag + ", status=" + status + ", user=" + user + "]";
	}

	
	
}
