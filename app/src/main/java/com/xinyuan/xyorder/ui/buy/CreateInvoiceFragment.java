package com.xinyuan.xyorder.ui.buy;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xinyuan.xyorder.common.bean.buy.InVoiceBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.InvoiceEven;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.EditTextWithDel;
import com.xinyuan.xyorder.widget.NormalDialog;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by f-x on 2017/11/2216:15
 */

public class CreateInvoiceFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.tv_header_right)
    ImageView tv_header_right;

    @BindView(R.id.ll_invoice_num)
    LinearLayout ll_invoice_num;
    @BindView(R.id.et_name)
    EditTextWithDel et_name;
    @BindView(R.id.et_code)
    EditTextWithDel et_code;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.rg_type)
    RadioGroup rg_type;
    @BindView(R.id.rb_invoicecon)
    RadioButton rb_invoicecon;
    @BindView(R.id.rb_invoiceperson)
    RadioButton rb_invoiceperson;
    @BindView(R.id.tv_ok)
    TextView tv_ok;

    private InVoiceBean inVoiceBean;
    private boolean isPerson = false;
    private boolean isFrist = true;

    public static CreateInvoiceFragment newInstance(InVoiceBean inVoiceBean) {
        CreateInvoiceFragment fragment = new CreateInvoiceFragment();
        fragment.inVoiceBean = inVoiceBean;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);

        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
        if (XEmptyUtils.isEmpty(inVoiceBean)) {
            tv_header_right.setVisibility(View.GONE);
            tv_header_center.setText("新增发票信息");
            isFrist = true;
        } else {
            tv_header_right.setVisibility(View.VISIBLE);
            tv_header_center.setText("编辑发票信息");
            isFrist = false;
            rb_invoicecon.setEnabled(false);
            rb_invoiceperson.setEnabled(false);
            et_name.setText(inVoiceBean.getTitle());
            et_code.setText(inVoiceBean.getEin());
            if (inVoiceBean.getInvoiceType().equals(Constants.COMPANY)) {
                isPerson = false;
                rb_invoicecon.setChecked(true);
                rb_invoiceperson.setChecked(false);
                ll_invoice_num.setVisibility(View.VISIBLE);

            } else {
                isPerson = true;
                rb_invoicecon.setChecked(false);
                rb_invoiceperson.setChecked(true);
                ll_invoice_num.setVisibility(View.GONE);

            }
        }


    }


    @Override
    public void initListener() {
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_invoicecon:
                        ll_invoice_num.setVisibility(View.VISIBLE);
                        isPerson = false;
                        break;
                    case R.id.rb_invoiceperson:
                        ll_invoice_num.setVisibility(View.GONE);
                        isPerson = true;

                        break;
                }
            }
        });
        tv_header_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NormalDialog calDialog = new NormalDialog(getActivity());
                calDialog.setMessage("确认要删除这条发票信息？");
                calDialog.setEnterText("确定");
                calDialog.setCancleText("取消");
                calDialog.setClickListener(new NormalDialog.DialogClickListener() {
                    @Override
                    public void enterListener() {
                        deleteInvoice(inVoiceBean.getInvoiceInfoId());
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
                calDialog.show();

            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InVoiceBean newInVoiceBean = new InVoiceBean();
                if (isPerson) {
                    if (XEmptyUtils.isEmpty(et_name.getText().toString().trim()) || XEmptyUtils.isSpace(et_name.getText().toString().trim())) {
                        XToast.error("请填写抬头名称");
                        return;
                    }
                    newInVoiceBean.setInvoiceType(Constants.INDIVIDUAL);
                    newInVoiceBean.setTitle(et_name.getText().toString().trim());
                } else {
                    if (XEmptyUtils.isEmpty(et_name.getText().toString().trim()) || XEmptyUtils.isSpace(et_name.getText().toString().trim())) {
                        XToast.error("请填写抬头名称");
                        return;
                    }
                    if (XEmptyUtils.isEmpty(et_code.getText().toString().trim()) || XEmptyUtils.isSpace(et_code.getText().toString().trim())) {
                        XToast.error("请填写税号或社会信用代码");
                        return;
                    }
                    newInVoiceBean.setEin(et_code.getText().toString().trim());
                    newInVoiceBean.setInvoiceType(Constants.COMPANY);
                    newInVoiceBean.setTitle(et_name.getText().toString().trim());
                }
                Gson gson = new Gson();
                if (isFrist) {
                    String json = gson.toJson(newInVoiceBean);
                    createInvoice(json);

                } else {
                    newInVoiceBean.setInvoiceInfoId(inVoiceBean.getInvoiceInfoId());
                    String json = gson.toJson(newInVoiceBean);
                    updateInvoice(json, inVoiceBean.getInvoiceInfoId());
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    /**
     * 删除发票
     */
    public void deleteInvoice(long id) {
        OkGo.<HttpResponseData<InVoiceBean>>delete(Constants.API_INVOICEINFO + "/" + id)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new DialogCallback<HttpResponseData<InVoiceBean>>(getActivity(), "删除中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<InVoiceBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            EventBus.getDefault().post(new InvoiceEven(InvoiceEven.FLASH));
                            XToast.info("已删除");
                            _mActivity.onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<InVoiceBean>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);
                    }
                });
    }

    /**
     * 新增发票
     */

    public void createInvoice(String info) {
        OkGo.<HttpResponseData<InVoiceBean>>put(Constants.API_INVOICEINFO)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .upJson(info)
                .execute(new DialogCallback<HttpResponseData<InVoiceBean>>(getActivity(), "添加中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<InVoiceBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            EventBus.getDefault().post(new InvoiceEven(InvoiceEven.FLASH));

                            XToast.info("已添加");
                            _mActivity.onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<InVoiceBean>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);
                    }
                });
    }


    /**
     * 编辑发票
     *
     * @param info
     * @param id
     */
    public void updateInvoice(String info, long id) {
        OkGo.<HttpResponseData<InVoiceBean>>post(Constants.API_INVOICEINFO + "/" + id)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .upJson(info)
                .execute(new DialogCallback<HttpResponseData<InVoiceBean>>(getActivity(), "修改中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<InVoiceBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            EventBus.getDefault().post(new InvoiceEven(InvoiceEven.FLASH));

                            XToast.info("已修改");
                            _mActivity.onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<InVoiceBean>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);
                    }
                });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_create_invoice;
    }
}
