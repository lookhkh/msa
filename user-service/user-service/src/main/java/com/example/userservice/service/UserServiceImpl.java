package com.example.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo;
	private final PasswordEncoder encoder;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd(encoder.encode(userDto.getPwd()));
		
		userRepo.save(userEntity);
		
		System.out.println(userEntity.toString());
		
		return mapper.map(userEntity, UserDto.class);
	}
	
	
	@Override
	public Iterable<UserEntity> getUserByAll() {
		return userRepo.findAll();
	}
	
	
	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity userEntity=userRepo.findByUserId(userId);
		
		if(userEntity == null) {
			throw new UsernameNotFoundException("user Not Found");
		}
		
		
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		
		List<ResponseOrder> orders = new ArrayList<>();
		userDto.setOrders(orders);
		
		return userDto;
	}
}
