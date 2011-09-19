package com.lendamage.agilegtd.android;

import android.os.Bundle;

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

}