package com.example.yukka.model.roslina.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.common.PageResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.EntityNotFoundException;
import com.example.yukka.model.roslina.Roslina;
import com.example.yukka.model.roslina.RoslinaMapper;
import com.example.yukka.model.roslina.RoslinaRequest;
import com.example.yukka.model.roslina.RoslinaResponse;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwoscResponse;
import com.example.yukka.model.roslina.wlasciwosc.WlasciwosciRodzaje;
import com.example.yukka.model.uzytkownik.Uzytkownik;

@Service
@Transactional
public class RoslinaService {
    @Autowired
    RoslinaRepository roslinaRepository;

    @Autowired
    WlasciwoscRepository wlasciwoscRepository;

    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    FileUtils fileUtils;

    @Autowired
    RoslinaMapper roslinaMapper;

    public Collection<Roslina> getSome(int amount) {
        System.out.println("BOOOOOOOOOI " + amount);
        Collection<Roslina> beep = roslinaRepository.getSomePlants(amount);
        Iterable<Roslina> properties = beep;
        for (Roslina property : properties) {
            System.out.println(property.getNazwa());
            System.out.println(property.getGleby());
            
        }

        return roslinaRepository.getSomePlants(2);
    }

    @Transactional(readOnly = true)
    public PageResponse<RoslinaResponse> findAllRosliny(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("roslina.nazwa").descending());
        Page<Roslina> rosliny = roslinaRepository.findAllRosliny(pageable);
        List<RoslinaResponse> roslinyResponse = rosliny.stream()
                .map(roslinaMapper::toRoslinaResponse)
                .toList();
        return new PageResponse<>(
                roslinyResponse,
                rosliny.getNumber(),
                rosliny.getSize(),
                rosliny.getTotalElements(),
                rosliny.getTotalPages(),
                rosliny.isFirst(),
                rosliny.isLast()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<RoslinaResponse> findAllRoslinyWithParameters(int page, int size, RoslinaRequest request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("roslina.nazwa").descending());

        Roslina ros = roslinaMapper.toRoslina(request);

        //System.out.println("Rozmiar relat: " + request.getWlasciwosciAsMap().size());
      //  System.out.println("Rozmiar okresow: " + ros.getOkresyOwocowania().size());
     //   System.out.println("Rozmiar kwiatow: " + ros.getKwiaty().size());

        Page<Roslina> rosliny = roslinaRepository.findAllRoslinyWithParameters(
            ros, 
            ros.getFormy(), ros.getGleby(), ros.getGrupy(), ros.getKoloryLisci(),
            ros.getKoloryKwiatow(), ros.getKwiaty(), ros.getNagrody(), ros.getOdczyny(),
            ros.getOkresyKwitnienia(), ros.getOkresyOwocowania(), ros.getOwoce(), ros.getPodgrupa(),
            ros.getPokroje(), ros.getSilyWzrostu(), ros.getStanowiska(), ros.getWalory(),
            ros.getWilgotnosci(), ros.getZastosowania(), ros.getZimozielonosci(),
            pageable);

        List<RoslinaResponse> roslinyResponse = rosliny.stream()
                .map(roslinaMapper::toRoslinaResponse)
                .toList();
        return new PageResponse<>(
                roslinyResponse,
                rosliny.getNumber(),
                rosliny.getSize(),
                rosliny.getTotalElements(),
                rosliny.getTotalPages(),
                rosliny.isFirst(),
                rosliny.isLast()
        );
    }

    @Transactional(readOnly = true)
    public RoslinaResponse findById(Long id) {
        Roslina ros = roslinaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o id: " + id));
        return roslinaMapper.roslinaToRoslinaResponseWithWlasciwosci(ros);
    }

    @Transactional(readOnly = true)
    public RoslinaResponse findByNazwaLacinska(String nazwaLacinska) {
        Roslina ros = roslinaRepository.findByNazwaLacinskaWithWlasciwosci(nazwaLacinska)
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o nazwie łacińskiej: " + nazwaLacinska));
        RoslinaResponse res = roslinaMapper.roslinaToRoslinaResponseWithWlasciwosci(ros);

        System.out.println("Roslina: " + ros.getNazwa());
        System.out.println("Gleba: " + ros.getGleby() + " ||| " + "Response gleby " + res.getGleby());

        return res;
    }

