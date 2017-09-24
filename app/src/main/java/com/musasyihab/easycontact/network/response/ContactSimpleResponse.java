package com.musasyihab.easycontact.network.response;

/**
 * Created by musasyihab on 9/16/17.
 */

public class ContactSimpleResponse {
    private int id;
    private String first_name;
    private String last_name;
    private String profile_pic;
    private boolean favorite;
    private String url;

    public ContactSimpleResponse(int id, String first_name, String last_name, String profile_pic, boolean favorite, String url) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_pic = profile_pic;
        this.favorite = favorite;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
