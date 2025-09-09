package model;

import java.util.ArrayList;
import java.util.List;


public class Equipe {

    private int idEquipe;
    private String nomeEquipe;
    private String descricao;
    private List<Usuario> membros;


    public Equipe() {
        this.membros = new ArrayList<>();
    }

    public int getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }

    public String getNomeEquipe() {
        return nomeEquipe;
    }

    public void setNomeEquipe(String nomeEquipe) {
        this.nomeEquipe = nomeEquipe;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }

    public void adicionarMembro(Usuario membro) {
        if (this.membros != null) {
            this.membros.add(membro);
        }
    }

    @Override
    public String toString() {
        return "Equipes{" +
                "idEquipe=" + idEquipe +
                ", nomeEquipe='" + nomeEquipe + '\'' +
                '}';
    }
}