package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.IntentParams.ACTION_POSITION_EXTRA;
import static com.lendamage.agilegtd.android.IntentParams.FOLDER_PATH_EXTRA;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.Path;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 *  Activity to view the action.
 *  The path to the folder where the action is located
 *  and the position of the action within the folder
 *  are passed in extra.
 */
public class ViewActionActivity extends AbstractActionActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_action_activity);
        
        OnClickListener copyListener = new OnClickListener() {
            public void onClick(View v) {
                copyAction();
            }
        };
        findViewById(R.id.copy_button).setOnClickListener(copyListener);
        findViewById(R.id.action_folders).setOnClickListener(copyListener);
        
        OnClickListener editListener = new OnClickListener() {
            public void onClick(View v) {
                editAction();
            }
        };
        findViewById(R.id.edit_button).setOnClickListener(editListener);
        findViewById(R.id.action_body).setOnClickListener(editListener);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (this.action == null) {
            return;
        }
        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(this.action.getHead());
        TextView bodyView = (TextView)findViewById(R.id.action_body);
        bodyView.setText(this.action.getBody());
        
        ViewGroup foldersView = (ViewGroup)findViewById(R.id.action_folders);
        for (Folder folder : this.action.getFolders()) {
            TextView folderView = new TextView(this);
            Path path = folder.getPath();
            if (path.isRoot()) {
                folderView.setText(R.string.root_folder);
            } else {
                folderView.setText(path.toString());
            }
            foldersView.addView(folderView);
        }
    }
    
    void copyAction() {
        //TODO
    }
    
    void editAction() {
        Intent intent = new Intent(this, EditActionActivity.class);
        intent.putExtra(FOLDER_PATH_EXTRA, this.folder.getPath().toString());
        intent.putExtra(ACTION_POSITION_EXTRA, this.folder.getActions().indexOf(this.action));
        startActivity(intent);
    }

}