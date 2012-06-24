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

/**
 *  Activity to create a shortcut on desktop.
 */
public class ShortcutActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent shortcutIntent = new Intent(Intent.ACTION_SEND);
        shortcutIntent.setClass(this, ActionReceiveActivity.class);

        Intent result = new Intent();
        result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        result.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.new_action_label));
        result.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));
        setResult(RESULT_OK, result);

        finish();
    }

}