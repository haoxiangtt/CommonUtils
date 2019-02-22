package cn.bfy.utils.customsharepreference;

import java.util.Map;
import java.util.Set;

/**
 * Created by Pan on 2016/10/9.
 */

public interface CustomSharePreferences {
    /**
     * SharedPreference loading flag: when set, the file on disk will
     * be checked for modification even if the shared preferences
     * instance is already loaded in this process.  This behavior is
     * sometimes desired in cases where the application has multiple
     * processes, all writing to the same SharedPreferences file.
     * Generally there are better forms of communication between
     * processes, though.
     *
     */
    int MODE_MULTI_PROCESS = 0x0004;

    /**
     * Interface used for modifying values in a {@link CustomSharePreferences}
     * object.  All changes you make in an editor are batched, and not copied
     * back to the original {@link CustomSharePreferences} until you call {@link #commit}
     * or {@link #apply}
     */
    interface Editor {
        /**
         * Set a String value in the preferences editor, to be written back once
         * {@link #commit} or {@link #apply} are called.
         *
         * @param key The name of the preference to modify.
         * @param value The new value for the preference.
         *
         * @return Returns a reference to the same Editor object, so you can
         * chain put calls together.
         */
        Editor putString(String key, String value);

        /**
         * Set a set of String values in the preferences editor, to be written
         * back once {@link #commit} or {@link #apply} is called.
         *
         * @param key The name of the preference to modify.
         * @param values The set of new values for the preference.  Passing {@code null}
         *    for this argument is equivalent to calling {@link #remove(String)} with
         *    this key.
         * @return Returns a reference to the same Editor object, so you can
         * chain put calls together.
         */
        Editor putStringSet(String key, Set<String> values);

        /**
         * Set an int value in the preferences editor, to be written back once
         * {@link #commit} or {@link #apply} are called.
         *
         * @param key The name of the preference to modify.
         * @param value The new value for the preference.
         *
         * @return Returns a reference to the same Editor object, so you can
         * chain put calls together.
         */
        Editor putInt(String key, int value);

        /**
         * Set a long value in the preferences editor, to be written back once
         * {@link #commit} or {@link #apply} are called.
         *
         * @param key The name of the preference to modify.
         * @param value The new value for the preference.
         *
         * @return Returns a reference to the same Editor object, so you can
         * chain put calls together.
         */
        Editor putLong(String key, long value);

        /**
         * Set a float value in the preferences editor, to be written back once
         * {@link #commit} or {@link #apply} are called.
         *
         * @param key The name of the preference to modify.
         * @param value The new value for the preference.
         *
         * @return Returns a reference to the same Editor object, so you can
         * chain put calls together.
         */
        Editor putFloat(String key, float value);

        /**
         * Set a boolean value in the preferences editor, to be written back
         * once {@link #commit} or {@link #apply} are called.
         *
         * @param key The name of the preference to modify.
         * @param value The new value for the preference.
         *
         * @return Returns a reference to the same Editor object, so you can
         * chain put calls together.
         */
        Editor putBoolean(String key, boolean value);

        /**
         * Mark in the editor that a preference value should be removed, which
         * will be done in the actual preferences once {@link #commit} is
         * called.
         *
         * <p>Note that when committing back to the preferences, all removals
         * are done first, regardless of whether you called remove before
         * or after put methods on this editor.
         *
         * @param key The name of the preference to remove.
         *
         * @return Returns a reference to the same Editor object, so you can
         * chain put calls together.
         */
        Editor remove(String key);

        /**
         * Mark in the editor to remove <em>all</em> values from the
         * preferences.  Once commit is called, the only remaining preferences
         * will be any that you have defined in this editor.
         *
         * <p>Note that when committing back to the preferences, the clear
         * is done first, regardless of whether you called clear before
         * or after put methods on this editor.
         *
         * @return Returns a reference to the same Editor object, so you can
         * chain put calls together.
         */
        Editor clear();