    @Transactional(readOnly = true)
    public Set<WlasciwoscResponse> getWlasciwosciWithRelations() {
        Set<WlasciwosciRodzaje> responses = wlasciwoscRepository.getWlasciwosciWithRelacje();
        return responses.stream()
            .map(response -> WlasciwoscResponse.builder()
                .etykieta(response.getEtykieta())
                .nazwy(response.getNazwy())
                .build())
            .collect(Collectors.toSet());
    }

    public Roslina save(RoslinaRequest request) {
        Optional<Roslina> roslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isPresent()) {
            System.out.println("\n\n\nBITCH IS PRESENT\n\n\n");
            return null;
        }
        
        if(request.areWlasciwosciEmpty()) {
            Roslina pl = roslinaMapper.toRoslina(request);
            return roslinaRepository.addRoslina(pl);
        }

        System.out.println("\n\n\n Nazwa: " + request.getNazwa() + "\n\n\n");
        System.out.println("\n\n\n Relacje: " + request.getWlasciwosciAsMap() + "\n\n\n");

        return roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

    }

    public Roslina save(RoslinaRequest request, MultipartFile file, Authentication connectedUser) {
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        Optional<Roslina> roslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska());
        if (roslina.isPresent()) {
            System.out.println("\n\n\nENTITY IS PRESENT\n\n\n");
            return null;
        }
        
        String leObraz = fileStoreService.saveRoslina(file, request.getNazwaLacinska(), uzyt.getUzytId());
        request.setObraz(leObraz);
        if(request.areWlasciwosciEmpty()) {
            Roslina pl = roslinaMapper.toRoslina(request);
            Roslina ros = roslinaRepository.addRoslina(pl);
            return ros;
        }
        Roslina ros = roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

        return ros;
    }

    public Roslina saveSeededRoslina(RoslinaRequest request, MultipartFile file) {
        Roslina pl = roslinaMapper.toRoslina(request);
        
        String leObraz = fileStoreService.saveSeededRoslina(file, request.getObraz());
        request.setObraz(leObraz);
        if(request.areWlasciwosciEmpty()) {
            Roslina ros = roslinaRepository.addRoslina(pl);
            return ros;
        }
        Roslina ros = roslinaRepository.addRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());

        return ros;
    }

    public RoslinaResponse update(RoslinaRequest request) {
        Roslina roslina = roslinaRepository.findByNazwaLacinska(request.getNazwaLacinska())
        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono rośliny o nazwie łacińskiej: " + request.getNazwaLacinska()));

        if(request.areWlasciwosciEmpty()) {
            Roslina ros = roslinaRepository.updateRoslina(
                request.getNazwa(), request.getNazwaLacinska(), 
                request.getOpis(), request.getObraz(), 
                request.getWysokoscMin(), request.getWysokoscMax());
            return roslinaMapper.toRoslinaResponse(ros);
        }
        
        Roslina ros = roslinaRepository.updateRoslina(
            request.getNazwa(), request.getNazwaLacinska(), 
            request.getOpis(), request.getObraz(), 
            request.getWysokoscMin(), request.getWysokoscMax(), 
            request.getWlasciwosciAsMap());
        return roslinaMapper.roslinaToRoslinaResponseWithWlasciwosci(ros);
    }

    // Uwaga: to jest do głównej rośliny, nie customowej
    public void uploadRoslinaObraz(MultipartFile file, Authentication connectedUser, String latinName) {
        Roslina roslina = roslinaRepository.findByNazwaLacinska(latinName).get();
        Uzytkownik uzyt = ((Uzytkownik) connectedUser.getPrincipal());
        
        String pfp = fileStoreService.saveRoslina(file, latinName, uzyt.getUsername());
        if(pfp == null){
            return;
        }
        fileUtils.deleteObraz(roslina.getObraz());
        
        roslina.setObraz(pfp);
        roslinaRepository.updateRoslina(roslina.getNazwa(), roslina.getNazwaLacinska(), roslina.getOpis(), roslina.getObraz(), roslina.getWysokoscMin(), roslina.getWysokoscMax());
    }


    // Usuwanie po ID zajmuje ogromną ilość czasu i wywołuje HeapOverflow, więc lepiej jest użyć UNIQUE atrybutu jak nazwaLacinska
    public void deleteByNazwaLacinska(String nazwaLacinska) {
        roslinaRepository.deleteByNazwaLacinska(nazwaLacinska);
    }


}
