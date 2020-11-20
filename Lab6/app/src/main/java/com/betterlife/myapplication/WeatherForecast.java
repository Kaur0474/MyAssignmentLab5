package com.betterlife.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {


    ProgressBar progressBar;
    ImageView currentweatherphotoimg;
    TextView currenttemperaturetv, mintemperaturetv, maxtemperaturetv, uvtemperaturetv;

    String TAG="weatherapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        currentweatherphotoimg = findViewById(R.id.currentweatherphoto);
        currenttemperaturetv = findViewById(R.id.currenttemperature);
        mintemperaturetv = findViewById(R.id.mintemperature);
        maxtemperaturetv = findViewById(R.id.maxtemperature);
        uvtemperaturetv = findViewById(R.id.uvtemperature);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);

        new ForecastQuery().execute();

    }


    class ForecastQuery extends AsyncTask<String, Integer, String> {
        String uvIndex, minTemp, maxTemp, currentTemp;
        Bitmap weatherPhoto;

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream response = connection.getInputStream();


                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser myParser = factory.newPullParser();
                myParser.setInput(response, "UTF-8");


                int event = myParser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = myParser.getName();

                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if (name.equals("temperature")) {
                                currentTemp = myParser.getAttributeValue(null, "value");
                                onProgressUpdate(25);
                                minTemp = myParser.getAttributeValue(null, "min");
                                onProgressUpdate(50);
                                maxTemp = myParser.getAttributeValue(null, "max");
                                onProgressUpdate(75);



                            } else if (name.equals("weather")) {
                                String iconName = myParser.getAttributeValue(null, "icon");

                                if (!fileExistance(iconName + ".png")) {
                                    Bitmap image = null;
                                    URL imageurl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                    connection = (HttpURLConnection) imageurl.openConnection();
                                    connection.connect();
                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        image = BitmapFactory.decodeStream(connection.getInputStream());
                                        weatherPhoto = image;
                                    }

                                    onProgressUpdate(100);


                                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();

                                    Log.d(TAG,"Image Downloaded and loaded");

                                } else {
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(iconName + ".png");
                                        Bitmap bm = BitmapFactory.decodeStream(fis);
                                        weatherPhoto=bm;
                                        Log.d(TAG,"Image Loaded locally");

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                            break;

                        case XmlPullParser.END_TAG:

                            break;
                    }
                    event = myParser.next();
                }


                url = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                connection = (HttpURLConnection) url.openConnection();
                response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();


                JSONObject jsonObject = new JSONObject(result);
                uvIndex = jsonObject.getString("value");


            } catch (Exception ae) {
                ae.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            currenttemperaturetv.setText("Current Temperature: " + currentTemp);
            mintemperaturetv.setText("Min Temperature: " + minTemp);
            maxtemperaturetv.setText("Max Temperature: " + maxTemp);
            uvtemperaturetv.setText("UV Index " + uvIndex);
            currentweatherphotoimg.setImageBitmap(weatherPhoto);

            progressBar.setVisibility(View.INVISIBLE);

            super.onPostExecute(s);
        }
    }


    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

}
