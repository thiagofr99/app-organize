package com.devthiagofurtado.organize.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.devthiagofurtado.organize.R;
import com.devthiagofurtado.organize.config.ConfiguracaoFirebase;
import com.devthiagofurtado.organize.helper.DateCustom;
import com.devthiagofurtado.organize.model.Lancamento;
import com.devthiagofurtado.organize.model.UsuarioSnap;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitaActivity extends AppCompatActivity {
    FloatingActionButton fab;
    private EditText valorReceita, descricaoReceita, categoriaReceita, dataReceita;
    //Busca Instancia do Firebase
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    //Busca Instancia do Firebase
    private DatabaseReference reference = ConfiguracaoFirebase.getDatabaseReference();

    private Lancamento lancamento;
    private Double receitaTotal;
    private Double receitaGerada;
    private Double receitaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        valorReceita = findViewById(R.id.editValorReceita);
        descricaoReceita = findViewById(R.id.descricaoReceita);
        categoriaReceita = findViewById(R.id.categoriaReceita);
        dataReceita = findViewById(R.id.dataReceita);

        fab = findViewById(R.id.fabConfirm2);

        dataReceita.setText(DateCustom.dataAtual());

        recuperarReceitaTotal();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validarCampos()) {
                    Double valor = Double.parseDouble(valorReceita.getText().toString());
                    String descricao = descricaoReceita.getText().toString();
                    String categoria = categoriaReceita.getText().toString();
                    String data = dataReceita.getText().toString();
                    String mesAno = DateCustom.dataFormatMes(data);
                    String tipoLancamento = "receita";
                    lancamento = new Lancamento();
                    lancamento.setValorLanc(valor);
                    lancamento.setDescricao(descricao);
                    lancamento.setDataLanc(data);
                    lancamento.setTipoLanc(tipoLancamento);
                    lancamento.setCategoria(categoria);

                    String uidAut = autenticacao.getCurrentUser().getUid();

                    receitaGerada = Double.parseDouble(valorReceita.getText().toString());
                    receitaAtualizada=receitaTotal+receitaGerada;

                    reference.child("usuario").child(uidAut).child("totalReceita").setValue(receitaAtualizada);
                    reference.child("lancamentos").child(uidAut).child(mesAno).push().setValue(lancamento);


                    Toast.makeText(ReceitaActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    public boolean validarCampos() {
        String valor = valorReceita.getText().toString();
        String descricao = descricaoReceita.getText().toString();
        String categoria = categoriaReceita.getText().toString();
        String data = dataReceita.getText().toString();
        if (!valor.isEmpty()) {
            if (!data.isEmpty()) {
                if (!categoria.isEmpty()) {
                    if (!descricao.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(ReceitaActivity.this, "Preencha o campo: Descrição", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ReceitaActivity.this, "Preencha o campo: Categoria", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ReceitaActivity.this, "Preencha o campo: Data", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ReceitaActivity.this, "Preencha o campo: Valor", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarReceitaTotal() {
        String uidUsuario = autenticacao.getCurrentUser().getUid();
        DatabaseReference usuarioRef = reference.child("usuario").child(uidUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsuarioSnap usuario = snapshot.getValue(UsuarioSnap.class);
                receitaTotal = usuario.getTotalReceita();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}