package com.lendamage.agilegtd.android;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.lendamage.agilegtd.android.ResizableFrameLayout.OnMeasureListener;

/**
 *  Base activity for the activities which changes the folder.
 */
public abstract class AbstractEditActionActivity extends AbstractActionActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initViews();
    }
    
    abstract protected void setContentView();
    
    protected void initViews() {
        final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        final View button = findViewById(R.id.ok_big_button);
        final ResizableFrameLayout bodyFrame = (ResizableFrameLayout)findViewById(R.id.action_body_frame);
        bodyFrame.setOnMeasureListener(new OnMeasureListener() {
            public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int height = View.MeasureSpec.getSize(heightMeasureSpec);
                if (((float)height / screenHeight) < 0.7) {  //height is less than 70%
                    button.setVisibility(View.GONE);
                } else {
                    button.setVisibility(View.VISIBLE);
                }
            }
        });
        
        OnClickListener okListener = new OnClickListener() {
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                onOkClick();
            }
        };
        findViewById(R.id.ok_button).setOnClickListener(okListener);
        findViewById(R.id.ok_big_button).setOnClickListener(okListener);
    }
    
    protected boolean validate() {
        EditText body = (EditText)findViewById(R.id.action_body);
        if (body.length() == 0) {
            Toast.makeText(this, R.string.enter_action_body_error, Toast.LENGTH_LONG).show();
            body.requestFocus();
            return false;
        }
        return true;
    }
    
    protected abstract void onOkClick();
    
}