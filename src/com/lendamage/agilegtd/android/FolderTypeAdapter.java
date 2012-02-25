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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lendamage.agilegtd.model.FolderType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Adapter which represents the list of folder types.
 *  This adapter uses the following resources:
 *  <ul>
 *  <li>array/folder_types_values</li>
 *  <li>array/folder_types_names</li>
 *  <ul>
 */
public class FolderTypeAdapter extends BaseAdapter {

    /** Current context */
    Context context;
    /** List of values */
    List<FolderType> values;
    /** List of names */
    List<String> names;
    
    public FolderTypeAdapter(Context context) {
        this.context = context;
        this.values = stringsToFolderTypes(context.getResources().getStringArray(R.array.folder_types_values));
        this.names = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.folder_types_names)));
    }
    
    public int getCount() {;
        return this.values.size();
    }

    public Object getItem(int position) {
        return this.values.get(position);
    }

    public long getItemId(int position) {
        FolderType value = this.values.get(position);
        if (value == null) {
            return -1l;
        }
        return value.ordinal();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getCommonView(position, convertView, parent, android.R.layout.simple_spinner_item);
    }
    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCommonView(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    /**
     *  Removes the specified folder type from the list.
     */
    public void removeFolderType(FolderType type) {
        int index = this.values.indexOf(type);
        if (index < 0) {
            return;
        }
        this.values.remove(index);
        this.names.remove(index);
    }
    
    View getCommonView(int position, View convertView, ViewGroup parent, int viewLayoutId) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(
                    viewLayoutId, parent, false);
        }
        TextView text = (TextView)view.findViewById(android.R.id.text1);
        text.setText(this.names.get(position));
        
        return view;
    }
    
    /**
     *  Helper method to bind the view, returns the position within the spinner
     *  of the specified folder type. 
     */
    int getPosition(FolderType type) {
        int result = this.values.indexOf(type);
        if (result >= 0) {
            return result;
        }
        return this.values.indexOf(null);
    }
    
    static List<FolderType> stringsToFolderTypes(String[] strings) {
        List<FolderType> result = new ArrayList<FolderType>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            try {
                result.add(FolderType.valueOf(string));
            } catch (IllegalArgumentException e) {
                result.add(null);
            }
        }
        return result;
    }

}
