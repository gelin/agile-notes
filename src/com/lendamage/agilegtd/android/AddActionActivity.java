package com.lendamage.agilegtd.android;

import android.os.Bundle;
import android.view.WindowManager.LayoutParams;

/**
 *  Activity to add a new action.
 *  The path to the folder where to add the activity is passed in extra.
 */
public class AddActionActivity extends AbstractFolderActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_action_activity);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        System.out.println(this.getClass().getName() + ".onWindowFocusChanged(): " + hasFocus);
    }
    
    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
        super.onWindowAttributesChanged(params);
        System.out.println(this.getClass().getName() + ".onWindowAttributesChanged(): " + params);
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println(this.getClass().getName() + ".onAttachedToWindow()");
    }

}