package com.xinyuan.xyorder.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.xinyuan.xyorder.R;

public class NormalDialog extends Dialog {

    private TextView message;
    private TextView cancel,enter;
    private View divider;
    private DialogClickListener clickListenerInterface;

    public interface DialogClickListener{
        public void enterListener();
        public void cancelListener();
    }

    public NormalDialog(Context context) {
        super(context, R.style.edit_dialog);
        init();
    }

    private void init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_normal);

        message = (TextView) findViewById(R.id.message_tv);
        cancel = (TextView) findViewById(R.id.cancel_tv);
        enter = (TextView) findViewById(R.id.enter_tv);
        divider = findViewById(R.id.divider);

        cancel.setOnClickListener(new ClickListener());
        enter.setOnClickListener(new ClickListener());
    }

    public void setMessage(int resId){
        message.setText(resId);
    }

    public void setMessage(String msg){
        message.setText(msg);
    }

    public void setEnterText(String et){
        enter.setText(et);
    }

    public void setCancleText(String et){
        cancel.setText(et);
    }
    public void setClickListener(DialogClickListener clickListener){
        clickListenerInterface = clickListener;
    }

    public void isNoCancel(){
        cancel.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        clickListenerInterface.cancelListener();
    }

    private class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cancel_tv:
                    clickListenerInterface.cancelListener();
                    dismiss();
                    break;
                case R.id.enter_tv:
                    clickListenerInterface.enterListener();
                    dismiss();
                    break;
            }
        }
    }

}
