package com.jrsf.wheatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView temp_text;
    TextView date_text;
    TextView weatherDesc_text;
    TextView city_text;
    ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp_text = findViewById(R.id.temp_text);
        date_text = findViewById(R.id.date_text);
        weatherDesc_text = findViewById(R.id.weatherDesc_text);
        city_text = findViewById(R.id.city_text);
        weatherImage = findViewById(R.id.weatherImage);

        //weatherImage.setImageResource(R.drawable.icon_2);
        date_text.setText(getCurrentDate());

        //temp_text.setText("34");

        String url = "http://api.openweathermap.org/data/2.5/weather?q=Lisbon,pt&APPID=759934bb612d5538701c4774b7cc212e&units=metric";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseObject) {

                        //temp_text.setText("Response: " + response.toString());
                        Log.v("WEATHER", "Response: " + responseObject.toString());

                        try{
                            JSONObject mainJSONObject = responseObject.getJSONObject("main");
                            JSONArray weatherArray = responseObject.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                            String temp = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp")));
                            String weatherDescription = firstWeatherObject.getString("description");
                            String iconName = firstWeatherObject.getString("icon");
                            String city = responseObject.getString("name");

                            temp_text.setText(temp);
                            weatherDesc_text.setText(weatherDescription);
                            city_text.setText(city);

                            int iconResourceId = getResources().getIdentifier(iconName, "drawable", getPackageName());
                            weatherImage.setImageResource(iconResourceId);

                        }catch (JSONException e){
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM, dd");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }
}
