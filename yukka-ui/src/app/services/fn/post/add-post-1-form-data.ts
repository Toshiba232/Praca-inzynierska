/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Post } from '../../models/post';
import { PostRequest } from '../../models/post-request';

export interface AddPost1$FormData$Params {
      body: {
'request'?: PostRequest;
'file': Blob;
}
}

export function addPost1$FormData(http: HttpClient, rootUrl: string, params: AddPost1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<Post>> {
  const rb = new RequestBuilder(rootUrl, addPost1$FormData.PATH, 'post');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Post>;
    })
  );
}

addPost1$FormData.PATH = '/rest/neo4j/posty';