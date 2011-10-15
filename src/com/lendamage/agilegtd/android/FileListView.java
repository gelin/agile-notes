package com.lendamage.agilegtd.android;

import java.io.File;

import com.lendamage.agilegtd.android.FileListAdapter.OnCheckListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FileListView extends ListView {
    
    /** External on check listener */
    OnCheckListener onCheckListener;
    
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
        if (!(adapter instanceof FileListAdapter)) {
            throw new IllegalArgumentException("only FileListAdapter is accepted");
        }
        super.setAdapter(adapter);
        ((FileListAdapter)adapter).setOnCheckListener(this.internalOnCheckListener);
    }
    
    /**
     *  Sets the folder for files.
     */
    public void setFolder(File folder) {
        setAdapter(new FileListAdapter(getContext(), folder));
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
    
    /**
     *  Sets the listener which receives the events when the list item is checked/clicked.
     */
    void setOnCheckListener(OnCheckListener listener) {
        this.onCheckListener = listener;
    }
    
    /** Internal on check listener */
    OnCheckListener internalOnCheckListener = new OnCheckListener() {
        public void onCheck(File selected) {
            if (FileListView.this.onCheckListener == null) {
                return;
            }
            FileListView.this.onCheckListener.onCheck(selected);
        }
    };

}
