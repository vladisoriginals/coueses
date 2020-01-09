package android.example.quakereport.utils;

import android.example.quakereport.data.Earthquake;
import android.example.quakereport.R;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public final class QueryUtils {


    private static final String SAMPLE_JSON_REQUEST = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final String FORMAT_RESPONSE = "format";
    private static final String LIMIT = "limit";
    private static final String MIN_MAG = "minmag";
    private static final String TIME = "orderby";

        private QueryUtils() {
        }

        public static URL createUrl( String minMag, String orderBy){
            Uri buildUri =Uri.parse(SAMPLE_JSON_REQUEST)
                    .buildUpon()
                    .appendQueryParameter(FORMAT_RESPONSE, "geojson")
                    .appendQueryParameter(LIMIT, "10")
                    .appendQueryParameter(MIN_MAG, minMag)
                    .appendQueryParameter(TIME, orderBy)
                    .build();
            URL url = null;
            try {
                url = new URL(buildUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }

        public static String getResponseFromURL(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {

                InputStream in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } catch (UnknownHostException e) {
                return null;
            } finally {
                urlConnection.disconnect();
            }

        }
        public static List<Earthquake> extractEarthquakes(String responseFromURL) {

            List<Earthquake> earthquakes = new ArrayList<>();
            double mag;
            String place;
            String locationPrimary;
            String locationOffset;
            String date;
            String url;

            try {
                JSONObject response = new JSONObject(responseFromURL);
                JSONArray jsonArray = response.getJSONArray("features");
                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject currentEarthquake = jsonArray.getJSONObject(i);
                    JSONObject properties = currentEarthquake.getJSONObject("properties");

                    mag = properties.getDouble("mag");
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    String magToDisplay = decimalFormat.format(mag);


                    place = properties.getString("place");
                    String[] location = place.split(",");
                    if (location.length==2 ) {
                        locationPrimary = location[1];
                        locationOffset = location[0];

                    }else{
                        locationPrimary = location[0];
                        locationOffset = "Near the";
                    }



                    date = properties.getString("time");
                    Date dateClass = new Date(Long.parseLong(date));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault() );
                    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    String dateToDisplay = dateFormat.format(dateClass);
                    String timeDisplay = timeFormat.format(dateClass);

                    url = properties.getString("url");


                    earthquakes.add(new Earthquake(magToDisplay,locationPrimary,locationOffset,dateToDisplay,timeDisplay,url));
                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }
            return earthquakes;
        }

    public static int getMagnitudeColor(double magnitude){
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return magnitudeColorResourceId;
    }

    }

