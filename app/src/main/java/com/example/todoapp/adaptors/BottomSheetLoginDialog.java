package com.example.todoapp.adaptors;

import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.LoginActivity;
import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.example.todoapp.models.Admin;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetLoginDialog extends BottomSheetDialogFragment {

    EditText emailEditText, passwordEditText;
    Button loginButton;

    Boolean pass = false;
    Boolean passEmail = false;
    Boolean passPassword = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.login_dialog, null, false);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        emailEditText = view.findViewById(R.id.textFieldEmail);
        passwordEditText = view.findViewById(R.id.textFieldPassword);

        loginButton = view.findViewById(R.id.buttonMasuk);

        Intent main = new Intent(getContext(), MainActivity.class);
        main.putExtra("isAdmin", true);

        ArrayList<Admin> adminList = new ArrayList<>();

        adminList.add(new Admin("1", "vyaninsya@gmail.com", "12345"));

//        loginButton.setBackground(Color.BLACK);

        loginButton.setOnClickListener(view1 -> {

            for (int i = 0; i < adminList.toArray().length; i++) {
                String email = adminList.get(i).getEmail();
                String password = adminList.get(i).getPassword();
                String emailLogin = emailEditText.getText().toString();
                String passwordLogin = passwordEditText.getText().toString();

                if (email.equals(emailLogin)) {
                    passEmail = true;
                }

                if (passEmail.equals(true)) {
                    if (password.equals(passwordLogin)) {
                        passPassword = true;
                    }
                }

                if (passEmail && passPassword == true) {
                    pass = true;
                } else {
                    Toast.makeText(getContext(), "Check Kembali Akun Anda", Toast.LENGTH_SHORT).show();
                }
            }

            if (pass == true) {
                startActivity(main);

                dismiss();

                emailEditText.setText("");
                passwordEditText.setText("");
            }
            pass = false;
            passEmail = false;
            passPassword = false;
        });

        return view;
    }
}
