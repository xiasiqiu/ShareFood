package com.xinyuan.xyorder.app;

import android.app.Application;
import android.content.Context;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.taobao.sophix.SophixManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.ImagePickerLoader;
import com.xinyuan.xyorder.util.LocationManager;
import com.xinyuan.xyorder.util.ScreenAdaptation;
import com.youth.xframe.XFrame;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import me.yokeyword.fragmentation.Fragmentation;
import okhttp3.OkHttpClient;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/15
 */
public class MyApplication extends Application {

    public static ImagePicker imagePicker;
    //    public static String Token;
    public static String cid;
    private IWXAPI api;
    public static Context mApplicationContext;


    @Override
    public void onCreate() {
        super.onCreate();
        SophixManager.getInstance().queryAndLoadNewPatch();
        new ScreenAdaptation(this, 720, 1280).register();
        mApplicationContext = this;
        XFrame.init(this);
        Fragmentation.builder().stackViewMode(Fragmentation.NONE)
                .install();
        LocationManager.getInstance().getAMapLocationClient(getApplicationContext());
        getOKHttp();
        getImageSelect();
        regToWx();

    }

    public static Context getMyApplicationContext() {
        return mApplicationContext;
    }


    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.registerApp(Constants.WX_APP_ID);
    }

    /**
     * 网络框架加载
     */
    private void getOKHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("TOKEN", DataUtil.getToken(this));
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        try {
            OkGo.getInstance().init(this)                           //必须调用初始化
                    .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                    .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                    .addCommonHeaders(httpHeaders)
                    .setRetryCount(3);
            //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 设置图片选择器
     */
    private void getImageSelect() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImagePickerLoader());   //设置图片加载器
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }


}
