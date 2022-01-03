package com.example.userservice.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RequestUser {

	@NotNull(message = "This Can`t be null")
	@Size(min = 2,message = "this can`t be less than 2")
	@Email
	private String email;
	
	@NotNull(message = "This Can`t be null")
	@Size(min = 2, message = "this can`t be less than 2")
	private String name;
	
	@NotNull(message = "This Can`t be null")
	@Size(min = 8,message = "this can`t be less than 8")
	private String pwd;
}
