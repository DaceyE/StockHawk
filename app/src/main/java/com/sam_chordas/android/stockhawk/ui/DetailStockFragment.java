package com.sam_chordas.android.stockhawk.ui;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.StockHawkContract;
import com.sam_chordas.android.stockhawk.data.StockHawkProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 */
public class DetailStockFragment extends Fragment {

    private static final String LOG_TAG = "stockhawk " + DetailStockActivity.class.getSimpleName();

    /**
     * Key for saved instance, Used to save and fetch the mSymbol field
     */
    private static final String SYMBOL_KEY = "thesymbolkey";

    private String mSymbol;
    private View mRootView;
    private LineChart mLineChart;
    private Date mDate; //The oldest date of the data

    public DetailStockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_detail_stock, container, false);

        mLineChart = ((LineChart) mRootView.findViewById(R.id.chart1));

        if (savedInstanceState != null) {
            mSymbol = savedInstanceState.getString(SYMBOL_KEY);
        }

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mSymbol != null && getView() != null) {
            setUp();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SYMBOL_KEY, mSymbol);
    }

    /**
     * sets the _id field used to get the corresponding historical data
     *
     * @param symbol
     */
    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    /**
     * Sets up the chart.  Uses many of the helper methods below
     */
    private void setUp() {
        Cursor cursor = getActivity().getContentResolver()
                .query(StockHawkProvider.HistoricalData.CONTENT_URI,
                        new String[]{StockHawkContract.HistoricalDataColumns.CLOSE,
                                StockHawkContract.HistoricalDataColumns.Date},
                        StockHawkContract.HistoricalDataColumns.SYMBOL + " = ?  ", //whereClause
                        new String[]{mSymbol}, //whereArgs, get the data for symbol
                        null);

        //Sets the mDate field, which is used be various methods
        setDate(cursor);

        //Calls helper method
        mLineChart.setData(createLineData(cursor));
        mLineChart.setDrawBorders(true);
        mLineChart.animateXY(1000, 800);
        mLineChart.setPinchZoom(true);
        mLineChart.setDescription(null);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawGridLines(false);

        formatAxis(xAxis);
        formatAxis(mLineChart.getAxisLeft());
        formatAxis(mLineChart.getAxisRight());

        addMonthLimitLines(mLineChart);
    }

    /**
     * Sets up standard formatting used on every axis.  Additional specific formatting can be done
     * outside of this method.
     *
     * @param axis The axis to format
     */
    private void formatAxis(AxisBase axis) {
        Typeface robotoLight = Typeface
                .createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        axis.setTextColor(Color.LTGRAY);
        axis.setTextSize(16);
        axis.setTypeface(robotoLight);
    }

    /**
     * Creates LimitLines on the XAxis for the months.
     *
     * @param lineChart The chart to add the limit lines
     */
    private void addMonthLimitLines(LineChart lineChart) {
        XAxis xAxis = lineChart.getXAxis();

        List<String> list = lineChart.getLineData().getXVals();
        int size = list.size();

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(mDate);

        xAxis.addLimitLine(createLimitLine(0, monthFormat.format(cal.getTime())));
        cal.add(GregorianCalendar.MONTH, 1);

        for (int i = 1; i < size; i++) {
            int day = Integer.parseInt(list.get(i));
            if (day == 1) {

                xAxis.addLimitLine(createLimitLine(i, monthFormat.format(cal.getTime())));
                cal.add(GregorianCalendar.MONTH, 1);
            }
        }
    }

    /**
     * Creates and formats a limit line
     *
     * @param f     The width of the limit line
     * @param label label to apply. i.e name of month
     * @return THe limitLine
     */
    private LimitLine createLimitLine(float f, String label) {
        LimitLine limitLine = new LimitLine(f, label);
        limitLine.setLineWidth(0.2f);
        limitLine.setLineColor(Color.WHITE);
        limitLine.setTextSize(12f);
        limitLine.setTextColor(getResources().getColor(R.color.material_blue_600));

        return limitLine;
    }

    /**
     * Sets the mDate field to the oldest date in the cursor.
     * Modifies the cursor position.
     *
     * @param cursor
     */
    private void setDate(Cursor cursor) {
        cursor.moveToLast(); //Data is order with the oldest at the end
        try {
            mDate = Utils.YQL_DATE_FORMAT.parse(
                    cursor.getString(
                            cursor.getColumnIndex(StockHawkContract.HistoricalDataColumns.Date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the LineData.  First it creates a DataSet and uses that to create IDataSet.  Finally,
     * that is used to make the LineData
     * Modifies the cursor position
     *
     * @param cursor The cursor containing the data.  Modifies the position.
     * @return
     */
    private LineData createLineData(Cursor cursor) {
        ArrayList<Entry> closeEntries = new ArrayList<>(150);
        ArrayList<String> x = new ArrayList<>(150);

        int closeIndex = cursor.getColumnIndex(StockHawkContract.HistoricalDataColumns.CLOSE);
        int dateIndex = cursor.getColumnIndex(StockHawkContract.HistoricalDataColumns.Date);

        //The data is ordered with the oldest at the highest index
        cursor.moveToLast();

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd"); //formats to two date digits
        try {
            Date oldest = (Date) mDate.clone();
            GregorianCalendar oldestCal = new GregorianCalendar();
            oldestCal.setTime(oldest);

            cursor.moveToFirst();
            Date newest = Utils.YQL_DATE_FORMAT.parse(cursor.getString(dateIndex));
            GregorianCalendar newestCal = new GregorianCalendar();
            newestCal.setTime(newest);

            //Creates a list of all the days, used for x
            while (oldestCal.compareTo(newestCal) <= 0) {
                x.add(dayFormat.format(oldestCal.getTime()));
                oldestCal.add(GregorianCalendar.DAY_OF_YEAR, 1);
            }

            //Loop gets data for y
            int i = 0;
            cursor.moveToPosition(cursor.getCount());
            while (cursor.moveToPrevious()) {
                String date = cursor.getString(dateIndex).substring(8);

                if (date.equals(x.get(i))) {
                    closeEntries.add(new Entry(cursor.getFloat(closeIndex), i));
                } else {
                    while (!date.equals(x.get(i))) {
                        i++;
                    }
                    closeEntries.add(new Entry(cursor.getFloat(closeIndex), i));
                }
                i++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LineDataSet closeDataSet =
                new LineDataSet(closeEntries, getString(R.string.close_data_set_label));
        //Formats the look
        closeDataSet.setDrawCircles(false);
        closeDataSet.setDrawFilled(true);
        closeDataSet.setValueTextColor(Color.LTGRAY);

        int color = getResources().getColor(R.color.material_green_700);
        closeDataSet.setColor(color); //Line color!
        closeDataSet.setFillColor(color);

        //The date.  In the future this could contain multiple sets such as high, open, volume etc.
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(closeDataSet);

        return new LineData(x, sets);
    }

}
