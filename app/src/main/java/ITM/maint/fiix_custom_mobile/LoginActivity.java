package ITM.maint.fiix_custom_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Collections;
import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.FiixObject;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.ui.view.ProgressBarDialog;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.LoginViewModel;


public class LoginActivity extends AppCompatActivity {
    protected static final String TAG = "LoginActivity";
    protected static String saved_email;
    protected static SharedPreferences sharedPreferences;
    private TextInputEditText txtUserName;
    private TextInputEditText txtPassword;
    private TextInputLayout layoutUserName;
    private TextInputLayout layoutPassword;
    private String username;
    private String password;
    private Boolean defaultExists = false;

    private ProgressBarDialog progressBarDialog;
    private LoginViewModel viewmodel;
    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        viewmodel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewmodel.init();

        viewmodel.getUserResponseLiveData().observe(this,  new Observer<List<FiixObject>>() {
                @Override
                public void onChanged(List<FiixObject> users) {
                    Boolean updateSharedPrefs = false;
                    progressBarDialog.dismiss();
                    if (users != null){
                        if (users.isEmpty()){
                            layoutUserName.setError("User not found");
                        } else {
                            User user = (User) Collections.unmodifiableList(users).get(0);
                            int result = verifyCredentials((User) user);

                            if (result == 0){
                                String updatedUsername = txtUserName.getText().toString();
                                String updatedPassword = txtPassword.getText().toString();
                                if (username == null || password == null) {
                                    updateSharedPrefs = true;
                                } else if (!username.equalsIgnoreCase(updatedUsername)){
                                    updateSharedPrefs = true;
                                }

                                if (updateSharedPrefs) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("DefaultUser", updatedUsername);
                                    editor.putString("DefaultPass", updatedPassword);
                                    editor.commit();
                                }

                                viewmodel.addUser(user);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                layoutUserName.setError("User not found");

                            }
                        }
                    } else {
                        layoutUserName.setError("User not found");
                    }
                }
            });
        progressBarDialog = new ProgressBarDialog(this);

        layoutUserName = (TextInputLayout) findViewById(R.id.usernameInputLayout);
        layoutPassword = (TextInputLayout) findViewById(R.id.passwordInputLayout);

        txtUserName = (TextInputEditText) findViewById(R.id.login_username);
        txtPassword = (TextInputEditText) findViewById(R.id.login_password);
        txtPassword.addTextChangedListener(new ValidationTextWatcher(txtPassword));
        txtUserName.addTextChangedListener(new ValidationTextWatcher(txtUserName));

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        username = sharedPreferences.getString("DefaultUser", null);
        if (username != null){
            txtUserName.setText(username);
            defaultExists = true;
        }

        password = sharedPreferences.getString("DefaultPass", null);
        if (password != null){
            txtPassword.setText(password);
        } else {
            defaultExists = false;
        }

        Button btnLogin = (Button) findViewById(R.id.Login_Button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (validateUserName()) {
                    if (validatePassword()) {
                        String updatedUsername = txtUserName.getText().toString();
                        if (username == null) username = updatedUsername;
                        progressBarDialog.show();
                        viewmodel.findUser(updatedUsername);
                    }
                }
                return;
            }
        });

    }

    @Override
    protected void onPause() {
        progressBarDialog.dismiss();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        viewmodel.dispose();
        super.onDestroy();
    }

    private int verifyCredentials(User user){

        if (user.getUsername().equalsIgnoreCase(username) && user.getIsGroup() == 0 ) {
            return 0;
        }
        return -1;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateUserName() {
        if (txtUserName.getText().toString().trim().isEmpty()) {
            layoutUserName.setError("User name is required");
            requestFocus(layoutUserName);
            return false;
        } else{
                layoutUserName.setErrorEnabled(false);
        }
            return true;
    }

    private boolean validatePassword() {
        if (txtPassword.getText().toString().trim().isEmpty()) {
            layoutPassword.setError("Password is required");
            requestFocus(layoutPassword);
            return false;
        } else {
            layoutPassword.setErrorEnabled(false);
        }
        return true;
    }


    private class ValidationTextWatcher implements TextWatcher {

        private View view;
        private boolean backspaceFlag;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (before == 1 && count == 0)
                backspaceFlag = true;
            else
                backspaceFlag = false;
        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (view.getId()) {
                case R.id.login_password:
                    validatePassword();
                    break;
                case R.id.login_username:
                    validateUserName();
                    break;
            }

            Log.d(TAG, "In afterTextChanged = " + s.toString());
        }
    }

}
