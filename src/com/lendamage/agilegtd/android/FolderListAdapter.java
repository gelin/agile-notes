package com.lendamage.agilegtd.android;

import com.lendamage.agilegtd.model.Folder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *  Adapter which represents the list of subfolders and actions withing the
 *  specified folder.
 */
public class FolderListAdapter extends BaseAdapter {

    /** Current context */
    Context context;
    /** Folder over which the adapter works */
    Folder folder;
    
    public FolderListAdapter(Context context, Folder folder) {
        this.context = context;
        this.folder = folder;
    }
    
    public int getCount() {;
        //TODO: add actions.
        return folder.getFolders().size();
    }

    public Object getItem(int position) {
        //TODO: add actions
        return folder.getFolders().get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //TODO: add actions
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(
                    R.layout.folder_list_item, parent, false);
        }
        TextView folderName = (TextView)view.findViewById(R.id.folder_name);
        folderName.setText(folder.getFolders().get(position).getName());
        
        return view;
    }
    
    /**
     *  Checks that the specified position is the position of the first folder.
     */
    boolean isFirstFolder(int position) {
        //TODO: add actions
        return position == 0;
    }
    
    /**
     *  Checks that the specified position is the position of the last folder.
     */
    boolean isLastFolder(int position) {
        //TODO: add actions
        return position == getCount() - 1;
    }

}
