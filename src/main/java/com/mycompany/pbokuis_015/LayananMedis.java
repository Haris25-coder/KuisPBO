package com.mycompany.pbokuis_015;

public class LayananMedis extends EntRumahSakit {
    private int stok;

    public LayananMedis(String id, String nama, int stok) {
        super(id, nama);
        this.stok = stok;
    }

    public int getStok() {
        return stok;
    }

    public void kurangiStok(int jumlah) throws TransaksinException {
        if (this.stok < jumlah) {
            throw new TransaksinException("Stok/Kuota layanan " + nama + " tidak mencukupi!");
        }
        this.stok -= jumlah;
    }
}