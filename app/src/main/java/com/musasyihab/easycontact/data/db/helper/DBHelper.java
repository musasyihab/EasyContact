package com.musasyihab.easycontact.data.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.util.Constants;

import java.sql.SQLException;

/**
 * Created by musasyihab on 9/16/17.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private Dao<ContactModel, Integer> cityDao = null;

    public DBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        Log.v("Database","CREATE TABLE");
        createTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.v("Database","UPGRADE TABLE");

        resetDatabase();
        createTable();
    }

    private void createTable() {

        try {
            TableUtils.createTableIfNotExists(connectionSource, ContactModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void resetDatabase() {

        try {
            TableUtils.dropTable(connectionSource, ContactModel.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<ContactModel, Integer> getContactDao() throws SQLException {
        if (cityDao == null) {
            cityDao = getDao(ContactModel.class);
        }
        return cityDao;
    }


}
