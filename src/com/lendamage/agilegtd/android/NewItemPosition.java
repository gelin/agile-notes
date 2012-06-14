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

import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.ModelSettings;

/**
 *  Position where to insert the new item: as the first of the last item in the list.
 *  This class holds some UI related constants.
 */
public enum NewItemPosition {
    FIRST(R.string.new_item_position_first, R.drawable.plus_top, ModelSettings.NewItemPosition.LAST),
    LAST(R.string.new_item_position_last, R.drawable.plus_bottom, ModelSettings.NewItemPosition.FIRST);

    /** Resource for title */
    int titleRes;
    /** Resource for icon */
    int iconRes;
    /** Next position when switching in round-robin */
    ModelSettings.NewItemPosition nextPosition;

    NewItemPosition(int titleRes, int iconRes, ModelSettings.NewItemPosition nextPosition) {
        this.titleRes = titleRes;
        this.iconRes = iconRes;
        this.nextPosition = nextPosition;
    }

    int getTitleRes() {
        return this.titleRes;
    }

    int getIconRes() {
        return this.iconRes;
    }

    ModelSettings.NewItemPosition getNextPosition() {
        return this.nextPosition;
    }

    /**
     *  Gets the option from the model
     *  @param  model the data model
     *  @return null or enumeration instance
     */
    public static NewItemPosition valueOf(Model model) {
        switch (model.getSettings().getNewItemPosition()) {
            case FIRST:
                return FIRST;
            case LAST:
                return LAST;
            default:
                return null;
        }
    }
}
