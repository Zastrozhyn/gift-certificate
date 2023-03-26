package ru.clevertec.ecl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate insert(@RequestBody GiftCertificate giftCertificate) {
        return giftCertificateService.create(giftCertificate);
    }

    @GetMapping("/{id}")
    public GiftCertificate findById(@PathVariable Long id) {
        System.out.println(giftCertificateService.findById(id));
        return giftCertificateService.findById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate update(@PathVariable Long id, @RequestBody GiftCertificate giftCertificate) {
        return giftCertificateService.update(id, giftCertificate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        giftCertificateService.delete(id);
    }

    @GetMapping()
    public List<GiftCertificate> findByAttributes(@RequestParam(required = false, name = "tagName") String tagName,
                                                  @RequestParam(required = false, name = "searchPart") String searchPart,
                                                  @RequestParam(required = false, name = "sortingField") String sortingField,
                                                  @RequestParam(required = false, name = "orderSort") String orderSort,
                                                  @RequestParam(required = false, name = "search") String search){
        return giftCertificateService.findByAttributes(tagName, searchPart, sortingField, orderSort, search);
    }

    @PutMapping("/{id}/tags")
    public GiftCertificate addTagToCertificate(@PathVariable Long id, @RequestBody Tag tag){
        return giftCertificateService.addTagToCertificate(tag, id);
    }

    @DeleteMapping ("/{id}/tags")
    public GiftCertificate deleteTagFromCertificate(@PathVariable Long id, @RequestBody Tag tag){
        return giftCertificateService.deleteTagFromCertificate(tag, id);
    }

}
