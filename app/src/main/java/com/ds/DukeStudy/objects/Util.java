package com.ds.DukeStudy.objects;

//  Utility fields and methods

import android.text.TextUtils;
import android.widget.EditText;

public class Util {

    private static final String EMPTY_FIELD = "Required!";
    private static final String INCORRECT_EMAIL = "Email must contain @duke.edu";
    private static final String INCORRECT_PASSWORD = "Password too short! Enter at least 6 characters.";

    public static Boolean validateString(String input, EditText textField) {
        if (TextUtils.isEmpty(input)) {
            textField.setError(EMPTY_FIELD);
            return false;
        }
        return true;
    }

    public static Boolean validateEmail(String email, EditText textField) {
        if (!validateString(email, textField)) {
            return false;
        } else if (!email.contains("@duke.edu")) {
            textField.setError(INCORRECT_EMAIL);
            return false;
        }
        return true;
    }

    public static Boolean validatePassword(String password, EditText textField) {
        if (!validateString(password, textField)) {
            return false;
        } else if (password.length() < 6) {
            textField.setError(INCORRECT_PASSWORD);
            return false;
        }
        return true;
    }

    public static Boolean validateNumber(String input, EditText textField) {
        if (TextUtils.isEmpty(input)) {
            textField.setError(EMPTY_FIELD);
            return false;
        }
        return true;
    }
}
