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

import com.xinyuan.xyorder.R;
import com.youth.xframe.utils.XDensityUtils;

/**
 * Created by f-x on 2017/10/3114:03
 */

public class ReduceButton extends View {

    private Paint paint;
    private Paint outPaint;
    private Rect textRect = new Rect();
    private RectF rectf = new RectF();
    private Path addPath = new Path();
    private Paint addPaint;
    private int color;

    public ReduceButton(Context context) {
        super(context);
        initPaint();
    }

    public ReduceButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        initAdd();
    }

    private void initAdd() {
        addPaint = new Paint();
        addPaint.setStrokeWidth(3);
        addPaint.setStyle(Paint.Style.STROKE);
        addPaint.setAntiAlias(true);
        addPaint.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        int height = getHeight();
        int padding = getHeight() / 5;
        addPath.moveTo(padding, height / 2);
        addPath.lineTo(height - padding, height / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw_o2c(canvas);
        canvas.drawPath(addPath, addPaint);

    }

    public void setNoRduce() {
        paint.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        addPaint.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        invalidate();
    }

    public void setCanRduce() {
        paint.setColor(getContext().getResources().getColor(R.color.bg_gray));
        addPaint.setColor(getContext().getResources().getColor(R.color.bg_gray));
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initAdd();
    }

    private void draw_o2c(Canvas canvas) {
        rectf.left = 2;
        rectf.top = 2;
        rectf.right = getWidth() - 2;
        rectf.bottom = getHeight() - 2;
        canvas.drawColor(Color.WHITE);
        canvas.drawRoundRect(rectf, 180, 180, paint);
    }

}
