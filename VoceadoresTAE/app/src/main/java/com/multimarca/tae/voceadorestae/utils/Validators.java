package com.multimarca.tae.voceadorestae.utils;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erick on 12/23/15. Multimarca
 */
public class Validators {


    public static Boolean isValidEmail(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Boolean isValidPhone(String phone, int carrier) {
        if( carrier != 26 && carrier != 30 ) {
            return (Patterns.PHONE.matcher(phone).matches() && phone.length() == 10);
        }else {
            if( carrier == 26 ){
                return (Patterns.PHONE.matcher(phone).matches() && phone.length() == 11);
            }else {
                Pattern pattern = Pattern.compile("(([a-zA-Z]){4})([0-9]{9})$");
                Matcher matcher = pattern.matcher(phone);
                return (matcher.matches() && phone.length() == 13);
            }
        }
    }
    public static Boolean isValidReference(String reference, int carrier) {
        return true;
    }


    public static Boolean isLLength(String validate, int Length) {
        return (validate.length() < Length);
    }

    public static Boolean isULength(String validate, int Length) {
        return (validate.length() > Length);
    }
    public static Boolean isELength(String validate, int Length) {
        return (validate.length() == Length);
    }

    public static Boolean showError(View tInput, String error){

        TextInputLayout textInputLayout = ((TextInputLayout)tInput);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(error);
        return true;
    }
    public static Boolean hideError(View tInput){

        TextInputLayout textInputLayout = ((TextInputLayout)tInput);
        textInputLayout.setErrorEnabled(false);
        textInputLayout.refreshDrawableState();
        return false;

    }


}
