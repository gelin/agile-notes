package com.lendamage.agilegtd.android;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 *  Base activity for the activities which changes the folder.
 */
abstract class AbstractEditFolderActivity extends AbstractFolderActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initViews();
    }
    
    abstract protected void setContentView();
    
    protected void initViews() {
        OnClickListener okListener = new OnClickListener() {
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                onOkClick();
            }
        };
        findViewById(R.id.ok_button).setOnClickListener(okListener);
        findViewById(R.id.ok_big_button).setOnClickListener(okListener);
        
        Spinner type = (Spinner)findViewById(R.id.folder_type);
        type.setAdapter(new FolderTypeAdapter(this));
    }
    
    protected boolean validate() {
        EditText name = (EditText)findViewById(R.id.folder_name);
        if (name.length() == 0) {
            Toast.makeText(this, R.string.enter_folder_name_error, Toast.LENGTH_LONG).show();
            name.requestFocus();
            return false;
        }
        return true;
    }
    
    protected abstract void onOkClick();

}