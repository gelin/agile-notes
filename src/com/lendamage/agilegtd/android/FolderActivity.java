package com.lendamage.agilegtd.android;

import com.lendamage.agilegtd.android.model.impl.SQLiteModel;
import com.lendamage.agilegtd.model.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class FolderActivity extends Activity {
    
    /** Add folder dialog ID */
    private static final int ADD_FOLDER_DIALOG = 0;
    
    /** Current model */
    Model model;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.folder);
        findViewById(R.id.add_folder_button).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(ADD_FOLDER_DIALOG);
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        model = ModelAccessor.openModel(this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        model.close();
    };
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case ADD_FOLDER_DIALOG:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.add_folder);
            View content = getLayoutInflater().inflate(R.layout.add_folder_dialog, 
                    (ViewGroup)findViewById(R.id.add_folder_dialog_root));
            final EditText name = (EditText)content.findViewById(R.id.folder_name);
            final Spinner type = (Spinner)content.findViewById(R.id.folder_type);
            
            builder.setView(content);
            
            //TODO: refactor to use Enum directly
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.folder_types_names, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(adapter);
            
            builder.setPositiveButton(R.string.create_folder, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: closer to reality
                    model.getRootFolder().newFolder(name.getText().toString(), null);
                }
            });
            Dialog dialog = builder.create();
            //http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/java/android/preference/DialogPreference.java;h=bbad2b6d432ce44ad05ddbc44487000b150135ef;hb=HEAD
            Window window = dialog.getWindow();
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            return dialog;
        default:
            return null;
        }
    }
    
    private void setTitle(String title) {
        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(title);
    }
    
}