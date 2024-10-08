package com.example.yukka.model.social.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.yukka.common.PageResponse;
import com.example.yukka.handler.EntityAlreadyExistsException;
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.social.powiadomienie.PowiadomienieDTO;
import com.example.yukka.model.social.powiadomienie.TypPowiadomienia;
import com.example.yukka.model.social.repository.RozmowaPrywatnaRepository;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatna;
import com.example.yukka.model.social.rozmowaPrywatna.RozmowaPrywatnaResponse;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.controller.UzytkownikRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RozmowaPrywatnaService {

    @Autowired
    private final RozmowaPrywatnaRepository rozmowaPrywatnaRepository;

    @Autowired
    private final UzytkownikRepository uzytkownikRepository;

    @Autowired
    private final CommonMapperService commonMapperService;

    @Autowired
    private final PowiadomienieService powiadomienieService;


    public PageResponse<RozmowaPrywatnaResponse> findRozmowyPrywatneOfUzytkownik(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,    
            Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("priv.ostatnioAktualizowana").descending());
        Page<RozmowaPrywatna> rozmowy = rozmowaPrywatnaRepository.findRozmowyPrywatneOfUzytkownik(uzyt.getEmail(), pageable);

        return commonMapperService.rozmowaPrywatnaPagetoPageRozmowaPrywatnaResponse(rozmowy);
    }

    public RozmowaPrywatnaResponse findRozmowaPrywatna(String odbiorcaId, Authentication connectedUser) {
        Uzytkownik nadawca = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik odbiorca = uzytkownikRepository.findByUzytId(odbiorcaId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorcaId));

        if (nadawca.getNazwa().equals(odbiorca.getNazwa())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }
        
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaWithKomentarze(odbiorca.getUzytId(), nadawca.getUzytId())
        .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));

        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    public RozmowaPrywatnaResponse findRozmowaPrywatnaByNazwa(String nazwa, Authentication connectedUser) {
        Uzytkownik nadawca = (Uzytkownik) connectedUser.getPrincipal();

        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + nazwa));

        if (nadawca.getNazwa().equals(odbiorca.getNazwa())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }
        
        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaWithKomentarze(odbiorca.getUzytId(), nadawca.getUzytId())
        .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));

        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    public RozmowaPrywatnaResponse findRozmowaPrywatnaById(Long id, Authentication connectedUser) {
        Uzytkownik nadawca = (Uzytkownik) connectedUser.getPrincipal();

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaById(id)
            .orElseThrow(() -> new EntityNotFoundException("Rozmowa prywatna nie istnieje"));

        if(rozmowa.getUzytkownicy().stream().noneMatch(u -> u.getUzytId().equals(nadawca.getUzytId())) 
            && (!nadawca.isAdmin() && !nadawca.isPracownik())) {
            throw new IllegalArgumentException("Nie masz dostępu do tej rozmowy");
        }

        return commonMapperService.toRozmowaPrywatnaResponse(rozmowa);
    }

    public RozmowaPrywatnaResponse inviteToRozmowaPrywatna(String odbiorcaNazwa, Authentication currentUser) {
        Uzytkownik nadawca = (Uzytkownik) currentUser.getPrincipal();

        return commonMapperService.toRozmowaPrywatnaResponse(inviteToRozmowaPrywatna(odbiorcaNazwa, nadawca));
    }

    public RozmowaPrywatna inviteToRozmowaPrywatna(String odbiorcaNazwa, Uzytkownik currentUser) {
        Uzytkownik nadawca = currentUser;
        Uzytkownik odbiorca = uzytkownikRepository.findByNazwa(odbiorcaNazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika odbiorcy o nazwie: " + odbiorcaNazwa));

        if (nadawca.getEmail().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie można rozmawiać sam ze sobą");
        }

        if (rozmowaPrywatnaRepository.findRozmowaPrywatnaByUzytId(nadawca.getUzytId(), odbiorca.getUzytId()).isPresent()) {
            throw new EntityAlreadyExistsException("Rozmowa prywatna już istnieje");
        }

        RozmowaPrywatna rozmowaPrywatna = rozmowaPrywatnaRepository.inviteToRozmowaPrywatna(nadawca.getUzytId(), odbiorca.getUzytId(), LocalDateTime.now());

        createPowiadomienie(TypPowiadomienia.ZAPROSZENIE, nadawca, odbiorca);

        return rozmowaPrywatna;
    }

    public RozmowaPrywatna acceptRozmowaPrywatna(String nazwa, Authentication currentUser) {
        Uzytkownik odbiorca = (Uzytkownik) currentUser.getPrincipal();

        return acceptRozmowaPrywatna(nazwa, odbiorca);
    }

    public RozmowaPrywatna acceptRozmowaPrywatna(String nazwa, Uzytkownik currentUser) {
        Uzytkownik odbiorca = currentUser;
        Uzytkownik nadawca = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        if (nadawca.getEmail().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie można akceptować rozmowy z samym sobą");
        }

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByUzytId(odbiorca.getUzytId(), nadawca.getUzytId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        if (rozmowa.isAktywna()) {
            throw new IllegalArgumentException("Rozmowa jest już aktywna");
        }

        if(rozmowa.getNadawca().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie możesz zaakceptować rozmowy, do której sam zaprosiłeś");
        }

        rozmowa.setAktywna(true);
        rozmowa = rozmowaPrywatnaRepository.acceptRozmowaPrywatna(nadawca.getUzytId(), odbiorca.getUzytId());

        createPowiadomienie(TypPowiadomienia.ZAPROSZENIE_ZAAKCEPTOWANE, nadawca, odbiorca);

        return rozmowa;
    }

    public void rejectRozmowaPrywatna(String uzytkownikNazwa, Authentication currentUser) {
        Uzytkownik odbiorca = (Uzytkownik) currentUser.getPrincipal();
        Uzytkownik nadawca = uzytkownikRepository.findByNazwa(uzytkownikNazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + uzytkownikNazwa));

        if (nadawca.getEmail().equals(odbiorca.getEmail())) {
            throw new IllegalArgumentException("Nie można odrzucać rozmowy z samym sobą");
        }

        RozmowaPrywatna rozmowa = rozmowaPrywatnaRepository.findRozmowaPrywatnaByUzytId(odbiorca.getUzytId(), nadawca.getUzytId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rozmowy prywatnej"));

        if (rozmowa.isAktywna()) {
            throw new IllegalArgumentException("Rozmowa jest już aktywna");
        }

        rozmowa.setAktywna(false);
        rozmowaPrywatnaRepository.rejectRozmowaPrywatna(nadawca.getUzytId(), odbiorca.getUzytId(), rozmowa);

        createPowiadomienie(TypPowiadomienia.ZAPROSZENIE_ODRUCONE, nadawca, odbiorca);
    }

    private void createPowiadomienie(TypPowiadomienia typ, Uzytkownik nadawca, Uzytkownik odbiorca) {
        PowiadomienieDTO powiadomienie = PowiadomienieDTO.builder()
            .typ(typ.name())
            .odnosnik(nadawca.getNazwa())
            .uzytkownikNazwa(nadawca.getNazwa())
            .avatar(nadawca.getAvatar())
            .build();
        powiadomienieService.addPowiadomienie(powiadomienie, odbiorca);
    }


}