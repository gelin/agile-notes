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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;
import com.lendamage.agilegtd.model.FolderTree.Node;

public class SelectMultiFolderTreeAdapter extends AbstractFolderTreeAdapter {

    /** Selected folders */
    Set<Folder> selected = new HashSet<Folder>();
    
    public SelectMultiFolderTreeAdapter(Context context, FolderTree tree, 
            Set<Folder> selected) {
        super(context, tree);
        this.selected.clear();
        this.selected.addAll(selected);
        for (Folder folder : this.selected) {
            expandTo(folder);
        }
    }
    
    @Override
    protected View inflateView(ViewGroup parent) {
        return LayoutInflater.from(this.context).inflate(
                R.layout.select_multi_folder_tree_item, parent, false);
    }

    @Override
    protected void bindView(View view, final Node node) {
        CheckBox check = (CheckBox)view.findViewById(R.id.check_button);
        check.setChecked(this.selected.contains(node.getFolder()));
        check.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                check(node.getId());
            }
        });
    }
    
    void check(long id) {
        Folder checking = this.tree.getNodeById(id).getFolder();
        if (this.selected.contains(checking)) {
            this.selected.remove(checking);
        } else {
            this.selected.add(checking);
        }
        //TODO: optimize
        notifyDataSetChanged();
    }
    
    Set<Folder> getSelected() {
        return Collections.unmodifiableSet(this.selected);
    }

}
