# VotingView
自定义投票控件  

![自定义投票控件](https://github.com/xuliangliang1992/VotingView/blob/master/VotingView.jpg)
# 1、计算左右布局的长度
```Android
		float leftLength;
        float rightLength;
        //一边为0人时的最短距路
        float minLength = 120 + getHeight();
        if (leftNum != 0 && rightNum != 0) {
            //都不为零看比例
            leftLength = minLength + ((float) leftNum / (leftNum + rightNum)) * (getWidth() - 2 * minLength);
        } else {
            if (leftNum == 0) {
                if (rightNum == 0) {
                    //左右都为0 各占一半
                    leftLength = getWidth() / 2;
                } else {
                    //左边为0右边不为0 
                    leftLength = minLength;
                }
            } else {
                //左边不为0右边为0
                rightLength = minLength;
                leftLength = getWidth() - rightLength;
            }
        }
```

# 2、绘制左边布局
**先画半圆，再画梯型，最后填充颜色**
```Android
		//画半圆
        RectF leftOval = new RectF(0, 10, getHeight() - 10, getHeight() - 10);
        mPath.moveTo(getHeight() / 2 - 5, getHeight() / 2);
        mPath.arcTo(leftOval, 90, 180);

        //画矩形
        mPath.moveTo(getHeight() / 2 - 5, 10);
        mPath.lineTo(leftLength + mInclination - 10, 10);
        mPath.lineTo(leftLength - mInclination - 10, getHeight() - 10);
        mPath.lineTo(getHeight() / 2 - 5, getHeight() - 10);

        //渐变色
        Shader leftShader = new LinearGradient(0, 0, leftLength + mInclination - 10, 0, leftStartColor, leftEndColor, Shader.TileMode.CLAMP);
        mPaint.setShader(leftShader);

        //左边布局绘制完毕
        canvas.drawPath(mPath, mPaint);
        mPaint.setShader(null);
        mPath.reset();
```

# 2、绘制右边布局
**先画半圆，再画梯型，最后填充颜色**
```Android
		//画右边半圆
        RectF rightOval = new RectF(getWidth() - getHeight(), 0, getWidth(), getHeight());
        mPath.moveTo(getWidth() - getHeight() / 2, getHeight() / 2);
        mPath.arcTo(rightOval, -90, 180);
        //画右边矩形
        mPath.moveTo(leftLength + mInclination + 10, 0);
        mPath.lineTo(getWidth() - getHeight() / 2, 0);
        mPath.lineTo(getWidth() - getHeight() / 2, getHeight());
        mPath.lineTo(leftLength - mInclination + 10, getHeight());

        //渐变色
        Shader rightShader = new LinearGradient(leftLength + mInclination + 10, getHeight(), getWidth(), getHeight(), rightStartColor, rightEndColor, Shader.TileMode.CLAMP);
        mPaint.setShader(rightShader);

        //右边布局绘制完毕
        canvas.drawPath(mPath, mPaint);
        mPaint.setShader(null);
```

# 3、画中间白线
```Android
		//画中间白线
        mPath.moveTo(leftLength + mInclination - 10, 0);
        mPath.lineTo(leftLength + mInclination + 10, 0);
        mPath.lineTo(leftLength - mInclination + 10, getHeight());
        mPath.lineTo(leftLength - mInclination - 10, getHeight());
        mPath.close();
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(mPath, mPaint);
        mPath.reset();
```

# 4、画文字
```Android
		String leftText = String.valueOf(leftNum);
        String rightText = String.valueOf(rightNum);

        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.getTextBounds(leftText, 0, leftText.length(), mBound);

        //左边文字
        canvas.drawText(leftText, getHeight() / 2 + 6, getHeight() / 2 - 36 + mBound.height() / 2, mPaint);
        canvas.drawText("支持", getHeight() / 2 + 6, getHeight() / 2 + 12 + mBound.height() / 2, mPaint);

        mPaint.setColor(textColor);
        mPaint.getTextBounds(rightText, 0, rightText.length(), mBound);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        //右边文字
        canvas.drawText(rightText, getWidth() - getHeight() / 2 - 6, getHeight() / 2 - 36 + mBound.height() / 2, mPaint);
        canvas.drawText("不支持", getWidth() - getHeight() / 2 - 6, getHeight() / 2 + 12 + mBound.height() / 2, mPaint);

```
