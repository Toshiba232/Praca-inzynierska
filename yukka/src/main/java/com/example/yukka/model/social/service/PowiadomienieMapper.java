package com.example.yukka.model.social.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.yukka.common.PageResponse;
import com.example.yukka.model.social.powiadomienie.Powiadomienie;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;

@Service
public class PowiadomienieMapper {


    public PowiadomienieDTO toPowiadomienieResponse(Powiadomienie powiadomienie) {
        if (powiadomienie == null) {
            return null;
        }
        return PowiadomienieDTO.builder()
            .typ(powiadomienie.getTyp())
            .tytul(powiadomienie.getTytul())
            .odnosnik(powiadomienie.getOdnosnik())
            .opis(powiadomienie.getOpis())
            .nazwyRoslin(powiadomienie.getNazwyRoslin())
            .avatar(powiadomienie.getAvatar())
            .uzytkownikNazwa(powiadomienie.getUzytkownikNazwa())
            .iloscPolubien(powiadomienie.getIloscPolubien())
            .data(powiadomienie.getData())
            .dataUtworzenia(powiadomienie.getDataUtworzenia())
            .build();
    }

    public PageResponse<PowiadomienieDTO> PowiadomieniePageToPagePowiadomienieResponse(Page<Powiadomienie> powiadomienia) {
            List<PowiadomienieDTO> powiadomieniaResponse = powiadomienia.getContent().stream()
            .map(this::toPowiadomienieResponse)
            .collect(Collectors.toList());
    
            return new PageResponse<>(
                powiadomieniaResponse,
                powiadomienia.getNumber(),
                powiadomienia.getSize(),
                powiadomienia.getTotalElements(),
                powiadomienia.getTotalPages(),
                powiadomienia.isFirst(),
                powiadomienia.isLast()
        );
    }

}
