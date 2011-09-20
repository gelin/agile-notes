package com.lendamage.agilegtd.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 *  The FrameLayout which has OnSizeChangedListener which allows
 *  to notify some outside code about changes of this frame size.
 */
public class ResizableFrameLayout extends FrameLayout {

    /** On size changed listener instance */
    OnSizeChangedListener onSizeChangeListener = null;
    /** On measure listener instance */
    OnMeasureListener onMeasureListener = null;
    
    public interface OnSizeChangedListener {
        public void onSizeChanged(int w, int h, int oldw, int oldh);
    }
    
    public interface OnMeasureListener {
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec);
    }
    
    public ResizableFrameLayout(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public ResizableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableFrameLayout(Context context) {
        super(context);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.onSizeChangeListener != null) {
            this.onSizeChangeListener.onSizeChanged(w, h, oldw, oldh);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.onMeasureListener != null) {
            this.onMeasureListener.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
    
    /**
     *  Sets the on size changed listener.
     */
    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        this.onSizeChangeListener = listener;
    }
    
    /**
     *  Sets the on measure listener.
     */
    public void setOnMeasureListener(OnMeasureListener listener) {
        this.onMeasureListener = listener;
    }

}
