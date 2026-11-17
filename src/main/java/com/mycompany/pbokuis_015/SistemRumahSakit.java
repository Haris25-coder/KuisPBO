package com.mycompany.pbokuis_015;

import java.io.*;
import java.util.*;

public class SistemRumahSakit implements Serializable {
    private String namaSistem;
    private HashMap<String, Pasien> daftarPasien;
    private HashMap<String, LayananMedis> daftarLayanan;
    private ArrayList<TransaksiMedis> riwayatTransaksi;

    public SistemRumahSakit(String namaSistem) {
        this.namaSistem = namaSistem;
        this.daftarPasien = new HashMap<>();
        this.daftarLayanan = new HashMap<>();
        this.riwayatTransaksi = new ArrayList<>();
    }

    public void tambahPasien(String id, String nama) {
        daftarPasien.put(id, new Pasien(id, nama));
    }

    public void tambahLayananMedis(String id, String nama, int stok) {
        daftarLayanan.put(id, new LayananMedis(id, nama, stok));
    }
    public void jalankanTransaksiDariFile(String filepath) throws Exception {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + filepath + " tidak ditemukan di direktori project.");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        reader.close();

        String rawData = content.toString().trim();
        if (rawData.isEmpty()) return;

        String[] blokTransaksi = rawData.split("\\|");
        for (String blok : blokTransaksi) {
            if (blok.trim().isEmpty()) continue;
            
            String[] parts = blok.split("/");
            String idAktor = "";
            String kodeObjek = "";
            int jml = 0;

            for (String p : parts) {
                if (p.startsWith("ID-")) {
                    idAktor = p.substring(3);
                } else if (p.startsWith("KD-")) {
                    kodeObjek = p.substring(3);
                } else if (p.startsWith("JML-")) {
                    jml = Integer.parseInt(p.substring(4));
                }
            }

            try {
                if (!daftarPasien.containsKey(idAktor)) {
                    throw new TransaksinException("Aktor ID " + idAktor + " tidak terdaftar!");
                }
                if (!daftarLayanan.containsKey(kodeObjek)) {
                    throw new TransaksinException("Layanan Kode " + kodeObjek + " tidak terdaftar!");
                }

                LayananMedis layanan = daftarLayanan.get(kodeObjek);
                layanan.kurangiStok(jml);
                
                riwayatTransaksi.add(new TransaksiMedis (idAktor, kodeObjek, jml));
                System.out.println("SUKSES: Transaksi " + idAktor + " membooking " + kodeObjek + " sebanyak " + jml);

            } catch (TransaksinException e) {
                System.out.println("[WARNING] Transaksi dilewati: " + e.getMessage());
            }
        }
    }
    public void simpanLaporanAkhir() {
        // 1. Object Serialization
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data_master.bak"))) {
            oos.writeObject(this);
            System.out.println("Serialization berhasil disimpan ke file: data_master.bak");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal melakukan serialisasi: " + e.getMessage());
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter("laporan.txt"))) {
            pw.println("ID_AKTOR,KODE_LAYANAN,JUMLAH");
            for (TransaksiMedis tr : riwayatTransaksi) {
                pw.println(tr.getIdAktor() + "," + tr.getKodeObjek() + "," + tr.getJumlah());
            }
            System.out.println("Laporan teks berhasil diekspor ke: laporan.txt (Format CSV)");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menulis laporan teks: " + e.getMessage());
        }
    }
}