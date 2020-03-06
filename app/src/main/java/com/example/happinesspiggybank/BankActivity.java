package com.example.happinesspiggybank;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BankActivity extends AppCompatActivity {
    HappyDbManager dbManager;
    ArrayList<HappyList> happyDataList;
    Button btnCan;
    TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        btnCan = (Button) findViewById(R.id.cancel);
        txtCount = (TextView) findViewById(R.id.count);

        dbManager = HappyDbManager.getInstance(this);

        this.getHappyData();
        this.getCntData();

        ListView listView = (ListView)findViewById(R.id.happyList);
        final BankListAdapter myAdapter = new BankListAdapter(this, happyDataList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), myAdapter.getItem(position).getDate(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ShowActivity.class);

                intent.putExtra("date", myAdapter.getItem(position).getDate());
                intent.putExtra("time", myAdapter.getItem(position).getTime());
                intent.putExtra("content", myAdapter.getItem(position).getCont());

                startActivity(intent);
            }
        });

        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
    }
    public void getHappyData() {
        happyDataList = new ArrayList<>();

        String[] columns = new String[] {"_id", "date", "time", "content"};

        Cursor cursor = dbManager.query(columns, null, null, null, null, "date, time");

        if(cursor != null) {
            while (cursor.moveToNext()) {
                HappyList currentData = new HappyList();

                currentData.setId(cursor.getInt(0));
                currentData.setDate(cursor.getString(1));
                currentData.setTime(cursor.getString(2));
                currentData.setContent(cursor.getString(3));

                happyDataList.add(currentData);
            }
        }
    }

    public void getCntData() {
        int cnt = dbManager.getCntQuery();
        txtCount.setText("총 " + cnt + "개의 행복");
        String strCnt = String.valueOf(cnt);

        Spannable span = (Spannable) txtCount.getText();
        int endIter = strCnt.length() + 2;

        span.setSpan(new RelativeSizeSpan(1.7f), 2, endIter, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.parseColor("#ebc795")), 2, endIter, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }
}



