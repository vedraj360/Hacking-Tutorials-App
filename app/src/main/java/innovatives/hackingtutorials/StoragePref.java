package innovatives.hackingtutorials;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Ved&Div on 24-03-2018.
 */


public class StoragePref {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "hack";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String count = "count";

    public StoragePref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setCount(int c)
    {
        editor.putInt(count,c);
        editor.commit();
    }

    public int getCount()
    {
        return pref.getInt(count,0);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void launchHomeScreen(boolean f) {
        setFirstTimeLaunch(f);
        }

}