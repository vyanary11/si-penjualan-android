package com.pratamatechnocraft.silaporanpenjualan.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pratamatechnocraft.silaporanpenjualan.LoginActivity;
import com.pratamatechnocraft.silaporanpenjualan.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String KD_USER = "KD_USER";
    public static final String TOKEN = "TOKEN";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences =context.getSharedPreferences( PREF_NAME, PRIVATE_MODE );
        editor = sharedPreferences.edit();

    }

    public void createSession(String kd_user, String token){
        editor.putBoolean(LOGIN, true);
        editor.putString( "KD_USER", kd_user );
        editor.putString( "TOKEN", token);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean( LOGIN, false );
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent intent = new Intent( context, LoginActivity.class );
            context.startActivity( intent );
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>(  );
        user.put( KD_USER, sharedPreferences.getString( KD_USER, null ) );
        user.put( TOKEN, sharedPreferences.getString( TOKEN, null ) );
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent( context, LoginActivity.class );
        context.startActivity( intent );
        ((MainActivity) context).finish();

    }
}
