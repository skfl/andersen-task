package com.andersentask.bookshop.user.domain.mapper;

import com.andersentask.bookshop.user.domain.dto.UserDto;
import com.andersentask.bookshop.user.domain.model.User;

public class UserMapper {

    public static UserDto entityToDto(User user) {
        if (user == null) {
            return new UserDto();
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public static User dtoToEntity(UserDto userDto) {
        if (userDto == null) {
            return new User();
        }
        return User.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();
    }
}
