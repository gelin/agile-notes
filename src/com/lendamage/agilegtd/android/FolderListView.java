/* 
 *  Copyright (C) 2011 Denis Nelubin
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Drag'N'Drop functionality:
 *  Copyright (C) 2010 Eric Harlow
 *  Licensed under the Apache License, Version 2.0
 */

package com.lendamage.agilegtd.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FolderListView extends ListView {
    
    boolean dragMode;

    int startPosition;
    int endPosition;
    int dragPointOffset;       //Used to adjust drag view location
    int dragArea;
    
    ImageView dragView;
    GestureDetector gestureDetector;
    
    DropListener dropListener;
    DragListener dragListener;

    public FolderListView(Context context) {
        super(context);
        init();
    }

    public FolderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FolderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    void init() {
        this.dragArea = (int)(40.0f * getResources().getDisplayMetrics().density + 0.5f);   //40dp
        this.dragListener = this.defaultDragListener;
    }
    
    public void setDropListener(DropListener listener) {
        this.dropListener = listener;
    }

    public void setDragListener(DragListener listener) {
        this.dragListener = listener;
    }
    
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof DropListener) {
            this.dropListener = (DropListener)adapter;
        }
        if (adapter instanceof DragListener) {
            this.dragListener = (DragListener)adapter;
        }
        super.setAdapter(adapter);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();  
        
        if (action == MotionEvent.ACTION_DOWN && x > (this.getWidth() - this.dragArea)) {
            this.dragMode = true;
        }

        if (!this.dragMode) { 
            return super.onTouchEvent(ev);
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.startPosition = pointToPosition(x, y);
                if (this.startPosition != INVALID_POSITION) {
                    int itemPosition = this.startPosition - getFirstVisiblePosition();
                    this.dragPointOffset = y - getChildAt(itemPosition).getTop();
                    this.dragPointOffset -= ((int)ev.getRawY()) - y;
                    startDrag(itemPosition, y);
                    drag(0, y);// replace 0 with x if desired
                }   
                break;
            case MotionEvent.ACTION_MOVE:
                drag(0, y);// replace 0 with x if desired
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            default:
                this.dragMode = false;
                this.endPosition = pointToPosition(x, y);
                stopDrag(this.startPosition - getFirstVisiblePosition());
                if (this.dropListener != null && 
                        this.startPosition != INVALID_POSITION && this.endPosition != INVALID_POSITION) { 
                     this.dropListener.onDrop(this.startPosition, this.endPosition);
                }
                break;
        }
        return true;
    }
    
    /**
     *  Moves the drag view
     */
    private void drag(int x, int y) {
        if (this.dragView != null) {
            WindowManager.LayoutParams layoutParams = 
                (WindowManager.LayoutParams)this.dragView.getLayoutParams();
            layoutParams.x = x;
            layoutParams.y = y - this.dragPointOffset;
            WindowManager windowManager = (WindowManager)getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.updateViewLayout(this.dragView, layoutParams);

            if (this.dragListener != null) {
                this.dragListener.onDrag(x, y, null);// change null to "this" when ready to use
            }
        }
    }
    
    /** 
     *  Enables the drag view for dragging
     */
    private void startDrag(int itemIndex, int y) {
        stopDrag(itemIndex);

        View item = getChildAt(itemIndex);
        if (item == null) {
            return;
        }
        item.setDrawingCacheEnabled(true);
        if (this.dragListener != null) {
            this.dragListener.onStartDrag(item);
        }
        
        // Create a copy of the drawing cache so that it does not get recycled
        // by the framework when the list tries to clean up memory
        Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
        
        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - this.dragPointOffset;

        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;
        
        Context context = getContext();
        ImageView v = new ImageView(context);
        v.setImageBitmap(bitmap);

        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(v, windowParams);
        this.dragView = v;
    }

    /** 
     *  Destroys drag view
     */
    private void stopDrag(int itemIndex) {
        if (this.dragView == null) {
            return;
        }
        if (this.dragListener != null) {
            this.dragListener.onStopDrag(getChildAt(itemIndex));
        }
        this.dragView.setVisibility(GONE);
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(this.dragView);
        this.dragView.setImageDrawable(null);
        this.dragView = null;
    }
    
    private DragListener defaultDragListener = new DragListener() {

        int backgroundColor = 0x22ffffff;
        int defaultBackgroundColor;
        
            public void onDrag(int x, int y, ListView listView) {
                //nothing to do
            }

            public void onStartDrag(View itemView) {
                itemView.setVisibility(View.INVISIBLE);
                defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
                itemView.setBackgroundColor(backgroundColor);
            }

            public void onStopDrag(View itemView) {
                itemView.setVisibility(View.VISIBLE);
                itemView.setBackgroundColor(defaultBackgroundColor);
            }
        
    };

}
