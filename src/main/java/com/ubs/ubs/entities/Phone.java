package com.ubs.ubs.entities;

import com.ubs.ubs.entities.enums.PhoneType;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_telefone")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ddd;
    private String numero;
    private PhoneType tipo;

    @ManyToOne
    @JoinColumn(name = "id_dono")
    private User dono;

    public Phone() {}

    public Phone(Long id, String ddd, String numero, PhoneType tipo) {
        this.id = id;
        this.ddd = ddd;
        this.numero = numero;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public PhoneType getTipo() {
        return tipo;
    }

    public void setTipo(PhoneType tipo) {
        this.tipo = tipo;
    }

    public User getDono() {
        return dono;
    }

    public void setDono(User dono) {
        this.dono = dono;
    }
}
