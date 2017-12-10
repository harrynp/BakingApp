package com.github.harrynp.tasty.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by harry on 12/2/2017.
 */

@Parcel
public class Step {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("shortDescription")
    @Expose
    String shortDescription;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("videoURL")
    @Expose
    String videoURL;
    @SerializedName("thumbnailURL")
    @Expose
    String thumbnailURL;
    @SerializedName("stepView")
    @Expose
    boolean stepViewed;

    public Step(){
    }

    public Step(int id, String shortDescription,
                String description, String videoURL,
                String thumbnailURL){
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.stepViewed = false;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public boolean getStepViewed(){
        return stepViewed;
    }

    public void setStepViewed(boolean stepViewed){
        this.stepViewed = stepViewed;
    }
}
