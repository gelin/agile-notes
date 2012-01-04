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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.Path;

/**
 *  Activity to view the action.
 *  The path to the folder where the action is located
 *  and the position of the action within the folder
 *  are passed in extra.
 */
public class ViewActionActivity extends AbstractActionActivity {
    
    static final String FOLDERS_SEPARATOR = "   ";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_action_activity);
        
        findViewById(R.id.copy_button).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                copyAction();
            }
        });
        
        findViewById(R.id.edit_button).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                editAction();
            }
        });
        
        ListView actionView = (ListView)findViewById(R.id.action_view);
        actionView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int)id) {
                case ActionViewAdapter.FOLDERS_VIEW_TYPE:
                    copyAction();
                    break;
                case ActionViewAdapter.BODY_VIEW_TYPE:
                    editAction();
                    break;
                }
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (this.action == null) {
            return;
        }
        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(this.action.getHead());
        
        ListView actionView = (ListView)findViewById(R.id.action_view);
        actionView.setAdapter(new ActionViewAdapter(this, this.action));
    }
    
    void copyAction() {
        startActionActivity(CopyActionActivity.class, this.action);
    }
    
    void editAction() {
        startActionActivity(EditActionActivity.class, this.action);
    }
    
    static class ActionViewAdapter extends BaseAdapter {
        //TODO: remove this adapter, it's crazy

        public static final int FOLDERS_VIEW_TYPE = 0;
        public static final int BODY_VIEW_TYPE = 1;
        static final int[] VIEW_TYPES = {FOLDERS_VIEW_TYPE, BODY_VIEW_TYPE};
        
        Context context;
        Action action;
        
        ActionViewAdapter(Context context, Action action) {
            this.context = context;
            this.action = action;
        }
        
        public int getCount() {
            return VIEW_TYPES.length;
        }
        
        @Override
        public int getViewTypeCount() {
            return VIEW_TYPES.length;
        }
        
        @Override
        public int getItemViewType(int position) {
            return VIEW_TYPES[position];
        }
        
        @Override
        public boolean hasStableIds() {
            return true;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return VIEW_TYPES[position];
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            switch (VIEW_TYPES[position]) {
            case FOLDERS_VIEW_TYPE:
                if (view == null) {
                    view = LayoutInflater.from(this.context).inflate(
                                R.layout.action_folders_item, parent, false); 
                }
                TextView foldersView = (TextView)view.findViewById(R.id.action_folders_text);
                StringBuilder folders = new StringBuilder();
                for (Folder folder : this.action.getFolders()) {
                    Path path = folder.getPath();
                    if (folders.length() > 0) {
                        folders.append(FOLDERS_SEPARATOR);
                    }
                    if (path.isRoot()) {
                        folders.append(this.context.getString(R.string.root_folder));
                    } else {
                        folders.append(path.toString());
                    }
                }
                foldersView.setText(folders);
                break;
            case BODY_VIEW_TYPE:
                if (view == null) {
                    view = LayoutInflater.from(this.context).inflate(
                            R.layout.action_body_item, parent, false);; 
                }
                TextView bodyView = (TextView)view.findViewById(R.id.action_body);
                bodyView.setText(this.action.getBody());
                break;
            }
            return view;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_action:
                ShareUtils.sendAction(this, R.string.share_action_title, this.action);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}