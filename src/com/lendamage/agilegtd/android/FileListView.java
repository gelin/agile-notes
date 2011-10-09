package com.lendamage.agilegtd.android;

import java.io.File;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FileListView extends ListView {

    public FileListView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FileListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileListView(Context context) {
        super(context);
        init();
    }
    
    /**
     *  Common init method.
     */
    void init() {
        setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileListAdapter adapter = (FileListAdapter)parent.getAdapter();
                adapter.check(position);
            }
        });
    }
    
    /**
     *  Don't call this. Call {@link #setFolder(File)}.
     *  @throws IllegalStateException
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        throw new IllegalStateException(FileListView.class + " uses predefined adapter");
    }
    
    /**
     *  Sets the folder for files.
     */
    public void setFolder(File folder) {
        super.setAdapter(new FileListAdapter(
                getContext(), folder));
    }
    
    /**
     *  Returns the selected file.
     */
    public File getSelected() {
        FileListAdapter adapter = (FileListAdapter)getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("the folder is not initialized, call setFolder()");
        }
        return adapter.getSelected();
    }

}
