package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.Collection;
import java.util.List;

@Mapper(uses = TagMapper.class)
public interface CertificateMapper {
    GiftCertificate mapToEntity(GiftCertificateDto model);

    @Mapping(source = "tags", target = "tags")
    GiftCertificateDto mapToDto(GiftCertificate entity);

    List<GiftCertificateDto> mapToDto(Collection<GiftCertificate> entities);
}
