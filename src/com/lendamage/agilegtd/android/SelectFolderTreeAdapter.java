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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import android.widget.TextView;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;
import com.lendamage.agilegtd.model.FolderTree.Node;

/**
 *  The adapter which represents the tree of folders with radio buttons and
 *  allows to choose one of the folder.
 */
public class SelectFolderTreeAdapter extends AbstractFolderTreeAdapter {

    /** Selected folder */
    Folder selected;
    /** Current folder */
    Folder current;
    
    public SelectFolderTreeAdapter(Context context, FolderTree tree, 
            Folder current, Folder selected) {
        super(context, tree);
        this.current = current;
        this.selected = selected;
        expandTo(this.selected);
    }
    
    @Override
    protected View inflateView(ViewGroup parent) {
        return LayoutInflater.from(this.context).inflate(
                R.layout.select_folder_tree_item, parent, false);
    }

    @Override
    protected void bindView(View view, final Node node) {
        RadioButton radio = (RadioButton)view.findViewById(R.id.check_button);
        radio.setChecked(node.getFolder().equals(this.selected));
        radio.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                check(node.getId());
            }
        });
        boolean enable = !node.getFolder().getPath().startsWith(this.current.getPath());
        view.setEnabled(enable);
        //view.setClickable(enable);    //WTF?
        radio.setEnabled(enable);
        TextView folderName = (TextView)view.findViewById(R.id.folder_name);
        folderName.setEnabled(enable);
    }
    
    void check(long id) {
        Folder checking = this.tree.getNodeById(id).getFolder();
        if (checking.getPath().startsWith(this.current.getPath())) {
            return; //avoid illegal checks
        }
        this.selected = checking;
        //TODO: optimize
        notifyDataSetChanged();
    }
    
    Folder getSelected() {
        return this.selected;
    }

}
