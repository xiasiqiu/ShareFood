package com.xinyuan.xyorder.app;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.multidex.MultiDex;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.xinyuan.xyorder.BuildConfig;
import com.youth.xframe.utils.log.XLog;

/**
 * Created by f-x on 2018/1/9  16:16
 * Description
 */

public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";

    @Keep
    @SophixEntry(MyApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
        MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setSecretMetaData("24759141-1", "7a7536a17d79795889f35ca60a97364c", "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCc9d3yMnWD3mqgZiBHVKCjyu1buTHSvD9OtbxxwJUqj2YSBkbVEQ4X2ZjvZFWxspIgXO/5vBRxtmQ1cgdzPHgv6nuMBOvqnt1iNiMZUKJIFQYZdLij4DP3ubuX3GSEkC9FFgTiu35MszTm/KHvTw3HNUXCnk6C+efzJhG8kD7pCT2GvToZe0Bnvz3gENtvZQTPKIIAdsI2mHcmAYEfDDTNDNvgtchfH3amwPNIJeAXeip39Ppd3e9GKXmJkGePdhhYxlfA2IJMm/p2H9zyxVYMb4Zh+UFNsadCK4+10hwbBWkCUfiOHvxjn76OXRXzYMDZPsbpNz0z9spV9OnBWWr1AgMBAAECggEAX42KPTycr9fCP72MXwBU/wG1uAYpH6p8O9WFUBa2eH9t2wE5M9K7KqfMY8hIJvKX5N3PNdBa/dLUdHjjsjKY1ErCRb5lRKLMEVL10R52fL1Xxj15fkIqQOAwJMYF53HDZSJ/fOw5ekYKqagYiRAuShzTSCSJlVC/9RUy7QE/WyvQ7Rtw8I5tZX77BU/a56fntwnYx2rz1JsegEeEV8wO993hAo0gN/evDnKy+SBzSY2MZPNGfS7AMi0JX25u07QnxLc8fHg8N3USIQO7GFSC+cP7eQYzcsJ36CsCApw8uejLlPJKnN37NblDGOIItYZcnQP2dsnsdryzcwChPJBc7QKBgQDaqHTiywRg4n/k9zwWY6d/OslM8caD33tsdqaTAmdALNXEUmn8zqcEI+oWppARH57vMuy/aZcMSXGhNV36nD1g9cSj5gS2C3hxwDpinTKPkJx3TBELFHs0aePbP/7kFuCsxlAPvkcU2iAHR5DrmJciS9tU6Se5c1OIkY2Df9GaBwKBgQC3xAsGQmlpXFibGBOoGqQFKN9O0WmSAhajKieMjZXdi8x+wAEGYKlfhhTKmhTg7ZJdpxDe2twJS1abBBnMHNbXlEq772GZPry35ANJz+godK7hjCchEytu/0ytNKNWKPo3XNq9R/uHSol1FTDHdajWITpU4jcBuU1128JxIxzEIwKBgA876VjbuCRf43U6XG9a5hKRL/EybxhE+75kzdo0Mqlf9kzFSSkZ5Lz/G5IUbLkksNgNfj0fyCxM+aO3qiKJvCUDOn4Iyo88YVnbgLXaKemis6QEIrPY63SNZBikbsfJaEHhVzahlSY4xD59l/UyHmV/T3qs+92J0WoUaf2dbCcFAoGARiAQmO3k7ejogx7X+XFZIALt3FT7dVVIsDEY8IDjLKz5d3DrszRVzfcLnhjYWNEv5n8nZ/Oavg062mLhOUvUDGYxcrAxzVnsiDwBqTwmerG0PqvODsgS7gOMjhglMG7otRHBAzQh3wnplH1mpObYbAY4DUZZ+VVuIsie1a+xfYMCgYAlXS97pmrvC0Bl7LiZiRF3Omk5xRkVbyRFKyiMzDRZzBkxlHWIgKOoMLtO2vQ0pfwtdjFs38UjhlCWu0msWTUU/fjQk+jhQmOZhOmopprYThGbeDSqDiI5YlTJ03o1g22fd5R7+HJCuIhrcFqn1TOTdlcok2vQULUrk+oTYiZNlQ==")
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            XLog.e("--------------------------sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            XLog.e("-----------------------sophix preload patch success. restart app to make effect.");
                        } else {
                            XLog.e("-----------------------sophix preload patch error.code=" + code);

                        }
                    }
                }).initialize();
    }
}
