package android.example.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.example.pets.adapter.AdapterPets;
import android.example.pets.data.PetContract;
import android.example.pets.data.PetsHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity {

    private PetsHelper mDbHelper;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view)-> {
                Intent intent = new Intent(CatalogActivity.this, EditActivity.class);
                startActivity(intent);
        });
         // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new PetsHelper(this);
        recyclerView = findViewById(R.id.rv_pets);
        displayDatabaseInfo();

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        Cursor cursor = getContentResolver().query(PetContract.PetsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        try {
            if (cursor != null) {
                AdapterPets adapterPets = new AdapterPets(cursor);
                recyclerView.setAdapter(adapterPets);
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void insertPet(){
        ContentValues values = new ContentValues();
        values.put(PetContract.PetsEntry.COLUMN_NAME, "Ritchie");
        values.put(PetContract.PetsEntry.COLUMN_BREED, "pomeranian");
        values.put(PetContract.PetsEntry.COLUMN_GENDER, PetContract.PetsEntry.GENDER_MALE);
        values.put(PetContract.PetsEntry.COLUMN_WEIGHT, 7);
        Uri newRowURi = getContentResolver().insert(PetContract.PetsEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

