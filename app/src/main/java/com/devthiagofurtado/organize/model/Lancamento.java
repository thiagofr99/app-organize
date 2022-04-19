package com.devthiagofurtado.organize.model;

public class Lancamento {
    private String dataLanc, categoria, descricao, tipoLanc;
    private Double valorLanc;



    public Lancamento() {
    }

    public String getDataLanc() {
        return dataLanc;
    }

    public void setDataLanc(String dataLanc) {
        this.dataLanc = dataLanc;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoLanc() {
        return tipoLanc;
    }

    public void setTipoLanc(String tipoLanc) {
        this.tipoLanc = tipoLanc;
    }

    public Double getValorLanc() {
        return valorLanc;
    }

    public void setValorLanc(Double valorLanc) {
        this.valorLanc = valorLanc;
    }
}
