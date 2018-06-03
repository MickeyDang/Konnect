package mmd.konnect.Activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import mmd.konnect.Constants;
import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.R;

public class LoginActivity extends AppCompatActivity {

    final int RC_SIGN_IN = 1;
    final int MIN_PASS_LENGTH = 5;
    final String LOG_TAG = this.getClass().getSimpleName();

    Button signInGoogleButton;
    Button signInEmailButton;
    ProgressBar loadingIcon;
    GoogleSignInClient gsc;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        loadingIcon = findViewById(R.id.loadingIcon);
        signInGoogleButton = findViewById(R.id.signInGoogle);
        signInEmailButton = findViewById(R.id.signInEmail);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_key))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        if (getIntent() != null && getIntent().getBooleanExtra(Constants.KEYS.SIGN_OUT_KEY, false)) {
            gsc.signOut();
        }
        signInGoogleButton.setOnClickListener(view -> attemptSignInGoogle());
        signInEmailButton.setOnClickListener(view -> attemptSignInEmail());

    }

    private void goToLobby() {
        Intent intent = new Intent(this, LobbyActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void attemptSignInGoogle() {
        initProgressView();
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void attemptSignInEmail() {

        final View loginView = View.inflate(this, R.layout.view_email_login, null);

        //prompt for fields
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(loginView)
                .setTitle(R.string.prompt_email_sign_in)
                .setPositiveButton(R.string.nav_button_next, (dialogInterface, i) -> {

                    String email = ((EditText) loginView.findViewById(R.id.emailField)).getText().toString();
                    String password = ((EditText) loginView.findViewById(R.id.passwordField)).getText().toString();

                    //assert fields nonNull
                    if (!email.isEmpty() && !password.isEmpty() && password.length() > MIN_PASS_LENGTH) {
                        initProgressView();

                        FirebaseClient.getInstance().initUser(email, password, resultCode -> {
                            removeProgressView();

                            switch(resultCode) {
                                case FirebaseClient.Callback.SUCCESS:
                                    goToLobby();
                                    break;
                                case FirebaseClient.Callback.AUTH_FAILED:
                                    Toast.makeText(LoginActivity.this, getString(R.string.toast_failed_login), Toast.LENGTH_SHORT)
                                            .show();
                                    Log.e(LOG_TAG, "Could not create user");
                                    break;
                                case FirebaseClient.Callback.CONNECTION_FAILED:
                                    Toast.makeText(LoginActivity.this, getString(R.string.toast_failed_login), Toast.LENGTH_SHORT)
                                            .show();
                                    Log.e(LOG_TAG, "Connection failed");
                                    break;
                            }

                        });
                    } else {
                        Toast.makeText(this, getString(R.string.toast_empty_field), Toast.LENGTH_SHORT)
                                .show();
                    }

                });

        builder.create().show();
    }

    private void initProgressView() {
        loadingIcon.setVisibility(View.VISIBLE);
    }

    private void removeProgressView() {
        loadingIcon.setVisibility(View.GONE);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            FirebaseClient.getInstance().initUser(account, new FirebaseClient.Callback<Boolean>() {
                @Override
                public void onResult(Boolean aBoolean) {
                    removeProgressView();
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
            // Refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, getString(R.string.toast_failed_login), Toast.LENGTH_SHORT)
                    .show();
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
