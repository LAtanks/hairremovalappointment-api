package br.com.latanks.cidasdepilacao_api.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneValidator {

    public static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public static boolean isValid(String phone){
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, "BR");
            return phoneUtil.isValidNumber(number);
        }catch (NumberParseException e){
            return false;
        }
    }
}
