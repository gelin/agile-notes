package com.lendamage.agilegtd.android;

import java.util.List;
import java.util.Set;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
    /** Current model */
    Model model;
    /** Folder over which the adapter works */
    Folder folder;
    /** List of folders */
    List<Folder> folders;
    /** List of actions */
    List<Action> actions;
    /** Set of completed folders */
    List<Folder> completedFolders;
    
    public FolderListAdapter(Context context, Model model, Folder folder) {
        this.context = context;
        this.model = model;
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
        this.completedFolders = this.model.findFolders(FolderType.COMPLETED);
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
    void bindActionView(View view, final Action action) {
        CheckBox actionCheck = (CheckBox)view.findViewById(R.id.check_button);
        final TextView actionHead = (TextView)view.findViewById(R.id.action_head);
        boolean completed = false;
        if (this.completedFolders.size() == 0) {
            actionCheck.setVisibility(View.GONE);
            actionCheck.setOnCheckedChangeListener(null);
        } else {
            completed = isCompleted(action);
            actionCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //TODO: check that this doesn't call in unnecessary cases
                    completeAction(action, isChecked);
                    strikeOut(actionHead, isChecked);
                }
            });
            actionCheck.setVisibility(View.VISIBLE);
            actionCheck.setChecked(completed);
        }
        actionHead.setText(action.getHead());
        strikeOut(actionHead, completed);
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
    
    boolean isCompleted(Action action) {
        Set<Folder> actionFolders = action.getFolders();
        for (Folder completed : this.completedFolders) {
            if (actionFolders.contains(completed)) {
                return true;
            }
        }
        return false;
    }
    
    void strikeOut(TextView view, boolean strike) {
        if (strike) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            view.setPaintFlags(view.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
    
    void completeAction(Action action, boolean completed) {
        if (completed) {
            action.getFolders().addAll(this.completedFolders);
        } else {
            action.getFolders().removeAll(this.completedFolders);
        }
        if (FolderType.COMPLETED.equals(this.folder.getType())) {
            update();
            notifyDataSetChanged();
        }
    }

}
