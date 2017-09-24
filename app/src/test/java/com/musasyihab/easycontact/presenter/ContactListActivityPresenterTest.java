package com.musasyihab.easycontact.presenter;

import com.google.common.collect.Lists;
import com.musasyihab.easycontact.RxJavaTestPlugins;
import com.musasyihab.easycontact.contactlist.ContactListActivityVP;
import com.musasyihab.easycontact.contactlist.ContactListPresenter;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.data.repository.ContactRepository;
import com.musasyihab.easycontact.util.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by musasyihab on 9/24/17.
 */

@Config(sdk = 18, manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ContactListActivityPresenterTest {
    private final static ContactModel CONTACT_SAMPLE1 = new ContactModel(1, "John", "Doe", null, null, null, false, null, null);
    private final static ContactModel CONTACT_SAMPLE2 = new ContactModel(2, "Jane", "Doe", null, null, null, false, null, null);

    private static List<ContactModel> CONTACT_LIST = Lists.newArrayList(CONTACT_SAMPLE1, CONTACT_SAMPLE2);

    @Mock
    private ContactRepository mContactsRepository;

    @Mock
    private ContactListActivityVP.View mContactListView;

    private ContactListPresenter mContactListPresenter;

    @Before
    public void setupTasksPresenter() {
        RxJavaTestPlugins.setMainThreadScheduler();

        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mContactListPresenter = new ContactListPresenter(mContactsRepository);
        mContactListPresenter.setView(mContactListView);

    }

    @After
    public void tearDown() {
        RxJavaTestPlugins.resetJavaTestPlugins();
    }

    @Test
    public void fetchData(){
        mockFetchDataSuccess(mContactsRepository);

        TestSubscriber<List<ContactModel>> testSubscriber = new TestSubscriber<>();
        mContactsRepository.getAllContactsFromNetwork().subscribe(testSubscriber);

        mContactListPresenter.fetchData();

        verify(mContactListView).showLoading();
        verify(mContactListView).updateData(CONTACT_SAMPLE1);

        List<ContactModel> actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_LIST), true);

    }

    @Test
    public void loadLocalData(){
        mockLoadLocalData(mContactsRepository);

        TestSubscriber<List<ContactModel>> testSubscriber = new TestSubscriber<>();
        mContactsRepository.getAllContactsFromDatabase().subscribe(testSubscriber);

        mContactListPresenter.loadLocalData();

        verify(mContactListView).showLoading();
        verify(mContactListView).updateData(CONTACT_SAMPLE1);
        verify(mContactListView).updateData(CONTACT_SAMPLE2);

        List<ContactModel> actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_LIST), true);

    }

    @Test
    public void loadLocalDataIfFetchNetworkFail(){
        mockFetchDataFail(mContactsRepository);
        mockLoadLocalData(mContactsRepository);

        mContactListPresenter.fetchData();

        verify(mContactListView).showSnackbar("Network Error");

        verify(mContactListView, atLeastOnce()).showLoading();
        verify(mContactListView).updateData(CONTACT_SAMPLE1);
        verify(mContactListView).updateData(CONTACT_SAMPLE2);

        TestSubscriber<List<ContactModel>> testSubscriber = new TestSubscriber<>();
        mContactsRepository.getAllContactsFromDatabase().subscribe(testSubscriber);

        List<ContactModel> actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_LIST), true);

    }



    private void mockFetchDataSuccess(ContactRepository contactRepository) {
        when(contactRepository.getAllContactsFromNetwork()).thenReturn(Observable.just(CONTACT_LIST).concatWith(Observable.<List<ContactModel>>never()));
    }

    private void mockFetchDataFail(ContactRepository contactRepository) {
        Exception e = new Exception("Network Error");
        when(contactRepository.getAllContactsFromNetwork()).thenReturn(Observable.error(e));
    }

    private void mockLoadLocalData(ContactRepository contactRepository) {
        when(contactRepository.getAllContactsFromDatabase()).thenReturn(Observable.just(CONTACT_LIST).concatWith(Observable.<List<ContactModel>>never()));
    }
}
