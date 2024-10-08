/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { RoslinaResponse } from '../../models/roslina-response';

export interface UpdateRoslinaObraz$Params {
  'nazwa-lacinska': string;
      body?: {
'file': Blob;
}
}

export function updateRoslinaObraz(http: HttpClient, rootUrl: string, params: UpdateRoslinaObraz$Params, context?: HttpContext): Observable<StrictHttpResponse<RoslinaResponse>> {
  const rb = new RequestBuilder(rootUrl, updateRoslinaObraz.PATH, 'put');
  if (params) {
    rb.path('nazwa-lacinska', params['nazwa-lacinska'], {});
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<RoslinaResponse>;
    })
  );
}

updateRoslinaObraz.PATH = '/rosliny/{nazwa-lacinska}/obraz';
