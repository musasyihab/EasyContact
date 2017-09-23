package com.musasyihab.easycontact.dagger.module;

import com.musasyihab.easycontact.network.ApiService;
import com.musasyihab.easycontact.network.ImageApiService;
import com.musasyihab.easycontact.util.Constants;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by musasyihab on 9/16/17.
 */

@Module
public class NetworkModule {

    public NetworkModule() {

    }

    @Provides
    public OkHttpClient provideClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides
    public Retrofit provideRetrofit(String baseURL, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    public ApiService provideApiService() {
        return provideRetrofit(Constants.HOST_URL, provideClient()).create(ApiService.class);
    }

    @Provides
    public ImageApiService provideImageApiService() {
        return provideRetrofit(Constants.CLOUDINARY_HOST_URL, provideClient()).create(ImageApiService.class);
    }

}
