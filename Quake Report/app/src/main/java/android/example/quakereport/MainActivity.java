package android.example.quakereport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.quakereport.adapter.AdapterEarthquake;
import android.example.quakereport.data.Earthquake;
import android.example.quakereport.loader.EarthquakeLoader;
import android.example.quakereport.utils.QueryUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

   private List<Earthquake> earthquakes;
   private ProgressBar progressBar;
   private TextView errorMessage;
   private RecyclerView earthQuakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        errorMessage = findViewById(R.id.error_message);
        earthQuakeList = findViewById(R.id.rv_earthquake);

        getSupportLoaderManager().initLoader(1,null,this);
    }

    public void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMag = preferences.getString(
                getString(R.string.settings_min_magnitude_key),
                        getString(R.string.settings_min_magnitude_default));
        String orderBy = preferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        URL url = QueryUtils.createUrl(minMag, orderBy);
        return new EarthquakeLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (data == null || data.equals("")){
            progressBar.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
            earthQuakeList.setVisibility(View.INVISIBLE);
        }else {
            earthquakes = QueryUtils.extractEarthquakes(data);
            AdapterEarthquake adapterEarthquake = new AdapterEarthquake(earthquakes, getApplicationContext(), MainActivity.this::openWebPage);
            earthQuakeList.setAdapter(adapterEarthquake);

            progressBar.setVisibility(View.INVISIBLE);
            earthQuakeList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        earthquakes = new ArrayList<>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}


