package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.IntentParams.FOLDER_PATH_EXTRA;
import static com.lendamage.agilegtd.android.Tag.TAG;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.lendamage.agilegtd.android.model.impl.SimplePath;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.Model;

/**
 *  Base class for activites which works with the folder and
 *  takes the folder path in intent.
 */
abstract class AbstractFolderActivity extends Activity {
    
    /** Current model */
    protected Model model;
    /** Current folder */
    protected Folder folder;
    
    @Override
    protected void onResume() {
        super.onResume();
        this.model = ModelAccessor.openModel(this);
        this.folder = null;
        Intent intent = getIntent();
        String path = null;
        if (intent.hasExtra(FOLDER_PATH_EXTRA)) {
            path = intent.getStringExtra(FOLDER_PATH_EXTRA);
            this.folder = this.model.getFolder(new SimplePath(path));
            if (this.folder == null) {
                Log.w(TAG, "folder " + path + " is not found");
            }
        }
        if (this.folder == null) {
            this.folder = this.model.getRootFolder();
        }
        //Log.d(TAG, "AbstractFolderActivity.onResume() model=" + this.model + " folder=" + this.folder);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        this.model.close();
    };

}