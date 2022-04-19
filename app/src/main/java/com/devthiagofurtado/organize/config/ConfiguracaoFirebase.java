package com.devthiagofurtado.organize.config;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static FirebaseAuth autenticar;
    private static DatabaseReference referencia;


    //retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){

        //Retorna a instancia do firebase

        if(autenticar==null){
            autenticar = FirebaseAuth.getInstance();
        }

        return autenticar;
    }

    //retorna a instancia do FirebaseS Lancamentos
    public static DatabaseReference getDatabaseReference(){
        if(referencia==null){
            referencia = FirebaseDatabase.getInstance().getReference();
        }
        return referencia;
    }



}
