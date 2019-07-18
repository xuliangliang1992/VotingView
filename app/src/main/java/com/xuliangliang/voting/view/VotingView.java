package com.xuliangliang.voting.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xuliangliang.voting.R;


/**
 * 投票视图
 *
 * @author xll
 * @date 2019-07-17
 */
public class VotingView extends View {

    /**
     * 左边的数量
     */
    private int leftNum;
    /**
     * 左边结束色
     */
    private int leftEndColor;
    /**
     * 左边开始色
     */
    private int leftStartColor;
    /**
     * 右边的数量
     */
    private int rightNum;
    /**
     * 右边开始色
     */
    private int rightStartColor;
    /**
     * 右边结束色
     */
    private int rightEndColor;
    /**
     * 白线倾斜度
     */
    private int mInclination;
    /**
     * 左边字体颜色
     */
    private int textColor;
    /**
     * 字体大小
     */
    private int textSize;
    /**
     * 左边边框距离
     */

    private Paint mPaint;
    private Path mPath;

    /**
     * 包含文字的框
     */
    private Rect mBound;

    public VotingView(Context context) {
        this(context, null);
    }

    public VotingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VotingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VotingView, defStyleAttr, 0);
        int n = typedArray.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.VotingView_leftNum:
                    leftNum = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.VotingView_leftStartColor:
                    leftStartColor = typedArray.getColor(attr, Color.parseColor("#FF7566"));
                    break;
                case R.styleable.VotingView_leftEndColor:
                    leftEndColor = typedArray.getColor(attr, Color.parseColor("#FFBD8D"));
                    break;
                case R.styleable.VotingView_rightNum:
                    rightNum = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.VotingView_rightStartColor:
                    rightStartColor = typedArray.getColor(attr, Color.parseColor("#13CCFF"));
                    break;
                case R.styleable.VotingView_rightEndColor:
                    rightEndColor = typedArray.getColor(attr, Color.parseColor("#0091FF"));
                    break;
                case R.styleable.VotingView_inclination:
                    mInclination = typedArray.getInteger(attr, 40);
                    break;
                case R.styleable.VotingView_textColor:
                    textColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.VotingView_textSize:
                    textSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                default:
                    break;
            }
        }

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mPath = new Path();
        mBound = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getPaddingLeft() + getWidth() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getPaddingTop() + getHeight() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float leftLength;
        float rightLength;
        float minLength = 120 + getHeight();
        if (leftNum != 0 && rightNum != 0) {
            leftLength = minLength + ((float) leftNum / (leftNum + rightNum)) * (getWidth() - 2 * minLength);
        } else {
            if (leftNum == 0) {
                if (rightNum == 0) {
                    leftLength = getWidth() / 2;
                } else {
                    leftLength = minLength;
                }
            } else {
                rightLength = minLength;
                leftLength = getWidth() - rightLength;
            }
        }

        RectF leftOval = new RectF(0, 10, getHeight() - 10, getHeight() - 10);
        RectF rightOval = new RectF(getWidth() - getHeight(), 0, getWidth(), getHeight());

        mPath.moveTo(getHeight() / 2 - 5, getHeight() / 2);
        mPath.arcTo(leftOval, 90, 180);

        mPath.moveTo(getHeight() / 2 - 5, 10);
        mPath.lineTo(leftLength + mInclination - 10, 10);
        mPath.lineTo(leftLength - mInclination - 10, getHeight() - 10);
        mPath.lineTo(getHeight() / 2 - 5, getHeight() - 10);

        Shader leftShader = new LinearGradient(0, 0, leftLength + mInclination - 10, 0, leftStartColor, leftEndColor, Shader.TileMode.CLAMP);
        mPaint.setShader(leftShader);

        canvas.drawPath(mPath, mPaint);
        mPaint.setShader(null);
        mPath.reset();

        mPath.moveTo(leftLength + mInclination - 10, 0);
        mPath.lineTo(leftLength + mInclination + 10, 0);
        mPath.lineTo(leftLength - mInclination + 10, getHeight());
        mPath.lineTo(leftLength - mInclination - 10, getHeight());
        mPath.close();
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(mPath, mPaint);
        mPath.reset();

        mPath.moveTo(getWidth() - getHeight() / 2, getHeight() / 2);
        mPath.arcTo(rightOval, -90, 180);
        mPath.moveTo(leftLength + mInclination + 10, 0);
        mPath.lineTo(getWidth() - getHeight() / 2, 0);
        mPath.lineTo(getWidth() - getHeight() / 2, getHeight());
        mPath.lineTo(leftLength - mInclination + 10, getHeight());

        Shader rightShader = new LinearGradient(leftLength + mInclination + 10, getHeight(), getWidth(), getHeight(), rightStartColor, rightEndColor, Shader.TileMode.CLAMP);
        mPaint.setShader(rightShader);

        canvas.drawPath(mPath, mPaint);
        mPaint.setShader(null);

        String leftText = String.valueOf(leftNum);
        String rightText = String.valueOf(rightNum);

        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.getTextBounds(leftText, 0, leftText.length(), mBound);

        canvas.drawText(leftText, getHeight() / 2 + 6, getHeight() / 2 - 36 + mBound.height() / 2, mPaint);
        canvas.drawText("支持", getHeight() / 2 + 6, getHeight() / 2 + 12 + mBound.height() / 2, mPaint);

        mPaint.setColor(textColor);
        mPaint.getTextBounds(rightText, 0, rightText.length(), mBound);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(rightText, getWidth() - getHeight() / 2 - 6, getHeight() / 2 - 36 + mBound.height() / 2, mPaint);
        canvas.drawText("不支持", getWidth() - getHeight() / 2 - 6, getHeight() / 2 + 12 + mBound.height() / 2, mPaint);

    }

    /**
     * 动态设置
     *
     * @param leftNum  左边
     * @param rightNum 右边
     */
    public void setNum(int leftNum, int rightNum) {
        this.leftNum = leftNum;
        this.rightNum = rightNum;
        postInvalidate();
    }


}
