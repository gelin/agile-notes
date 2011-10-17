package com.lendamage.agilegtd.android;

import java.util.List;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *  Adapter which represents the list of subfolders and actions within the
 *  specified folder.
 */
public class FolderListAdapter extends BaseAdapter {

    /** View type of folder */
    static final int FOLDER_VIEW_TYPE = 0;
    /** View type of action */
    static final int ACTION_VIEW_TYPE = 1;
    /** Number of view types */
    static final int VIEW_TYPES = 2;
    
    /** Current context */
    Context context;
    /** Folder over which the adapter works */
    Folder folder;
    /** List of folders */
    List<Folder> folders;
    /** List of actions */
    List<Action> actions;
    
    public FolderListAdapter(Context context, Folder folder) {
        this.context = context;
        this.folder = folder;
        update();
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }
    
    @Override
    public int getViewTypeCount() {
        return VIEW_TYPES;
    }
    
    @Override
    public int getItemViewType(int position) {
        if (isFolder(position)) {
            return FOLDER_VIEW_TYPE;
        } else {
            return ACTION_VIEW_TYPE;
        }
    }
    
    public int getCount() {;
        //update();     //avoid unnesessary access to DB
        return this.folders.size() + this.actions.size();
    }

    public Object getItem(int position) {
        if (isFolder(position)) {
            return this.folders.get(position);
        } else {
            return this.actions.get(toActionPosition(position));
        }
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (isFolder(position)) {
            if (view == null) {
                view = inflateFolderView(parent); 
            }
            bindFolderView(view, this.folders.get(position));
        } else {
            if (view == null) {
                view = inflateActionView(parent); 
            }
            bindActionView(view, this.actions.get(toActionPosition(position)));
        }
        return view;
    }
    
    /**
     *  Updates the folders and actions lists.
     */
    void update() {
        this.folders = this.folder.getFolders();
        this.actions = this.folder.getActions();
    }
    
    /**
     *  Checks that the folder (not action) is located at the specified position.
     */
    boolean isFolder(int position) {
        return position < this.folders.size();
    }
    
    /**
     *  Creates new view for folder.
     */
    View inflateFolderView(ViewGroup parent) {
        return LayoutInflater.from(this.context).inflate(
                R.layout.folder_list_item, parent, false);
    }
    
    /**
     *  Binds folder to view.
     */
    void bindFolderView(View view, Folder folder) {
        TextView folderName = (TextView)view.findViewById(R.id.folder_name);
        folderName.setText(folder.getName());
    }
    
    /**
     *  Creates new view for action.
     */
    View inflateActionView(ViewGroup parent) {
        return LayoutInflater.from(this.context).inflate(
                R.layout.action_list_item, parent, false);
    }
    
    /**
     *  Binds action to view.
     */
    void bindActionView(View view, Action action) {
        TextView actionHead = (TextView)view.findViewById(R.id.action_head);
        actionHead.setText(action.getHead());
    }
    
    /**
     *  Converts the list position to the position in the actions list.
     */
    int toActionPosition(int position) {
        return position - this.folders.size();
    }
    
    /**
     *  Checks that the specified position is the position of the first folder.
     */
    boolean isFirstFolder(int position) {
        return isFolder(position) && position == 0;
    }
    
    /**
     *  Checks that the specified position is the position of the last folder.
     */
    boolean isLastFolder(int position) {
        return isFolder(position) && position == this.folders.size() - 1;
    }
    
    /**
     *  Checks that the specified position is the position of the first action.
     */
    boolean isFirstAction(int position) {
        return !isFolder(position) && position == this.folders.size();
    }
    
    /**
     *  Checks that the specified position is the position of the last action.
     */
    boolean isLastAction(int position) {
        return !isFolder(position) && position == this.folders.size() + this.actions.size() - 1;
    }

}
