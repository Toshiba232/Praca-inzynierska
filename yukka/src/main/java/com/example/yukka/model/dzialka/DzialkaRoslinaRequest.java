package com.example.yukka.model.dzialka;
import java.util.List;
import java.util.Set;

import com.example.yukka.validations.ValidRoslinaIdAlboNazwaLacinska;
import com.example.yukka.validations.pozycje.ValidPozycje;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

//@ValidRoslinaIdAlboNazwaLacinska
@ValidPozycje
public class DzialkaRoslinaRequest {
    private String roslinaId;
    private String nazwaLacinska;

    @Min(value = 1, message = "Numer działki musi być > 0")
    @Max(value = 10, message = "Numer działki musi być <= 10")
    @NotNull(message = "Numer działki jest wymagany")
    private Integer numerDzialki;

    @Min(value = 0, message = "Pozycja x musi być >= 0")
    @Max(value = 19, message = "Pozycja x musi być <= 19")
    @NotNull(message = "Pozycja x jest wymagana")
    private Integer x;

    @Min(value = 0, message = "Pozycja y musi być >= 0")
    @Max(value = 19, message = "Pozycja y musi być <= 19")
    @NotNull(message = "Pozycja y jest wymagana")
    private Integer y;


    @NotEmpty(message = "Pozycje są wymagane")
    @Size(min = 1, max = 400, message = "Ilość kafelków dla rośliny powinna wynosić od 1 do 400")
    Set<Pozycja> pozycje;

    @JsonIgnore
    public int[] getPozycjeX() {
        int[] tabX = new int[pozycje.size()];
        int i = 0;
        for (Pozycja p : pozycje) {
            tabX[i++] = p.getX();
        }
        return tabX;
    }

    @JsonIgnore
    public int[] getPozycjeY() {
        int[] tabY = new int[pozycje.size()];
        int i = 0;
        for (Pozycja p : pozycje) {
            tabY[i++] = p.getY();
        }
        return tabY;
    }


    // Null jak nie dopina się żadnego obrazu
    private String obraz;

}
