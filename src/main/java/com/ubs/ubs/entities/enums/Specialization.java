package com.ubs.ubs.entities.enums;

public enum Specialization {
    ENFERMEIRO,
    MEDICO,
    CARDIOLOGIA,
    DERMATOLOGIA,
    ORTOPEDIA,
    GINECOLOGIA,
    PEDIATRIA,
    NEUROLOGIA,
    OFTALMOLOGIA,
    ANESTESIOLOGIA,
    CIRURGIA_GERAL,
    ONCOLOGIA,
    PSIQUIATRIA,
    RADIOLOGIA,
    UROLOGIA,
    ENDOCRINOLOGIA,
    GASTROENTEROLOGIA;

    public String getFormattedName() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase().replace("_", " ");
    }
}