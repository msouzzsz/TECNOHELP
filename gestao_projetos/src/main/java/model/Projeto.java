package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Projeto {

    private int id;
    private String nome;
    private String descricao;
    private LocalDate dataInicioPrevista;
    private LocalDate dataTerminoPrevista;
    private LocalDate dataInicioReal;
    private LocalDate dataTerminoReal;
    private StatusProjeto status;
    private Usuario gerenteResponsavel;

    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Projeto(int id, String nome, String descricao, LocalDate dataInicioPrevista, LocalDate dataTerminoPrevista,
                   LocalDate dataInicioReal, LocalDate dataTerminoReal, StatusProjeto status, Usuario gerenteResponsavel) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicioPrevista = dataInicioPrevista;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.dataInicioReal = dataInicioReal;
        this.dataTerminoReal = dataTerminoReal;
        this.status = status;
        this.gerenteResponsavel = gerenteResponsavel;
    }

    public Projeto() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicioPrevista() {
        return dataInicioPrevista;
    }

    public void setDataInicioPrevista(LocalDate dataInicioPrevista) {
        this.dataInicioPrevista = dataInicioPrevista; // Corrigido
    }

    public LocalDate getDataTerminoPrevista() {
        return dataTerminoPrevista;
    }

    public void setDataTerminoPrevista(LocalDate dataTerminoPrevista) {
        this.dataTerminoPrevista = dataTerminoPrevista;
    }

    public LocalDate getDataInicioReal() {
        return dataInicioReal;
    }

    public void setDataInicioReal(LocalDate dataInicioReal) {
        this.dataInicioReal = dataInicioReal;
    }

    public LocalDate getDataTerminoReal() {
        return dataTerminoReal;
    }

    public void setDataTerminoReal(LocalDate dataTerminoReal) { // Adicionado
        this.dataTerminoReal = dataTerminoReal;
    }

    public StatusProjeto getStatus() {
        return status;
    }

    public void setStatus(StatusProjeto status) {
        this.status = status;
    }

    public Usuario getGerenteResponsavel() {
        return gerenteResponsavel;
    }

    public void setGerenteResponsavel(Usuario gerenteResponsavel) {
        this.gerenteResponsavel = gerenteResponsavel;
    }


    @Override
    public String toString() {
        return this.nome;
    }
}