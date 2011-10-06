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
        //TODO: expand to selected
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
