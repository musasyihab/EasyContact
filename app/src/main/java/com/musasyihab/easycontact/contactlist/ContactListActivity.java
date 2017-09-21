package com.musasyihab.easycontact.contactlist;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musasyihab.easycontact.BaseActivity;
import com.musasyihab.easycontact.R;
import com.musasyihab.easycontact.adapter.ContactListAdapter;
import com.musasyihab.easycontact.data.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactListActivity extends BaseActivity implements ContactListActivityVP.View, ContactListAdapter.ContactListListener {

    private static final String CONTACT_LIST = "CONTACT_LIST";
    private static final String CONTACT_ID_LIST = "CONTACT_ID_LIST";

    @Inject
    ContactListPresenter presenter;

    @BindView(R.id.contact_list_main_layout)
    CoordinatorLayout mMainLayout;
    @BindView(R.id.contact_list_view)
    RecyclerView mContactListView;
    @BindView(R.id.contact_list_empty)
    TextView mContactListEmpty;
    @BindView(R.id.contact_list_loading)
    ProgressBar mContactListLoading;

    private List<ContactModel> mContactList = new ArrayList<>();
    private List<Integer> mContactIDList = new ArrayList<>();
    private ContactListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        getAppComponent().inject(this);

        ButterKnife.bind(this);

        if (savedInstanceState!=null){
            if(savedInstanceState.containsKey(CONTACT_LIST)){
                String sContacts = savedInstanceState.getString(CONTACT_LIST);
                mContactList = new Gson().fromJson(sContacts,
                        new TypeToken<List<ContactModel>>() {}.getType());
                loadContactToView();
            }
            if(savedInstanceState.containsKey(CONTACT_ID_LIST)){
                String sContactIds = savedInstanceState.getString(CONTACT_ID_LIST);
                mContactIDList = new Gson().fromJson(sContactIds,
                        new TypeToken<List<ContactModel>>() {}.getType());
            }
        }

    }

    private void loadContactToView(){
        if(mContactList.size()>0){
            mAdapter = new ContactListAdapter(mContactList, this);
            mContactListView.setHasFixedSize(true);
            mContactListView.setLayoutManager(new LinearLayoutManager(this));
            mContactListView.setAdapter(mAdapter);

            mContactListView.setVisibility(View.VISIBLE);
            mContactListEmpty.setVisibility(View.GONE);
        } else {
            mContactListView.setVisibility(View.GONE);
            mContactListEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mContactList.size()==0 && mContactIDList.size()==0) {
            presenter.setView(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String contactList = new Gson().toJson(mContactList);
        String contactIdList = new Gson().toJson(mContactIDList);
        outState.putString(CONTACT_LIST, contactList);
        outState.putString(CONTACT_ID_LIST, contactIdList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showLoading() {
        mContactListView.setVisibility(View.GONE);
        mContactListEmpty.setVisibility(View.GONE);
        mContactListLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadContactToView();
        mContactListLoading.setVisibility(View.GONE);
    }

    @Override
    public void updateData(ContactModel contact) {
        if(!mContactIDList.contains(contact.getId())) {
            mContactIDList.add(contact.getId());
            mContactList.add(contact);
        }
    }

    @Override
    public void showSnackbar(String msg) {
        Snackbar.make(mMainLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onContactClick(ContactModel clickedItem) {
        Toast.makeText(this, "Click on contact id: "+clickedItem.getId(), Toast.LENGTH_SHORT).show();
    }
}
