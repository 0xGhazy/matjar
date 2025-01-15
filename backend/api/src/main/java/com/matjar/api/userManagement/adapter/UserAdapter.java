package com.matjar.api.userManagement.adapter;

import com.matjar.api.userManagement.dto.UserDto;
import com.matjar.api.userManagement.entity.User;
import org.modelmapper.ModelMapper;

public class UserAdapter {

    private final ModelMapper modelMapper = new ModelMapper();

    public UserDto toDto(User user) { return modelMapper.map(user, UserDto.class); }

    public User toEntity(UserDto userDto) { return modelMapper.map(userDto, User.class); }

}
