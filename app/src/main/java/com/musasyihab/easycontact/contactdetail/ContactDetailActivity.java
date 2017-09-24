package com.musasyihab.easycontact.contactdetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musasyihab.easycontact.BaseActivity;
import com.musasyihab.easycontact.R;
import com.musasyihab.easycontact.contactform.ContactFormActivity;
import com.musasyihab.easycontact.data.model.ContactModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by musasyihab on 9/21/17.
 */

public class ContactDetailActivity extends BaseActivity implements ContactDetailActivityVP.View {

    public static final String CONTACT_ID = "CONTACT_ID";
    private static final String CONTACT_MODEL = "CONTACT_MODEL";

    @Inject
    ContactDetailPresenter presenter;

    @BindView(R.id.contact_detail_main_layout)
    CoordinatorLayout mMainLayout;
    @BindView(R.id.contact_detail_layout)
    ScrollView mLayout;
    @BindView(R.id.contact_detail_empty)
    TextView mContactDetailEmpty;
    @BindView(R.id.contact_detail_loading)
    ProgressBar mContactDetailLoading;
    @BindView(R.id.contact_detail_avatar)
    ImageView mContactAvatar;
    @BindView(R.id.contact_detail_name)
    TextView mContactName;
    @BindView(R.id.contact_detail_phone_text)
    TextView mContactPhone;
    @BindView(R.id.contact_detail_email_text)
    TextView mContactEmail;
    @BindView(R.id.contact_detail_phone_main_layout)
    LinearLayout mPhoneMainLayout;
    @BindView(R.id.contact_detail_email_main_layout)
    LinearLayout mEmailMainLayout;

    private ActionBar mActionBar;
    private MenuItem mFavoriteMenu;
    private ProgressDialog mLoadingDialog;

    private ContactModel mContact;
    private int mContactId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

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
            mContactName.setText(mContact.getFullname());
            mContactPhone.setText(mContact.getPhoneNumber());
            mContactEmail.setText(mContact.getEmail());

            Glide.with(this)
                    .load(mContact.getNormalizeProfilePic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(mContactAvatar);

            if(mContact.getPhoneNumber()==null || mContact.getPhoneNumber().isEmpty()){
                mPhoneMainLayout.setVisibility(View.GONE);
            } else {
                mPhoneMainLayout.setVisibility(View.VISIBLE);
            }

            if(mContact.getEmail()==null || mContact.getEmail().isEmpty()){
                mEmailMainLayout.setVisibility(View.GONE);
            } else {
                mEmailMainLayout.setVisibility(View.VISIBLE);
            }

            mLayout.setVisibility(View.VISIBLE);
            mContactDetailEmpty.setVisibility(View.GONE);

            updateFavoriteMenuIcon();
        } else {
            mLayout.setVisibility(View.GONE);
            mContactDetailEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mContact == null) {
            presenter.setView(this);
            presenter.loadData(mContactId);
        } else {
            presenter.loadLocalData(mContactId);
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
        inflater.inflate(R.menu.contact_detail_menu, menu);
        mFavoriteMenu = menu.findItem(R.id.action_contact_favorite);
        return true;
    }

    private void updateFavoriteMenuIcon(){
        if(mFavoriteMenu!=null){
            if(mContact!=null && mContact.isFavorite()) {
                mFavoriteMenu.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_on_white, null));
            }
            else {
                mFavoriteMenu.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_off_white, null));
            }
        }
    }

    private void updateContactFavorite(){
        if(presenter!=null)
            presenter.updateFavorite(mContact, mContactId);
    }

    private void deleteContact(){
        new AlertDialog.Builder(this)
            .setTitle("Delete Contact")
            .setMessage("Are you sure want to delete this contact?")
            .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                presenter.deleteContact(mContactId);
            })
            .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_contact_edit:
                // open edit contact activity
                Intent detailIntent = new Intent(this, ContactFormActivity.class);
                detailIntent.putExtra(ContactFormActivity.CONTACT_ID, mContactId);
                startActivity(detailIntent);
                break;
            case R.id.action_contact_favorite:
                // update contact favorite status
                updateContactFavorite();
                break;
            case R.id.action_contact_share:
                // share contact
                String msgBody = mContact.getFullname()+"\n"+mContact.getPhoneNumber()+"\n"+mContact.getEmail();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mContact.getFullname());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msgBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Contact"));
                break;
            case R.id.action_contact_delete:
                // delete contact
                deleteContact();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        mLayout.setVisibility(View.GONE);
        mContactDetailEmpty.setVisibility(View.GONE);
        mContactDetailLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadContactToView();
        mContactDetailLoading.setVisibility(View.GONE);
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
        updateFavoriteMenuIcon();
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

    @OnClick(R.id.contact_detail_phone_layout)
    public void onPhoneClick() {
        // call number
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+mContact.getPhoneNumber()));
        startActivity(callIntent);
    }

    @OnClick(R.id.contact_detail_phone_message)
    public void onMessageClick() {
        // message number
        Intent messageIntent = new Intent(Intent.ACTION_VIEW);
        messageIntent.setData(Uri.parse("sms:"+mContact.getPhoneNumber()));
        startActivity(messageIntent);
    }

    @OnClick(R.id.contact_detail_email_layout)
    public void onEmailClick() {
        // send email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mContact.getEmail()});

        /* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

}
