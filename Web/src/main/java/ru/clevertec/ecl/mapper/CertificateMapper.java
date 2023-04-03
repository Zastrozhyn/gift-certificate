package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

@Mapper(uses = TagMapper.class)
public interface CertificateMapper {

    @Mapping(target = "duration", expression = "java(toDuration(model.getDuration()))")
    GiftCertificate mapToEntity(GiftCertificateDto model);

    @Mapping(target = "duration", expression = "java(toLong(entity.getDuration()))")
    GiftCertificateDto mapToDto(GiftCertificate entity);

    List<GiftCertificateDto> mapToDto(Collection<GiftCertificate> entities);

    default Duration toDuration(long days) {
        return Duration.ofDays(days);
    }

    default long toLong(Duration duration) {
        return duration.toDays();
    }
}
