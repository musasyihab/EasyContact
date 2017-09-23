package com.musasyihab.easycontact.network;

import com.musasyihab.easycontact.network.request.ImageUploadRequest;
import com.musasyihab.easycontact.network.response.ImageUploadResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by musasyihab on 9/17/17.
 */

public interface ImageApiService {
    @POST("/v1_1/musasyihab/auto/upload")
    Observable<ImageUploadResponse> uploadImage(@Body ImageUploadRequest request);
}
