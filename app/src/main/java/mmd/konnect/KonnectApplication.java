package mmd.konnect;

import android.app.Application;

import java.lang.ref.WeakReference;

public class KonnectApplication extends Application {

    /*Use sparingly. Prevents modularity of code.
    Should only be used to get context in hard to get places and reduce verbosity */
    private static WeakReference<KonnectApplication> mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = new WeakReference<KonnectApplication>(this);
    }

    public static KonnectApplication getInstance() throws IllegalStateException {
        if (mInstance == null || mInstance.get() == null) {
            throw new IllegalStateException("getInstance called without application being created");
        }
        return mInstance.get();
    }
}
