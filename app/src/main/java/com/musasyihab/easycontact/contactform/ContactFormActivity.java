package com.musasyihab.easycontact.contactform;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musasyihab.easycontact.BaseActivity;
import com.musasyihab.easycontact.R;
import com.musasyihab.easycontact.contactdetail.ContactDetailActivity;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
import com.musasyihab.easycontact.network.request.ImageUploadRequest;
import com.musasyihab.easycontact.util.BitmapUtils;
import com.musasyihab.easycontact.util.Constants;
import com.musasyihab.easycontact.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by musasyihab on 9/22/17.
 */

public class ContactFormActivity extends BaseActivity implements ContactFormActivityVP.View {

    public static final String CONTACT_ID = "CONTACT_ID";

    private static final String FORM_FIRST_NAME = "FORM_FIRST_NAME";
    private static final String FORM_LAST_NAME = "FORM_LAST_NAME";
    private static final String FORM_PHONE = "FORM_PHONE";
    private static final String FORM_EMAIL = "FORM_EMAIL";

    private static final int ACTIVITY_PHOTO_RESULT = 21;
    private static final int ACTIVITY_GALERY_RESULT = 22;

    @Inject
    ContactFormPresenter presenter;

    @BindView(R.id.contact_form_main_layout)
    CoordinatorLayout mMainLayout;
    @BindView(R.id.contact_form_avatar)
    ImageView mContactAvatar;
    @BindView(R.id.contact_form_avatar_btn)
    ImageView mChoosePictureBtn;
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

    private ProgressDialog mLoadingDialog;

    private ContactModel mContact;
    private int mContactId = -1;
    private String avatarURL;
    private String photoFilePaths;
    private Uri photoSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        getAppComponent().inject(this);

        ButterKnife.bind(this);

        mContactId = getIntent().getIntExtra(CONTACT_ID, -1);

