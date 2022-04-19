package com.devthiagofurtado.organize.model;

import com.devthiagofurtado.organize.config.ConfiguracaoFirebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    String nome;
    String email;
    String senha;
    double totalReceita;
    double totalDespesa;


    public Usuario() {
    }

    public void salvar(String uidAut){
       Task<Void> reference = ConfiguracaoFirebase.getDatabaseReference().child("usuario").child(uidAut).setValue(this);

    }

    public Usuario(String nome, double totalDespesa, double totalReceita) {
        this.nome = nome;
        this.totalDespesa = totalDespesa;
        this.totalReceita = totalReceita;
    }


    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getTotalReceita() {
        return totalReceita;
    }

    public void setTotalReceita(double totalReceita) {
        this.totalReceita = totalReceita;
    }

    public double getTotalDespesa() {
        return totalDespesa;
    }

    public void setTotalDespesa(double totalDespesa) {
        this.totalDespesa = totalDespesa;
    }
}
