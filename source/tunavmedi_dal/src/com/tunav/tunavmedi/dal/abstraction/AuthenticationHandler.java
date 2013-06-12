
package com.tunav.tunavmedi.dal.abstraction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tunav.tunavmedi.dal.R;

public abstract class AuthenticationHandler {

    private static final String tag = "AuthenticationHandler";
    private Context mContext;
    private String name;
    private String key;
    private String error = "Authentication Failed! Wrong id/password combination.";

    public AuthenticationHandler(Context context) {
        mContext = context;
        name = mContext.getResources().getString(R.string.sp_authentication);
        key = mContext.getResources().getString(R.string.spkey_authenticated);
    }

    public String getDisplayName() {
        return null;
    }

    public String getError() {
        return error;
    }

    public String getPhoto() {
        return null;
    }

    public boolean getStatus() {
        SharedPreferences shared = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return shared.getBoolean(key, false);
    }

    public void login(String username, String password) {
        setStatus(true);
    }

    public void logout() {
        setStatus(false);
    }

    public void setError(String msg) {
        error = msg;
    }

    protected void setStatus(boolean value) {
        Editor editor = mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
