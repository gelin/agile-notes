package com.lendamage.agilegtd.android;

import com.lendamage.agilegtd.model.Folder;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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
        
        ListView tree = (ListView)findViewById(R.id.folder_tree);
        tree.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoveFolderTreeAdapter adapter = (MoveFolderTreeAdapter)parent.getAdapter();
                adapter.select(id);
            }
        });
        
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
        ListView treeView = (ListView)findViewById(R.id.folder_tree);
        treeView.setAdapter(new MoveFolderTreeAdapter(
                this, this.model.getRootFolder().getFolderTree(), this.folder, this.parent));
    }
    
    void moveFolder() {
        ListView tree = (ListView)findViewById(R.id.folder_tree);
        MoveFolderTreeAdapter adapter = (MoveFolderTreeAdapter)tree.getAdapter();
        Folder moveTo = adapter.getSelected();
        moveTo.getFolders().add(this.folder);
    }

}