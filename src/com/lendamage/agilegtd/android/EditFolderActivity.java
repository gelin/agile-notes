package com.lendamage.agilegtd.android;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderType;

/**
 *  Activity to edit already existed folder.
 *  The path to the folder is passed in extra.
 */
public class EditFolderActivity extends AbstractEditFolderActivity {
    
    @Override
    protected void setContentView() {
        setContentView(R.layout.edit_folder_activity);
    }
    
    @Override
    protected void bindViews() {
        EditText name = (EditText)findViewById(R.id.folder_name);
        Spinner type = (Spinner)findViewById(R.id.folder_type);
        name.setText(this.folder.getName());
        FolderTypeAdapter adapter = (FolderTypeAdapter)type.getAdapter();
        type.setSelection(adapter.getPosition(this.folder.getType()));
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
