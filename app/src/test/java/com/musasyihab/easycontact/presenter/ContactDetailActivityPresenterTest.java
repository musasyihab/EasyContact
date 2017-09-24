package com.musasyihab.easycontact.presenter;

import com.musasyihab.easycontact.RxJavaTestPlugins;
import com.musasyihab.easycontact.contactdetail.ContactDetailActivityVP;
import com.musasyihab.easycontact.contactdetail.ContactDetailPresenter;
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

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by musasyihab on 9/25/17.
 */

@Config(sdk = 18, manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ContactDetailActivityPresenterTest {
    private final static ContactModel CONTACT_SAMPLE1 = new ContactModel(1, "John", "Doe", null, null, null, false, null, null);
    private final static ContactModel CONTACT_SAMPLE1_FAV = new ContactModel(1, "John", "Doe", null, null, null, true, null, null);

    @Mock
    private ContactRepository mContactsRepository;

    @Mock
    private ContactDetailActivityVP.View mContactDetailView;

    private ContactDetailPresenter mContactDetailPresenter;

    @Before
    public void setupTasksPresenter() {
        RxJavaTestPlugins.setMainThreadScheduler();

        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mContactDetailPresenter = new ContactDetailPresenter(mContactsRepository);
        mContactDetailPresenter.setView(mContactDetailView);

    }

    @After
    public void tearDown() {
        RxJavaTestPlugins.resetJavaTestPlugins();
    }

    @Test
    public void fetchData(){
        mockFetchDataSuccess(mContactsRepository);

        TestSubscriber<ContactModel> testSubscriber = new TestSubscriber<>();
        mContactsRepository.getContactDetailFromNetwork(1).subscribe(testSubscriber);

        mContactDetailPresenter.loadData(1);

        verify(mContactDetailView).showLoading();
        verify(mContactDetailView).updateData(CONTACT_SAMPLE1);

        ContactModel actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_SAMPLE1), true);

    }

    @Test
    public void loadLocalData(){
        mockLoadLocalData(mContactsRepository);

        TestSubscriber<ContactModel> testSubscriber = new TestSubscriber<>();
        mContactsRepository.getContactDetailFromDatabase(1).subscribe(testSubscriber);

        mContactDetailPresenter.loadLocalData(1);

        verify(mContactDetailView).showLoading();
        verify(mContactDetailView).updateData(CONTACT_SAMPLE1);

        ContactModel actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_SAMPLE1), true);

    }

    @Test
    public void loadLocalDataIfFetchNetworkFail(){
        mockFetchDataFail(mContactsRepository);
        mockLoadLocalData(mContactsRepository);

        mContactDetailPresenter.loadData(1);

        verify(mContactDetailView).showSnackbar("Network Error");

        verify(mContactDetailView, atLeastOnce()).showLoading();
        verify(mContactDetailView).updateData(CONTACT_SAMPLE1);

        TestSubscriber<ContactModel> testSubscriber = new TestSubscriber<>();
        mContactsRepository.getContactDetailFromDatabase(1).subscribe(testSubscriber);

        ContactModel actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_SAMPLE1), true);

    }

    @Test
    public void changeContactFavorite(){
        mockChangeFavorite(mContactsRepository);

        TestSubscriber<ContactModel> testSubscriber = new TestSubscriber<>();
        mContactsRepository.updateContactFavorite(CONTACT_SAMPLE1, 1).subscribe(testSubscriber);

        mContactDetailPresenter.updateFavorite(CONTACT_SAMPLE1, 1);

        verify(mContactDetailView).showLoadingDialog();
        verify(mContactDetailView).updateData(CONTACT_SAMPLE1_FAV);

        ContactModel actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_SAMPLE1_FAV), true);

    }


    private void mockFetchDataSuccess(ContactRepository contactRepository) {
        when(contactRepository.getContactDetailFromNetwork(1)).thenReturn(Observable.just(CONTACT_SAMPLE1).concatWith(Observable.<ContactModel>never()));
    }

    private void mockFetchDataFail(ContactRepository contactRepository) {
        Exception e = new Exception("Network Error");
        when(contactRepository.getContactDetailFromNetwork(1)).thenReturn(Observable.error(e));
    }

    private void mockLoadLocalData(ContactRepository contactRepository) {
        when(contactRepository.getContactDetailFromDatabase(1)).thenReturn(Observable.just(CONTACT_SAMPLE1).concatWith(Observable.<ContactModel>never()));
    }

    private void mockChangeFavorite(ContactRepository contactRepository) {
        when(contactRepository.updateContactFavorite(CONTACT_SAMPLE1, 1)).thenReturn(Observable.just(CONTACT_SAMPLE1_FAV).concatWith(Observable.<ContactModel>never()));
    }
}
