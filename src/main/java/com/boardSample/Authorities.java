package com.boardSample;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="AUTHORITIES")
public class Authorities {


	
	@Id
	@Column(name="USERNAME",length =50,nullable=false)
	private String userId;
	
	private String authority;
	

	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId=userId;
	}
		
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority=authority;
	}
	
	
}