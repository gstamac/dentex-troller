package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.exifinterface.media.ExifInterface;
import butterknife.BindView;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.AtFriendContactsAdapter;
import im.helmsman.helmsmanandroid.dao.ContactDao;
import im.helmsman.helmsmanandroid.presenter.AtFriendsPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.AtFriendsView;
import im.helmsman.helmsmanandroid.view.SearchView;
import im.helmsman.helmsmanandroid.view.TitleBar;
import im.helmsman.helmsmanandroid.view.WaveSideBar;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AtFriendsActivity extends Mvp2BaseActivity<AtFriendsView, AtFriendsPresenter> implements AtFriendsView, SearchView.OnSearchViewTextChangeListener, WaveSideBar.OnSelectIndexItemListener, AdapterView.OnItemClickListener {
    private AtFriendContactsAdapter adapter;
    private List<ContactDao> contactDao = new ArrayList();
    private InputMethodManager imm;

    @BindView(R.id.iv_search)
    ImageView ivSearch;

    @BindView(R.id.lv_atfriend_contact_list)
    ListView lvAtfriendContactList;

    @BindView(R.id.sv_search_contact)
    SearchView svSearchContact;

    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @BindView(R.id.wsb_at_friend_sidebar)
    WaveSideBar wsbAtFriendSidebar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AtFriendsView
    public void showLoadContactFailedMessage(String str) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public AtFriendsPresenter initPresenter() {
        return new AtFriendsPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_at_friends);
        this.wsbAtFriendSidebar.setOnSelectIndexItemListener(this);
        setStatusBar(R.color.red_state);
        this.svSearchContact.setOnSearchViewTextChangeListener(this);
        this.imm = (InputMethodManager) getSystemService("input_method");
        AtFriendContactsAdapter atFriendContactsAdapter = new AtFriendContactsAdapter(this.contactDao);
        this.adapter = atFriendContactsAdapter;
        atFriendContactsAdapter.setFirstItemTitle(getString(R.string.recent_contacts));
        this.lvAtfriendContactList.setAdapter((ListAdapter) this.adapter);
        this.wsbAtFriendSidebar.setIndexItems("#", ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "B", "C", "D", ExifInterface.LONGITUDE_EAST, ExifInterface.LONGITUDE_EAST, "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", ExifInterface.LATITUDE_SOUTH, ExifInterface.GPS_DIRECTION_TRUE, "U", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, ExifInterface.LONGITUDE_WEST, "X", "Y", "Z");
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        ((AtFriendsPresenter) this.presenter).loadData();
        ((AtFriendsPresenter) this.presenter).updateContactsByNet();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AtFriendsView
    public void updateListView(List<ContactDao> list) {
        this.contactDao.clear();
        this.contactDao.addAll(list);
        this.adapter.notifyDataSetChanged();
        this.lvAtfriendContactList.setOnItemClickListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.view.WaveSideBar.OnSelectIndexItemListener
    public void onSelectIndexItem(String str) {
        for (ContactDao contactDao : this.contactDao) {
            if (str.equals(contactDao.getIndex())) {
                this.lvAtfriendContactList.setSelection(this.contactDao.indexOf(contactDao));
                return;
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        String username = this.contactDao.get(i).getUsername();
        Intent intent = new Intent();
        intent.putExtra("username", username);
        setResult(1, intent);
        finish();
    }

    @OnClick({R.id.iv_search})
    public void onClick() {
        this.titleBar.hideCenterView();
        this.ivSearch.setVisibility(8);
        this.svSearchContact.setVisibility(0);
        this.svSearchContact.getEditText().requestFocus();
        this.imm.toggleSoftInput(2, 0);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.ivSearch.getVisibility() == 8) {
            this.titleBar.showCenterView();
            this.ivSearch.setVisibility(0);
            this.svSearchContact.setVisibility(8);
            this.svSearchContact.setText("");
            this.imm.hideSoftInputFromWindow(this.svSearchContact.getEditText().getWindowToken(), 0);
            this.adapter.setFirstItemTitle(getString(R.string.recent_contacts));
            this.adapter.notifyDataSetChanged();
            return;
        }
        super.onBackPressed();
    }

    @Override // im.helmsman.helmsmanandroid.view.SearchView.OnSearchViewTextChangeListener
    public void onTextChange(String str) {
        ((AtFriendsPresenter) this.presenter).searchDataBaseByUserName(str);
        this.adapter.setFirstItemTitle(getString(R.string.search_resul));
    }
}
