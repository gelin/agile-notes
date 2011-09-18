package com.lendamage.agilegtd.android;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderType;

public class AddFolderActivity extends AbstractEditFolderActivity {

    @Override
    protected void setContentView() {
        setContentView(R.layout.add_folder_activity);
    }

    @Override
    protected void onOkClick() {
        EditText name = (EditText)findViewById(R.id.folder_name);
        Spinner type = (Spinner)findViewById(R.id.folder_type);
        try {
            this.folder.newFolder(name.getText().toString(), (FolderType)type.getSelectedItem());
            finish();
        } catch (FolderAlreadyExistsException e) {
            Toast.makeText(this, R.string.folder_already_exists_error, Toast.LENGTH_LONG).show();
            name.requestFocus();
        }
    }

}