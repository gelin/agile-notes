package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.IntentParams.FOLDER_PATH_EXTRA;
import android.app.Activity;
import android.content.Intent;

import com.lendamage.agilegtd.android.model.impl.SimplePath;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.Model;

/**
 *  Common class for activites which works with the folder and
 *  takes the folder path in intent.
 */
public abstract class AbstractFolderActivity extends Activity {
    
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
        if (intent.hasExtra(FOLDER_PATH_EXTRA)) {
            this.folder = this.model.getFolder(new SimplePath(intent.getStringExtra(FOLDER_PATH_EXTRA)));
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