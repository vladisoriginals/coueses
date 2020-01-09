package android.example.quakereport.loader;

import android.content.Context;
import android.example.quakereport.utils.QueryUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.URL;

public class EarthquakeLoader extends AsyncTaskLoader<String> {
    private URL url;
    public EarthquakeLoader(@NonNull Context context, URL url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String responseJSON = null;
        if (url == null){
            return null;
        }
        try {
            responseJSON = QueryUtils.getResponseFromURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseJSON;
    }
}
