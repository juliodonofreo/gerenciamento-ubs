package com.ubs.ubs.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_funcao")
public class Funcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long authority;

    @ManyToMany(mappedBy = "funcoes")
    private Set<Usuario> usuarios = new HashSet<>();



    public Funcao(){}

    public Funcao(Long id, Long authority) {
        this.id = id;
        this.authority = authority;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthority() {
        return authority;
    }

    public void setAuthority(Long authority) {
        this.authority = authority;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }
}