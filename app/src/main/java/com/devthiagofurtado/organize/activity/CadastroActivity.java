package com.devthiagofurtado.organize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devthiagofurtado.organize.R;
import com.devthiagofurtado.organize.config.ConfiguracaoFirebase;
import com.devthiagofurtado.organize.helper.Base64Custom;
import com.devthiagofurtado.organize.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoNome, campoEmail, campoSenha;
    private Button buttonCadastrar;
    private FirebaseAuth autenticar;
    private DatabaseReference referenciado;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().setTitle("Cadastro");

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        campoNome = findViewById(R.id.editNome);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);


        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = campoNome.getText().toString();
                String senha = campoSenha.getText().toString();
                String email = campoEmail.getText().toString();

                if (!nome.isEmpty()) {
                    if (!email.isEmpty()) {
                        if (!senha.isEmpty()) {

                            usuario = new Usuario(nome, email, senha);

                            cadastrarUsuario();




                        } else {
                            Toast.makeText(CadastroActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastroActivity.this, "Preencha o email", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CadastroActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cadastrarUsuario() {
       // Toast.makeText(this,usuario.getEmail()+" "+usuario.getSenha(),Toast.LENGTH_LONG).show();


        autenticar = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticar.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
/*
Base 64 estudo


String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
usuario.setIdUsuario(idUsuario);
usuario.salvar();

 */


                    //Loga usuario criado
                    autenticar.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha());

                    //Busca o UID do usuario logado
                    String uid = autenticar.getCurrentUser().getUid();


                    //Cria novo usuario para salvar no Firebase, salvando nome do usuario e total despesa e receita
                    String nome= usuario.getNome();
                    usuario = new Usuario(nome,0,0);
                    usuario.salvar(uid);
                    //Encerra cadastro activity
                    finish();

                    //Toast confirmando o sucesso do cadastro
                    Toast.makeText(CadastroActivity.this,"Sucesso ao cadastrar usuario",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CadastroActivity.this,PrincipalActivity.class));
                } else {
                    String excessao = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        excessao = "Digite uma senha mais forte!";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        excessao = "Digite um e-mail válido!";
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        excessao = "Esta conta já foi cadastrada";
                    }
                    catch ( Exception e){
                        excessao="Erro a cadastrar usuário: "+ e.getMessage();
                        e.printStackTrace();
                    }


                    Toast.makeText(CadastroActivity.this,excessao,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}