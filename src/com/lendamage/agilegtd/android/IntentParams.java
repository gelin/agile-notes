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

/**
 *  Common constants to use with intents.
 */
public class IntentParams {

    /** Intent extra to pass folder path as a string to open specified folder */
    public static final String FOLDER_PATH_EXTRA = 
            IntentParams.class.getName() + ".FOLDER_PATH_EXTRA";
    
    /** Intent extra to pass action position withing the folder */
    public static final String ACTION_POSITION_EXTRA = 
            IntentParams.class.getName() + ".ACTION_POSITION_EXTRA";
    
    private IntentParams() {
        //avoid instantiation
    }
    
}
