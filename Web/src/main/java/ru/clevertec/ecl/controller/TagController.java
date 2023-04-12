package ru.clevertec.ecl.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final TagMapper mapper = Mappers.getMapper(TagMapper.class);

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody Tag tag) {
        return mapper.mapToDto(tagService.create(tag));
    }

    @GetMapping
    public List<TagDto> findAll(@RequestParam(required = false, defaultValue = "10", name = "pageSize") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "0", name = "page") Integer page) {
        return mapper.mapToDto(tagService.findAll(pageSize, page));
    }

    @GetMapping("/{id}")
    public TagDto findById(@PathVariable Long id) {
        return mapper.mapToDto(tagService.findTag(id));
    }

    @DeleteMapping(value = "/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable(name = "tagId") Long id) {
        tagService.delete(id);
    }

    @GetMapping("/tags")
    public List<TagDto>  getMostPopularTag() {
        return mapper.mapToDto(tagService.getMostPopularTag());
    }
}