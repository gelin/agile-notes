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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

/**
 *  Common constants to use with intents.
 */
public class IntentUtils {

    /** Intent extra to pass folder path as a string to open specified folder */
    public static final String EXTRA_FOLDER_PATH =
            IntentUtils.class.getName() + ".EXTRA_FOLDER_PATH";
    
    /** Intent extra to pass action position withing the folder */
    public static final String EXTRA_ACTION_POSITION =
            IntentUtils.class.getName() + ".EXTRA_ACTION_POSITION";

    /** Intent extra to pass action body for the new action */
    public static final String EXTRA_ACTION_BODY =
            IntentUtils.class.getName() + ".EXTRA_ACTION_BODY";

    public static void startFolderActivity(Context context, Class<? extends Activity> activity, Folder folder) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(EXTRA_FOLDER_PATH, folder.getPath().toString());
        context.startActivity(intent);
    }

    public static void startActionActivity(Context context, Class<? extends Activity> activity, Folder folder, Action action) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(EXTRA_FOLDER_PATH, folder.getPath().toString());
        intent.putExtra(EXTRA_ACTION_POSITION, folder.getActions().indexOf(action));
        context.startActivity(intent);
    }
    
    private IntentUtils() {
        //avoid instantiation
    }
    
}
