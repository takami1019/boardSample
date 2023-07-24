package com.boardSample;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name="msgdata")
public class Message {

	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	@Column
	private long id;
	
	@Column(nullable=true)
	private String userName;
	
	@Column(nullable=true)
	@NotBlank
	private String content;
	
	@Column(nullable=true)
	private Date datetime;
	
	
	public long getId() {
	return id;
	}
	public void setId(long id) {
		this.id=id;
	}
	
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime=datetime;
	}
		
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content=content;
	}
							
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName=userName;
	}
	
}
