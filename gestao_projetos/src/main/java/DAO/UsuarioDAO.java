package DAO;

import Utilidade.ConexaoBancoDeDados;
import model.Perfil;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void salvarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome_completo, CPF, Email, Login, Senha, Perfil) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getLogin());
            stmt.setString(5, usuario.getSenha());
            stmt.setString(6, usuario.getPerfil().name());

            stmt.executeUpdate();
            System.out.println("Usuário salvo com sucesso!");
        }
    }

    public List<Usuario> buscarGerentes() throws SQLException {
        List<Usuario> gerentes = new ArrayList<>();
        String sql = "SELECT ID, nome_completo FROM usuarios WHERE Perfil = 'GERENTE'";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario gerente = new Usuario();
                gerente.setId(rs.getInt("ID"));
                gerente.setNomeCompleto(rs.getString("nome_completo"));
                gerentes.add(gerente);
            }
        }
        return gerentes;
    }

    public List<Usuario> listarTodos() throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT ID, nome_completo, CPF, Email, Login, Senha, Perfil FROM usuarios ORDER BY nome_completo";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {


            while (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setId(rs.getInt("ID"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setCpf(rs.getString("CPF"));
                usuario.setEmail(rs.getString("Email"));
                usuario.setLogin(rs.getString("Login"));
                usuario.setSenha(rs.getString("Senha")); // Lembre-se que senhas devem ser criptografadas

                String perfilDoBanco = rs.getString("Perfil");
                if (perfilDoBanco != null) {
                    usuario.setPerfil(Perfil.valueOf(perfilDoBanco.toUpperCase()));
                }

                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
}
