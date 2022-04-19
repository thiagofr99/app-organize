package com.devthiagofurtado.organize.activity;

import android.content.Intent;
import android.os.Bundle;

import com.devthiagofurtado.organize.adapter.LancamentoAdapter;
import com.devthiagofurtado.organize.config.ConfiguracaoFirebase;
import com.devthiagofurtado.organize.helper.DateCustom;
import com.devthiagofurtado.organize.model.Lancamento;
import com.devthiagofurtado.organize.model.Usuario;
import com.devthiagofurtado.organize.model.UsuarioSnap;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devthiagofurtado.organize.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    private DatabaseReference reference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference usuarioRef;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo;
    private Double despesaTotal, receitaTotal, resumo;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerLancamento;
    private LancamentoAdapter lancamentoAdapter;
    private List<Lancamento> listaLancamento = new ArrayList<>();
    private RecyclerView recyclerView;
    private String mesAnoSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");

        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.calendarView);
        textoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        recyclerView = findViewById(R.id.recyclerLancamentos);


        //Configurar adapter
        lancamentoAdapter = new LancamentoAdapter(listaLancamento, this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(lancamentoAdapter);

    }

    public void recuperarLancamentos() {
        String uidUser = autenticacao.getCurrentUser().getUid();
        usuarioRef = reference.child("lancamentos").child(uidUser).child(mesAnoSelecionado);

        valueEventListenerLancamento = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //snapshot.getChildren() recupera todos os objetos child e values

                listaLancamento.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Lancamento lancamento = dados.getValue(Lancamento.class);
                    listaLancamento.add(lancamento);


                   /* Log.i("TEXTOLAN", dados.getValue().toString());
                    Log.i("TEXTOLAN", listaLancamento.get(0).getTipoLanc());
*/

                }

                lancamentoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void recuperarResumo() {
        String uidUser = autenticacao.getCurrentUser().getUid();
        usuarioRef = reference.child("usuario").child(uidUser);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

                UsuarioSnap usuarioSave = snapshot.getValue(UsuarioSnap.class);
                despesaTotal = usuarioSave.getTotalDespesa();
                receitaTotal = usuarioSave.getTotalReceita();
                resumo = receitaTotal - despesaTotal;


                textoSaldo.setText("R$ " + decimalFormat.format(resumo));
                //appBarLayout.setBackground(R.color.colorPrimaryReceitas);
                textoSaudacao.setText("Bem vindo, " + usuarioSave.getNome());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesas(View view) {
        startActivity(new Intent(this, DespesaActivity.class));
    }

    public void adicionarReceitas(View view) {
        startActivity(new Intent(this, ReceitaActivity.class));
    }


    //Configurar um adapter


    public void configurarCalendarView() {


        final String dataAtual = DateCustom.dataAtual();
        mesAnoSelecionado = DateCustom.dataFormatMes(dataAtual);
//        Log.i("MES","Mes: "+mesAnoSelecionado);
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                if ((date.getMonth() + 1) > 9) {
                    mesAnoSelecionado = (date.getMonth() + 1) + "-" + date.getYear();
                } else {
                    mesAnoSelecionado = "0" + (date.getMonth() + 1) + "-" + date.getYear();
                }

                usuarioRef.removeEventListener(valueEventListenerLancamento);
                recuperarLancamentos();
//                Log.i("MES","Mes: "+mesAnoSelecionado);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        configurarCalendarView();
        recuperarResumo();
        recuperarLancamentos();

    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        usuarioRef.removeEventListener(valueEventListenerLancamento);
    }
}