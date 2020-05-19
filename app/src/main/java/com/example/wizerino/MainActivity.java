package com.example.wizerino;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static ListView Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Search(View view) {
        AutoCompleteTextView textView=findViewById(R.id.autoCompleteTextView3);
        String stringValue=textView.getText().toString();
        Data = findViewById(R.id.listView1);
        WeatherReader procces = new WeatherReader();
        Object[] object=new Object[2];
        object[0]=view;
        object[1]=stringValue;
        procces.execute(object);
    }
}
