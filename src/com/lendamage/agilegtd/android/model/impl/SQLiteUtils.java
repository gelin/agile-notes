package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FULL_NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.TYPE_COLUMN;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.lendamage.agilegtd.android.Tag;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Path;

/**
 *  Statuc utility methods
 */
public class SQLiteUtils {

    static SQLiteFolder insertFolder(SQLiteDatabase db, Path path, FolderType type) {
        return insertFolder(db, path, type, 0);
    }
    
    static SQLiteFolder insertFolder(SQLiteDatabase db, Path path, FolderType type, long parentId) {
        Log.d(Tag.TAG, "inserting " + path + " " + type);
        SQLiteStatement st = db.compileStatement(
                "INSERT into " + FOLDER_TABLE + 
                " (" + FULL_NAME_COLUMN + ", " + NAME_COLUMN + ", " + TYPE_COLUMN + ", " + FOLDER_ID_COLUMN + ")" +
                " values (?, ?, ?, ?)");
        st.bindString(1, String.valueOf(path));
        st.bindString(2, path.getLastSegment());
        if (type != null) {
            st.bindString(3, String.valueOf(type));
        }
        if (parentId != 0) {
            st.bindLong(4, parentId);
        }
        long id = st.executeInsert();
        SQLiteFolder result = new SQLiteFolder(db, id);
        result.path = path;
        result.name = path.getLastSegment();
        result.type = type;
        //TODO: link to parent?
        return result;
    }
    
    /**
     *  Returns the folder from the current cursor position.
     */
    static SQLiteFolder getFolder(SQLiteDatabase db, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(ID_COLUMN));
        SQLiteFolder result = new SQLiteFolder(db, id);
        Path path = new SimplePath(cursor.getString(cursor.getColumnIndex(FULL_NAME_COLUMN)));
        result.path = path;
        result.name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
        String type = cursor.getString(cursor.getColumnIndex(TYPE_COLUMN));
        if (type != null) {
            result.type = FolderType.valueOf(type);
        }
        //TODO: link to parent?
        return result;
    }

}
