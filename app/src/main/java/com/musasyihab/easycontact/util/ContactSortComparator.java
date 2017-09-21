package com.musasyihab.easycontact.util;

import com.musasyihab.easycontact.data.model.ContactModel;

import java.util.Comparator;

/**
 * Created by musasyihab on 9/21/17.
 */

public class ContactSortComparator implements Comparator<ContactModel> {

    // this function is for sorting contact by favorite first then by first name
    @Override
    public int compare(ContactModel c1, ContactModel c2) {
        int b1 = c1.isFavorite() ? 1 : 0;
        int b2 = c2.isFavorite() ? 1 : 0;

        int i = b2 - b1;

        if (i != 0) return i;

        return c1.getFirstName().toUpperCase().compareTo(c2.getFirstName().toUpperCase());

    }
}
