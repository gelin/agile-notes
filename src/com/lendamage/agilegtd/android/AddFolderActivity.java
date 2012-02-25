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

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lendamage.agilegtd.model.FolderAlreadyExistsException;
import com.lendamage.agilegtd.model.FolderType;

/**
 *  Activity to add a new folder.
 *  The path to the parent folder is passed in extra.
 */
public class AddFolderActivity extends AbstractEditFolderActivity {

    @Override
    protected void setContentView() {
        setContentView(R.layout.add_folder_activity);
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        Spinner typeView = (Spinner)findViewById(R.id.folder_type);
        FolderTypeAdapter adapter = (FolderTypeAdapter)typeView.getAdapter();
        FolderType type = null;
        if (this.folder.getType() != null) {
            type = this.folder.getType().getChildType();
        }
        typeView.setSelection(adapter.getPosition(type));
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