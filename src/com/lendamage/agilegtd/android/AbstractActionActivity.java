package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.IntentParams.ACTION_POSITION_EXTRA;
import static com.lendamage.agilegtd.android.IntentParams.FOLDER_PATH_EXTRA;
import static com.lendamage.agilegtd.android.Tag.TAG;

import java.util.Set;

import android.content.Intent;
import android.util.Log;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

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
        if (this.folder == null) {
            finish();
            return;
        }
        if (this.action == null) {
            getActionFromIntent();
        } else {
            restoreAction();
        }
    }
    
    void getActionFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(ACTION_POSITION_EXTRA)) {
            int position = intent.getIntExtra(ACTION_POSITION_EXTRA, 0);
            try {
                this.action = this.folder.getActions().get(position);
            } catch (IndexOutOfBoundsException e) {
                Log.w(TAG, "action at " + position + " in " + this.folder.getPath() + " is not found");
                finish();
            }
        }
    }
    
    void restoreAction() {
        this.action = this.model.findAction(this.action);
        if (this.action == null) {
            Log.w(TAG, "action in " + this.folder.getPath() + " cannot be restored");
            finish();
            return;
        }
        Set<Folder> folders = this.action.getFolders();
        if (folders.isEmpty()) {
            Log.w(TAG, "action has no folders, illegal model state");
            finish();
            return;
        }
        this.folder = folders.iterator().next();
        Intent intent = getIntent();
        intent.putExtra(FOLDER_PATH_EXTRA, this.folder.getPath().toString());
        intent.putExtra(ACTION_POSITION_EXTRA, this.folder.getActions().indexOf(this.action));
    }

}