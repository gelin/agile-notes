package com.lendamage.agilegtd.android;

import android.widget.EditText;

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
    protected void onOkClick() {
        EditText body = (EditText)findViewById(R.id.action_body);
        String actionBody = body.getText().toString();
        String actionHead = ActionHelper.getHeadFromBody(actionBody);
        this.folder.newAction(actionHead, actionBody);
        finish();
    }

}