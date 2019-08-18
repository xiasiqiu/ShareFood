package com.xinyuan.xyorder.common.bean.mine;

import java.io.Serializable;

/**
 * Created by f-x on 2017/10/1916:53
 * 身份证
 */

public class UserCardBean implements Serializable {

    private static final long serialVersionUID = -2696561221323282931L;
    private String documentType;            // 证件类型
    private String fullFacePhotoUrl;        //正面照
    private String handFullFacePhotoUrl;    //手持证件正面照
    private String readyName;               //真实姓名
    private String reverseSideAsUrl;        //反面照
    private long shopId;                    //店铺ID

    public String getDocumentType() {
        return documentType;
    }

    public String getFullFacePhotoUrl() {
        return fullFacePhotoUrl;
    }

    public String getHandFullFacePhotoUrl() {
        return handFullFacePhotoUrl;
    }

    public String getReadyName() {
        return readyName;
    }

    public String getReverseSideAsUrl() {
        return reverseSideAsUrl;
    }

    public long getShopId() {
        return shopId;
    }
}
