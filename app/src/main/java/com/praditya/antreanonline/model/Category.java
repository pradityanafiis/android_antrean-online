package com.praditya.antreanonline.model;

import java.io.Serializable;

public enum Category implements Serializable {
    BENGKEL("1", "Bengkel"),
    CUCI_KENDARAAN("2", "Cuci Kendaraan"),
    KECANTIKAN("3", "Kecantikan"),
    KESEHATAN("4", "Kesehatan"),
    MAKANAN("5", "Makanan"),
    PELAYANAN_PUBLIK("6", "Pelayanan Publik"),
    PENDIDIKAN("7", "Pendidikan"),
    PERBANKAN("8", "Perbankan"),
    UMUM("9", "Umum");

    private String id;
    private String name;

    Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
