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

import android.support.v4.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import static com.lendamage.agilegtd.android.IntentUtils.EXTRA_ACTION_BODY;

/**
 *  Activity to add a new action.
 *  The path to the folder where to add the activity is passed in extra.
 */
public class AddActionActivity extends AbstractEditActionActivity {
    
    @Override
    protected void setContentView() {
        setContentView(R.layout.add_action_activity);
    }

    @Override
    protected void bindViews() {
        String bodyText = getIntent().getStringExtra(EXTRA_ACTION_BODY);
        if (bodyText == null) {
            return;
        }
        EditText body = (EditText)findViewById(R.id.action_body);
        body.setText(bodyText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_action_options, menu);
        return true;
    }
    
    @Override
    protected void onOkClick() {
        EditText body = (EditText)findViewById(R.id.action_body);
        String actionBody = body.getText().toString();
        String actionHead = ActionHelper.getHeadFromBody(actionBody);
        this.folder.newAction(actionHead, actionBody);
        finish();
    }

}