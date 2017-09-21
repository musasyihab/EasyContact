package com.musasyihab.easycontact.network;

import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
import com.musasyihab.easycontact.network.response.ContactResponse;
import com.musasyihab.easycontact.network.response.ContactSimpleResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by musasyihab on 9/17/17.
 */

public interface ApiService {

    @GET("/contacts.json")
    Observable<List<ContactSimpleResponse>> getContactList();

    @GET("/contacts/{id}.json")
    Observable<ContactResponse> getContactById(@Path("id") int id);

    @POST("/contacts.json")
    Observable<ContactResponse> addContact(@Body CreateUpdateContactRequest request);

    @PUT("/contacts/{id}.json")
    Observable<ContactResponse> editContact(@Path("id") int id, @Body CreateUpdateContactRequest request);

    @DELETE("/contacts/{id}.json")
    Observable<Void> deleteContact(@Path("id") int id);

}