        if (savedInstanceState!=null){
            if(savedInstanceState.containsKey(FORM_FIRST_NAME)){
                String sValue = savedInstanceState.getString(FORM_FIRST_NAME);
                mFirstNameInput.setText(sValue);
            }
            if(savedInstanceState.containsKey(FORM_LAST_NAME)){
                String sValue = savedInstanceState.getString(FORM_LAST_NAME);
                mLastNameInput.setText(sValue);
            }
            if(savedInstanceState.containsKey(FORM_PHONE)){
                String sValue = savedInstanceState.getString(FORM_PHONE);
                mPhoneInput.setText(sValue);
            }
            if(savedInstanceState.containsKey(FORM_EMAIL)){
                String sValue = savedInstanceState.getString(FORM_EMAIL);
                mEmailInput.setText(sValue);
            }
        }

    }

    private void loadContactToView(){
        if(mContact != null){
            mFirstNameInput.setText(mContact.getFirstName());
            mLastNameInput.setText(mContact.getLastName());
            mEmailInput.setText(mContact.getEmail());
            mPhoneInput.setText(mContact.getPhoneNumber());

            Glide.with(this)
                    .load(mContact.getNormalizeProfilePic())
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
    protected void onDestroy() {
        super.onDestroy();
        if(photoFilePaths!=null){
            BitmapUtils.deleteImageFile(this, photoFilePaths);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(FORM_FIRST_NAME, mFirstNameInput.getText().toString());
        outState.putString(FORM_LAST_NAME, mLastNameInput.getText().toString());
        outState.putString(FORM_PHONE, mPhoneInput.getText().toString());
        outState.putString(FORM_EMAIL, mEmailInput.getText().toString());
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
                    if (photoFilePaths!=null) {
                        ImageUploadRequest request = new ImageUploadRequest();
                        request.setFile(Constants.IMAGE_UPLOAD_DATA_TYPE+getBase64());
                        request.setUpload_preset(Constants.IMAGE_UPLOAD_PRESET);
                        presenter.uploadImage(request);
                    } else {
                        submitContact();
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
            mFirstNameLabel.setError(getString(R.string.firstname_required));
            status = false;
        } else {
            mFirstNameLabel.setError("");
            mFirstNameLabel.setErrorEnabled(false);
        }

        if(mLastNameInput.getText().toString().isEmpty()){
            mLastNameLabel.setErrorEnabled(true);
            mLastNameLabel.setError(getString(R.string.lastname_required));
            status = false;
        } else {
            mLastNameLabel.setError("");
            mLastNameLabel.setErrorEnabled(false);
        }

        if(!mEmailInput.getText().toString().isEmpty() && !Utils.isEmailFormatCorrect(mEmailInput.getText().toString())){
            mEmailLabel.setErrorEnabled(true);
            mEmailLabel.setError(getString(R.string.email_invalid));
            status = false;
        } else {
            mEmailLabel.setError("");
            mEmailLabel.setErrorEnabled(false);
        }

        return status;
    }

    private void saveNewContact(){
        CreateUpdateContactRequest request = generateRequest();
        presenter.createContact(request);
    }

    private void updateExistingContact(){
        CreateUpdateContactRequest request = generateRequest();
        presenter.updateContact(request, mContactId);
    }

    private CreateUpdateContactRequest generateRequest(){
        CreateUpdateContactRequest request = new CreateUpdateContactRequest();
        request.setFirst_name(mFirstNameInput.getText().toString());
        request.setLast_name(mLastNameInput.getText().toString());
        request.setPhone_number(mPhoneInput.getText().toString());
        request.setEmail(mEmailInput.getText().toString());

        boolean isFavorite = mContact != null && mContact.isFavorite();
        request.setFavorite(isFavorite);

        if(avatarURL!=null){
            request.setProfile_pic(avatarURL);
        } else if(mContact!=null) {
            request.setProfile_pic(mContact.getProfilePic());
        }

        return request;
    }

    @Override
    public void showLoadingDialog(String msg) {
        if(mLoadingDialog==null) {
            mLoadingDialog = ProgressDialog.show(this, "",
                    "", true);
        }
        String message = msg != null ? msg : getString(R.string.saving_contact);
        mLoadingDialog.setMessage(message);
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
        if(mContactId==-1){
            Intent detailIntent = new Intent(this, ContactDetailActivity.class);
            detailIntent.putExtra(ContactDetailActivity.CONTACT_ID, mContact.getId());
            startActivity(detailIntent);
        }
    }

    @Override
    public void submitContact() {
        if (mContactId == -1) {
            saveNewContact();
        } else {
            updateExistingContact();
        }
    }

    @Override
    public void updateData(ContactModel contact) {
        mContact = contact;
        loadContactToView();
    }

    @Override
    public void showSnackbar(String msg) {
        Snackbar.make(mMainLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setImageURL(String url) {
        avatarURL = url;
    }

    @OnClick(R.id.contact_form_avatar_btn)
    public void selectImage() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_picture))
                .setPositiveButton(getString(R.string.take_picture), (dialog, whichButton) -> {
                    checkCameraPermission();
                })
                .setNegativeButton(getString(R.string.choose_gallery), (dialog, whichButton) -> {
                    checkGalleryPermission();
                }).show();
    }

    private void checkCameraPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                        !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_ASK_PERMISSIONS_CAMERA);
                } else {
                    openCamera();
                }
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    private void checkGalleryPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_ASK_PERMISSIONS_STORAGE);
                } else {
                    openGallery();
                }
            } else {
                openGallery();
            }
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_ASK_PERMISSIONS_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openCamera();
                } else {
                    // Permission Denied
                    Toast.makeText(this, getString(R.string.access_camera_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.REQUEST_CODE_ASK_PERMISSIONS_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openGallery();
                } else {
                    // Permission Denied
                    Toast.makeText(this, getString(R.string.access_storage_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openCamera(){
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = BitmapUtils.createTempImageFile(this);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    photoFilePaths = photoFile.getAbsolutePath();
                    photoSelected = FileProvider.getUriForFile(this,
                            Constants.FILE_PROVIDER_AUTHORITY,
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoSelected);
                    startActivityForResult(takePictureIntent, ACTIVITY_PHOTO_RESULT);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");

        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_gallery)), ACTIVITY_GALERY_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case ACTIVITY_PHOTO_RESULT://
                if (resultCode == RESULT_OK) {
                    updateImageToView();
                } else {
                    BitmapUtils.deleteImageFile(this, photoFilePaths);
                }
                break;
            case ACTIVITY_GALERY_RESULT:
                if(resultCode == RESULT_OK && imageReturnedIntent != null
                        && imageReturnedIntent.getData() != null) {

                    photoSelected = imageReturnedIntent.getData();
                    if (photoSelected != null) {
                        photoFilePaths = Utils.getRealPathFromURI(this, photoSelected);
                        updateImageToView();
                    }
                }
                break;
        }
    }

    private void updateImageToView(){
        Glide.with(this)
                .load(photoFilePaths)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mContactAvatar);
    }

    private String getBase64(){
        Bitmap bm = BitmapUtils.resamplePic(this, photoFilePaths);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String result = Base64.encodeToString(b, Base64.DEFAULT);

        return result;
    }

}
