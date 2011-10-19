package com.lendamage.agilegtd.android;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *  Start activity for this application.
 *  Creates the initial set of folders if the model is empty.
 *  Starts FolderActivity for the root folder.
 */
public class LaunchActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model model = ModelAccessor.openModel(this);
        if (model.getRootFolder().getFolders().isEmpty() && model.getRootFolder().getActions().isEmpty()) {
            createInitialFolders(model);
        }
        model.close();
        startActivity(new Intent(this, FolderActivity.class));
        finish();
    }
    
    void createInitialFolders(Model model) {
        Folder root = model.getRootFolder();
        root.newFolder(getString(R.string.inbox_folder), FolderType.INBOX);
        root.newFolder(getString(R.string.projects_folder), FolderType.PROJECTS);
        Folder contexts = root.newFolder(getString(R.string.contexts_folder), FolderType.CONTEXTS);
        contexts.newFolder(getString(R.string.at_work_context_folder), FolderType.CONTEXT);
        contexts.newFolder(getString(R.string.at_home_context_folder), FolderType.CONTEXT);
        Folder priorities = root.newFolder(getString(R.string.priorities_folder), FolderType.PRIORITIES);
        priorities.newFolder(getString(R.string.high_priority_folder), FolderType.PRIORITY);
        priorities.newFolder(getString(R.string.normal_priority_folder), FolderType.PRIORITY);
        priorities.newFolder(getString(R.string.low_priority_folder), FolderType.PRIORITY);
        root.newFolder(getString(R.string.persons_folder), FolderType.PERSONS);
        root.newFolder(getString(R.string.completed_folder), FolderType.COMPLETED);
        root.newFolder(getString(R.string.trash_folder), FolderType.TRASH);
    }

}