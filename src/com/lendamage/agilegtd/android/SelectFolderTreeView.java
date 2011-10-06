package com.lendamage.agilegtd.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;

public class SelectFolderTreeView extends ListView {

    public SelectFolderTreeView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SelectFolderTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectFolderTreeView(Context context) {
        super(context);
        init();
    }
    
    /**
     *  Common init method.
     */
    void init() {
        setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectFolderTreeAdapter adapter = (SelectFolderTreeAdapter)parent.getAdapter();
                adapter.check(id);
            }
        });
    }
    
    /**
     *  Don't call this. Call {@link #setTree(FolderTree, Folder, Folder)}.
     *  @throws IllegalStateException
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        throw new IllegalStateException(SelectFolderTreeView.class + " uses predefined adapter");
    }
    
    /**
     *  Sets the initial tree state.
     */
    public void setTree(FolderTree tree, Folder current, Folder selected) {
        super.setAdapter(new SelectFolderTreeAdapter(
                getContext(), tree, current, selected));
    }
    
    /**
     *  Returns the selected folder.
     */
    public Folder getSelected() {
        SelectFolderTreeAdapter adapter = (SelectFolderTreeAdapter)getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("the tree is not initialized, call setTree()");
        }
        return adapter.getSelected();
    }

}
