package cn.richinfo.utils.customsharepreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.AbstractMap;
import java.util.HashMap;

/**
 * Created by Pan on 2016/10/10.
 */
@TargetApi(19)
public class SharePreferencesManager {
    private static final String TAG = "SharePreferencesManager";
    /**
     * Map from preference name, to cached preferences.
     */
    private static AbstractMap<String, CustomSharePreferencesImpl> sSharedPrefs;

    private static SharePreferencesManager sharePreferencesManager;
    private File mPreferencesDir;
//    private static Context mContext;
    private static String mDirName = "custom_shared_prefs";

    private SharePreferencesManager() {}

    public static SharePreferencesManager getSharePreferencesManager() {
        synchronized (SharePreferencesManager.class) {
            if (sharePreferencesManager == null) {
                sharePreferencesManager = new SharePreferencesManager();
            }
            return sharePreferencesManager;
        }
    }

    /**
     *  初始化，SharePreference存储目录名称默认是包名
     * @param context
     */
    public void init(Context context) {
        init(context, mDirName);
    }

    /**
     * 初始化
     * @param context 上下文
     * @param dirName SharePreference存储目录名称
     */
    public void init(Context context, String dirName) {
//        mContext = context.getApplicationContext();
        mDirName = dirName;
    }

    public CustomSharePreferences getSharedPreferences(String name, int mode) {
        CustomSharePreferencesImpl sp;
        synchronized (SharePreferencesManager.class) {
            if (sSharedPrefs == null) {
                sSharedPrefs = new HashMap<String, CustomSharePreferencesImpl>();
            }
            sp = sSharedPrefs.get(name);
            if (sp == null) {
                File prefsFile = getSharedPrefsFile(name);
                sp = new CustomSharePreferencesImpl(prefsFile, mode);
                sSharedPrefs.put(name, sp);
                return sp;
            }
        }
        if ((mode & CustomSharePreferences.MODE_MULTI_PROCESS) != 0) {
            // If somebody else (some other process) changed the prefs
            // file behind our back, we reload it.  This has been the
            // historical (if undocumented) behavior.
            sp.startReloadIfChangedUnexpectedly();
        }
        return sp;
    }

    public File getSharedPrefsFile(String name) {
        return makeFilename(getPreferencesDir(), name + ".xml");
    }

    private File makeFilename(File base, String name) {
        if (name.indexOf(File.separatorChar) < 0) {
            return new File(base, name);
        }
        throw new IllegalArgumentException(
                "File " + name + " contains a path separator");
    }

    private File getPreferencesDir() {
        synchronized (SharePreferencesManager.class) {
            if (mPreferencesDir == null) {
                mPreferencesDir = new File(Environment.getExternalStorageDirectory(), mDirName);
            }
            return mPreferencesDir;
        }
    }
}
