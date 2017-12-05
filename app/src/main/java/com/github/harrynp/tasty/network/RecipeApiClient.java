package com.github.harrynp.tasty.network;


import android.util.MalformedJsonException;

import com.github.harrynp.tasty.data.pojo.Recipe;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by harry on 12/4/2017.
 */

public class RecipeApiClient {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private RecipeApi recipeApi;

    public RecipeApiClient() {
        //Capturing JSON using https://stackoverflow.com/questions/32514410/logging-with-retrofit-2
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        recipeApi = retrofit.create(RecipeApi.class);
    }

    public void getRecipes(final RecipeApiListener<Recipe> listener) {
        Call<ArrayList<Recipe>> call = recipeApi.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(new MalformedJsonException(response.body().toString()));
                    Timber.d("JSON not formatted correctly: %s", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                t.printStackTrace();
                listener.onFailure(t);
            }
        });
    }

}
