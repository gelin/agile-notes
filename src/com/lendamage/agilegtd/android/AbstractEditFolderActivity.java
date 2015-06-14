/*
    Agile GTD. Flexible Android implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lendamage.agilegtd.android;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.lendamage.agilegtd.model.FolderType;

/**
 *  Base activity for the activities which changes the folder.
 */
abstract class AbstractEditFolderActivity extends AbstractFolderActivity {
    
    /** bind view flag */
    private boolean binded = false;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initViews();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (!this.binded) {
            bindViews();
            this.binded = true;
        }
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
        findViewById(R.id.ok_big_button).setOnClickListener(okListener);
    }
    
    protected void bindViews() {
        Spinner type = (Spinner)findViewById(R.id.folder_type);
        type.setAdapter(getAdapter());
    }

    FolderTypeAdapter getAdapter() {
        FolderTypeAdapter adapter = new FolderTypeAdapter(this);
        removeFolderType(adapter, FolderType.INBOX);
        removeFolderType(adapter, FolderType.COMPLETED);
        removeFolderType(adapter, FolderType.TRASH);
        return adapter;
    }

    void removeFolderType(FolderTypeAdapter adapter, FolderType type) {
        if (canRemove(type)) {
            adapter.removeFolderType(type);
        }
    }

    boolean canRemove(FolderType type) {
        if (getModel().findFolders(type).isEmpty()) {
            return false;   //no folders with this type
        } 
        if (type.equals(getFolder().getType())) {
            return false;   //leave the type of current folder
        }
        return true;    //other folders can be removed
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ok: {
                if (!validate()) {
                    return true;
                }
                onOkClick();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    protected abstract void onOkClick();

}