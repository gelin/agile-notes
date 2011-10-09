package com.lendamage.agilegtd.android;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *  Adapter which represents the list of file in the folder on the disk.
 */
public class FileListAdapter extends BaseAdapter {

    /** Current context */
    Context context;
    /** Folder over which the adapter works */
    File folder;
    /** Files of this folder */
    File[] files;
    /** Selected file */
    File selected;
    
    public FileListAdapter(Context context, File folder) {
        this.context = context;
        this.folder = folder;
        this.files = this.folder.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (!pathname.isFile()) {
                    return false;
                }
                if (!pathname.getName().endsWith(".xml")) {
                    return false;
                }
                return true;
            }
        });
        if (this.files == null) {
            this.files = new File[]{};
            return;
        }
        Arrays.sort(this.files, new Comparator<File>() {
            public int compare(File file1, File file2) {
                long time1 = file1.lastModified();
                long time2 = file2.lastModified();
                if (time1 < time2) {
                    return -1;
                } else if (time1 > time2) {
                    return 1;
                }
                return 0;
            }
        });
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }
    
    public int getCount() {;
        return this.files.length;
    }

    public Object getItem(int position) {
        return this.files[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(
                    R.layout.file_list_item, parent, false); 
        }
        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(this.files[position].getName());
        return view;
    }

    public void check(int position) {
        this.selected = this.files[position];
        //TODO: optimize
        notifyDataSetChanged();
    }
    
    File getSelected() {
        return this.selected;
    }

}
