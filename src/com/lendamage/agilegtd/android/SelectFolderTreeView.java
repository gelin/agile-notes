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
