package com.ubs.ubs.entities;

import com.ubs.ubs.enums.Especializacao;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class Medico extends Usuario{

    public Especializacao especializacao;

    public Medico() {
    }

    public Medico(Long id, String nome, String email, String senha, Especializacao especializacao) {
        super(id, nome, email, senha);
        this.especializacao = especializacao;
    }

    public Especializacao getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(Especializacao especializacao) {
        this.especializacao = especializacao;
    }
}
