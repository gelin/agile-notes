package com.lendamage.agilegtd.android;

import android.content.Context;

import com.lendamage.agilegtd.android.model.impl.SQLiteModel;
import com.lendamage.agilegtd.model.Model;

/**
 *  Convenience class to create the model
 *  under specified path.
 */
class ModelAccessor {
    
    /** Database name */
    static final String DATABASE_NAME = "agilegtd.db";
    
    /**
     *  Returns the new model.
     */
    public static Model openModel(Context context) {
        return new SQLiteModel(context, DATABASE_NAME);
    }

}
