package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sam_chordas.android.stockhawk.R;

public class DetailStockActivity extends AppCompatActivity {

    private String LOG_TAG = "stockhawk " + DetailStockActivity.class.getSimpleName();
    public static final String SYMBOL = "com.sam_chordas.android.stockhawk.ui.SYMBOL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stock);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pass the id of the selected item to the fragment so the related data can be displayed
        String symbol = getIntent().getExtras().getString(SYMBOL);
        DetailStockFragment fragment = (DetailStockFragment) getFragmentManager()
                .findFragmentById(R.id.fragment);
        Log.e(LOG_TAG, symbol);
        fragment.setSymbol(symbol);
    }

}
