package com.xinyuan.xyorder.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.youth.xframe.widget.XToast;

/**
 * Created by f-x on 2017/10/1817:04
 * 购物车加减控件
 */

public class AddWidget extends FrameLayout {

    private ReduceButton sub;
    private TextView tv_count;
    private int count = 0;
    private AddButton addbutton;
    private boolean sub_anim, circle_anim;
    private GoodBean goodBean;

    public interface OnAddClick {
        void showSpec(GoodBean goodBean);

        void onAddClick(View view, GoodBean gb);

        void onSubClick(GoodBean gb);
    }

    private OnAddClick onAddClick;
    private AddButton.ShowSpec showSpec;

    public AddWidget(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public AddWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_addwidget, this);
        addbutton = findViewById(R.id.addbutton);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AddWidget);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.AddWidget_circle_anim:
                    circle_anim = a.getBoolean(R.styleable.AddWidget_circle_anim, false);
                    break;
                case R.styleable.AddWidget_sub_anim:
                    sub_anim = a.getBoolean(R.styleable.AddWidget_sub_anim, false);
                    break;
            }

        }
        a.recycle();
        sub = findViewById(R.id.iv_sub);
        tv_count = findViewById(R.id.tv_count);
    }

    public void setListener() {
        addbutton.isSpec(goodBean, isSpec);
        addbutton.setShowSpec(new AddButton.ShowSpec() {
            @Override

            public void showSpec(GoodBean gb) {
                onAddClick.showSpec(gb);
            }
        });
        addbutton.setAnimListner(new AddButton.AnimListner() {
            @Override
            public void onStop() {
                if (count == 0) {
                    ViewAnimator.animate(sub)
                            .translationX(addbutton.getLeft() - sub.getLeft(), 0)
                            .rotation(360)
                            .alpha(0, 255)
                            .duration(200)
                            .interpolator(new DecelerateInterpolator())
                            .andAnimate(tv_count)
                            .translationX(addbutton.getLeft() - tv_count.getLeft(), 0)
                            .rotation(360)
                            .alpha(0, 255)
                            .interpolator(new DecelerateInterpolator())
                            .duration(200)
                            .start()
                    ;
                }
                count++;
                int cou = goodBean.getChooesNum() + 1;
                if (isCar) {
                    tv_count.setText(cou + "");
                } else {
                    tv_count.setText(count + "");
                }
                goodBean.setChooesNum(cou);
                goodBean.setSelectCount(count);
                onAddClick.onAddClick(addbutton, goodBean);
            }
        });
        sub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noRudce) {
                    XToast.info("多规格商品只能去购物车删减哟");
                } else {
                    if (count == 0) {
                        return;
                    }
                    if (count == 1 && sub_anim) {
                        tv_count.setAlpha(0);
                        sub.setAlpha(0);
                        // subAnim();
                    }
                    count--;
                    if (isCar) {
                        goodBean.setSelectCount(goodBean.getChooesNum() - 1);
                        tv_count.setText(goodBean.getSelectCount() == 0 ? "1" : goodBean.getSelectCount() + "");

                    } else {
                        tv_count.setText(count == 0 ? "1" : count + "");

                        goodBean.setSelectCount(count);

                    }
                    if (onAddClick != null) {
                        onAddClick.onSubClick(goodBean);
                    }
                }
            }
        });
    }

    public View setSubOnClick() {
        return sub;
    }

//        ci_no_reduce.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                XToast.info("多规格商品只能去购物车删减哟");
//            }
//        });


    private void subAnim() {
        ViewAnimator.animate(sub)
                .translationX(0, addbutton.getLeft() - sub.getLeft())
                .rotation(-360)
                .alpha(255, 0)
                .duration(300)
                .interpolator(new AccelerateInterpolator())
                .andAnimate(tv_count)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        if (circle_anim) {
                            addbutton.expendAnim();
                        }
                    }
                })
                .translationX(0, addbutton.getLeft() - tv_count.getLeft())
                .rotation(-360)
                .alpha(255, 0)
                .interpolator(new AccelerateInterpolator())
                .duration(300)
                .start()
        ;
    }

    private boolean isSpec;
    private boolean noRudce;
    private boolean isCar;

    public void setData(OnAddClick onAddClick, GoodBean goodBean, boolean isCar, boolean isSpec, boolean noRudce) {
        this.isSpec = isSpec;
        this.noRudce = noRudce;
        this.goodBean = goodBean;
        this.onAddClick = onAddClick;
        this.isCar = isCar;
        sub.setNoRduce();
        count = goodBean.getSelectCount();
        if (count == 0) {
            sub.setAlpha(0);
            tv_count.setAlpha(0);
        } else {
            sub.setAlpha(1f);
            tv_count.setAlpha(1f);
            if (!noRudce) {
                sub.setNoRduce();
            } else {
                sub.setCanRduce();
            }
            if (isCar) {
                tv_count.setText(goodBean.getChooesNum() + "");
            } else {
                tv_count.setText(count + "");

            }
        }
    }

    public void setState(int count) {
        addbutton.setState(count > 0);
    }

    public void expendAdd(int count) {
        this.count = count;
        tv_count.setText(count == 0 ? "1" : count + "");
        if (count == 0) {
            subAnim();
        }
    }
}
