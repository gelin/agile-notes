package com.lendamage.agilegtd.android;

import android.content.Intent;
import android.test.AndroidTestCase;
import com.lendamage.agilegtd.android.model.impl.SQLiteModel;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;

import static com.lendamage.agilegtd.model.ModelSettings.NewItemPosition.LAST;

public class ShareUtilsTest extends AndroidTestCase {

    SQLiteModel model;

    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
        model.getSettings().setNewItemPosition(LAST);
    }

    public void testSendActionIntent() {
        Action action = model.getRootFolder().newAction("head", "body");
        Intent intent = ShareUtils.sendActionIntent(action);
        assertEquals(Intent.ACTION_SEND, intent.getAction());
        assertEquals("text/plain", intent.getType());
        assertEquals("head", intent.getStringExtra(Intent.EXTRA_SUBJECT));
        assertEquals("body", intent.getStringExtra(Intent.EXTRA_TEXT));
    }

    public void testSendActionIntentNullHead() {
        Action action = model.getRootFolder().newAction(null, "body");
        Intent intent = ShareUtils.sendActionIntent(action);
        assertEquals(Intent.ACTION_SEND, intent.getAction());
        assertEquals("text/plain", intent.getType());
        assertFalse(intent.hasExtra(Intent.EXTRA_SUBJECT));
        assertEquals("body", intent.getStringExtra(Intent.EXTRA_TEXT));
    }

    public void testSendActionIntentNullBody() {
        Action action = model.getRootFolder().newAction("head", null);
        Intent intent = ShareUtils.sendActionIntent(action);
        assertEquals(Intent.ACTION_SEND, intent.getAction());
        assertEquals("text/plain", intent.getType());
        assertEquals("head", intent.getStringExtra(Intent.EXTRA_SUBJECT));
        assertFalse(intent.hasExtra(Intent.EXTRA_TEXT));
    }

    public void testReceiveActionIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "head");
        intent.putExtra(Intent.EXTRA_TEXT, "body");
        //TODO: now it opens the activity, how to test it?
        //assertTrue(ShareUtils.receiveActionIntent(getContext(), intent, model.getRootFolder()));
        //Action action = model.getRootFolder().getActions().get(0);
        //assertEquals("head", action.getHead());
        //assertEquals("head\n\nbody", action.getBody());
    }

    public void testReceiveActionIntentInvalidAction() {
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "head");
        intent.putExtra(Intent.EXTRA_TEXT, "body");
        assertFalse(ShareUtils.receiveActionIntent(getContext(), intent, model.getRootFolder()));
        assertEquals(0, model.getRootFolder().getActions().size());
    }

    public void testReceiveActionIntentInvalidType() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, "head");
        intent.putExtra(Intent.EXTRA_TEXT, "body");
        assertFalse(ShareUtils.receiveActionIntent(getContext(), intent, model.getRootFolder()));
        assertEquals(0, model.getRootFolder().getActions().size());
    }

    public void testReceiveActionIntentNullHead() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "head\nbody");
        //TODO: now it opens the activity, how to test it?
        //assertTrue(ShareUtils.receiveActionIntent(getContext(), intent, model.getRootFolder()));
        //Action action = model.getRootFolder().getActions().get(0);
        //assertEquals("head", action.getHead());
        //assertEquals("head\nbody", action.getBody());
    }

    public void testReceiveActionIntentNullBody() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "head");
        //TODO: now it opens the activity, how to test it?
        //assertTrue(ShareUtils.receiveActionIntent(getContext(), intent, model.getRootFolder()));
        //Action action = model.getRootFolder().getActions().get(0);
        //assertEquals("head", action.getHead());
        //assertEquals("head", action.getBody());
    }

    public void testReceiveActionIntentNullAll() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        assertFalse(ShareUtils.receiveActionIntent(getContext(), intent, model.getRootFolder()));
        assertEquals(0, model.getRootFolder().getActions().size());
    }

    public void testSendFolderIntent() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        model.getRootFolder().newAction("action1", "action1\nbody1");
        model.getRootFolder().newAction("action2", "action2\nbody2");
        folder.newAction("action3", "action3\nbody3");
        Intent intent = ShareUtils.sendFolderIntent(model.getRootFolder());
        assertEquals(Intent.ACTION_SEND, intent.getAction());
        assertEquals("text/plain", intent.getType());
        assertEquals("", intent.getStringExtra(Intent.EXTRA_SUBJECT));
        assertEquals(
                "* folder\n" +
                "** action3\n" +
                "body3\n" +
                "* action1\n" +
                "body1\n" +
                "* action2\n" +
                "body2\n",
                intent.getStringExtra(Intent.EXTRA_TEXT));
    }

}
