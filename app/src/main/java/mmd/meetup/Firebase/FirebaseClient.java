package mmd.meetup.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import mmd.meetup.R;

/**
 * Created by mickeydang on 2018-03-29.
 */

public class FirebaseClient {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static FirebaseClient fcInstance;

    public static FirebaseClient getInstance() {
        if (fcInstance == null) {
            fcInstance = new FirebaseClient();
        }

        return fcInstance;
    }

    private FirebaseClient() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void initUser(GoogleSignInAccount acct, final Callback<Boolean> callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = mAuth.getCurrentUser();
                            callback.onResult(true);
                        } else {
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                            callback.onResult(false);
                        }
                    }
                });
    }

    public interface Callback<T> {
        void onResult(T t);
    }

}
