package xiaodou.colorgame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;


public class ColorLayout extends ViewGroup implements View.OnClickListener {
    private int mWidth = 0;
    private int mHeight = 0;
    //每条边上的个数
    private int mHorizontalCount = 2;
    private int mCommonColor = Color.BLUE;
    private int mParticularColor = Color.GRAY;

    public ColorLayout(Context context) {
        this(context, null, 0);
    }

    public ColorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCommonColor = randomCommonColor();
        mParticularColor = calculateParticularColor(mCommonColor);
    }

    public int randomCommonColor() {
        int color = new Random().nextInt(0xffffff);


        /**
         * 16进制颜色解析规则：
         * 如果长度小于两位则补足三位；
         * 如果是三位，则每位算两个；
         * 如果是四位则后面两个算两位；
         * 如果是五位，则最后一个算两个。
         */

        parseColor(color);

        return color;

    }

    public String parseColor(int color) {
        String colorString = Integer.toHexString(color);
        if (colorString.length() < 3) {
            for (int i = 0; i < (3 - colorString.length()); i++) {
                colorString = colorString + colorString;
            }
        }
        if (colorString.length() == 3) {
            String[] items = new String[colorString.length()];
            for (int i = 0; i < colorString.length(); i++) {
                items[i] = colorString.substring(i, i + 1);
            }
            for (int i = 0; i < colorString.length(); i++) {
                items[i] = items[i] + items[i];
            }
            colorString = "";
            for (int i = 0; i < 3; i++) {
                colorString = colorString + items[i];
            }
            return colorString;
        }

        if (colorString.length() == 4) {
            String[] items = new String[colorString.length()];
            for (int i = 0; i < colorString.length(); i++) {
                items[i] = colorString.substring(i, i + 1);
            }

            for (int i = 3; i > 1; i--) {
                items[i] = items[i] + items[i];
            }
            colorString = "";
            for (int i = 0; i < 4; i++) {
                colorString = colorString + items[i];
            }

            return colorString;
        }

        if (colorString.length() == 5) {
            String[] items = new String[colorString.length()];
            for (int i = 0; i < colorString.length(); i++) {
                items[i] = colorString.substring(i, i + 1);
            }

            colorString = colorString + items[4];
            return colorString;
        }
        Log.d("随机颜色", "解析颜色 ： " + color + " | " + Integer.toHexString(color) + " | " + colorString);

        return colorString;
    }

    public int[] getRGB(int color) {
        int rgb[] = new int[3];

        rgb[0] = Color.red(color);
        rgb[1] = Color.green(color);
        rgb[2] = Color.blue(color);

        return rgb;
    }

    public int calculateParticularColor(int color) {
        int rgb[] = getRGB(color);

        /**
         * 计算差值，分数越高，差值越小，越难分辨
         */
        int dx = (60+(mScore+mScore)*(60/(mScore+2))) / (mScore+1)+60/(mScore+1);
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = Math.abs(rgb[i] -= dx);
        }
        color = Color.rgb(rgb[0], rgb[1], rgb[2]);

        Log.d("随机颜色", "特殊颜色 ： " + color + " | " + Integer.toHexString(Math.abs(color)));
        parseColor(color);
        return color;
    }

    private void addChildColor() {
        int width = mWidth / mHorizontalCount;
        int x = new Random().nextInt(mHorizontalCount * mHorizontalCount);
        if (x < 0) {
            x = 0;
        }
        if (x > mHorizontalCount * mHorizontalCount) {
            x = mHorizontalCount * mHorizontalCount - 1;
        }
        for (int i = 0; i < mHorizontalCount * mHorizontalCount; i++) {
            ImageView view = new ImageView(getContext());
            view.setOnClickListener(this);
            //view.setBackgroundColor(i == x ? 1291049 : 8110964);
            view.setBackgroundColor(i == x ? Color.parseColor("#" + parseColor(mParticularColor)) : Color.parseColor("#" + parseColor(mCommonColor)));
            view.setImageDrawable(getResources().getDrawable(R.drawable.itembg1));
            view.setTag(i == x ? 1 : 2);
            addView(view, width, width);

        }
    }

    public int getHorizontalCount() {
        return mHorizontalCount;
    }

    public void setHorizontalCount(int count) {
        mHorizontalCount = count;
    }

    public int getCommonColor() {
        return mCommonColor;
    }

    public void setCommonColor(int color) {
        this.mCommonColor = color;
    }

    public int getParticularColor() {
        return mParticularColor;
    }

    public void setParticularColor(int color) {
        this.mParticularColor = color;
    }

    private boolean measureFirst = true;
    private boolean layoutFirst = true;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (measureFirst) {
            addChildColor();
            measureFirst = false;
        }
    }

    public void pause() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setClickable(false);
        }
    }

    private int mScore = 0;

    private int mClickCount = 1;

    private int remain = 1;

    public void reset() {
        mScore = 0;
        mClickCount = 1;
        remain = 1;
        mHorizontalCount = 2;

        reDraw();
    }

    public void goon() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setClickable(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
        mWidth = widthSize;
        mHeight = widthSize;

        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = null;

        int childWidth = 0;
        int childCount = getChildCount();
        int left = 0, top = 0, right = 0, bottom = 0;

        childWidth = mWidth / mHorizontalCount;


        if (layoutFirst) {
            for (int i = 0; i < childCount; i++) {
                childView = getChildAt(i);
                int hor = i % mHorizontalCount;
                int ver = i / mHorizontalCount;
                left = childWidth * (hor);
                right = left + childWidth;
                top = childWidth * (ver);
                bottom = top + childWidth;
                childView.layout(left, top, right, bottom);
            }
            layoutFirst = false;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }


    public void onAdd() {
        mScore++;
        if (mOnClickRightColorListener != null) {
            mOnClickRightColorListener.clickRightColor(mScore);
        }

        if (remain == 0) {
            mHorizontalCount++;
            remain = mHorizontalCount - 1;
        } else {
            remain--;
        }

        reDraw();

        mClickCount++;
    }

    public void reDraw() {
        removeAllViews();
        layoutFirst = true;
        mCommonColor = randomCommonColor();
        mParticularColor = calculateParticularColor(mCommonColor);
        addChildColor();
        invalidate();

    }

    /**
     * 0,1,2,3,4,5,6,7,8,9,10
     * 1,2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,6,7
     */
    private int count[] = new int[]{1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 7};

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        if (tag == 1) {
            int color = v.getDrawingCacheBackgroundColor();
            int id = v.getId();
            onAdd();
        }
    }

    public interface OnClickRightColorListener {
        void clickRightColor(int score);
    }

    private OnClickRightColorListener mOnClickRightColorListener;

    public void setOnClickRightColorListener(OnClickRightColorListener listener) {
        mOnClickRightColorListener = listener;
    }
}
