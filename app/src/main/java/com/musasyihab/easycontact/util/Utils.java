package com.musasyihab.easycontact.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.network.response.ContactResponse;
import com.musasyihab.easycontact.network.response.ContactSimpleResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.API_DATE_FORMAT);
        Date formatDate = new Date();
        try {
            formatDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }

    public static boolean isEmailFormatCorrect(String email){
        if (email == null) {
            return false;
        }
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);

        if(!m.matches()){
            return false;
        }
        return true;
    }

    public static String normalizeAvatarUrl(String url){

        if(url.contains("?")){
            String[] split = url.split("\\?");
            url = split[0];
            url = url.replace("http://gojek-contacts-app.herokuapp.com","");

        }
        return url;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                return contentUri.getPath();
            }
            else{
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
