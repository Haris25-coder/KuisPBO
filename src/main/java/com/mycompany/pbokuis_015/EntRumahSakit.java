package com.mycompany.pbokuis_015;

import java.io.Serializable;

public class EntRumahSakit implements Serializable {
    protected String id;
    protected String nama;

    public EntRumahSakit (String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
}