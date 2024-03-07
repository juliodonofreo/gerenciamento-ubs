package com.ubs.ubs.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("1")
public class Paciente extends User {

    private String cpf;
    private Instant data_nascimento;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_paciente_funcao",
            joinColumns = @JoinColumn(name = "funcao_id"),
            inverseJoinColumns = @JoinColumn(name = "paciente_id"))
    private Set<Role> funcoes = new HashSet<>();

    public Paciente(){
        super();
    }

    public Paciente(Long id, String nome, String email, String senha, String cpf, Instant data_nascimento) {
        super(id, nome, email, senha);
        this.cpf = cpf;
        this.data_nascimento = data_nascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Instant getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Instant data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public Set<Role> getFuncoes() {
        return funcoes;
    }
}
