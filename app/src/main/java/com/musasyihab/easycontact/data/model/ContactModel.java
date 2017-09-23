package com.musasyihab.easycontact.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.musasyihab.easycontact.util.Utils;

import java.util.Date;

/**
 * Created by musasyihab on 9/16/17.
 */

@DatabaseTable(tableName = "contact")
public class ContactModel {
    public static final String ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String FAVORITE = "favorite";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    @DatabaseField(columnName = ID, id = true)
    private int id;
    @DatabaseField(columnName = FIRST_NAME)
    private String firstName;
    @DatabaseField(columnName = LAST_NAME)
    private String lastName;
    @DatabaseField(columnName = PROFILE_PIC)
    private String profilePic;
    @DatabaseField(columnName = EMAIL)
    private String email;
    @DatabaseField(columnName = PHONE_NUMBER)
    private String phoneNumber;
    @DatabaseField(columnName = FAVORITE)
    private boolean favorite;
    @DatabaseField(columnName = CREATED_AT)
    private Date createdAt;
    @DatabaseField(columnName = UPDATED_AT)
    private Date updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullname(){
        if(lastName !=null && !lastName.isEmpty()){
            return firstName + " " + lastName;
        }
        return firstName;
    }

    public String getNormalizeProfilePic(){
        return Utils.normalizeAvatarUrl(profilePic);
    }
}

