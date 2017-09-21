package com.musasyihab.easycontact.data.db.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.musasyihab.easycontact.data.db.dao.ContactDao;
import com.musasyihab.easycontact.data.model.ContactModel;

import java.sql.SQLException;

/**
 * Created by musasyihab on 9/17/17.
 */

public class ContactDaoImpl extends BaseDaoImpl<ContactModel, Integer> implements ContactDao {

    public ContactDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, ContactModel.class);
    }

}
