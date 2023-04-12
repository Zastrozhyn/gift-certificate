package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.entity.User;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserMapper {
    User mapToEntity(UserDto model);

    UserDto mapToDto(User entity);

    List<UserDto> mapToDto(Collection<User> entities);
}
