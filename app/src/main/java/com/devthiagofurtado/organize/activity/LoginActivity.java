package com.devthiagofurtado.organize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devthiagofurtado.organize.R;
import com.devthiagofurtado.organize.config.ConfiguracaoFirebase;
import com.devthiagofurtado.organize.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha;
    private Button buttonEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        buttonEntrar = findViewById(R.id.buttonEntrar);

        getSupportActionBar().setTitle("Login");

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();



                    if (!email.isEmpty()) {
                        if (!senha.isEmpty()) {
                            //Toast.makeText(LoginActivity.this,email + " "+ senha,Toast.LENGTH_SHORT).show();
                            usuario = new Usuario(email,senha);
                            validarLogin(usuario);



                        } else {
                            Toast.makeText(LoginActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Preencha o email", Toast.LENGTH_SHORT).show();
                    }


            }
        });

    }

    public void validarLogin(Usuario u){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(u.getEmail(),u.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(LoginActivity.this,PrincipalActivity.class));
                    Toast.makeText(LoginActivity.this,"Login efetuado com sucesso",Toast.LENGTH_SHORT).show();
                } else {

                    String excessao = "";
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        excessao = "Email ou senha não correspondem a um usuário cadastrado.";
                    }
                    catch (FirebaseAuthInvalidUserException e){
                        excessao = "Email não possui cadastro.";
                    }
                    catch ( Exception e){
                        excessao="Erro a efetuar login: "+ e.getMessage();
                        e.printStackTrace();
                    }



                    Toast.makeText(LoginActivity.this,excessao,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

  /*  @Override
    protected void onStop() {
        super.onStop();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();
    }
*/


}