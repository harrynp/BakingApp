package com.github.harrynp.tasty.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by harry on 12/2/2017.
 */

@Parcel
public class Ingredient {
    @SerializedName("quantity")
    @Expose
    Float quantity;
    @SerializedName("measure")
    @Expose
    String measure;
    @SerializedName("ingredient")
    @Expose
    String ingredient;

    public Ingredient(){
    }

    public Ingredient(Float quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
