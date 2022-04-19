package com.devthiagofurtado.organize.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCustom {
    public static String dataAtual() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }





    public static String dataFormatMes(String date) {
//Quebra a string em pedaços baseado no caracter definido
        String retornoData[] = date.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];
        String mesAno = mes+"-"+ano;
        return mesAno;
    }
}
