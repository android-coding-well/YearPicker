package com.hwj.juneng.yp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

public class YearPicker extends View {

    public static final int ALIGN_MODE_CENTER = 0;//年份居中对齐
    public static final int ALIGN_MODE_BOTTOM = 1;//年份以底部对齐

    @IntDef({ALIGN_MODE_CENTER, ALIGN_MODE_BOTTOM})
    public @interface AlignMode {
    }


    private Paint paint;
    private int selectedColor = 0xff222222;//选中年份的颜色
    private int selectedSize = sp2px(20);//选中年份的大小
    //private int normalColor = 0xffaaaaaa;//未选中年份的颜色
    private int unselectedAlpha = 200;//未选中年份的透明度,255-不透明
    private int unselectedSize = sp2px(14);//未选中年份的大小

    private int bgColor = 0x00000000;//背景颜色
    private int pointerColor = 0xff0000ff;//指针颜色
    private boolean pointerVisibility = true;//指针颜色
    private float pointerHeightScale = 0.2f;//指针高度占总高度的比例
    private float pointerWidthScale = 0.1f;//指针宽度占总宽度的比例

    private int padding = dp2px(15);//上下间距
    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);//放中间
    private int yearGap = dp2px(20);//label（即年份）间的间距

    private int maxYear = Integer.MAX_VALUE;//最大的年份
    private int minYear = 0;//最小的年份

    private int alignMode = ALIGN_MODE_BOTTOM;

    private float minWidth;//控件最小宽度
    private float textHeight;//单个label(即年份)的高度
    private int width;//控件总宽
    private int height;//控件总高
    private float delayW;//每次滑动在x轴上的差值
    OnItemSelectedListener onItemSelectedListener;
    private static final String TAG = "YearPicker";

    SparseArray<Float> map = new SparseArray<Float>();//存放年份的位置

    public YearPicker(Context context) {
        this(context, null, 0);
    }

    public YearPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.YearPicker, defStyleAttr, 0);
        selectedColor = ta.getColor(R.styleable.YearPicker_yp_selectedColor, selectedColor);
        selectedSize = (int) ta.getDimension(R.styleable.YearPicker_yp_selectedSize, selectedSize);
        unselectedAlpha = ta.getInt(R.styleable.YearPicker_yp_unselectedAlpha, unselectedAlpha);
        unselectedSize = (int) ta.getDimension(R.styleable.YearPicker_yp_unselectedSize, unselectedSize);
        bgColor = ta.getColor(R.styleable.YearPicker_yp_bgColor, bgColor);
        pointerColor = ta.getColor(R.styleable.YearPicker_yp_pointerColor, pointerColor);
        pointerHeightScale = ta.getFloat(R.styleable.YearPicker_yp_pointerHeightScale, pointerHeightScale);
        pointerWidthScale = ta.getFloat(R.styleable.YearPicker_yp_pointerwidthScale, pointerWidthScale);
        pointerVisibility = ta.getInt(R.styleable.YearPicker_yp_pointerVisibility, 1) == 1 ? true : false;
        padding = (int) ta.getDimension(R.styleable.YearPicker_yp_padding, padding);
        currentYear = ta.getInt(R.styleable.YearPicker_yp_defaultYear, currentYear);
        yearGap = (int) ta.getDimension(R.styleable.YearPicker_yp_yearGap, yearGap);
        maxYear = ta.getInt(R.styleable.YearPicker_yp_maxYear, maxYear);
        minYear = ta.getInt(R.styleable.YearPicker_yp_minYear, minYear);
        alignMode = ta.getInt(R.styleable.YearPicker_yp_alignMode, alignMode);
        ta.recycle();

        setClickable(true);
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(selectedSize);

        textHeight = paint.descent() - paint.ascent();
        minWidth = paint.measureText(currentYear + "") * 3 + yearGap * 4;
    }

    /**
     * 设置年份选择监听器
     * @param listener
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener= listener;
    }

    public void setSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = selectedColor;
        invalidate();
    }

    /**
     *  设置选中年份的大小
    * @param selectedSize
     */
    public void setSelectedSize(int selectedSize) {
        this.selectedSize = selectedSize;
        invalidate();
    }

    /**
     *  设置未选中年份的透明度
     * @param unselectedAlpha
     */
    public void setUnselectedAlpha(@IntRange(from = 0, to = 255) int unselectedAlpha) {
        this.unselectedAlpha = unselectedAlpha;
        invalidate();
    }

    /**
     * 设置未选中年份的大小
     * @param unselectedSize
     */
    public void setUnselectedSize(int unselectedSize) {
        this.unselectedSize = unselectedSize;
        invalidate();
    }


    public void setBgColor(@ColorInt int bgColor) {
        this.bgColor = bgColor;
        invalidate();
    }

    /**
     * 设置指针的颜色
     * @param pointerColor
     */
    public void setPointerColor(@ColorInt int pointerColor) {
        this.pointerColor = pointerColor;
        invalidate();
    }

    public void setPointerVisibility(boolean pointerVisibility) {
        this.pointerVisibility = pointerVisibility;
        invalidate();
    }

    /**
     * 设置指针高度所占的比例
     * @param pointerHeightScale
     */
    public void setPointerHeightScale(@FloatRange(from = 0, to = 1) float pointerHeightScale) {
        this.pointerHeightScale = pointerHeightScale;
        invalidate();
    }

    /**
     * 设置指针宽度所占的比例
     * @param pointerWidthScale
     */
    public void setPointerWidthScale(@FloatRange(from = 0, to = 1) float pointerWidthScale) {
        this.pointerWidthScale = pointerWidthScale;
        invalidate();
    }

    public void setPadding(int padding) {
        this.padding = padding;
        invalidate();
    }

    public void setCurrentYear(int currentYear) {
        if (currentYear >= minYear && currentYear <= maxYear) {
            this.currentYear = currentYear;
            invalidate();
        }

    }

    public void setYearGap(int yearGap) {
        this.yearGap = yearGap;
        invalidate();
    }

    public void setMaxYear(int maxYear) {
        if (maxYear >= currentYear) {
            this.maxYear = maxYear;
            invalidate();
        }

    }

    public void setMinYear(int minYear) {
        if (minYear <= currentYear) {
            this.minYear = minYear;
            invalidate();
        }
    }

    /**
     * 设置对齐模式
     * @param alignMode
     */
    public void setAlignMode(@AlignMode int alignMode) {
        this.alignMode = alignMode;
        invalidate();
    }

    public boolean isPointerVisibility() {
        return pointerVisibility;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public int getMinYear() {
        return minYear;
    }

    public int getAlignMode() {
        return alignMode;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = resolveSize((int) minWidth, widthMeasureSpec);
        height = (int) (textHeight + padding * 2);

        genYearPositionByCurrentYear();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        //画背景
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new RectF(0, 0, width, height), paint);


        if (pointerVisibility) {
            //画三角形指针
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(pointerColor);
            Path path = new Path();
            float h = height * (1 - pointerHeightScale);
            float w = width * pointerWidthScale / 2.0f;
            path.moveTo(width / 2, h);// 此点为多边形的起点
            path.lineTo(width / 2 - w, height);
            path.lineTo(width / 2 + w, height);
            path.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path, paint);
        }


        paint.setColor(selectedColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);//居中画文字


        paint.setTextSize(selectedSize);
        float disLabel = paint.measureText(currentYear + "") + yearGap;//相邻两个年份中点之间的距离
        float totalSize = selectedSize - unselectedSize;//选中文字大小与普通文字大小的差距
        for (int i = -2; i < 3; i++) {


            int year = currentYear + i;

            float moveX = delayW + map.get(year);//

            float disSize = Math.abs(width / 2 - moveX);
            paint.setAlpha(255 - (int) ((255 - unselectedAlpha) * disSize / disLabel));//0-透明 255-不透
            paint.setTextSize(selectedSize - totalSize * disSize / disLabel);
            if (alignMode == ALIGN_MODE_CENTER) {
                float yearH = paint.descent() - paint.ascent();
                canvas.drawText(year + "", moveX, height / 2 + yearH / 3, paint);
            } else {
                canvas.drawText(year + "", moveX, height / 2 + textHeight / 3, paint);
            }

            map.append(year, moveX);
        }

    }

    float downX = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //return super.dispatchTouchEvent(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                // Log.e(TAG,"ACTION_MOVE");
                if (event.getX() < width && event.getX() > 0) {//防止滑动距离超出控件的宽度
                    float dw = event.getX() - downX;
                    int latestYear = findLatestYear();
                    if (dw > 0) {//向右滑动
                        if (latestYear > minYear || (map.get(latestYear) - width / 2) < 0) {//不能小于最小年份,第二个条件是为了能让最小年份滑到中间
                            downX = event.getX();
                            delayW = dw;
                            //Log.e(TAG, delayW + "");
                            invalidate();
                        }

                    } else if (latestYear < maxYear || (map.get(latestYear) - width / 2) > 0) {//向左滑动，不能大于最大年份，,第二个条件是为了能让最大年份滑到中间
                        downX = event.getX();
                        delayW = dw;
                        //Log.e(TAG, delayW + "");
                        invalidate();
                    }
                }


                break;
            case MotionEvent.ACTION_CANCEL:
                // Log.e(TAG,"ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP:
                //  Log.e(TAG,"ACTION_UP");

                //找到距离中点最近的年份
                int latestYear = findLatestYear();
                if (currentYear != latestYear) {
                    currentYear=latestYear;
                    if (onItemSelectedListener != null) {
                        onItemSelectedListener.onSelected(currentYear);
                    }
                }
                //根据当前年份重新计算位置
                genYearPositionByCurrentYear();

                delayW = 0;//防止年份不居中
                //刷新
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 找到距离中点最近的年份
     *
     * @return
     */
    private int findLatestYear() {
        float min = width / 2;
        int cy = currentYear;
        for (int i = -2; i < 3; i++) {
            if (Math.abs(map.get(currentYear + i) - width / 2) < min) {
                min = Math.abs(map.get(currentYear + i) - width / 2);
                cy = currentYear + i;
            }
        }
        return cy;
    }

    /**
     * 根据当前年份生成前后各个年份的位置
     */
    private void genYearPositionByCurrentYear() {
        paint.setTextSize(selectedSize);
        float yearWidth = paint.measureText(currentYear + "");
        map.append(currentYear, width / 2.0f);
        map.append(currentYear - 1, width / 2.0f - (yearWidth + yearGap));
        map.append(currentYear - 2, width / 2.0f - 2 * (yearWidth + yearGap));
        map.append(currentYear + 1, width / 2.0f + (yearWidth + yearGap));
        map.append(currentYear + 2, width / 2.0f + 2 * (yearWidth + yearGap));
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    public interface OnItemSelectedListener {
        /**
         * 选中的年份
         *
         * @param year
         */
        void onSelected(int year);
    }
}