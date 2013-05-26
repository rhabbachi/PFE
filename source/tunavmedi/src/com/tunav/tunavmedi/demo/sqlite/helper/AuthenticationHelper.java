package com.tunav.tunavmedi.demo.sqlite.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.tunav.tunavmedi.abstraction.AuthenticationHandler;
import com.tunav.tunavmedi.demo.sqlite.database.CredentialsContract;
import com.tunav.tunavmedi.demo.sqlite.database.DemoSQLite;


public class AuthenticationHelper extends AuthenticationHandler {

    private static final String tag = "AuthenticationHelper";
    private DemoSQLite mDBHelper = null;

    public AuthenticationHelper(Context context) {
        Log.v(tag, "AuthenticationHelper()");
        mDBHelper = new DemoSQLite(context);
    }

    @Override
    public Integer authenticate(String login, String password) {
        Log.i(tag, "authenticate()");
        Log.v(tag, "login is " + login + " passhash is " + password);
        // hash password if needed
        // String hash = null;
        // if (hash != null) {
        // byte[] passwordHash = null;
        // try {
        // MessageDigest digest;
        //
        // Log.d(TAG, "Hash used by the implementation is " + hash);
        // digest = MessageDigest.getInstance(hash);
        // digest.reset();
        // digest.update(password.getBytes());
        // password = digest.digest().toString();
        // Log.d(TAG, "Hashed password is " + password);
        // } catch (NoSuchAlgorithmException nsae) {
        // Log.d(TAG, "NoSuchAlgorithmException", nsae);
        // }
        // }

        SQLiteDatabase database;
        Cursor cursor = null;
        Integer userID = null;
        try {
            database = mDBHelper.getReadableDatabase();
        } catch (SQLiteException tr) {
            Log.e(tag, "Database probleme!");
            Log.d(tag, "Database probleme!", tr);
            return null;
        }

        String[] projection = {CredentialsContract._ID};
        String selection = CredentialsContract.KEY_LOGIN + " LIKE ? AND "
                + CredentialsContract.KEY_PASSHASH + " LIKE ?";
        String[] selectionArgs = {login, password};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "1";
        try {
            if (database != null) {
                cursor = database.query(CredentialsContract.TABLE_NAME, projection,
                        selection, selectionArgs, groupBy, having, orderBy, limit);
            }

            if (cursor != null && cursor.moveToFirst()) {
                userID = cursor.getInt(cursor
                        .getColumnIndexOrThrow(CredentialsContract._ID));
            } else {
                Log.e(tag, "Cursor empty!");
            }
        } catch (IllegalArgumentException tr) {
            // something wired happened, log and don't connect
            Log.e(tag, "IllegalArgumentException");
            Log.d(tag, "IllegalArgumentException", tr);
        } finally {
            assert cursor != null;
            cursor.close();
        }
        return userID;
    }

    @Override
    public void revokeToken(byte[] token) {
        // TODO if authentication method support token
    }

    @Override
    public byte[] requestToken() {
        // TODO if authentication method support token
        return null;
    }
}
