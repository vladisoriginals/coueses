package android.example.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PetsHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pets.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PetContract.PetsEntry.TABLE_NAME + " (" +
                    PetContract.PetsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PetContract.PetsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    PetContract.PetsEntry.COLUMN_BREED + " TEXT, " +
                    PetContract.PetsEntry.COLUMN_GENDER + " INTEGER NOT NULL, " +
                    PetContract.PetsEntry.COLUMN_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

    public PetsHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
        public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
