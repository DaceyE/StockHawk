package com.sam_chordas.android.stockhawk.ui;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.StockHawkContract;
import com.sam_chordas.android.stockhawk.data.StockHawkProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailStockActivityFragment extends Fragment {

    public DetailStockActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_stock, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        listView.setAdapter(new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_2,
                getActivity().getContentResolver()
                        .query(StockHawkProvider.HistoricalData.CONTENT_URI, null, null, null, null),
                new String[]{StockHawkContract.HistoricalDataColumns.SYMBOL,
                        StockHawkContract.HistoricalDataColumns.Date},
                new int[]{android.R.id.text1, android.R.id.text2},
                0)
        );

        return rootView;
    }
}
