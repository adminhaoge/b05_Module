package edu.wschina.b05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private ArrayAdapter<CharSequence> fromResource;
    private ArrayAdapter<CharSequence> charSequenceArrayAdapter;
    private ArrayAdapter<CharSequence> fromResource1;
    private Button bt_start;
    private CharSequence timeSlot;
    private CharSequence date;
    private CharSequence seat;
    private SharedPreferences cart_json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initShared();
        initView();
        initTabType();
        initData();
        initTimeSlot();
        startButton();
    }

    private void initShared() {
        cart_json = getSharedPreferences("CART_JSON", 0);
    }

    private void startButton() {
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = cart_json.edit();
                edit.putString("timeSlot", (String) timeSlot);
                edit.putString("date", (String) date);
                edit.putString("seat", (String) seat);
                edit.commit();
                Intent intent = new Intent(getApplication(),OrderPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initTimeSlot() {
        fromResource1 = ArrayAdapter.createFromResource(this,
                R.array.time, android.R.layout.simple_spinner_dropdown_item);

        spinner3.setAdapter(fromResource1);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeSlot = fromResource1.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initData() {
        List<CharSequence> feturDateList = new ArrayList<>();
        for (int i = 1; i<8;i++){
            feturDateList.add(getFetureDate(i));
        }
        charSequenceArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, feturDateList);
        spinner2.setAdapter(charSequenceArrayAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                date = charSequenceArrayAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initTabType() {
        fromResource = ArrayAdapter.createFromResource(this,
                R.array.city, android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(fromResource);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seat = fromResource.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private String getFetureDate(int past){
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_YEAR,instance.get(Calendar.DAY_OF_YEAR) +past);
        Date time = instance.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String format = simpleDateFormat.format(time);
        return format;
    }

    private void initView() {
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        bt_start = findViewById(R.id.bt_queren);

    }


}