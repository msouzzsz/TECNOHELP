package model;

public class Usuario {
    private int id;
    private String NomeCompleto;
    private String Cpf;
    private String Email;
    private String Login;
    private String Senha;
    private Enum Perfil;

    public Usuario() {
    }

    public Usuario( String nomeCompleto, String cpf, String email, String login, String senha, Enum perfil) {
        this.NomeCompleto = nomeCompleto;
        this.Cpf = cpf;
        this.Email = email;
        this.Login = login;
        this.Senha = senha;
        this.Perfil = perfil;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNomeCompleto() {
        return NomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.NomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return Cpf;
    }

    public void setCpf(String cpf) {
        this.Cpf = cpf;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        this.Login = login;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        this.Senha = senha;
    }

    public Enum getPerfil() {
        return Perfil;
    }

    public void setPerfil(Enum perfil) {
        this.Perfil = perfil;
    }


    @Override
    public String toString() {
        return NomeCompleto; // ou id + " - " + nomeCompleto, se preferir
    }

}
