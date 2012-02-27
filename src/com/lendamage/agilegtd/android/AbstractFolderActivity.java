/*
 * Agile GTD. Flexible Android implementation of GTD.
 * Copyright (C) 2012  Denis Nelubin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.lendamage.agilegtd.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.view.MenuItem;
import android.util.Log;
import com.lendamage.agilegtd.android.model.impl.SimplePath;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

import static com.lendamage.agilegtd.android.IntentUtils.EXTRA_FOLDER_PATH;
import static com.lendamage.agilegtd.android.Tag.TAG;

/**
 *  Base class for activites which works with the folder and
 *  takes the folder path in intent.
 */
abstract class AbstractFolderActivity extends AbstractModelActivity {

    /** Current folder */
    protected Folder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.folder = null;
        Intent intent = getIntent();
        String path = null;
        if (intent.hasExtra(EXTRA_FOLDER_PATH)) {
            path = intent.getStringExtra(EXTRA_FOLDER_PATH);
            this.folder = this.model.getFolder(new SimplePath(path));
            if (this.folder == null) {
                Log.w(TAG, "folder " + path + " is not found");
            }
        }
        if (this.folder == null) {
            this.folder = this.model.getRootFolder();
        }
        //Log.d(TAG, "AbstractFolderActivity.onResume() model=" + this.model + " folder=" + this.folder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    protected void startFolderActivity(Class<? extends Activity> activity, Folder folder) {
        IntentUtils.startFolderActivity(this, activity, folder);
    }

    protected void startActionActivity(Class<? extends Activity> activity, Action action) {
        IntentUtils.startActionActivity(this, activity, this.folder, action);
    };

}