        /**
         * Commit your preferences changes back from this Editor to the
         * {@link CustomSharePreferences} object it is editing.  This atomically
         * performs the requested modifications, replacing whatever is currently
         * in the CustomSharePreferences.
         *
         * <p>Note that when two editors are modifying preferences at the same
         * time, the last one to call commit wins.
         *
         * <p>If you don't care about the return value and you're
         * using this from your application's main thread, consider
         * using apply instead.
         *
         * @return Returns true if the new values were successfully written
         * to persistent storage.
         */
        boolean commit();
        /**
         * Commit your preferences changes back from this Editor to the
         * {@link CustomSharePreferences} object it is editing.  This atomically
         * performs the requested modifications, replacing whatever is currently
         * in the SharedPreferences.
         *
         * <p>Note that when two editors are modifying preferences at the same
         * time, the last one to call apply wins.
         *
         * <p>Unlike {@link #commit}, which writes its preferences out
         * to persistent storage synchronously, {@link #apply}
         * commits its changes to the in-memory
         * {@link CustomSharePreferences} immediately but starts an
         * asynchronous commit to disk and you won't be notified of
         * any failures.  If another editor on this
         * {@link CustomSharePreferences} does a regular {@link #commit}
         * while a {@link #apply} is still outstanding, the
         * {@link #commit} will block until all async commits are
         * completed as well as the commit itself.
         *
         * <p>As {@link CustomSharePreferences} instances are singletons within
         * a process, it's safe to replace any instance of {@link #commit} with
         * {@link #apply} if you were already ignoring the return value.
         *
         * <p>You don't need to worry about Android component
         * lifecycles and their interaction with <code>apply()</code>
         * writing to disk.  The framework makes sure in-flight disk
         * writes from <code>apply()</code> complete before switching
         * states.
         *
         * <p class='note'>The SharedPreferences.Editor interface
         * isn't expected to be implemented directly.  However, if you
         * previously did implement it and are now getting errors
         * about missing <code>apply()</code>, you can simply call
         * {@link #commit} from <code>apply()</code>.
         */
        void apply();
    }

    /**
     * Retrieve all values from the preferences.
     *
     * <p>Note that you <em>must not</em> modify the collection returned
     * by this method, or alter any of its contents.  The consistency of your
     * stored data is not guaranteed if you do.
     *
     * @return Returns a map containing a list of pairs key/value representing
     * the preferences.
     *
     * @throws NullPointerException
     */
    Map<String, ?> getAll();

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a String.
     *
     * @throws ClassCastException
     */
    String getString(String key, String defValue);

    /**
     * Retrieve a set of String values from the preferences.
     *
     * <p>Note that you <em>must not</em> modify the set instance returned
     * by this call.  The consistency of the stored data is not guaranteed
     * if you do, nor is your ability to modify the instance at all.
     *
     * @param key The name of the preference to retrieve.
     * @param defValues Values to return if this preference does not exist.
     *
     * @return Returns the preference values if they exist, or defValues.
     * Throws ClassCastException if there is a preference with this name
     * that is not a Set.
     *
     * @throws ClassCastException
     */
    Set<String> getStringSet(String key, Set<String> defValues);

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * an int.
     *
     * @throws ClassCastException
     */
    int getInt(String key, int defValue);

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a long.
     *
     * @throws ClassCastException
     */
    long getLong(String key, long defValue);

    /**
     * Retrieve a float value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a float.
     *
     * @throws ClassCastException
     */
    float getFloat(String key, float defValue);

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a boolean.
     *
     * @throws ClassCastException
     */
    boolean getBoolean(String key, boolean defValue);

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     *         otherwise false.
     */
    boolean contains(String key);

    /**
     * Create a new Editor for these preferences, through which you can make
     * modifications to the data in the preferences and atomically commit those
     * changes back to the CustomSharePreferences object.
     *
     * <p>Note that you <em>must</em> call {@link Editor#commit} to have any
     * changes you perform in the Editor actually show up in the
     * CustomSharePreferences.
     *
     * @return Returns a new instance of the {@link Editor} interface, allowing
     * you to modify the values in this CustomSharePreferences object.
     */
    Editor edit();
}
