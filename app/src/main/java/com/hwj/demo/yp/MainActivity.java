package com.hwj.demo.yp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hwj.juneng.yp.YearPicker;

public class MainActivity extends AppCompatActivity {

    YearPicker yearPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yearPicker=(YearPicker)findViewById(R.id.yp_year);
        yearPicker.setOnItemSelectedListener(new YearPicker.OnItemSelectedListener() {
            @Override
            public void onSelected(int year) {
                Toast.makeText(MainActivity.this, "选择了"+year, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
