package com.lendamage.agilegtd.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;
import com.lendamage.agilegtd.model.FolderTree.Node;

public class MoveFolderTreeAdapter extends AbstractFolderTreeAdapter {

    /** Selected folder */
    Folder selection;
    
    public MoveFolderTreeAdapter(Context context, FolderTree tree) {
        super(context, tree);
    }
    
    @Override
    protected View inflateView(ViewGroup parent) {
        return LayoutInflater.from(this.context).inflate(
                R.layout.move_folder_tree_item, parent, false);
    }

    @Override
    protected void bindView(View view, Node node) {
        // TODO Auto-generated method stub
    }

}
