package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.IntentParams.ACTION_POSITION_EXTRA;
import static com.lendamage.agilegtd.android.IntentParams.FOLDER_PATH_EXTRA;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

/**
 *  Activity which displays the folder content: subfolders and activities.
 *  The path to the folder to display is passed in extra.
 *  If no path is passed, the root folder is displayed.
 *  It's the start activity of the application.
 */
public class FolderActivity extends AbstractFolderActivity {

    /** Delete folder confirmation dialog */
    static final int DELETE_FOLDER_CONFIRM_DIALOG = 0;
    /** Delete action confirmation dialog */
    static final int DELETE_ACTION_CONFIRM_DIALOG = 1;
    
    /** Folder to delete */
    Folder folderToDelete = null;
    /** Action to delete */
    Action actionToDelete = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_activity);
        
        findViewById(R.id.add_folder_button).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FolderActivity.this, AddFolderActivity.class);
                intent.putExtra(FOLDER_PATH_EXTRA, FolderActivity.this.folder.getPath().toString());
                startActivity(intent);
            }
        });
        
        findViewById(R.id.add_action_button).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FolderActivity.this, AddActionActivity.class);
                intent.putExtra(FOLDER_PATH_EXTRA, FolderActivity.this.folder.getPath().toString());
                startActivity(intent);
            }
        });
        
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        registerForContextMenu(foldersActions);
        foldersActions.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FolderListAdapter adapter = (FolderListAdapter)parent.getAdapter();
                if (adapter.isFolder(position)) {
                    openFolder((Folder)parent.getItemAtPosition(position));
                } else {
                    openAction((Action)parent.getItemAtPosition(position));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!this.folder.getPath().isRoot()) {
            setTitle(this.folder.getPath().toString());
        }
        ListView foldersActionsList = (ListView)findViewById(R.id.folders_actions);
        foldersActionsList.setAdapter(new FolderListAdapter(this, this.folder));
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        FolderListAdapter adapter = (FolderListAdapter)foldersActions.getAdapter();
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
        if (adapter.isFolder(info.position)) {
            createFolderContextMenu(adapter, info.position, menu);
        } else {
            createActionContextMenu(adapter, info.position, menu);
        }
    }
    
    void createFolderContextMenu(FolderListAdapter adapter, int position, ContextMenu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.folder_menu, menu);
        if (adapter.isFirstFolder(position)) {
            menu.findItem(R.id.move_up_folder).setEnabled(false);
        }
        if (adapter.isLastFolder(position)) {
            menu.findItem(R.id.move_down_folder).setEnabled(false);
        }
        Folder folder = (Folder)adapter.getItem(position);
        menu.setHeaderTitle(folder.getName());
    }
    
    void createActionContextMenu(FolderListAdapter adapter, int position, ContextMenu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        if (adapter.isFirstAction(position)) {
            menu.findItem(R.id.move_up_action).setEnabled(false);
        }
        if (adapter.isLastAction(position)) {
            menu.findItem(R.id.move_down_action).setEnabled(false);
        }
        Action action = (Action)adapter.getItem(position);
        menu.setHeaderTitle(action.getHead());
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        Object itemObject = foldersActions.getItemAtPosition(info.position);
        switch (item.getItemId()) {
        case R.id.open_folder:
            openFolder((Folder)itemObject);
            return true;
        case R.id.edit_folder:
            editFolder((Folder)itemObject);
            return true;
        case R.id.move_up_folder:
            moveUpFolder((Folder)itemObject);
            return true;
        case R.id.move_down_folder:
            moveDownFolder((Folder)itemObject);
            return true;
        case R.id.move_to_folder:
            moveFolder((Folder)itemObject);
            return true;
        case R.id.delete_folder:
            this.folderToDelete = (Folder)itemObject;
            showDialog(DELETE_FOLDER_CONFIRM_DIALOG);
            return true;
        case R.id.open_action:
            openAction((Action)itemObject);
            return true;
        case R.id.edit_action:
            editAction((Action)itemObject);
            return true;
        case R.id.move_up_action:
            moveUpAction((Action)itemObject);
            return true;
        case R.id.move_down_action:
            moveDownAction((Action)itemObject);
            return true;
        case R.id.copy_to_action:
            //TODO
            return true;
        case R.id.delete_action:
            this.actionToDelete = (Action)itemObject;
            showDialog(DELETE_ACTION_CONFIRM_DIALOG);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }
    
    void openFolder(Folder folder) {
        Intent intent = new Intent(this, FolderActivity.class);
        intent.putExtra(FOLDER_PATH_EXTRA, folder.getPath().toString());
        startActivity(intent);
    }
    
    void editFolder(Folder folder) {
        Intent intent = new Intent(this, EditFolderActivity.class);
        intent.putExtra(FOLDER_PATH_EXTRA, folder.getPath().toString());
        startActivity(intent);
    }
    
    void moveUpFolder(Folder folder) {
        List<Folder> folders = this.folder.getFolders();
        int position = folders.indexOf(folder);
        if (position <= 0) {
            return;
        }
        folders.add(position - 1, folder);
        updateFoldersActions();
    }
    
    void moveDownFolder(Folder folder) {
        List<Folder> folders = this.folder.getFolders();
        int position = folders.indexOf(folder);
        if (position >= folders.size()) {
            return;
        }
        folders.add(position + 1, folder);
        updateFoldersActions();
    }
    
    void moveFolder(Folder folder) {
        Intent intent = new Intent(this, MoveFolderActivity.class);
        intent.putExtra(FOLDER_PATH_EXTRA, folder.getPath().toString());
        startActivity(intent);
    }
    
    void deleteFolder(Folder folder) {
        this.folder.getFolders().remove(folder);
        updateFoldersActions();
    }
    
    void openAction(Action action) {
        Intent intent = new Intent(this, ViewActionActivity.class);
        intent.putExtra(FOLDER_PATH_EXTRA, this.folder.getPath().toString());
        intent.putExtra(ACTION_POSITION_EXTRA, this.folder.getActions().indexOf(action));
        startActivity(intent);
    }
    
    void editAction(Action action) {
        Intent intent = new Intent(this, EditActionActivity.class);
        intent.putExtra(FOLDER_PATH_EXTRA, this.folder.getPath().toString());
        intent.putExtra(ACTION_POSITION_EXTRA, this.folder.getActions().indexOf(action));
        startActivity(intent);
    }
    
    void moveUpAction(Action action) {
        List<Action> actions = this.folder.getActions();
        int position = actions.indexOf(action);
        if (position <= 0) {
            return;
        }
        actions.add(position - 1, action);
        updateFoldersActions();
    }
    
    void moveDownAction(Action action) {
        List<Action> actions = this.folder.getActions();
        int position = actions.indexOf(action);
        if (position >= actions.size()) {
            return;
        }
        actions.add(position + 1, action);
        updateFoldersActions();
    }
    
    void deleteAction(Action action) {
        this.folder.getActions().remove(action);
        updateFoldersActions();
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
        case DELETE_FOLDER_CONFIRM_DIALOG:
            builder.setTitle(R.string.delete_folder).
                    setCancelable(true).
                    setMessage(R.string.delete_folder_confirm).
                    setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteFolder(FolderActivity.this.folderToDelete);
                        }
                    }).
                    setNegativeButton(R.string.cancel_button, null);
            return builder.create();
        case DELETE_ACTION_CONFIRM_DIALOG:
            builder.setTitle(R.string.delete_action).
                    setCancelable(true).
                    setMessage(R.string.delete_action_confirm).
                    setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteAction(FolderActivity.this.actionToDelete);
                        }
                    }).
                    setNegativeButton(R.string.cancel_button, null);
            return builder.create();
        default:
            return super.onCreateDialog(id);
        }
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
        case DELETE_FOLDER_CONFIRM_DIALOG:
            dialog.setTitle(this.folderToDelete.getName());
            return;
        case DELETE_ACTION_CONFIRM_DIALOG:
            dialog.setTitle(this.actionToDelete.getHead());
            return;
        }
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.backup_menu:
            startActivity(new Intent(this, BackupActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    void setTitle(String title) {
        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(title);
    }
    
    void updateFoldersActions() {
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        FolderListAdapter adapter = (FolderListAdapter)foldersActions.getAdapter();
        adapter.notifyDataSetChanged();
    }

}