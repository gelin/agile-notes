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

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;

import java.util.List;
import java.util.Set;

/**
 *  Adapter which represents the list of subfolders and actions within the
 *  specified folder.
 */
public class FolderListAdapter extends BaseAdapter implements DropListener {

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
    
    public int getCount() {
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
     *  @param  position    position in the list
     *  @return true if on this position there is a folder
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
            actionCheck.setVisibility(View.VISIBLE);
            actionCheck.setChecked(completed);
            actionCheck.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    boolean checked = ((CheckBox)v).isChecked();
                    //Log.d(TAG, (checked ? "checked" : "unchecked") + " for " + action);
                    completeAction(action, checked);
                    strikeOut(actionHead, checked);
                }
            });
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

    public void onDrop(int from, int to) {
        if (to < 0) {
            to = 0;
        }
        if (to > getCount()) {
            to = getCount();
        }
        if (isFolder(from)) {
            if (to > this.folders.size()) {
                to = this.folders.size();
            }
            this.folders.add(to, this.folders.get(from));
        } else {
            int fromAction = toActionPosition(from);
            int toAction = toActionPosition(to);
            if (toAction < 0) {
                toAction = 0;
            }
            this.actions.add(toAction, this.actions.get(fromAction));
        }
        //update();
        notifyDataSetChanged();
    }

}
