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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.lendamage.agilegtd.model.Folder;

import java.util.Set;

/**
 *  Activity which displays the tree of folders
 *  and allows to copy the specified action into another folder.
 */
public class CopyActionActivity extends AbstractActionActivity {

    /** Delete action confirmation dialog */
    static final int DELETE_ACTION_CONFIRM_DIALOG = 1;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.copy_action_activity);
        
        View okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                copyAction();
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
        setTitle(getAction().getHead());
        SelectMultiFolderTreeView treeView = (SelectMultiFolderTreeView)findViewById(R.id.folder_tree);
        treeView.setTree(getModel().getRootFolder().getFolderTree(), getAction().getFolders());
    }
    
    void copyAction() {
        SelectMultiFolderTreeView tree = (SelectMultiFolderTreeView)findViewById(R.id.folder_tree);
        Set<Folder> newFolders = tree.getSelected();
        if (newFolders.isEmpty()) {
            showDialog(DELETE_ACTION_CONFIRM_DIALOG);
            return;
        }
        Set<Folder> oldFolders = getAction().getFolders();
        oldFolders.addAll(newFolders);
        oldFolders.retainAll(newFolders);
        finish();
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
        case DELETE_ACTION_CONFIRM_DIALOG:
            builder.setTitle(R.string.delete_action).
                    setCancelable(true).
                    setMessage(R.string.delete_action_confirm).
                    setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getAction().getFolders().clear();
                            finish();
                        }
                    }).
                    setNegativeButton(R.string.cancel_button, null);
            return builder.create();
        default:
            return super.onCreateDialog(id);
        }
    }

}