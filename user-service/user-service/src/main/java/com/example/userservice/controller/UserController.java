package com.example.userservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UserController {

	private final Environment env;
	
	@Autowired
	private Greeting greeting;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/health-check")
	public String status() {
		return "it is working well in userSerive on port" +env.getProperty("local.server.port");
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		//return env.getProperty("greeting.message");
		return greeting.getMessage();
	}
	
	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto dto = mapper.map(user, UserDto.class);
		
		ResponseUser responseUser = mapper.map(userService.createUser(dto), ResponseUser.class);

		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers(){
		Iterable<UserEntity> userList = userService.getUserByAll();
		List<ResponseUser> result = new ArrayList<>();
		
		userList.forEach(a->result.add(new ModelMapper().map(a, ResponseUser.class)));
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseUser> getUsers(@PathVariable String userId){
		UserDto user = userService.getUserByUserId(userId);
		ResponseUser returnValue = new ModelMapper().map(user, ResponseUser.class);
		
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
}
