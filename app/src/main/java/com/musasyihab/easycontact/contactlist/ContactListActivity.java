package com.musasyihab.easycontact.contactlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.musasyihab.easycontact.BaseActivity;
import com.musasyihab.easycontact.R;
import com.musasyihab.easycontact.adapter.ContactListAdapter;
import com.musasyihab.easycontact.contactdetail.ContactDetailActivity;
import com.musasyihab.easycontact.contactform.ContactFormActivity;
import com.musasyihab.easycontact.data.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactListActivity extends BaseActivity implements ContactListActivityVP.View, ContactListAdapter.ContactListListener {

    private static final String SCROLL_POSITION = "SCROLL_POSITION";

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
    private ContactListAdapter mAdapter;
    private int scrollPosition = 0;
    private boolean dataFetched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        getAppComponent().inject(this);

        ButterKnife.bind(this);

        if (savedInstanceState!=null){
            if(savedInstanceState.containsKey(SCROLL_POSITION)){
                scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
            }
        }

    }

    private void loadContactToView(){
        if(mContactList.size()>0){
            mAdapter = new ContactListAdapter(mContactList, this);
            mContactListView.setHasFixedSize(true);
            mContactListView.setLayoutManager(new LinearLayoutManager(this));
            mContactListView.setAdapter(mAdapter);
            mContactListView.scrollToPosition(scrollPosition);

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
        if(!dataFetched) {
            presenter.setView(this);
            mContactList = new ArrayList<>();
            presenter.fetchData();
            dataFetched = true;
        } else {
            mContactList = new ArrayList<>();
            presenter.loadLocalData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mContactListView != null)
            scrollPosition = ((LinearLayoutManager) mContactListView.getLayoutManager()).findFirstVisibleItemPosition();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SCROLL_POSITION, scrollPosition);
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
        mContactList.add(contact);
    }

    @Override
    public void showSnackbar(String msg) {
        Snackbar.make(mMainLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onContactClick(ContactModel clickedItem) {
        Intent detailIntent = new Intent(this, ContactDetailActivity.class);
        detailIntent.putExtra(ContactDetailActivity.CONTACT_ID, clickedItem.getId());
        startActivity(detailIntent);
    }

    @OnClick(R.id.contact_list_add_fab)
    public void onAddClick(){
        Intent formIntent = new Intent(this, ContactFormActivity.class);
        startActivity(formIntent);
    }
}
