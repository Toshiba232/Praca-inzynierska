package com.example.yukka.model.uzytkownik;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Ustawienia {
    @Id @GeneratedValue
    private Long id;

    @Property("statystyki_profilu")
    @Builder.Default
    private boolean statystykiProfilu = true;

    @Property("ogrod_pokaz")
    @Builder.Default
    private boolean ogrodPokaz = true;

    @Property("powiadomienia_komentarze_odpowiedz")
    @Builder.Default
    private boolean powiadomieniaKomentarzeOdpowiedz = true;

    @Property("powiadomienia_ogrod_kwitnienie")
    @Builder.Default
    private boolean powiadomieniaOgrodKwitnienie = true;

    @Property("powiadomienia_ogrod_owocowanie")
    @Builder.Default
    private boolean powiadomieniaOgrodOwocowanie = true;

   // @Relationship(type = "MA_USTAWIENIA", direction = Relationship.Direction.INCOMING)
   // private List<Uzytkownik> uzytkownik;

}
