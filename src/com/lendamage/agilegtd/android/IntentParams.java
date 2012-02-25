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
    public static final String EXTRA_FOLDER_PATH =
            IntentParams.class.getName() + ".EXTRA_FOLDER_PATH";
    
    /** Intent extra to pass action position withing the folder */
    public static final String EXTRA_ACTION_POSITION =
            IntentParams.class.getName() + ".EXTRA_ACTION_POSITION";

    /** Intent extra to pass action body for the new action */
    public static final String EXTRA_ACTION_BODY =
            IntentParams.class.getName() + ".EXTRA_ACTION_BODY";
    
    private IntentParams() {
        //avoid instantiation
    }
    
}
