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

import android.widget.EditText;
import android.widget.TextView;

/**
 *  Activity to edit the body of the action.
 *  The path to the folder where the action is located
 *  and the position of the action within the folder
 *  are passed in extra.
 */
public class EditActionActivity extends AbstractEditActionActivity {
    
    boolean binded = false;
    
    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }
    
    @Override
    protected void setContentView() {
        setContentView(R.layout.edit_action_activity);
    }
    
    void bindViews() {
        if (this.binded) {
            return;
        }
        TextView title = (TextView)findViewById(R.id.title);
        EditText body = (EditText)findViewById(R.id.action_body);
        title.setText(this.action.getHead());
        body.setText(this.action.getBody());
        this.binded = true;
    }
    
    @Override
    protected void onOkClick() {
        EditText body = (EditText)findViewById(R.id.action_body);
        String actionBody = body.getText().toString();
        String actionHead = ActionHelper.getHeadFromBody(actionBody);
        this.action.edit().setHead(actionHead).setBody(actionBody).commit();
        finish();
    }

}