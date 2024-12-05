package com.example.yukka.model.uzytkownik.controller;

import static java.io.File.separator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.yukka.auth.email.EmailService;
import com.example.yukka.auth.email.EmailTemplateName;
import com.example.yukka.auth.requests.EmailRequest;
import com.example.yukka.auth.requests.UsunKontoRequest;
import com.example.yukka.common.FileResponse;
import com.example.yukka.file.FileStoreService;
import com.example.yukka.file.FileUtils;
import com.example.yukka.handler.exceptions.BlockedUzytkownikException;
import com.example.yukka.handler.exceptions.EntityAlreadyExistsException;
import com.example.yukka.handler.exceptions.EntityNotFoundException;
import com.example.yukka.model.social.CommonMapperService;
import com.example.yukka.model.uzytkownik.Ustawienia;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.example.yukka.model.uzytkownik.UzytkownikResponse;
import com.example.yukka.model.uzytkownik.requests.ProfilRequest;
import com.example.yukka.model.uzytkownik.requests.StatystykiDTO;
import com.example.yukka.model.uzytkownik.requests.UstawieniaRequest;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UzytkownikService implements  UserDetailsService {
    @Value("${application.file.uploads.photos-output-path}")
    String fileUploadPath;

    @Autowired
    private final UzytkownikRepository uzytkownikRepository;
    private final FileUtils fileUtils;
    private final FileStoreService fileStoreService;
    private final CommonMapperService commonMapperService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nazwa) {
        return uzytkownikRepository.findByNameOrEmail(nazwa)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));
    }

    @Transactional(readOnly = true)
    public List<Uzytkownik> findAll(){
        return uzytkownikRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UzytkownikResponse findByEmail(String userEmail){
        Uzytkownik uzyt = uzytkownikRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + userEmail));

        return commonMapperService.toUzytkownikResponse(uzyt);
    }

    @Transactional(readOnly = true)
    public UzytkownikResponse findByNazwa(String nazwa){
        Uzytkownik uzyt = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        return commonMapperService.toUzytkownikResponse(uzyt);
    }

    @Transactional(readOnly = true)
    public FileResponse getLoggedInAvatar(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        Uzytkownik uzyt2 = uzytkownikRepository.findByEmail(uzyt.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + uzyt.getEmail()));
                
        return FileResponse.builder().content(fileUtils.readFile(uzyt2.getAvatar())).build();
    }

    @Transactional(readOnly = true)
    public UzytkownikResponse getBlokowaniAndBlokujacy(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        System.out.println("Pobieranie blokowanych i blokujących użytkowników");
        Uzytkownik uzyt2 = uzytkownikRepository.getBlokowaniAndBlokujacy(uzyt.getNazwa()).orElse(null);
      //  System.out.println("Użyt: " + uzyt2.getNazwa());
    //.orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + uzyt.getNazwa()));

        return commonMapperService.toUzytkownikResponse(uzyt2);
    }

    @Transactional(readOnly = true)
    public UzytkownikResponse getUstawienia(Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Pobieranie ustawień użytkownika " + uzyt.getNazwa());

        Uzytkownik uzytus = uzytkownikRepository.findByNazwa(uzyt.getNazwa())
        .orElseThrow( () -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + uzyt.getNazwa()));

        return  commonMapperService.toUzytkownikResponse(uzytus);
    }

    public UzytkownikResponse updateUstawienia(UstawieniaRequest ustawienia, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Zmiana ustawień użytkownika: " + uzyt.getNazwa());
        
        Ustawienia ust = commonMapperService.toUstawienia(ustawienia);
        Uzytkownik uzytkownik = uzytkownikRepository.updateUstawienia(ust, uzyt.getEmail());

        return commonMapperService.toUzytkownikResponse(uzytkownik);
    }

    public StatystykiDTO getStatystykiOfUzytkownik(String nazwa, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Pobieranie statystyk użytkownika: " + nazwa);

        Uzytkownik targetUzyt = uzytkownikRepository.findByNazwa(nazwa)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        if(!targetUzyt.getUstawienia().isStatystykiProfilu() && !uzyt.hasAuthenticationRights(targetUzyt, currentUser)) {
            return null;
        }
        StatystykiDTO statystyki = StatystykiDTO.builder()
                .posty(uzytkownikRepository.getPostyCountOfUzytkownik(nazwa))
                .komentarze(uzytkownikRepository.getKomentarzeCountOfUzytkownik(nazwa))
                .rosliny(uzytkownikRepository.getRoslinyCountOfUzytkownik(nazwa))
                .build();
                
        return statystyki;
    }

    public UzytkownikResponse updateProfil(ProfilRequest request, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Zmiana prfilu użytkownika: " + uzyt.getNazwa());
        
        Uzytkownik uzytkownik = uzytkownikRepository.updateProfil(uzyt.getEmail(), request.getImie(), request.getMiasto(), 
        request.getMiejsceZamieszkania(), request.getOpis());
        return commonMapperService.toUzytkownikResponse(uzytkownik);
    }

    public void sendChangeEmail(EmailRequest request, Authentication currentUser) throws MessagingException {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Zmiana emaila użytkownika: " + uzyt.getEmail() + " na " + request.getNowyEmail());

        if(uzyt.getEmail().equals(request.getNowyEmail())) {
            throw new IllegalArgumentException("Nie można zmienić emaila na obecny email");
        }
        Uzytkownik uzyt2 = uzytkownikRepository.findByEmail(uzyt.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika z podanym adresem email"));
        if(!passwordEncoder.matches(request.getHaslo(), uzyt2.getHaslo())) {
            throw new IllegalArgumentException("Podane hasło jest nieprawidłowe");
        }

        if(uzytkownikRepository.findByEmail(request.getNowyEmail()).isPresent()) {
            throw new EntityAlreadyExistsException("Użytkownik o podanym emailu już istnieje");
        }

        emailService.sendValidationEmail(uzyt, request.getNowyEmail(), EmailTemplateName.ZMIANA_EMAIL);
    }

    public UzytkownikResponse updateUzytkownikAvatar(MultipartFile file, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        return commonMapperService.toUzytkownikResponse(updateUzytkownikAvatar(file, uzyt));
    }

    public Uzytkownik updateUzytkownikAvatar(MultipartFile file, Uzytkownik currentUser) {
        Uzytkownik uzyt = currentUser;
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(uzyt.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + uzyt.getEmail()));

        if(file == null) {
            throw new IllegalArgumentException("Nie podano pliku");
        }
        fileUtils.deleteObraz(uzytkownik.getAvatar());

        String leObraz = fileStoreService.saveAvatar(file, uzyt.getUzytId());
        System.out.println("Zapisano avatar: " + leObraz);

        uzytkownik = uzytkownikRepository.updateAvatar(uzyt.getEmail(), leObraz);
        return uzytkownik;
    }


    public Boolean setBlokUzytkownik(String nazwa, Authentication currentUser, boolean blok){
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();

        Uzytkownik blokowany = uzytkownikRepository.findByNazwa(nazwa)
            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o nazwie: " + nazwa));

        if(blokowany.getEmail().equals(uzyt.getEmail())) {
            throw new IllegalArgumentException("Nie można blokować samego siebie");
        }

        if(blokowany.isAdmin() || blokowany.isPracownik()) {
            throw new IllegalArgumentException("Admini i pracownicy nie mogą być blokowani.");
        }

        System.out.println("Pobieranie blokowanych użytkowników");
        Optional<Uzytkownik> uzyt2 = uzytkownikRepository.getBlokowaniUzytkownicyOfUzytkownik(uzyt.getEmail());
        
        System.out.println("Sprawdzanie czy użytkownik jest już zablokowany");
        if(blok) {
            if(uzyt2.isPresent()) {
                System.out.println("SET BLOKUJE");
                Set<Uzytkownik> blokowani = uzyt2.get().getBlokowaniUzytkownicy();

                if(blokowani.stream().anyMatch(b -> b.getEmail().equals(blokowany.getEmail()))) {
                    throw new IllegalArgumentException("Użytkownik jest już zablokowany");
                }
            }
            return uzytkownikRepository.zablokujUzyt(blokowany.getEmail(), uzyt.getEmail());
        } else if(uzyt2.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono blokowanych użytkowników");
        }

        System.out.println("Odblokowywanie użytkownika");
        return uzytkownikRepository.odblokujUzyt(blokowany.getEmail(), uzyt.getEmail());
    }

    // Bez zabezpieczeń bo to tylko do seedowania
    public void addUzytkownik(Uzytkownik uzytkownik){
        Ustawienia ust = Ustawienia.builder().build();
        uzytkownikRepository.addUzytkownik(uzytkownik, ust);
    }

    // Bez zabezpieczeń bo to tylko do seedowania
    public void addPracownik(Uzytkownik uzytkownik){
        Ustawienia ust = Ustawienia.builder().build();
        
        String labels = uzytkownik.getLabels().stream()
                     .map(role -> "`" + role + "`")
                     .collect(Collectors.joining(":")
                     );

        uzytkownikRepository.addUzytkownik(uzytkownik, labels, ust);
    }


    public void removeSelf(UsunKontoRequest request, Authentication currentUser) {
        Uzytkownik uzyt = (Uzytkownik) currentUser.getPrincipal();
        log.info("Użytkownika o emailu: " + uzyt.getEmail() + " usuwa się sam");
        
        if(uzyt.isAdmin() || uzyt.isPracownik()) {
            throw new IllegalArgumentException("Nie można usuwać konta admina lub pracownika");
        }

        if(!passwordEncoder.matches(request.getHaslo(), uzyt.getHaslo())) {
            throw new IllegalArgumentException("Podane hasło jest nieprawidłowe");
        }



        // Wylogowanie
        SecurityContextHolder.clearContext();

        //uzytkownikRepository.removeUzytkownik(uzyt.getEmail());
        removeUzytkownikQueries(uzyt.getEmail());

        Path path = Paths.get(fileUploadPath + separator + "uzytkownicy" + separator + uzyt.getUzytId());
        System.out.println("Usuwanie folderu: " + path);
        fileUtils.deleteDirectory(path);
    }


    // Pomocnicze


    private void removeUzytkownikQueries(String email) {
        uzytkownikRepository.removePostyOfUzytkownik(email);
        uzytkownikRepository.removeKomentarzeOfUzytkownik(email);
        uzytkownikRepository.removeRoslinyOfUzytkownik(email);
        uzytkownikRepository.removeUzytkownik(email);
    }
    
    public Uzytkownik sprawdzBlokowanie(String nazwaUzytkownika, Uzytkownik connectedUser) {
        Uzytkownik odbiorca = uzytkownikRepository.getBlokowaniAndBlokujacy(nazwaUzytkownika)
            .orElse(null);
        if(odbiorca == null) {
            return uzytkownikRepository.findByNazwa(nazwaUzytkownika)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o podanej nazwie: " + nazwaUzytkownika));
        }
        
        for(Uzytkownik blokujacy : odbiorca.getBlokujacyUzytkownicy()) {
            if(blokujacy.getUzytId().equals(connectedUser.getUzytId())) {
                throw new BlockedUzytkownikException("Nie można komentować ani oceniać treści blokujących użytkowników");
            }
        }

        for(Uzytkownik blokowany : odbiorca.getBlokowaniUzytkownicy()) {
            if(blokowany.getUzytId().equals(connectedUser.getUzytId())) {
                throw new BlockedUzytkownikException("Nie można komentować ani oceniać treści zablokowanych użytkowników");
            }
        }
        
        return odbiorca;
    }

    public void seedRemoveUzytkownicyObrazy() {
        
        List<Uzytkownik> uzytkownicy = uzytkownikRepository.findAll();
        for(Uzytkownik uzyt : uzytkownicy) {

           // uzytkownikRepository.delete(uzyt);
            Path path = Paths.get(fileUploadPath + separator + "uzytkownicy" + separator + uzyt.getUzytId());
            System.out.println("Usuwanie folderu: " + path);
            fileUtils.deleteDirectory(path);
        }
    }

}
