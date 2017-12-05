package com.github.harrynp.tasty.network;

import java.util.ArrayList;

/**
 * Created by harry on 12/4/2017.
 */

public interface RecipeApiListener<T> {
    void onSuccess(ArrayList<T> result);

    void onFailure(Throwable throwable);
}
