package com.lendamage.agilegtd.android;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddFolderActivity extends AbstractFolderActivity {
    
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
        
        Spinner type = (Spinner)findViewById(R.id.folder_type);
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
        finish();
    }
    
}