package mmd.meetup.Activities;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import mmd.meetup.Constants;
import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.R;

public class LoginActivity extends AppCompatActivity {
    //todo find a background and modify in Sketch (use the paint background maybe?)
    //todo make an app logo in Sketch (use the puzzle material icon)

    final int RC_SIGN_IN = 1;
    final String LOG_TAG = this.getClass().getSimpleName();

    Button signInButton;
    ProgressBar loadingIcon;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingIcon = findViewById(R.id.loadingIcon);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_key))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        if (getIntent() != null && getIntent().getBooleanExtra(Constants.KEYS.SIGN_OUT_KEY, false)) {
            gsc.signOut();
        }

        signInButton = findViewById(R.id.signIn);
        signInButton.setOnClickListener(view -> attemptSignIn());

    }

    private void goToLobby() {
        Intent intent = new Intent(this, LobbyActivity.class);
        startActivity(intent);
    }

    private void attemptSignIn() {
        loadingIcon.setVisibility(View.VISIBLE);
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            FirebaseClient.getInstance().initUser(account, new FirebaseClient.Callback<Boolean>() {
                @Override
                public void onResult(Boolean aBoolean) {
                    loadingIcon.setVisibility(View.INVISIBLE);
                    if (aBoolean) {
                        goToLobby();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_failed_login), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


}
