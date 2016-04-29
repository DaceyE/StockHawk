package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {

    private String LOG_TAG = "stockhawk " + StockIntentService.class.getSimpleName();

    public StockIntentService() {
        super(StockIntentService.class.getName());
    }

    public StockIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
        StockTaskService stockTaskService = new StockTaskService(this);
        Bundle args = new Bundle();
        if (intent.getStringExtra("tag").equals("add")) {
            args.putString("symbol", intent.getStringExtra("symbol"));
        }

        Log.v(LOG_TAG, intent.getStringExtra("tag"));

        // We can call OnRunTask from the intent service to force it to run immediately instead of
        // scheduling a task.
        stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));


        if (intent.getBooleanExtra("getHistorical", false)){
            //fetch data immediately
            HistoricalDataTaskService historicalDataTaskService = new HistoricalDataTaskService(this);
            historicalDataTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));

            //Schedule periodic updates
            schedulePeriodic();
        }
    }

    /**
     * Schedules a periodic task to update the historical data approximately every 24 hours.
     */
    private void schedulePeriodic() {
        long period = 86400L;
        long flex = 3600L;
        String periodicTag = "periodic";

        Log.v(LOG_TAG, "schedulePeriodic executed");
        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(HistoricalDataTaskService.class)
                .setPeriod(period)
                .setFlex(flex)
                .setTag(periodicTag)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .build();

        GcmNetworkManager.getInstance(this).schedule(periodicTask);
    }

}
