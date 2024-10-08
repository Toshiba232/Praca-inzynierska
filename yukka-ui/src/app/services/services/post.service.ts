/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addOcenaToPost } from '../fn/post/add-ocena-to-post';
import { AddOcenaToPost$Params } from '../fn/post/add-ocena-to-post';
import { addPost1$FormData } from '../fn/post/add-post-1-form-data';
import { AddPost1$FormData$Params } from '../fn/post/add-post-1-form-data';
import { addPost1$Json } from '../fn/post/add-post-1-json';
import { AddPost1$Json$Params } from '../fn/post/add-post-1-json';
import { findAllPosty } from '../fn/post/find-all-posty';
import { FindAllPosty$Params } from '../fn/post/find-all-posty';
import { findAllPostyByConnectedUzytkownik } from '../fn/post/find-all-posty-by-connected-uzytkownik';
import { FindAllPostyByConnectedUzytkownik$Params } from '../fn/post/find-all-posty-by-connected-uzytkownik';
import { findAllPostyByUzytkownik } from '../fn/post/find-all-posty-by-uzytkownik';
import { FindAllPostyByUzytkownik$Params } from '../fn/post/find-all-posty-by-uzytkownik';
import { findAllPostyCountOfUzytkownik } from '../fn/post/find-all-posty-count-of-uzytkownik';
import { FindAllPostyCountOfUzytkownik$Params } from '../fn/post/find-all-posty-count-of-uzytkownik';
import { findPostById } from '../fn/post/find-post-by-id';
import { FindPostById$Params } from '../fn/post/find-post-by-id';
import { PageResponsePostResponse } from '../models/page-response-post-response';
import { Post } from '../models/post';
import { PostResponse } from '../models/post-response';
import { removeOcenaFromPost } from '../fn/post/remove-ocena-from-post';
import { RemoveOcenaFromPost$Params } from '../fn/post/remove-ocena-from-post';
import { removePost } from '../fn/post/remove-post';
import { RemovePost$Params } from '../fn/post/remove-post';

@Injectable({ providedIn: 'root' })
export class PostService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `addOcenaToPost()` */
  static readonly AddOcenaToPostPath = '/posty/oceny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addOcenaToPost()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOcenaToPost$Response(params: AddOcenaToPost$Params, context?: HttpContext): Observable<StrictHttpResponse<PostResponse>> {
    return addOcenaToPost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOcenaToPost$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOcenaToPost(params: AddOcenaToPost$Params, context?: HttpContext): Observable<PostResponse> {
    return this.addOcenaToPost$Response(params, context).pipe(
      map((r: StrictHttpResponse<PostResponse>): PostResponse => r.body)
    );
  }

  /** Path part for operation `removeOcenaFromPost()` */
  static readonly RemoveOcenaFromPostPath = '/posty/oceny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removeOcenaFromPost()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  removeOcenaFromPost$Response(params: RemoveOcenaFromPost$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return removeOcenaFromPost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removeOcenaFromPost$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  removeOcenaFromPost(params: RemoveOcenaFromPost$Params, context?: HttpContext): Observable<string> {
    return this.removeOcenaFromPost$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `findAllPosty()` */
  static readonly FindAllPostyPath = '/posty';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllPosty()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPosty$Response(params?: FindAllPosty$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePostResponse>> {
    return findAllPosty(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllPosty$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPosty(params?: FindAllPosty$Params, context?: HttpContext): Observable<PageResponsePostResponse> {
    return this.findAllPosty$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponsePostResponse>): PageResponsePostResponse => r.body)
    );
  }

  /** Path part for operation `addPost1()` */
  static readonly AddPost1Path = '/posty';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addPost1$FormData()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addPost1$FormData$Response(params: AddPost1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<Post>> {
    return addPost1$FormData(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addPost1$FormData$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addPost1$FormData(params: AddPost1$FormData$Params, context?: HttpContext): Observable<Post> {
    return this.addPost1$FormData$Response(params, context).pipe(
      map((r: StrictHttpResponse<Post>): Post => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addPost1$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addPost1$Json$Response(params: AddPost1$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<Post>> {
    return addPost1$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addPost1$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addPost1$Json(params: AddPost1$Json$Params, context?: HttpContext): Observable<Post> {
    return this.addPost1$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<Post>): Post => r.body)
    );
  }

  /** Path part for operation `findPostById()` */
  static readonly FindPostByIdPath = '/posty/{post-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findPostById()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostById$Response(params: FindPostById$Params, context?: HttpContext): Observable<StrictHttpResponse<PostResponse>> {
    return findPostById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findPostById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostById(params: FindPostById$Params, context?: HttpContext): Observable<PostResponse> {
    return this.findPostById$Response(params, context).pipe(
      map((r: StrictHttpResponse<PostResponse>): PostResponse => r.body)
    );
  }

  /** Path part for operation `removePost()` */
  static readonly RemovePostPath = '/posty/{post-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removePost()` instead.
   *
   * This method doesn't expect any request body.
   */
  removePost$Response(params: RemovePost$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return removePost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removePost$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  removePost(params: RemovePost$Params, context?: HttpContext): Observable<string> {
    return this.removePost$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `findAllPostyByConnectedUzytkownik()` */
  static readonly FindAllPostyByConnectedUzytkownikPath = '/posty/uzytkownik';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllPostyByConnectedUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPostyByConnectedUzytkownik$Response(params?: FindAllPostyByConnectedUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePostResponse>> {
    return findAllPostyByConnectedUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllPostyByConnectedUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPostyByConnectedUzytkownik(params?: FindAllPostyByConnectedUzytkownik$Params, context?: HttpContext): Observable<PageResponsePostResponse> {
    return this.findAllPostyByConnectedUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponsePostResponse>): PageResponsePostResponse => r.body)
    );
  }

  /** Path part for operation `findAllPostyByUzytkownik()` */
  static readonly FindAllPostyByUzytkownikPath = '/posty/uzytkownik/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllPostyByUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPostyByUzytkownik$Response(params: FindAllPostyByUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePostResponse>> {
    return findAllPostyByUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllPostyByUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPostyByUzytkownik(params: FindAllPostyByUzytkownik$Params, context?: HttpContext): Observable<PageResponsePostResponse> {
    return this.findAllPostyByUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponsePostResponse>): PageResponsePostResponse => r.body)
    );
  }

  /** Path part for operation `findAllPostyCountOfUzytkownik()` */
  static readonly FindAllPostyCountOfUzytkownikPath = '/posty/uzytkownik/{nazwa}/count';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllPostyCountOfUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPostyCountOfUzytkownik$Response(params: FindAllPostyCountOfUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return findAllPostyCountOfUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllPostyCountOfUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPostyCountOfUzytkownik(params: FindAllPostyCountOfUzytkownik$Params, context?: HttpContext): Observable<number> {
    return this.findAllPostyCountOfUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

}
