package com.lendamage.agilegtd.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;
import com.lendamage.agilegtd.model.FolderTree.Node;

public class MoveFolderTreeAdapter extends AbstractFolderTreeAdapter {

    /** Selected folder */
    Folder selected;
    /** Current folder */
    Folder current;
    
    public MoveFolderTreeAdapter(Context context, FolderTree tree, 
            Folder current, Folder selected) {
        super(context, tree);
        this.current = current;
        this.selected = selected;
        //TODO: expand to selected
    }
    
    @Override
    protected View inflateView(ViewGroup parent) {
        return LayoutInflater.from(this.context).inflate(
                R.layout.move_folder_tree_item, parent, false);
    }

    @Override
    protected void bindView(View view, final Node node) {
        RadioButton radio = (RadioButton)view.findViewById(R.id.check);
        radio.setChecked(node.getFolder().equals(this.selected));
        radio.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                select(node.getId());
            }
        });
        boolean enable = !node.getFolder().equals(this.current);
        //TODO: improve and fix
        view.setEnabled(enable);
        //view.setClickable(enable);
        radio.setEnabled(enable);
    }
    
    void select(long id) {
        this.selected = this.tree.getNodeById(id).getFolder();
        //TODO: optimize
        notifyDataSetChanged();
    }
    
    Folder getSelected() {
        return this.selected;
    }

}
