package com.github.harrynp.tasty.data.pojo;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.github.harrynp.tasty.data.database.RecipesContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by harry on 12/2/2017.
 */

@Entity(tableName = RecipesContract.RecipeEntry.TABLE_NAME)
@Parcel
public class Recipe {

    @PrimaryKey
    @ColumnInfo(index = true, name = RecipesContract.RecipeEntry.COLUMN_ID)
    @SerializedName(RecipesContract.RecipeEntry.COLUMN_ID)
    @Expose
    long id;

    @ColumnInfo(name = RecipesContract.RecipeEntry.COLUMN_NAME)
    @SerializedName(RecipesContract.RecipeEntry.COLUMN_NAME)
    @Expose
    String name;

    @ColumnInfo(name = RecipesContract.RecipeEntry.COLUMN_INGREDIENTS)
    @SerializedName(RecipesContract.RecipeEntry.COLUMN_INGREDIENTS)
    @Expose
    ArrayList<Ingredient> ingredients;

    @ColumnInfo(name = RecipesContract.RecipeEntry.COLUMN_STEPS)
    @SerializedName(RecipesContract.RecipeEntry.COLUMN_STEPS)
    @Expose
    ArrayList<Step> steps;

    @ColumnInfo(name = RecipesContract.RecipeEntry.COLUMN_SERVINGS)
    @SerializedName(RecipesContract.RecipeEntry.COLUMN_SERVINGS)
    @Expose
    Integer servings;

    @ColumnInfo(name = RecipesContract.RecipeEntry.COLUMN_IMAGE)
    @SerializedName(RecipesContract.RecipeEntry.COLUMN_IMAGE)
    @Expose
    String image;


    public Recipe(){
    }

    @Ignore
    public Recipe(int id, String name,
                  ArrayList<Ingredient> ingredients, ArrayList<Step> steps,
                  int servings, String image){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
