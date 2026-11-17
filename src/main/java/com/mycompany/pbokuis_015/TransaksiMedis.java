package com.mycompany.pbokuis_015;

import java.io.Serializable;

public class TransaksiMedis implements Serializable {
    private String idAktor;
    private String kodeObjek;
    private int jumlah;

    public TransaksiMedis(String idAktor, String kodeObjek, int jumlah) {
        this.idAktor = idAktor;
        this.kodeObjek = kodeObjek;
        this.jumlah = jumlah;
    }

    public String getIdAktor() {
        return idAktor;
    }

    public String getKodeObjek() {
        return kodeObjek;
    }

    public int getJumlah() {
        return jumlah;
    }
}