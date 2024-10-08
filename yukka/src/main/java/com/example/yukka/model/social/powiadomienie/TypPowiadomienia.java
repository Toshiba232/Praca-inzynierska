package com.example.yukka.model.social.powiadomienie;

public enum TypPowiadomienia {
    KOMENTARZ_POST("{uzytkownikNazwa} odpowiedział(a) na twój komentarz pod postem {tytul}"),
    POLUBIENIA_POST("Twój post {tytul} zebrał ponad {iloscPolubien} polubień!"),
    ZAPROSZENIE("{uzytkownikNazwa} zaprosił(a) cię do rozmowy!"),
    ZAPROSZENIE_ODRUCONE("{uzytkownikNazwa} odrzucił(a) twoje zaproszenie."),
    ZAPROSZENIE_ZAAKCEPTOWANE("{uzytkownikNazwa} zaakceptował(a) twoje zaproszenie."),
    WIADOMOSC_PRYWATNA("{uzytkownikNazwa} odpowiedział(a) tobie w wiadomosci prywatnej"),
    OWOCOWANIE_ROSLIN("Za {data} twoje rośliny {nazwyRoslin} zaczną owocować!"),
    OWOCOWANIE_ROSLIN_TERAZ("W aktualnym miesiącu twoje rośliny {nazwyRoslin} powinny owocować!"),
    KWITNIENIE_ROSLIN_TERAZ("W aktualnym miesiącu twoje rośliny {nazwyRoslin} powinny kwitnąć!"),
    PODLEWANIE_ROSLIN("Za {data} powinieneś podlać następujące rośliny: {nazwyRoslin}"),
    USUNIECIE_KOMENTARZA("Usunięto twój komentarz pod postem {tytul}"),
    BAN("Zostałeś zbanowany aż do {data}"),
    ODBANOWANIE("Twoje konto zostało odbanowane"),
    OCENA_ROSLINY("Ktoś ocenił twoją roślinę {tytul} {odnosnik}"),
    USUNIECIE_ROSLINY("Twoja roślina została usunięta bo tak"),
    GRATULACJE("Gratulacje użytkowniku! Zyskałeś(aś) ponad {iloscPolubien} polubień!"),
    SPECJALNE("{tytul}");

    private final String template;

    TypPowiadomienia(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
