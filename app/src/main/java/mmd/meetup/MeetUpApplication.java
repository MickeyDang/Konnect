package mmd.meetup;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class MeetUpApplication extends Application {

    /*Use sparingly. Prevents modularity of code.
    Should only be used to get context in hard to get places and reduce verbosity */
    private static WeakReference<MeetUpApplication> mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = new WeakReference<MeetUpApplication>(this);
    }

    public static MeetUpApplication getInstance() throws IllegalStateException{
        if (mInstance == null || mInstance.get() == null) {
            throw new IllegalStateException("getInstance called without application being created");
        }
        return mInstance.get();
    }
}
