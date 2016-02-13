package abassawo.c4q.nyc.flickrfeed.model;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by c4q-Abass on 10/27/15.
 */
public class QueryPrefs {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public static String getStoredQuery(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context ctx, String query){
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(PREF_SEARCH_QUERY, query).apply();
    }

    //Storing both query and id so we can compare the two.
    // if there are results, get 1st one. then verify if it === lastresult ID

    public static String getfLastResultId(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(PREF_LAST_RESULT_ID, null);
    }

    public static void setLastResultid(Context ctx, String lastResultId){
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(PREF_LAST_RESULT_ID, lastResultId).apply();
    }

    public static boolean isAlarmOn(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_IS_ALARM_ON, false);
    }
    public static void setAlarmOn(Context context, boolean isOn){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_IS_ALARM_ON, isOn).apply();
    }
}
