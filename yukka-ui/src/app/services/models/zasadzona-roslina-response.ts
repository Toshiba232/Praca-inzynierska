/* tslint:disable */
/* eslint-disable */
import { Pozycja } from '../models/pozycja';
import { RoslinaResponse } from '../models/roslina-response';
export interface ZasadzonaRoslinaResponse {
  kolor?: string;
  notatka?: string;
  obraz?: string;
  pozycje?: Array<Pozycja>;
  roslina?: RoslinaResponse;
  tabX?: Array<number>;
  tabY?: Array<number>;
  tekstura?: string;
  wyswietlanie?: string;
  x?: number;
  y?: number;
}
