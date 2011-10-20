/*
    Agile GTD. Flexible Android implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
