package com.github.harrynp.tasty.network;

import com.github.harrynp.tasty.data.pojo.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by harry on 12/3/2017.
 */

public interface RecipeApi {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
