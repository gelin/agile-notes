package com.lendamage.agilegtd.android;

import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;

public class SelectMultiFolderTreeView extends ListView {

    public SelectMultiFolderTreeView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SelectMultiFolderTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectMultiFolderTreeView(Context context) {
        super(context);
        init();
    }
    
    /**
     *  Common init method.
     */
    void init() {
        setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectMultiFolderTreeAdapter adapter = (SelectMultiFolderTreeAdapter)parent.getAdapter();
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
        throw new IllegalStateException(SelectMultiFolderTreeView.class + " uses predefined adapter");
    }
    
    /**
     *  Sets the initial tree state.
     */
    public void setTree(FolderTree tree, Set<Folder> selected) {
        super.setAdapter(new SelectMultiFolderTreeAdapter(
                getContext(), tree, selected));
    }
    
    /**
     *  Returns the selected folders.
     */
    public Set<Folder> getSelected() {
        SelectMultiFolderTreeAdapter adapter = (SelectMultiFolderTreeAdapter)getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("the tree is not initialized, call setTree()");
        }
        return adapter.getSelected();
    }

}
