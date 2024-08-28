/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RozmowaPrywatna } from '../../models/rozmowa-prywatna';

export interface AcceptRozmowaPrywatna$Params {
}

export function acceptRozmowaPrywatna(http: HttpClient, rootUrl: string, params?: AcceptRozmowaPrywatna$Params, context?: HttpContext): Observable<StrictHttpResponse<RozmowaPrywatna>> {
  const rb = new RequestBuilder(rootUrl, acceptRozmowaPrywatna.PATH, 'put');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<RozmowaPrywatna>;
    })
  );
}

acceptRozmowaPrywatna.PATH = '/rest/neo4j/rozmowy/{nadawca-uzyt-id}/accept';