package com.matjar.usermanagementapi.adapter;

import com.matjar.usermanagementapi.dto.UserDto;
import com.matjar.usermanagementapi.entity.User;
import org.modelmapper.ModelMapper;

public class UserAdapter {

    private final ModelMapper modelMapper = new ModelMapper();

    public UserDto toDto(User user) { return modelMapper.map(user, UserDto.class); }

    public User toEntity(UserDto userDto) { return modelMapper.map(userDto, User.class); }

}
