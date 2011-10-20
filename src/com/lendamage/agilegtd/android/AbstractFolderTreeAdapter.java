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
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;
import com.lendamage.agilegtd.model.FolderTree.Node;

/**
 *  Adapter which represents the tree of folders.
 *  The tree is represented by the list which can be expanded/collapsed by
 *  adding/removing of subitems of the current item.
 *  This class encapsulates only expanding/collapsing functionality,
 *  selection of folders should be implemented by subclasses.
 */
public abstract class AbstractFolderTreeAdapter extends BaseAdapter {

    /** Image level of the expand/collapse image for leaf item */
    static final int LEVEL_LEAF = 0;
    /** Image level of the expand/collapse image for collapsed item */
    static final int LEVEL_COLLAPSED = 1;
    /** Image level of the expand/collapse image for expanded item */
    static final int LEVEL_EXPANDED = 2;
    /** Padding for one depth unit */
    static final int DEPTH_PADDING = 16;
    
    /** Current context */
    Context context;
    /** Tree to work over */
    FolderTree tree;
    
    public AbstractFolderTreeAdapter(Context context, FolderTree tree) {
        this.context = context;
        this.tree = tree;
    }
    
    @Override
    public boolean hasStableIds() {
        return true;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    };
    
    public int getCount() {;
        return this.tree.getCount();
    }

    /**
     *  Returns the tree node.
     */
    public Object getItem(int position) {
        return this.tree.getNodeByPosition(position);
    }

    public long getItemId(int position) {
        return this.tree.getNodeByPosition(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflateView(parent); 
        }
        FolderTree.Node node = this.tree.getNodeByPosition(position);
        bindCommonView(view, node);
        bindView(view, node);
        return view;
    }

    /**
     *  Creates new view for tree item.
     */
    protected abstract View inflateView(ViewGroup parent);
    
    /**
     *  Binds common (for all subclasses) item view elements.
     */
    void bindCommonView(View view, final FolderTree.Node node) {
        ImageView expand = (ImageView)view.findViewById(R.id.expand_icon);
        expand.setImageLevel(node.isLeaf() ? LEVEL_LEAF : node.isExpanded() ? LEVEL_EXPANDED : LEVEL_COLLAPSED);
        expand.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                node.setExpanded(!node.isExpanded());
                notifyDataSetChanged();
            }
        });
        TextView name = (TextView)view.findViewById(R.id.folder_name);
        Folder folder = node.getFolder();
        if (folder.getPath().isRoot()) {
            name.setText(R.string.root_folder);
        } else {
            name.setText(folder.getName());
        }
        name.setPadding(node.getDepth() * DEPTH_PADDING, 
                name.getPaddingTop(), name.getPaddingRight(), name.getPaddingBottom());
    }
    
    /**
     *  Binds specific item view elements.
     */
    protected abstract void bindView(View view, FolderTree.Node node);
    
    /**
     *  Expands the tree to the specified folder.
     */
    protected void expandTo(Folder folder) {
        Node node = this.tree.getNodeByFolder(folder);
        if (node == null) {
            return;
        }
        Node parent = node.getParent();
        while (parent != null) {
            parent.setExpanded(true);
            parent = parent.getParent();
        }
    }

}
