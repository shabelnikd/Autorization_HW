package com.shabelnikd.autorization_hw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private MaterialButton logInButton;
    private boolean isAuth;

    private boolean editableGreaterZero(Editable... editable) {
        return Arrays.stream(editable)
                .map(Editable::toString)
                .map(String::trim)
                .noneMatch(String::isEmpty);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        logInButton = findViewById(R.id.login_btn);


        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                logInButton.setEnabled(editableGreaterZero(s, passwordEditText.getText()));
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                logInButton.setEnabled(editableGreaterZero(s, emailEditText.getText()));

            }
        });


        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Snackbar snackbar = Snackbar.make(
                        findViewById(R.id.main),
                        "Вы успешно вышли",
                        Snackbar.LENGTH_LONG
                );

                snackbar.show();

                findViewById(R.id.login_form_container).setVisibility(View.VISIBLE);
                findViewById(R.id.register_text2).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.register_text1)).setText("Добро пожаловать!");

                setEnabled(false);

            }
        };

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


        logInButton.setOnClickListener(click -> {
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.main),
                    "Вы успешно вошли",
                    Snackbar.LENGTH_LONG
            );

            ColorStateList prev = snackbar.getView().getBackgroundTintList();
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.btn_login_after));

            if (emailEditText.getText().toString().equals("admin")
                    && passwordEditText.getText().toString().equals("admin")) {
                snackbar.show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.main).getWindowToken(), 0);

                findViewById(R.id.login_form_container).setVisibility(View.INVISIBLE);
                findViewById(R.id.register_text2).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.register_text1)).setText("Добро пожаловать!\nadmin");
                onBackPressedCallback.setEnabled(true);
        } else {
                snackbar.setBackgroundTintList(prev);
                snackbar.setText("Попробуйте еще раз").show();
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());

            LinearLayout loginFormContainer = findViewById(R.id.login_form_container);
            LinearLayout welcomeTextContainer = findViewById(R.id.welcome_text);
            TransitionManager.beginDelayedTransition(loginFormContainer);
            TransitionManager.beginDelayedTransition(welcomeTextContainer);


            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom + imeInsets.bottom);
            return insets;
        });

    }
}