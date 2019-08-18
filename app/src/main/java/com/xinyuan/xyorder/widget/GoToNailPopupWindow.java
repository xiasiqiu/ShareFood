package com.xinyuan.xyorder.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinyuan.xyorder.R;


public class GoToNailPopupWindow extends PopupWindow{

    private Activity activity;
    private TextView title;
    private TextView baidu;
    private TextView gaode;
    private TextView cancel;
    private onNailListener listener;   
    private View naviView;
    

    public GoToNailPopupWindow(Activity context) {
    	this.activity = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        naviView = inflater.inflate(R.layout.navigation, null);
        setContentView(naviView);

        initPopWindow();
        initView();
    }

    private void initPopWindow(){
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        naviView.setFocusable(true);// 这个很重要
        naviView.setFocusableInTouchMode(true);

//        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        setBackgroundDrawable(dw);

        getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    showWindowAlpha();
                }
                return false;
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                showWindowAlpha();
            }
        });
        update();// 刷新状态
    }

    private void initView(){
        baidu = (TextView)naviView.findViewById(R.id.baidu);
        gaode = (TextView)naviView.findViewById(R.id.gaode);
        cancel = (TextView)naviView.findViewById(R.id.cancel);

        baidu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 dismiss();
	             showWindowAlpha();
	             if(listener != null){
	            	 listener.onBaiduListener();
	             }
			}
		});
        
        gaode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 dismiss();
	             showWindowAlpha();
	             if(listener != null){
	            	 listener.onGaodeListener();
	             }
			}
		});
        
        cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 dismiss();
	             showWindowAlpha();
			}
		});
    }

    public void showPopupWindow(View view){
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.7f;
        activity.getWindow().setAttributes(params);
        showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void showWindowAlpha() {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 1.0f;
        activity.getWindow().setAttributes(params);
    }

    public void setOnNailListener(onNailListener listener){
        this.listener = listener;
    }

    public interface onNailListener{
    	void onBaiduListener();
    	void onGaodeListener();
    }

}
