
package com.tunav.tunavmedi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ModuleLoader {
    public static final String tag = "ModuleLoader";

    private Context mContext;

    public ModuleLoader(Context context) {
        mContext = context;
        init();
    }

    private void copyAssets() {
        AssetManager assetManager = mContext.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("jars");
        } catch (IOException e) {
            Log.e(tag, "Failed to get asset file list.", e);
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("jars/" + filename);
                out = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                Log.e(tag, "Failed to copy asset file: " + filename, e);
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void init() {
        String name = mContext.getResources().getString(R.string.sp_moduleLoader);
        int mode = Context.MODE_PRIVATE;
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(name, mode);

        String key = mContext.getResources().getString(R.string.spkey_initialized);
        boolean defValue = false;

        if (!sharedPrefs.getBoolean(key, defValue)) {
            ;
        }
    }
}
