package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.IntentParams.FOLDER_PATH_EXTRA;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.lendamage.agilegtd.android.model.impl.SimplePath;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.Model;

public class AddFolderActivity extends Activity {
    
    /** Current model */
    Model model;
    /** Parent folder */
    Folder folder;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_folder_activity);
        findViewById(R.id.ok_button).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                createFolder();
            }
        });
        //TODO: refactor
        this.model = ModelAccessor.openModel(this);
        Intent intent = getIntent();
        if (intent.hasExtra(FOLDER_PATH_EXTRA)) {
            this.folder = model.getFolder(new SimplePath(intent.getStringExtra(FOLDER_PATH_EXTRA)));
        }
        if (this.folder != null) {
            setTitle(this.folder.getPath().toString());
        }
        if (this.folder == null) {
            this.folder = model.getRootFolder();
        }
        
        final Spinner type = (Spinner)findViewById(R.id.folder_type);
        //TODO: refactor to use Enum directly
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.folder_types_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
    }
    
    void createFolder() {
        EditText name = (EditText)findViewById(R.id.folder_name);
        //TODO: use type
        this.folder.newFolder(name.getText().toString(), null);
        this.model.close();
        finish();
    }
    
}