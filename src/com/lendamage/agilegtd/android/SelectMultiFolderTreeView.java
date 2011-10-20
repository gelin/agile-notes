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
