package com.xinyuan.xyorder.ui.mine.address;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.AddressPageEvent;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.ScreenUtils;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.XRegexUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fx on 2017/10/15.
 */

public class CreateAddressFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.et_user_phone)
    EditText et_user_phone;
    @BindView(R.id.tv_user_address)
    TextView tv_user_address;
    @BindView(R.id.et_user_name)
    EditText et_user_name;
    @BindView(R.id.rg_sex)
    RadioGroup rg_sex;
    @BindView(R.id.rb_male)
    RadioButton rb_male;
    @BindView(R.id.rb_female)
    RadioButton rb_female;
    @BindView(R.id.et_house_number)
    EditText et_house_number;

    private String address;
    private String detailAddress;
    private String simpleAddress;
    private Double lat, lng;
    private String sex = "MALE";
    private long contactId;
    private AddressInfo addressInfo;

    public static CreateAddressFragment newInstance(AddressInfo addressBean) {
        CreateAddressFragment fragment = new CreateAddressFragment();
        fragment.addressInfo = addressBean;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("");
        if (!XEmptyUtils.isEmpty(addressInfo)) {
            tv_user_address.setText(addressInfo.getAddress());
            et_user_name.setText(addressInfo.getContactName());
            et_user_phone.setText(addressInfo.getContactPhone());
            et_house_number.setText(addressInfo.getHouseNumber());
            address = addressInfo.getAddress();
            lat = addressInfo.getLatitude();
            lng = addressInfo.getLongitude();
            if (addressInfo.getGender().equals("MALE")) {
                rb_male.setChecked(true);
            } else {
                rb_female.setChecked(true);
            }
            tv_header_center.setText("修改收货地址");
        } else {
            tv_header_center.setText("新增收货地址");
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_female:
                        sex = "FEMALE";
                        break;
                    case R.id.rb_male:
                        sex = "MALE";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 編輯地址
     */
    private void editAddress() {
        AddressInfo addressInfo1 = new AddressInfo(address, et_user_name.getText().toString(), (et_user_phone.getText().toString().replaceAll(" ", "")), sex, lat, lng, addressInfo.getContactId(), et_house_number.getText().toString(), detailAddress, simpleAddress);
        String json = new Gson().toJson(addressInfo1);
        OkGo.<HttpResponseData<Void>>post(Constants.API_ADD_USER + "/" + addressInfo.getContactId())
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .upJson(json)
                .execute(new DialogCallback<HttpResponseData<Void>>(getActivity(), "修改中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<Void>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            EventBus.getDefault().post(new AddressPageEvent(AddressPageEvent.FALSH));
                            _mActivity.onBackPressed();
                            ScreenUtils.hideSoftInput(_mActivity);
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<Void>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });

    }

    /**
     * 新增地址
     */
    public void addAddress() {
        AddressInfo addressInfo = new AddressInfo(address, et_user_name.getText().toString(), (et_user_phone.getText().toString().replaceAll(" ", "")), sex, lat, lng, contactId, et_house_number.getText().toString(), detailAddress, simpleAddress);
        String json = new Gson().toJson(addressInfo);
        OkGo.<HttpResponseData<Void>>put(Constants.API_ADD_USER)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .upJson(json)
                .execute(new DialogCallback<HttpResponseData<Void>>(getActivity(), "添加中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<Void>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            EventBus.getDefault().post(new AddressPageEvent(AddressPageEvent.FALSH));
                            _mActivity.onBackPressed();

                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<Void>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });

    }


    @Subscribe
    public void onEvent(AddressPageEvent addressInfo) {
        if (AddressPageEvent.CHOOSELOCATION.equals(addressInfo.getFlag())) {
            AddressInfo info = (AddressInfo) addressInfo.getObj();
            address = info.getAddress();
            tv_user_address.setText(address);
            lat = info.getLatitude();
            lng = info.getLongitude();
            detailAddress = info.getDetailAddress();
            simpleAddress = info.getSimpleAddress();
        }
    }

    private void getContact() {
        XPermission.requestPermissions(getActivity(), 100, new String[]{Manifest.permission
                .READ_CONTACTS}, new XPermission.OnPermissionListener() {
            //权限申请成功时调用
            @Override
            public void onPermissionGranted() {
                startActivityForResult(new Intent(
                        Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);

            }

            //权限被用户禁止时调用
            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(getActivity());
            }
        });
    }


    @OnClick({R.id.tv_contact, R.id.tv_ok, R.id.iv_choose_address, R.id.tv_user_address, R.id.iv_header_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_contact:
                getContact();
                break;
            case R.id.iv_choose_address:
            case R.id.tv_user_address:
                start(ConfirmAddressFragment.newInstance(addressInfo));
                ScreenUtils.hideSoftInput(_mActivity);
                break;
            case R.id.tv_ok:
                if (XEmptyUtils.isEmpty(et_user_phone.getText().toString().trim())) {
                    XToast.error("请填写联系电话");
                    return;
                }
                if (!XRegexUtils.checkMobile((et_user_phone.getText().toString().replaceAll(" ", "")))) {
                    XToast.error("手机号格式不正确");
                    return;
                }

                if (XEmptyUtils.isEmpty(address) || XEmptyUtils.isEmpty(lat) || XEmptyUtils.isEmpty(lng)) {
                    XToast.error("请填写收货地址");
                    return;
                }
                if (XEmptyUtils.isEmpty(et_user_name.getText().toString())) {
                    XToast.error("请填写联系人");
                    return;
                }
                if(et_user_name.getText().toString().length()>10 ||et_user_name.getText().toString().length()<1){
                    XToast.error("联系人字数在1-10之间");
                    return;
                }
                ScreenUtils.hideSoftInput(_mActivity);
                if (!XEmptyUtils.isEmpty(addressInfo)) {
                    editAddress();
                } else {
                    addAddress();
                }
                break;
            case R.id.iv_header_left:
                _mActivity.onBackPressed();
                ScreenUtils.hideSoftInput(_mActivity);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver reContentResolverol = getActivity().getContentResolver();
            Uri contactData = data.getData();
            @SuppressWarnings("deprecation")
            Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);// TODO: 2017/10/15  managedQuery已经过时
            cursor.moveToFirst();
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phone.moveToNext()) {
                String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                et_user_phone.setText(usernumber);
            }
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_create_address;
    }

    @Override
    public void onStart() {
        registerEventBus(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        unregisterEventBus(this);
        super.onStop();
    }
}
