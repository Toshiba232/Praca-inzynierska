/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponsePostResponse } from '../../models/page-response-post-response';

export interface FindAllPosty$Params {
  page?: number;
  size?: number;
  szukaj?: string;
}

export function findAllPosty(http: HttpClient, rootUrl: string, params?: FindAllPosty$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePostResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllPosty.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
    rb.query('szukaj', params.szukaj, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponsePostResponse>;
    })
  );
}

findAllPosty.PATH = '/posty';
