package com.lendamage.agilegtd.android;

import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderType;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditFolderActivity extends AbstractEditFolderActivity {
    
    boolean binded = false;
    
    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }
    
    @Override
    protected void setContentView() {
        setContentView(R.layout.edit_folder_activity);
    }
    
    void bindViews() {
        if (this.binded) {
            return;
        }
        EditText name = (EditText)findViewById(R.id.folder_name);
        Spinner type = (Spinner)findViewById(R.id.folder_type);
        name.setText(this.folder.getName());
        FolderTypeAdapter adapter = (FolderTypeAdapter)type.getAdapter();
        type.setSelection(adapter.getPosition(this.folder.getType()));
        this.binded = true;
    }
    
    protected void onOkClick() {
        EditText name = (EditText)findViewById(R.id.folder_name);
        Spinner type = (Spinner)findViewById(R.id.folder_type);
        try {
            this.folder.edit().
                setName(name.getText().toString()).
                setType((FolderType)type.getSelectedItem()).
                commit();
            finish();
        } catch (FolderAlreadyExistsException e) {
            Toast.makeText(this, R.string.folder_already_exists_error, Toast.LENGTH_LONG).show();
            name.requestFocus();
        }
    }

}
