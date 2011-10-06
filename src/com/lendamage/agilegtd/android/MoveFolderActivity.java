package com.lendamage.agilegtd.android;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lendamage.agilegtd.model.Folder;

/**
 *  Activity which displays the tree of folders
 *  and allows to move the specified folder into another folder.
 */
public class MoveFolderActivity extends AbstractFolderActivity {

    /** Parent folder */
    Folder parent;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_folder_activity);
        
        View okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                moveFolder();
                finish();
            }
        });
        
        View cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.parent == null) {
            this.parent = this.model.getFolder(this.folder.getPath().getParent());
        }
        setTitle(this.folder.getName());
        SelectFolderTreeView treeView = (SelectFolderTreeView)findViewById(R.id.folder_tree);
        treeView.setTree(this.model.getRootFolder().getFolderTree(), this.folder, this.parent);
    }
    
    void moveFolder() {
        SelectFolderTreeView tree = (SelectFolderTreeView)findViewById(R.id.folder_tree);
        Folder moveTo = tree.getSelected();
        moveTo.getFolders().add(this.folder);
    }

}