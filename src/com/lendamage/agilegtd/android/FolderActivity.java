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
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.operations.Operations;

import java.util.List;
import java.util.Set;

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
    private Folder folderToDelete = null;
    /** Action to delete */
    private Action actionToDelete = null;

    /** Model operations */
    private Operations operations;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.operations = new Operations(getModel());
        setContentView(R.layout.folder_activity);
        
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

    private Operations getOperations() {
        return this.operations;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getFolder().getPath().isRoot()) {
            setTitle(getFolder().getPath().toString());
        }
        ListView foldersActionsList = (ListView)findViewById(R.id.folders_actions);
        foldersActionsList.setAdapter(new FolderListAdapter(this, getModel(), getFolder()));
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
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
            menu.findItem(R.id.move_first_folder).setEnabled(false);
        }
        if (adapter.isLastFolder(position)) {
            menu.findItem(R.id.move_down_folder).setEnabled(false);
            menu.findItem(R.id.move_last_folder).setEnabled(false);
        }
        Folder folder = (Folder)adapter.getItem(position);
        menu.setHeaderTitle(folder.getName());
    }
    
    void createActionContextMenu(FolderListAdapter adapter, int position, ContextMenu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        if (adapter.isFirstAction(position)) {
            menu.findItem(R.id.move_up_action).setEnabled(false);
            menu.findItem(R.id.move_first_action).setEnabled(false);
        }
        if (adapter.isLastAction(position)) {
            menu.findItem(R.id.move_down_action).setEnabled(false);
            menu.findItem(R.id.move_last_action).setEnabled(false);
        }
        Action action = (Action)adapter.getItem(position);
        menu.setHeaderTitle(action.getHead());
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
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
        case R.id.move_first_folder:
            moveFirstFolder((Folder)itemObject);
            return true;
        case R.id.move_last_folder:
            moveLastFolder((Folder)itemObject);
            return true;
        case R.id.move_to_folder:
            moveFolder((Folder)itemObject);
            return true;
        case R.id.share_folder:
            ShareUtils.sendFolder(this, R.string.share_folder_title, (Folder)itemObject);
            return true;
        case R.id.delete_folder:
            deleteFolder((Folder)itemObject);
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
        case R.id.move_first_action:
            moveFirstAction((Action)itemObject);
            return true;
        case R.id.move_last_action:
            moveLastAction((Action)itemObject);
            return true;
        case R.id.copy_to_action:
            copyAction((Action)itemObject);
            return true;
        case R.id.share_action:
            ShareUtils.sendAction(this, R.string.share_action_title, (Action)itemObject);
            return true;
        case R.id.delete_action:
            deleteAction((Action)itemObject);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }
    
    void openFolder(Folder folder) {
        startFolderActivity(FolderActivity.class, folder);
    }
    
    void editFolder(Folder folder) {
        startFolderActivity(EditFolderActivity.class, folder);
    }
    
    void moveUpFolder(Folder folder) {
        getOperations().moveUpFolder(getFolder(), folder);
        updateFoldersActions();
    }
    
    void moveDownFolder(Folder folder) {
        getOperations().moveDownFolder(getFolder(), folder);
        updateFoldersActions();
    }

    void moveFirstFolder(Folder folder) {
        getOperations().moveFirstFolder(getFolder(), folder);
        updateFoldersActions();
    }

    void moveLastFolder(Folder folder) {
        getOperations().moveLastFolder(getFolder(), folder);
        updateFoldersActions();
    }
    
    void moveFolder(Folder folder) {
        startFolderActivity(MoveFolderActivity.class, folder);
    }
    
    void deleteFolder(Folder folder) {
        if (getOperations().isDeletableToTrash(folder)) {
            getOperations().deleteFolder(folder);
            updateFoldersActions(true); //this folder is not updated directly, need to reread
        } else {
            this.folderToDelete = folder;
            showDialog(DELETE_FOLDER_CONFIRM_DIALOG);
        }
    }

    void openAction(Action action) {
        startActionActivity(ViewActionActivity.class, action);
    }
    
    void editAction(Action action) {
        startActionActivity(EditActionActivity.class, action);
    }
    
    void moveUpAction(Action action) {
        List<Action> actions = getFolder().getActions();
        int position = actions.indexOf(action);
        if (position <= 0) {
            return;
        }
        actions.add(position - 1, action);
        updateFoldersActions();
    }
    
    void moveDownAction(Action action) {
        List<Action> actions = getFolder().getActions();
        int position = actions.indexOf(action);
        if (position >= actions.size()) {
            return;
        }
        actions.add(position + 1, action);
        updateFoldersActions();
    }

    void moveFirstAction(Action action) {
        List<Action> actions = getFolder().getActions();
        actions.add(0, action);
        updateFoldersActions();
    }

    void moveLastAction(Action action) {
        List<Action> actions = getFolder().getActions();
        actions.add(actions.size(), action);
        updateFoldersActions();
    }
    
    void copyAction(Action action) {
        startActionActivity(CopyActionActivity.class, action);
    }
    
    void deleteAction(Action action) {
        if (getOperations().hasTrashFolder() || FolderType.TRASH.equals(getFolder().getType())) {
            this.actionToDelete = action;
            showDialog(DELETE_ACTION_CONFIRM_DIALOG);
        } else {
            deleteActionToTrash(action);
        }
    }
    
    void deleteActionFromFolder(Action action) {
        getFolder().getActions().remove(action);
        updateFoldersActions();
    }
    
    void deleteActionToTrash(Action action) {
        Set<Folder> folders = action.getFolders();
        //folders.addAll(this.trashFolders);    //TODO
        folders.remove(getFolder());
        updateFoldersActions(true);     //this folder is not updated directly, need to reread
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
                            getOperations().deleteFolder(FolderActivity.this.folderToDelete);
                            updateFoldersActions(true);
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
                            deleteActionFromFolder(FolderActivity.this.actionToDelete);
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
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.folder_options, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.new_item_position);
        NewItemPosition position = NewItemPosition.valueOf(getModel());
        item.setTitle(position.getTitleRes());
        item.setIcon(position.getIconRes());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.add_folder:
            IntentUtils.startFolderActivity(this, AddFolderActivity.class, getFolder());
            return true;
        case R.id.add_action:
            IntentUtils.startFolderActivity(this, AddActionActivity.class, getFolder());
            return true;
        case R.id.share_folder:
            ShareUtils.sendFolder(this, R.string.share_folder_title, getFolder());
            return true;
        case R.id.new_item_position:
            getModel().getSettings().setNewItemPosition(NewItemPosition.valueOf(getModel()).getNextPosition());
            invalidateOptionsMenu();
            updateFoldersActions(true);
            return true;
        case R.id.backup_menu:
            startActivity(new Intent(this, BackupActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    void updateFoldersActions() {
        updateFoldersActions(false);
    }
    
    void updateFoldersActions(boolean reread) {
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        FolderListAdapter adapter = (FolderListAdapter)foldersActions.getAdapter();
        if (reread) {
            adapter.update();
        }
        adapter.notifyDataSetChanged();
    }

}