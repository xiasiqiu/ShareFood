package com.xinyuan.xyorder.common.bean.store.store;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/191644
 * 店铺经营执照
 */

public class StoreLicenseBean implements Serializable {
    private static final long serialVersionUID = 8758315351211599748L;
    private String beginTime;       // 起始日期

    private String businessUrl;     //营业执照Url

    private String endTime;         //结束日期

    private String legal;           //法定代表人

    private boolean longTerm;       //是否长期

    private String regAddress;      //注册地址

    private String regNumber;       //注册号

    private long shopId;            //店铺ID

    private String subjectDocument;//主体资质 =['BUSINESS_LICENSE''LEGAL_PERSON_CERTIFICATE_OF_INSTITUTION''REGISTRATION_CERTIFICATE_OF_PRIVATE_NON_ENTERPRISE_UNITS', 'SOCIAL_ORGANIZATION_LEGAL_PERSON_REGISTRATION_CERTIFICATE'],

    private String unitName;       //单位名称

    public String getBeginTime() {
        return beginTime;
    }

    public String getBusinessUrl() {
        return businessUrl;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLegal() {
        return legal;
    }

    public boolean isLongTerm() {
        return longTerm;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public long getShopId() {
        return shopId;
    }

    public String getSubjectDocument() {
        return subjectDocument;
    }

    public String getUnitName() {
        return unitName;
    }
}
