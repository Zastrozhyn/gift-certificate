package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.entity.Tag;

import java.util.Collection;
import java.util.List;

@Mapper
public interface TagMapper {
    Tag mapToEntity(TagDto model);

    TagDto mapToDto(Tag entity);

    List<TagDto> mapToDto(Collection<Tag> entities);
}
