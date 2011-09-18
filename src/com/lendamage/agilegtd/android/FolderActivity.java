package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.IntentParams.FOLDER_PATH_EXTRA;

import java.util.List;

import com.lendamage.agilegtd.model.Folder;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
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

public class FolderActivity extends AbstractFolderActivity {

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
        
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        registerForContextMenu(foldersActions);
        foldersActions.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: add action
                openFolder((Folder)parent.getItemAtPosition(position));
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.folder_menu, menu);
        
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        FolderListAdapter adapter = (FolderListAdapter)foldersActions.getAdapter();
        if (adapter.isFirstFolder(info.position)) {
            menu.findItem(R.id.move_up_folder).setEnabled(false);
        }
        if (adapter.isLastFolder(info.position)) {
            menu.findItem(R.id.move_down_folder).setEnabled(false);
        }
        
        //TODO: add action
        Folder folder = (Folder)foldersActions.getItemAtPosition(info.position);
        menu.setHeaderTitle(folder.getName());
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        Folder folder = (Folder)foldersActions.getItemAtPosition(info.position);
        switch (item.getItemId()) {
        case R.id.open_folder:
            openFolder(folder);
            return true;
        case R.id.edit_folder:
            editFolder(folder);
            return true;
        case R.id.move_up_folder:
            moveUpFolder(folder);
            return true;
        case R.id.move_down_folder:
            moveDownFolder(folder);
            return true;
        case R.id.delete_folder:
            //TODO
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
        
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        FolderListAdapter adapter = (FolderListAdapter)foldersActions.getAdapter();
        adapter.notifyDataSetChanged();
    }
    
    void moveDownFolder(Folder folder) {
        List<Folder> folders = this.folder.getFolders();
        int position = folders.indexOf(folder);
        if (position >= folders.size()) {
            return;
        }
        folders.add(position + 1, folder);
        
        ListView foldersActions = (ListView)findViewById(R.id.folders_actions);
        FolderListAdapter adapter = (FolderListAdapter)foldersActions.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void setTitle(String title) {
        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(title);
    }

}