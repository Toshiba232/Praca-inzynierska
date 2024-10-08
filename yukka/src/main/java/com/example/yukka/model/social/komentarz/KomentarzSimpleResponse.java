package com.example.yukka.model.social.komentarz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KomentarzSimpleResponse {
    private Long id;
    private String komentarzId;
    private String opis;
    private boolean edytowany;
    private String dataUtworzenia;
    private String uzytkownikNazwa;
    private String postId;
    public byte[] obraz;
    public byte[] avatar;
}
