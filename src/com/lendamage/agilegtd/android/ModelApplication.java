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

import android.app.Application;
import com.lendamage.agilegtd.model.Model;

/**
 *  Application which holds the data model
 */
public class ModelApplication extends Application {

    private Model model;

    public Model getModel() {
        return this.model;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.model = ModelAccessor.openModel(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (this.model != null) {
            this.model.close();
        }
    }
}
