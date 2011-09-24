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