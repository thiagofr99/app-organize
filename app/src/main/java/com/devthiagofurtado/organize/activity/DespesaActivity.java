package com.devthiagofurtado.organize.activity;

import androidx.annotation.NonNull;
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
import com.devthiagofurtado.organize.model.Usuario;
import com.devthiagofurtado.organize.model.UsuarioSnap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class DespesaActivity extends AppCompatActivity {
    FloatingActionButton fab;
    private EditText valorDesp, descricaoDesp, categoriaDespesa, dataDespesa;

    //Busca Instancia do Firebase
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    //Busca Instancia do Firebase
    private DatabaseReference reference = ConfiguracaoFirebase.getDatabaseReference();

    private Lancamento lancamento;
    private Double despesaTotal;
    private Double despesaGerada;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        valorDesp = findViewById(R.id.editValorDespesa);
        descricaoDesp = findViewById(R.id.descricaoDespesa);
        categoriaDespesa = findViewById(R.id.categoriaDespesa);
        dataDespesa = findViewById(R.id.dataDespesa);

        //Preenche campo data com data atual
        dataDespesa.setText(DateCustom.dataAtual());

        recuperarDespesatotal();


        fab = findViewById(R.id.fabConfirm);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validarCampos()) {
                    Double valor = Double.parseDouble(valorDesp.getText().toString());
                    String descricao = descricaoDesp.getText().toString();
                    String categoria = categoriaDespesa.getText().toString();
                    String data = dataDespesa.getText().toString();
                    String mesAno = DateCustom.dataFormatMes(data);
                    String tipoLancamento = "despesa";

                    lancamento = new Lancamento();
                    lancamento.setValorLanc(valor);
                    lancamento.setDescricao(descricao);
                    lancamento.setDataLanc(data);
                    lancamento.setTipoLanc(tipoLancamento);
                    lancamento.setCategoria(categoria);

                    String uidAut = autenticacao.getCurrentUser().getUid();

                    despesaGerada = Double.parseDouble(valorDesp.getText().toString());
                    despesaAtualizada=despesaTotal+despesaGerada;

                    reference.child("usuario").child(uidAut).child("totalDespesa").setValue(despesaAtualizada);
                    reference.child("lancamentos").child(uidAut).child(mesAno).push().setValue(lancamento);


                    Toast.makeText(DespesaActivity.this, "Salvo com sucesso.", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

    }

    public boolean validarCampos() {
        String valor = valorDesp.getText().toString();
        String descricao = descricaoDesp.getText().toString();
        String categoria = categoriaDespesa.getText().toString();
        String data = dataDespesa.getText().toString();
        if (!valor.isEmpty()) {
            if (!data.isEmpty()) {
                if (!categoria.isEmpty()) {
                    if (!descricao.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(DespesaActivity.this, "Preencha o campo: Descrição", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(DespesaActivity.this, "Preencha o campo: Categoria", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(DespesaActivity.this, "Preencha o campo: Data", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(DespesaActivity.this, "Preencha o campo: Valor", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarDespesatotal() {
        String uidUsuario = autenticacao.getCurrentUser().getUid();
        DatabaseReference usuarioRef = reference.child("usuario").child(uidUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsuarioSnap usuario = snapshot.getValue(UsuarioSnap.class);
                despesaTotal = usuario.getTotalDespesa();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}