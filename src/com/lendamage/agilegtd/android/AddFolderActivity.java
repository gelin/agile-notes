package com.lendamage.agilegtd.android;

import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderType;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddFolderActivity extends AbstractFolderActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_folder_activity);
        
        OnClickListener okListener = new OnClickListener() {
            public void onClick(View v) {
                createFolder();
            }
        };
        findViewById(R.id.ok_button).setOnClickListener(okListener);
        findViewById(R.id.create_button).setOnClickListener(okListener);
        
        TextView name = (TextView)findViewById(R.id.folder_name);
        name.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/java/android/preference/DialogPreference.java;h=bbad2b6d432ce44ad05ddbc44487000b150135ef;hb=HEAD
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }
        });
        
        Spinner type = (Spinner)findViewById(R.id.folder_type);
        type.setAdapter(new FolderTypeAdapter(this));
    }
    
    void createFolder() {
        EditText name = (EditText)findViewById(R.id.folder_name);
        if (name.length() == 0) {
            Toast.makeText(this, R.string.enter_folder_name_error, Toast.LENGTH_LONG).show();
            name.requestFocus();
            return;
        }
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