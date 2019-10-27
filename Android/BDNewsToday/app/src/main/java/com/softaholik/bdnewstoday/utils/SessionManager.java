package com.softaholik.bdnewstoday.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


import java.util.HashMap;


/**
 * The type Session manager.
 */
public class SessionManager {


    // SharedPref file name
    private static final String PREF_NAME = "BDNewsTodayPref";

    private static final String NIGHT_MODE = "NightMode";

    /**
     * The Pref.
     */
// Shared Preferences
    SharedPreferences pref;
    /**
     * The Editor.
     */
// Editor for Shared preferences
    SharedPreferences.Editor editor;
    /**
     * The Context.
     */
// Context
    Context _context;
    /**
     * The Private mode.
     */
// Shared pref mode
    int PRIVATE_MODE = 0;


    /**
     * Instantiates a new Session manager.
     *
     * @param context the context
     */
// Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Set night mode state
     */
    public void setNightMode(Boolean state) {
        // Storing login value as TRUE
        editor.putBoolean(NIGHT_MODE, state);

        // commit changes
        editor.commit();
    }

    /**
     *
     * @return night mode state
     */

    public Boolean loadNightModeState(){
        return pref.getBoolean(NIGHT_MODE,false);
    }



}
