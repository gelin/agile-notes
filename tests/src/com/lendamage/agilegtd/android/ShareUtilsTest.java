package com.lendamage.agilegtd.android;

import android.content.Intent;
import android.test.AndroidTestCase;
import com.lendamage.agilegtd.android.model.impl.SQLiteModel;
import com.lendamage.agilegtd.model.Action;

public class ShareUtilsTest extends AndroidTestCase {

    SQLiteModel model;

    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }

    public void testSendAction() {
        Action action = model.getRootFolder().newAction("head", "body");
        Intent intent = ShareUtils.sendAction(action);
        assertEquals(Intent.ACTION_SEND, intent.getAction());
        assertEquals("text/plain", intent.getType());
        assertEquals("head", intent.getStringExtra(Intent.EXTRA_SUBJECT));
        assertEquals("body", intent.getStringExtra(Intent.EXTRA_TEXT));
    }

    public void testSendActionNullHead() {
        Action action = model.getRootFolder().newAction(null, "body");
        Intent intent = ShareUtils.sendAction(action);
        assertEquals(Intent.ACTION_SEND, intent.getAction());
        assertEquals("text/plain", intent.getType());
        assertFalse(intent.hasExtra(Intent.EXTRA_SUBJECT));
        assertEquals("body", intent.getStringExtra(Intent.EXTRA_TEXT));
    }

    public void testSendActionNullBody() {
        Action action = model.getRootFolder().newAction("head", null);
        Intent intent = ShareUtils.sendAction(action);
        assertEquals(Intent.ACTION_SEND, intent.getAction());
        assertEquals("text/plain", intent.getType());
        assertEquals("head", intent.getStringExtra(Intent.EXTRA_SUBJECT));
        assertFalse(intent.hasExtra(Intent.EXTRA_TEXT));
    }

}
