package com.lendamage.agilegtd.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lendamage.agilegtd.model.FolderType;

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
    /** Array of values */
    FolderType[] values;
    /** Array of names */
    String[] names;
    
    public FolderTypeAdapter(Context context) {
        this.context = context;
        this.values = stringsToFolderTypes(context.getResources().getStringArray(R.array.folder_types_values));
        this.names = context.getResources().getStringArray(R.array.folder_types_names);
    }
    
    public int getCount() {;
        return this.values.length;
    }

    public Object getItem(int position) {
        return this.values[position];
    }

    public long getItemId(int position) {
        FolderType value = this.values[position];
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
    
    View getCommonView(int position, View convertView, ViewGroup parent, int viewLayoutId) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(
                    viewLayoutId, parent, false);
        }
        TextView text = (TextView)view.findViewById(android.R.id.text1);
        text.setText(this.names[position]);
        
        return view;
    }
    
    /**
     *  Helper method to bind the view, returns the position within the spinner
     *  of the specified folder type. 
     */
    int getPosition(FolderType type) {
        int nullPosition = -1;
        int result = -1;
        for (int i = 0; i < this.values.length; i++) {
            FolderType value = this.values[i];
            if (value == null && nullPosition < 0) {
                nullPosition = i;
            }
            if (value != null && result < 0 && value.equals(type)) {
                result = i;
                return result;
            }
        }
        if (result < 0) {
            return nullPosition;
        }
        return result;
    }
    
    static FolderType[] stringsToFolderTypes(String[] strings) {
        FolderType[] result = new FolderType[strings.length];
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            try {
                result[i] = FolderType.valueOf(string);
            } catch (IllegalArgumentException e) {
                result[i] = null;
            }
        }
        return result;
    }

}
