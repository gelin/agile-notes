package com.lendamage.agilegtd.android;

import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lendamage.agilegtd.model.Folder;

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
        setTitle(this.action.getHead());
        SelectMultiFolderTreeView treeView = (SelectMultiFolderTreeView)findViewById(R.id.folder_tree);
        treeView.setTree(this.model.getRootFolder().getFolderTree(), this.action.getFolders());
    }
    
    void copyAction() {
        SelectMultiFolderTreeView tree = (SelectMultiFolderTreeView)findViewById(R.id.folder_tree);
        Set<Folder> newFolders = tree.getSelected();
        if (newFolders.isEmpty()) {
            showDialog(DELETE_ACTION_CONFIRM_DIALOG);
            return;
        }
        Set<Folder> oldFolders = this.action.getFolders();
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
                            CopyActionActivity.this.folder.getActions().remove(
                                    CopyActionActivity.this.action);
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