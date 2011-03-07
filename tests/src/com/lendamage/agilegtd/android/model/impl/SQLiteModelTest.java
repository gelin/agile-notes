package com.lendamage.agilegtd.android.model.impl;

import com.lendamage.agilegtd.model.Action;

import android.test.AndroidTestCase;

public class SQLiteModelTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testNewAction() {
        SQLiteAction a1 = (SQLiteAction)model.newAction("head1");
        assertFalse(0 == a1.id);
        assertEquals("head1", a1.getHead());
        assertNull(a1.getBody());
        SQLiteAction a2 = (SQLiteAction)model.newAction("head2");
        assertFalse(a1.id == a2.id);
        assertEquals("head2", a2.getHead());
        assertNull(a2.getBody());
    }

}
