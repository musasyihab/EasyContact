package com.musasyihab.easycontact.contactform;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musasyihab.easycontact.BaseActivity;
import com.musasyihab.easycontact.R;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
import com.musasyihab.easycontact.util.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by musasyihab on 9/22/17.
 */

public class ContactFormActivity extends BaseActivity implements ContactFormActivityVP.View {

    public static final String CONTACT_ID = "CONTACT_ID";
    private static final String CONTACT_MODEL = "CONTACT_MODEL";

    @Inject
    ContactFormPresenter presenter;

    @BindView(R.id.contact_form_main_layout)
    CoordinatorLayout mMainLayout;
    @BindView(R.id.contact_form_avatar)
    ImageView mContactAvatar;
    @BindView(R.id.contact_form_first_name_label)
    TextInputLayout mFirstNameLabel;
    @BindView(R.id.contact_form_first_name_input)
    EditText mFirstNameInput;
    @BindView(R.id.contact_form_last_name_label)
    TextInputLayout mLastNameLabel;
    @BindView(R.id.contact_form_last_name_input)
    EditText mLastNameInput;
    @BindView(R.id.contact_form_phone_label)
    TextInputLayout mPhoneLabel;
    @BindView(R.id.contact_form_phone_input)
    EditText mPhoneInput;
    @BindView(R.id.contact_form_email_label)
    TextInputLayout mEmailLabel;
    @BindView(R.id.contact_form_email_input)
    EditText mEmailInput;

    private ActionBar mActionBar;
    private ProgressDialog mLoadingDialog;

    private ContactModel mContact;
    private int mContactId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        getAppComponent().inject(this);

        ButterKnife.bind(this);

        mContactId = getIntent().getIntExtra(CONTACT_ID, -1);

        if (savedInstanceState!=null){
            if(savedInstanceState.containsKey(CONTACT_MODEL)){
                String sContact = savedInstanceState.getString(CONTACT_MODEL);
                mContact = new Gson().fromJson(sContact,
                        new TypeToken<ContactModel>() {}.getType());
                loadContactToView();
            }
        }

    }

    private void loadContactToView(){
        if(mContact != null){
            mFirstNameInput.setText(mContact.getFirstName());
            mFirstNameInput.setText(mContact.getLastName());
            mFirstNameInput.setText(mContact.getEmail());
            mFirstNameInput.setText(mContact.getPhoneNumber());

            Glide.with(this)
                    .load(mContact.getProfilePic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(mContactAvatar);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
        if(mContactId != -1) {
            presenter.loadData(mContactId);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String contactModel = new Gson().toJson(mContact);
        outState.putString(CONTACT_MODEL, contactModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_contact_save:
                // open edit contact activity
                if (validateData()) {
                    if (mContactId == -1) {
                        saveNewContact();
                    } else {
                        updateExistingContact();
                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateData(){
        boolean status = true;

        if(mFirstNameInput.getText().toString().isEmpty()){
            mFirstNameLabel.setErrorEnabled(true);
            mFirstNameLabel.setError("First name cannot be empty");
            status = false;
        } else {
            mFirstNameLabel.setError("");
            mFirstNameLabel.setErrorEnabled(false);
        }

        if(!mEmailInput.getText().toString().isEmpty() && !Utils.isEmailFormatCorrect(mEmailInput.getText().toString())){
            mEmailLabel.setErrorEnabled(true);
            mEmailLabel.setError("Email format not correct");
            status = false;
        } else {
            mEmailLabel.setError("");
            mEmailLabel.setErrorEnabled(false);
        }

        return status;
    }

    private void saveNewContact(){
        CreateUpdateContactRequest request = new CreateUpdateContactRequest();
        request.setFirst_name(mFirstNameInput.getText().toString());
        request.setLast_name(mLastNameInput.getText().toString());
        request.setPhone_number(mPhoneInput.getText().toString());
        request.setEmail(mEmailInput.getText().toString());
        request.setFavorite(false);

        presenter.createContact(request);
    }

    private void updateExistingContact(){
        CreateUpdateContactRequest request = new CreateUpdateContactRequest();
        request.setFirst_name(mFirstNameInput.getText().toString());
        request.setLast_name(mLastNameInput.getText().toString());
        request.setPhone_number(mPhoneInput.getText().toString());
        request.setEmail(mEmailInput.getText().toString());
        request.setFavorite(mContact.isFavorite());

        presenter.updateContact(request, mContactId);
    }

    @Override
    public void showLoadingDialog() {
        if(mLoadingDialog==null) {
            mLoadingDialog = ProgressDialog.show(this, "",
                    "Please wait...", true);
        }
        if(!mLoadingDialog.isShowing())
            mLoadingDialog.show();

    }

    @Override
    public void hideLoadingDialog() {
        if(mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void updateData(ContactModel contact) {
        mContact = contact;
    }

    @Override
    public void showSnackbar(String msg) {
        Snackbar.make(mMainLayout, msg, Snackbar.LENGTH_LONG).show();
    }

}
