package ru.clevertec.ecl.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.mapper.CertificateMapper;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final CertificateMapper mapper = Mappers.getMapper(CertificateMapper.class);

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificate) {
        return mapper.mapToDto(giftCertificateService.create(mapper.mapToEntity(giftCertificate)));
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable Long id) {
        System.out.println(giftCertificateService.findById(id));
        return mapper.mapToDto(giftCertificateService.findById(id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto update(@PathVariable Long id, @RequestBody GiftCertificateDto giftCertificate) {
        return mapper.mapToDto(giftCertificateService.update(id, mapper.mapToEntity(giftCertificate)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        giftCertificateService.delete(id);
    }

    @GetMapping()
    public List<GiftCertificateDto> findByAttributes(@RequestParam(required = false, name = "tagName") String tagName,
                                                     @RequestParam(required = false, name = "searchPart") String searchPart,
                                                     @RequestParam(required = false, name = "sortingField") String sortingField,
                                                     @RequestParam(required = false, name = "orderSort") String orderSort,
                                                     @RequestParam(required = false, defaultValue = "10", name = "pageSize") Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "0", name = "page") Integer page,
                                                     @RequestParam(required = false, name = "search") String search){
        return mapper.mapToDto(giftCertificateService.findByAttributes(tagName, searchPart,
                sortingField, orderSort, search, pageSize, page));
    }

    @PutMapping("/{id}/tags")
    public GiftCertificateDto addTagToCertificate(@PathVariable Long id, @RequestBody Tag tag){
        giftCertificateService.addTagToCertificate(tag, id);
        return mapper.mapToDto(giftCertificateService.findById(id));
    }

    @DeleteMapping ("/{id}/tags")
    public GiftCertificateDto deleteTagFromCertificate(@PathVariable Long id, @RequestBody Tag tag){
        return mapper.mapToDto(giftCertificateService.deleteTagFromCertificate(tag, id));
    }

}
