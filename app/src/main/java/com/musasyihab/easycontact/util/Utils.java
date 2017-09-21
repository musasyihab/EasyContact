package com.musasyihab.easycontact.util;

import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.network.response.ContactResponse;
import com.musasyihab.easycontact.network.response.ContactSimpleResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by musasyihab on 9/16/17.
 */

public class Utils {

    public static ContactModel mapContactResponseToModel(ContactResponse response){
        ContactModel model = new ContactModel();
        model.setId(response.getId());
        model.setFirstName(response.getFirst_name());
        model.setLastName(response.getLast_name());
        model.setEmail(response.getEmail());
        model.setPhoneNumber(response.getPhone_number());
        model.setProfilePic(Constants.HOST_URL+response.getProfile_pic());
        model.setFavorite(response.isFavorite());
        model.setCreatedAt(getDateFromAPI(response.getCreated_at()));
        model.setUpdatedAt(getDateFromAPI(response.getUpdated_at()));

        return model;
    }

    public static ContactModel mapContactSimpleResponseToModel(ContactSimpleResponse response){
        ContactModel model = new ContactModel();
        model.setId(response.getId());
        model.setFirstName(response.getFirst_name());
        model.setLastName(response.getLast_name());
        model.setProfilePic(Constants.HOST_URL+response.getProfile_pic());
        model.setFavorite(response.isFavorite());

        return model;
    }

    public static Date getDateFromAPI(String date) {
        String formatted = "";
        DateFormat formatter = new SimpleDateFormat(Constants.API_DATE_FORMAT);
        Date formatDate = new Date();
        try {
            Date dateStr = formatter.parse(date);
            formatted = formatter.format(dateStr);
            formatDate = formatter.parse(formatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }
}
