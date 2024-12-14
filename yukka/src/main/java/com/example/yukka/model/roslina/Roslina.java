package com.example.yukka.model.roslina;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.dzialka.ZasadzonaNa;
import com.example.yukka.model.roslina.wlasciwosc.Wlasciwosc;
import com.example.yukka.model.uzytkownik.Uzytkownik;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * Klasa reprezentująca roślinę.
 * 
 * <ul>
 * <li><strong>labels</strong>: dynamiczne etykiety rośliny</li>
 * <li><strong>id</strong>: unikalny identyfikator rośliny</li>
 * <li><strong>roslinaId</strong>: identyfikator rośliny</li>
 * <li><strong>nazwa</strong>: nazwa rośliny</li>
 * <li><strong>nazwaLacinska</strong>: łacińska nazwa rośliny</li>
 * <li><strong>opis</strong>: opis rośliny</li>
 * <li><strong>wysokoscMin</strong>: minimalna wysokość rośliny</li>
 * <li><strong>wysokoscMax</strong>: maksymalna wysokość rośliny</li>
 * <li><strong>obraz</strong>: ścieżka do obrazu rośliny</li>
 * <li><strong>dzialki</strong>: lista działek, na których roślina jest zasadzona</li>
 * <li><strong>uzytkownik</strong>: użytkownik, który stworzył roślinę</li>
 * <li><strong>formy</strong>: zestaw form rośliny</li>
 * <li><strong>gleby</strong>: zestaw typów gleby odpowiednich dla rośliny</li>
 * <li><strong>grupy</strong>: zestaw grup rośliny</li>
 * <li><strong>koloryLisci</strong>: zestaw kolorów liści rośliny</li>
 * <li><strong>koloryKwiatow</strong>: zestaw kolorów kwiatów rośliny</li>
 * <li><strong>kwiaty</strong>: zestaw kwiatów rośliny</li>
 * <li><strong>odczyny</strong>: zestaw odczynów gleby odpowiednich dla rośliny</li>
 * <li><strong>okresyKwitnienia</strong>: zestaw okresów kwitnienia rośliny</li>
 * <li><strong>okresyOwocowania</strong>: zestaw okresów owocowania rośliny</li>
 * <li><strong>owoce</strong>: zestaw owoców rośliny</li>
 * <li><strong>podgrupa</strong>: zestaw podgrup rośliny</li>
 * <li><strong>pokroje</strong>: zestaw pokrojów rośliny</li>
 * <li><strong>silyWzrostu</strong>: zestaw sił wzrostu rośliny</li>
 * <li><strong>stanowiska</strong>: zestaw stanowisk odpowiednich dla rośliny</li>
 * <li><strong>walory</strong>: zestaw walorów rośliny</li>
 * <li><strong>wilgotnosci</strong>: zestaw poziomów wilgotności odpowiednich dla rośliny</li>
 * <li><strong>zastosowania</strong>: zestaw zastosowań rośliny</li>
 * <li><strong>zimozielonosci</strong>: zestaw cech zimozieloności rośliny</li>
 * </ul>
 * 
 * Metody:
 * <ul>
 * <li><strong>isUzytkownikRoslina</strong>: sprawdza, czy roślina jest użytkownika</li>
 * <li><strong>areWlasciwosciEqual</strong>: porównuje dwa zestawy właściwości</li>
 * <li><strong>extractNazwy</strong>: wyodrębnia nazwy z zestawu właściwości</li>
 * </ul>
 */
@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Roslina {
    @DynamicLabels
    @Builder.Default
    private List<String> labels = new ArrayList<>();
    
    @Id @GeneratedValue
    private long id;

    @Property("roslinaId")
    private String roslinaId;

    @Property("nazwa")
    private String nazwa;
    @Property("nazwaLacinska")
    private String nazwaLacinska;
    @Property("opis")
    private String opis;
    @Property("wysokoscMin")
    @Builder.Default
    private Double wysokoscMin = 0.0;
    @Property("wysokoscMax")
    @Builder.Default
    private Double wysokoscMax = 0.0;
    
    @Property("obraz")
    @Builder.Default
    private String obraz ="default_plant.jpg";



    @JsonIgnore
    @Relationship(type = "ZASADZONA_NA", direction = Relationship.Direction.OUTGOING)
    private List<ZasadzonaNa> dzialki;
	
	@JsonIgnore
	@Relationship(type = "STWORZONA_PRZEZ", direction = Relationship.Direction.OUTGOING)
    private Uzytkownik uzytkownik;

    // Wlasciwosci

    @Relationship(type = "MA_FORME", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> formy = new HashSet<>();

    @Relationship(type = "MA_GLEBE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> gleby = new HashSet<>();

    @Relationship(type = "MA_GRUPE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> grupy = new HashSet<>();

    @Relationship(type = "MA_KOLOR_LISCI", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> koloryLisci = new HashSet<>();

    @Relationship(type = "MA_KOLOR_KWIATOW", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> koloryKwiatow = new HashSet<>();

    @Relationship(type = "MA_KWIAT", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> kwiaty = new HashSet<>();

    @Relationship(type = "MA_ODCZYN", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> odczyny = new HashSet<>();

    @Relationship(type = "MA_OKRES_KWITNIENIA", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> okresyKwitnienia = new HashSet<>();

    @Relationship(type = "MA_OKRES_OWOCOWANIA", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> okresyOwocowania = new HashSet<>();

    @Relationship(type = "MA_OWOC", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> owoce = new HashSet<>();

    @Relationship(type = "MA_PODGRUPE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> podgrupa = new HashSet<>();

    @Relationship(type = "MA_POKROJ", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> pokroje = new HashSet<>();

    @Relationship(type = "MA_SILE_WZROSTU", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> silyWzrostu = new HashSet<>();

    @Relationship(type = "MA_STANOWISKO", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> stanowiska = new HashSet<>();

    @Relationship(type = "MA_WALOR", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> walory = new HashSet<>();

    @Relationship(type = "MA_WILGOTNOSC", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> wilgotnosci = new HashSet<>();

    @Relationship(type = "MA_ZASTOSOWANIE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> zastosowania = new HashSet<>();

    @Relationship(type = "MA_ZIMOZIELONOSC_LISCI", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Wlasciwosc> zimozielonosci = new HashSet<>();

    

    public boolean isUzytkownikRoslina() {
        return labels.contains("UzytkownikRoslina");
    }

    @JsonIgnore
    boolean areWlasciwosciEqual(Set<Wlasciwosc> set1, Set<Wlasciwosc> set2) {
        return extractNazwy(set1).equals(extractNazwy(set2));
    }

    @JsonIgnore
    private Set<String> extractNazwy(Set<Wlasciwosc> wlasciwosci) {
        return wlasciwosci.stream()
                          .map(Wlasciwosc::getNazwa)
                          .collect(Collectors.toSet());
    }


}
