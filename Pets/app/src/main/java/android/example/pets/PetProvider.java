package android.example.pets;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.pets.data.PetContract;
import android.example.pets.data.PetsHelper;
import android.net.Uri;
import android.util.Log;

public class PetProvider extends ContentProvider {

    private PetsHelper mHelper;
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;
    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(PetContract.CONTENT_AUHTORITY, PetContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUHTORITY, PetContract.PATH_PETS + "/#", PET_ID);
    }
    public PetProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                return database.delete(PetContract.PetsEntry.TABLE_NAME, selection, selectionArgs);
            case PET_ID:
                // Delete a single row given by the ID in the URI
                selection = PetContract.PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(PetContract.PetsEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return PetContract.PetsEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.PetsEntry.CONTENT_ITEM_TYPE;
             default:
                 throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        if (match == PETS) {
            return insertPet(uri, values);
        }
        throw new IllegalArgumentException("Insertion is not supported for " + uri);
    }
    private Uri insertPet(Uri uri, ContentValues values) {

        String name = values.getAsString(PetContract.PetsEntry.COLUMN_NAME);
        if (name == null || name.equals("") ){
            throw new IllegalArgumentException("Pet requires a name");
        }

        Integer gender = values.getAsInteger(PetContract.PetsEntry.COLUMN_GENDER);
        if (gender == null || !PetContract.PetsEntry.isValidGender(gender)){
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        Integer weight = values.getAsInteger(PetContract.PetsEntry.COLUMN_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        long newRowId = db.insert(PetContract.PetsEntry.TABLE_NAME, null, values);
        if (newRowId == -1){
            Log.d(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public boolean onCreate() {
        mHelper = new PetsHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor;

        int math = sUriMatcher.match(uri);

        switch (math){
            case PETS:
                cursor = db.query(PetContract.PetsEntry.TABLE_NAME,null,null,null,null,null,null);
                break;

            case PET_ID:
                selection = PetContract.PetsEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(PetContract.PetsEntry.TABLE_NAME, null,selection,selectionArgs,null,null,null);
                break;

             default:
                 throw new IllegalArgumentException("Cannot query unknown URI " + uri );
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int match  = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return updatePet(uri, values, selection, selectionArgs);
            case PET_ID:
                selection = PetContract.PetsEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet( Uri uri, ContentValues values, String selection, String[] selectionArgs){
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PetContract.PetsEntry.COLUMN_NAME)) {
            String name = values.getAsString(PetContract.PetsEntry.COLUMN_NAME);
            if (name == null || name.equals("")) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(PetContract.PetsEntry.COLUMN_GENDER)) {
            Integer gender = values.getAsInteger(PetContract.PetsEntry.COLUMN_GENDER);
            if (gender == null || !PetContract.PetsEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(PetContract.PetsEntry.COLUMN_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(PetContract.PetsEntry.COLUMN_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        return  db.update(PetContract.PetsEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
