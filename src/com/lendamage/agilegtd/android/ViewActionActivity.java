package com.lendamage.agilegtd.android;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 *  Activity to view the action.
 *  The path to the folder where the action is located
 *  and the position of the action withing the folder
 *  are passed in extra.
 */
public class ViewActionActivity extends AbstractActionActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_action_activity);
        
        OnClickListener okListener = new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        };
        findViewById(R.id.ok_button).setOnClickListener(okListener);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (this.action == null) {
            return;
        }
        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(this.action.getHead());
        TextView bodyView = (TextView)findViewById(R.id.action_body);
        bodyView.setText(this.action.getBody());
    }

}