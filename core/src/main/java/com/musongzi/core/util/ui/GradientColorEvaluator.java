package com.musongzi.core.util.ui;

import android.animation.ArgbEvaluator;

/**
 * 颜色的平滑过渡差值计算实现
 */
public class GradientColorEvaluator extends ArgbEvaluator {


    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        // 获取两个颜色值
        int startInt = (Integer) startValue;
        int endInt = (Integer) endValue;

        // 计算开始和结束的Alpha、Red、Green、Blue值
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        // 计算当前的Alpha、Red、Green、Blue值
        int currentA = (int) (startA + fraction * (endA - startA));
        int currentR = (int) (startR + fraction * (endR - startR));
        int currentG = (int) (startG + fraction * (endG - startG));
        int currentB = (int) (startB + fraction * (endB - startB));

        // 将Alpha、Red、Green、Blue组合成颜色
        return currentA << 24 | currentR << 16 | currentG << 8 | currentB;
    }
}
