package com.sam_chordas.android.stockhawk.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by sam_chordas on 10/5/15.
 */
@Database(version = StockHawkDatabase.VERSION)
public class StockHawkDatabase {
    private StockHawkDatabase() {
    }

    public static final int VERSION = 7;

    @Table(StockHawkContract.QoutesColumns.class)
    public static final String QUOTES = "quotes";

    @Table(StockHawkContract.HistoricalDataColumns.class)
    public static final String HISTORICAL_DATA = "historical_data";
}
