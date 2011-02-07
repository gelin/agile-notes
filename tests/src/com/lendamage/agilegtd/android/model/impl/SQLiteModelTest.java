package com.lendamage.agilegtd.android.model.impl;

import android.test.AndroidTestCase;

public class SQLiteModelTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }

}
