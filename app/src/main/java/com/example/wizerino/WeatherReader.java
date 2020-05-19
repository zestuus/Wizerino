package com.example.wizerino;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeatherReader extends AsyncTask<Object, Void, Void> {
    View view;
    String data="";
    String city="";
    String dataParsed="";
    ArrayList<Day> days = new ArrayList<>();
    @Override
    protected Void doInBackground(Object... object) {
        //https://api.weatherbit.io/v2.0/forecast/daily?city=Lviv&key=10853fcdd325435592f1f50ba2839aaa
        //https://api.weatherbit.io/v2.0/current?city=Lviv&key=10853fcdd325435592f1f50ba2839aaa
        this.view=(View)object[0];
        this.city=(String)object[1];
        String url = "https://api.weatherbit.io/v2.0/forecast/daily?city=" + this.city + "&key=10853fcdd325435592f1f50ba2839aaa";
        try {
            URL weatherApiUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) weatherApiUrl.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {

                line = bufferedReader.readLine();
                data += line;
            }

            JSONObject all = new JSONObject(data);
            JSONArray JA = all.getJSONArray("data");

            for (int i = 0; i < JA.length(); ++i) {
                JSONObject JO = JA.getJSONObject(i);
                String valid_date = JO.getString("valid_date");
                String min_temp = JO.getString("min_temp");
                String max_temp = JO.getString("max_temp");
                String icon = (JO.getJSONObject("weather").getString("icon"));
                String description = (JO.getJSONObject("weather").getString("description"));
                String humidity = JO.getString("rh");
                Day singleParsed = new Day(valid_date, min_temp, max_temp, icon, description, humidity);
                days.add(singleParsed);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class DayAdapter extends ArrayAdapter<Day> {
        private int dayOfWeekIndex;
        private String dayOfWeek = "";
        public DayAdapter(Context context) {
            super(context, R.layout.my_list_item, days);
        }

        public int getResId(String resName, Class<?> c) {

            try {
                Field idField = c.getDeclaredField(resName);
                return idField.getInt(idField);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Day day = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.my_list_item, null);
            }
            Calendar c = Calendar.getInstance();
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(day.valid_date);
                c.setTime(date);
                dayOfWeekIndex = c.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeekIndex) {
                    case 1:
                        dayOfWeek = "Monday";
                        break;
                    case 2:
                        dayOfWeek = "Tuesday";
                        break;
                    case 3:
                        dayOfWeek = "Wednesday";
                        break;
                    case 4:
                        dayOfWeek = "Thursday";
                        break;
                    case 5:
                        dayOfWeek = "Friday";
                        break;
                    case 6:
                        dayOfWeek = "Saturday";
                        break;
                    case 7:
                        dayOfWeek = "Sunday";
                        break;
                    default:
                        dayOfWeek = "";
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((ImageView) convertView.findViewById(R.id.imageView))
                    .setImageResource(getResId(day.icon, R.drawable.class));
            ((TextView) convertView.findViewById(R.id.textView))
                    .setText(String.format("%s: %s", dayOfWeek, day.description));
            ((TextView) convertView.findViewById(R.id.textView3))
                    .setText(String.format("Low: %s℃   High: %s℃   Humidity: %s%s", day.min_temp, day.max_temp, day.humidity, "%"));
            return convertView;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ArrayAdapter<Day> adapter = new DayAdapter(view.getContext());
        MainActivity.Data.setAdapter(adapter);
//        MainActivity.Data.setText(dataParsed);
    }
}