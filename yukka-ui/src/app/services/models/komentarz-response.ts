/* tslint:disable */
/* eslint-disable */
import { PostResponse } from '../models/post-response';
export interface KomentarzResponse {
  avatar?: string;
  dataUtworzenia?: string;
  edytowany?: boolean;
  id?: number;
  komentarzId?: string;
  obraz?: string;
  ocenyLubi?: number;
  ocenyNieLubi?: number;
  odpowiadaKomentarzowi?: KomentarzResponse;
  odpowiedzi?: Array<KomentarzResponse>;
  opis?: string;
  post?: PostResponse;
  uzytkownikNazwa?: string;
}