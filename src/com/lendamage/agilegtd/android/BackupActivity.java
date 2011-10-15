package com.lendamage.agilegtd.android;

import static com.lendamage.agilegtd.android.Tag.TAG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.xml.XmlExporter;

/**
 *  Activity to backup and restore the model.
 */
public class BackupActivity extends Activity {

    /** Backup folder name */
    static final String BACKUP_FOLDER = "agilegtd";
    /** Date format for backup file name */
    static final SimpleDateFormat FILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSZ.'xml'");

    /** Backup folder */
    File backupFolder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_activity);
        
        this.backupFolder = getBackupFolder();
        TextView backupFolderView = (TextView)findViewById(R.id.backup_folder);
        backupFolderView.setText(this.backupFolder.toString());
        
        View backupButton = findViewById(R.id.backup_button);
        backupButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                backup();
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        checkStorageState();
        checkRestoreList();
    }
    
    void checkStorageState() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            findViewById(R.id.storage_mount_warning).setVisibility(View.GONE);
            findViewById(R.id.backup_button).setEnabled(true);
            //findViewById(R.id.restore_button).setEnabled(true);
        } else {
            findViewById(R.id.storage_mount_warning).setVisibility(View.VISIBLE);
            findViewById(R.id.backup_button).setEnabled(false);
            findViewById(R.id.restore_button).setEnabled(false);
        }
    }
    
    void checkRestoreList() {
        new UpdateRestoreList((FileListView)findViewById(R.id.backups_list)).
                execute(this.backupFolder);
    }
    
    File getBackupFolder() {
        File storage = Environment.getExternalStorageDirectory();
        return new File(storage, BACKUP_FOLDER);
    }
    
    File newBackupFile() throws IOException {
        File folder = getBackupFolder();
        folder.mkdirs();
        if (!folder.isDirectory()) {
            Log.w(TAG, "folder " + folder + " is not created");
        }
        String name = FILE_FORMAT.format(new Date());
        File file = new File(folder, name);
        file.createNewFile();
        return file;
    }
    
    void backup() {
        new Backup(this).execute();
    }
    
    void startProgress() {
        ImageView icon = (ImageView)findViewById(R.id.progress_icon);
        icon.setVisibility(View.VISIBLE);
        ((AnimationDrawable)icon.getDrawable()).start();
    }
    
    void stopProgress() {
        ImageView icon = (ImageView)findViewById(R.id.progress_icon);
        icon.setVisibility(View.INVISIBLE);
        ((AnimationDrawable)icon.getDrawable()).stop();
    }
    
    class UpdateRestoreList extends AsyncTask<File, Void, FileListAdapter> {
        FileListView view;
        public UpdateRestoreList(FileListView view) {
            this.view = view;
        }
        @Override
        protected void onPreExecute() {
            findViewById(R.id.restore_button).setEnabled(false);
            startProgress();
        }
        @Override
        protected FileListAdapter doInBackground(File... params) {
            return new FileListAdapter(this.view.getContext(), params[0]);
        }
        @Override
        protected void onPostExecute(FileListAdapter adapter) {
            this.view.setAdapter(adapter);
            //Log.d(TAG, adapter.folder + " contains files " + Arrays.toString(adapter.files));
            stopProgress();
            if (this.view.getCount() == 0) {
                findViewById(R.id.restore_button).setEnabled(false);
            }
        }
    }
    
    class Backup extends AsyncTask<Void, Void, Boolean> {
        Context context;
        File file;
        public Backup(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            findViewById(R.id.backup_button).setEnabled(false);
            startProgress();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                this.file = newBackupFile();
                Model model = ModelAccessor.openModel(this.context);
                XmlExporter.exportModel(model, new FileWriter(this.file));
                model.close();
                Log.i(TAG, "backup created " + this.file);
                return true;
            } catch (Exception e) {
                Log.w(TAG, "backup failed", e);
                return false;
            }
            
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(this.context, getString(R.string.backup_created, this.file), 
                        Toast.LENGTH_LONG).show();
                checkRestoreList();
            } else {
                Toast.makeText(this.context, R.string.backup_failed, Toast.LENGTH_LONG).show();
            }
            checkStorageState();
        }
    }

}