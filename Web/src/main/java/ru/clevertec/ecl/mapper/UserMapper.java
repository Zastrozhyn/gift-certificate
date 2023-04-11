package ru.clevertec.ecl.mapper;

import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.entity.User;

import java.util.Collection;
import java.util.List;

public interface UserMapper {
    User mapToEntity(UserDto model);

    UserDto mapToDto(User entity);

    List<UserDto> mapToDto(Collection<User> entities);
}
