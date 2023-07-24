package com.boardSample;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="USERS")
public class User {

	
	@Id
	@Column(name="USERNAME",length =50,nullable=false)
	private String userId;
	
	@Column(nullable=false)
	private String password;
	
	private String enabled;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId=userId;
	}
		
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password=password;
	}
	
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled=enabled;
	}
	
}