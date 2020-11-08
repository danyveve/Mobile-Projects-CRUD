package com.marcu.mobile.server.lab3.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "musical_instruments")
@Entity
@Data
public class MusicalInstrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    private String name;

    @Column
    private String category;

    @Column
    private String description;

    @Column(name = "quantity_on_stock")
    private int quantityOnStock;

    @Column
    private double price;

    @Column
    private String currency;
}
