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

import android.support.v4.app.FragmentActivity;
import com.lendamage.agilegtd.model.Model;

/**
 *  Abstract activity which works with {@link Model}.
 */
public class AbstractModelActivity extends FragmentActivity {

    /** Current model */
    protected Model model;

    @Override
    protected void onResume() {
        super.onResume();
        this.model = ModelAccessor.openModel(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.model.close();
    }
}
