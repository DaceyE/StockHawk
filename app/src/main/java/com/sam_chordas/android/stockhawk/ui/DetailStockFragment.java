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
public class DetailStockFragment extends Fragment {

    private static final String SYMBOL_KEY = "thesymbolkey";

    private String mSymbol;
    private View mRootView;

    public DetailStockFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mSymbol != null && getView() != null){
            setDetails(mSymbol);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_detail_stock, container, false);

        if (savedInstanceState != null) {
            mSymbol = savedInstanceState.getString(SYMBOL_KEY);
        }

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SYMBOL_KEY, mSymbol);
    }

    /**
     * sets the _id field used to get the corresponding historical data
     * @param symbol
     */
    public void setSymbol(String symbol){
        mSymbol = symbol;
    }

    /**
     * Initalizes the views of the fragments
     * @param symbol
     */
    private void setDetails(String symbol){
        ListView listView = (ListView) mRootView.findViewById(R.id.listView);

        listView.setAdapter(new SimpleCursorAdapter(
                        getActivity(),
                        android.R.layout.simple_list_item_2,
                        getActivity().getContentResolver()
                                .query(StockHawkProvider.HistoricalData.CONTENT_URI,
                                        null, //Select everything
                                        symbol != null ? StockHawkContract.HistoricalDataColumns.SYMBOL + " = ?  " : null, //whereClause
                                        symbol != null ? new String[]{symbol} : null, //whereArgs, get the data for symbol
                                        null), // No sort order
                        new String[]{StockHawkContract.HistoricalDataColumns.SYMBOL,
                                StockHawkContract.HistoricalDataColumns.Date},
                        new int[]{android.R.id.text1, android.R.id.text2},
                        0)
        );
    }

}
