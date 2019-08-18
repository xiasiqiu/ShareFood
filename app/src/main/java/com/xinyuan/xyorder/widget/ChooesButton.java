package com.xinyuan.xyorder.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.util.CommUtil;
import com.youth.xframe.utils.XDensityUtils;

/**
 * Created by f-x on 2017/12/22  19:44
 * Description
 */

public class ChooesButton extends View implements View.OnClickListener {

    private boolean isCircle = true;//false 加入购物车  true 只有加号
    private Paint paint;
    private Paint textPaint;
    private Rect textRect = new Rect();
    private RectF rectf = new RectF();
    private Path addPath = new Path();
    private Paint addPaint;

    interface AnimListner {
        void onStop();
    }

    private AddButton.AnimListner animListner;

    void setAnimListner(AddButton.AnimListner animListner) {
        this.animListner = animListner;
    }


    public ChooesButton(Context context) {
        super(context);
        setOnClickListener(this);
        initPaint();
    }

    public ChooesButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(XDensityUtils.sp2px(10));
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);

        initAdd();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initAdd();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw_o2c(canvas);
        if (isCircle) {
            canvas.drawPath(addPath, addPaint);
        } else {
            drawText(canvas);
        }
    }

    private void draw_o2c(Canvas canvas) {
        rectf.left = 0;
        rectf.top = 0;
        rectf.right = getWidth();
        rectf.bottom = getHeight();

        canvas.drawRoundRect(rectf, 180, 180, paint);
    }

    private void drawText(Canvas canvas) {
        textRect.left = 0;
        textRect.top = 0;
        textRect.right = getWidth();
        textRect.bottom = getHeight();
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText("加入购物车", textRect.centerX(), baseline, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        if (isCircle) {
            heightMeasureSpec = widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), View.MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        if (!CommUtil.isCarFastClick()) {
            if (animListner != null) {
                if (!isCircle) {
                    setClickable(false);
                    ViewAnimator.animate(this)
                            .width(getWidth(), getHeight())
                            .duration(300)
                            .onStop(new AnimationListener.Stop() {
                                @Override
                                public void onStop() {
                                    isCircle = true;
                                    invalidate();
                                    setClickable(true);
                                    animListner.onStop();
                                }
                            })
                            .start();
                } else {
                    animListner.onStop();
                }
            }
        }

    }

    private void initAdd() {
        addPaint = new Paint();
        addPaint.setStrokeWidth(3);
        addPaint.setStyle(Paint.Style.STROKE);
        addPaint.setAntiAlias(true);
        addPaint.setColor(Color.WHITE);

        int height = getHeight();
        int padding = getHeight() / 4;
        addPath.moveTo(padding, height / 2);
        addPath.lineTo(height - padding, height / 2);
        addPath.moveTo(height / 2, padding);
        addPath.lineTo(height / 2, height - padding);
    }

    public void setState(boolean state) {
        this.isCircle = state;
        invalidate();
    }

    public void expendAnim() {
        isCircle = false;
        ViewAnimator.animate(this)
                .width(getWidth(), getContext().getResources().getDimension(R.dimen.add_width))
                .duration(300)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        invalidate();
                    }
                })
                .start();
    }
}
