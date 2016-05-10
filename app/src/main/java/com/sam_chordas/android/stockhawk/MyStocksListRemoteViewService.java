package com.sam_chordas.android.stockhawk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.data.StockHawkContract;
import com.sam_chordas.android.stockhawk.data.StockHawkProvider;
import com.sam_chordas.android.stockhawk.ui.DetailStockActivity;

public class MyStocksListRemoteViewService extends RemoteViewsService {

    private static final String LOG_TAG = "stockhawk " + MyStocksListRemoteViewService.class.getSimpleName();

    public MyStocksListRemoteViewService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private Cursor mCursor;

        ListRemoteViewsFactory(Context context, Intent intent) {
            this.mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) {
                mCursor.close();
            }

            final long identityToken = Binder.clearCallingIdentity();
            mCursor = getContentResolver().query(
                    StockHawkProvider.Quotes.CONTENT_URI,
                    null,
                    StockHawkContract.QoutesColumns.ISCURRENT + " = ? ",
                    new String[]{"1"},
                    null
            );
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            return mCursor != null ? mCursor.getCount() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.i(LOG_TAG, "getViewAt called, pos: " + position);
            mCursor.moveToPosition(position);
            String symbol = mCursor.getString(
                    mCursor.getColumnIndex(StockHawkContract.QoutesColumns.SYMBOL));
            String bidPrice = mCursor.getString(
                    mCursor.getColumnIndex(StockHawkContract.QoutesColumns.BIDPRICE));
            String change = mCursor.getString(
                    mCursor.getColumnIndex(StockHawkContract.QoutesColumns.CHANGE));

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);

            remoteViews.setTextViewText(R.id.stock_symbol, symbol);
            remoteViews.setTextViewText(R.id.bid_price, bidPrice);
            remoteViews.setTextViewText(R.id.change, change);

            if (mCursor.getInt(mCursor.getColumnIndex("is_up")) == 1) {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }


            final Intent fillIntent = new Intent();
            fillIntent.putExtra(DetailStockActivity.SYMBOL, symbol);
            remoteViews.setOnClickFillInIntent(R.id.list_item, fillIntent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(mCursor.getColumnIndex(StockHawkContract.QoutesColumns._ID));
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
