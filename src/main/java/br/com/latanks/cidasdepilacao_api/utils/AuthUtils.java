package br.com.latanks.cidasdepilacao_api.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static String getAuthenticatedEmail(){
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
