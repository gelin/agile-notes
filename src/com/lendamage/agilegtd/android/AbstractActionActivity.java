package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.IntentParams.ACTION_POSITION_EXTRA;
import static com.lendamage.agilegtd.android.Tag.TAG;
import android.content.Intent;
import android.util.Log;

import com.lendamage.agilegtd.model.Action;

/**
 *  Base class for activites which works with the action.
 *  It takes the folder path and the action position withing the folder in intent.
 */
abstract class AbstractActionActivity extends AbstractFolderActivity {
    
    /** Current action */
    protected Action action;
    
    @Override
    protected void onResume() {
        super.onResume();
        this.action = null;
        if (this.folder == null) {
            return;
        }
        Intent intent = getIntent();
        if (intent.hasExtra(ACTION_POSITION_EXTRA)) {
            int position = intent.getIntExtra(ACTION_POSITION_EXTRA, 0);
            try {
                this.action = this.folder.getActions().get(position);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.w(TAG, "action at " + position + " in " + this.folder.getPath() + " is not found");
            }
        }
    }

}