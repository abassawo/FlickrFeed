package abassawo.c4q.nyc.flickrfeed;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by c4q-Abass on 10/27/15.
 */
public class QueryPrefs {
    private static final String PREF_SEARCH_QUERY = "searchQuery";

    public static String getStoredQuery(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context ctx, String query){
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(PREF_SEARCH_QUERY, query).apply();
    }
}
