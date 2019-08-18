package com.xinyuan.xyorder.common.bean;

import java.io.Serializable;

/**
 * Created by f-x on 2017/11/218:00
 */

public class UpdateBean implements Serializable {
    private static final long serialVersionUID = -6650951996225952536L;
    private String appName;
    private String appType;
    private boolean forcedUpdating;
    private boolean isUpdate;
    private String serverPackage;
    private String serverVersion;
    private String upadteTime;
    private String updateContent;
    private String updateUrl;
    private String versionId;

    public String getAppName() {
        return appName;
    }

    public String getAppType() {
        return appType;
    }

    public boolean isForcedUpdating() {
        return forcedUpdating;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public String getServerPackage() {
        return serverPackage;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public String getUpadteTime() {
        return upadteTime;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public String getVersionId() {
        return versionId;
    }

    @Override
    public String toString() {
        return "{\"appName\":\"" + appName + "\"," +
                "\"serverVersion\":\"" + serverVersion + "\"," +
                "\"forcedUpdating\":" +forcedUpdating  + "," +
                "\"updateUrl\":\"" + updateUrl + "\"," +
                "\"updateContent\":\"" + updateContent + "\"," +
                "\"isUpdate\":" + isUpdate + "," +
                "\"upadteTime\":" + "\"" + upadteTime + "\"" + "}";
    }
}
