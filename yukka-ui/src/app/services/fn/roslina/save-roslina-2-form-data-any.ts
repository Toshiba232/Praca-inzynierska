/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RoslinaRequest } from '../../models/roslina-request';

export interface SaveRoslina2$FormData$Any$Params {
      body: {
'request'?: RoslinaRequest;
'file': Blob;
}
}

export function saveRoslina2$FormData$Any(http: HttpClient, rootUrl: string, params: SaveRoslina2$FormData$Any$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
  const rb = new RequestBuilder(rootUrl, saveRoslina2$FormData$Any.PATH, 'post');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<string>;
    })
  );
}

saveRoslina2$FormData$Any.PATH = '/rest/neo4j/rosliny';