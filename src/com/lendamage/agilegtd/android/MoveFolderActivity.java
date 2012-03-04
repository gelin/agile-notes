/*
    Agile GTD. Flexible Android implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
        if (isNewItemFirst()) {
            moveTo.getFolders().add(0, this.folder);
        } else {
            moveTo.getFolders().add(this.folder);
        }
    }

}