package com.lendamage.agilegtd.android;

import android.os.Bundle;
import android.widget.ListView;

/**
 *  Activity which displays the tree of folders
 *  and allows to move the specified folder into another folder.
 */
public class MoveFolderActivity extends AbstractFolderActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_folder_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView treeView = (ListView)findViewById(R.id.folder_tree);
        treeView.setAdapter(new MoveFolderTreeAdapter(this, this.model.getRootFolder().getFolderTree()));
    }

}