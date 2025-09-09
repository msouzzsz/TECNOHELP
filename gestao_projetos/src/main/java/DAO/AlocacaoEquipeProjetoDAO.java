package DAO;

import Utilidade.ConexaoBancoDeDados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlocacaoEquipeProjetoDAO {


    public int buscarEquipeIdPorNome(String NomeEquipe) {

        String sql = "SELECT id FROM equipes WHERE NomeEquipe = ?";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, NomeEquipe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ID da equipe: " + e.getMessage());
        }
        return -1;
    }


    public int buscarProjetoIdPorNome(String nome) {

        String sql = "SELECT id FROM projetos WHERE nome = ?";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ID do projeto: " + e.getMessage());
        }
        return -1;
    }

    public List<String> buscarProjetos() {
        List<String> projetos = new ArrayList<>();

        String sql = "SELECT nome FROM projetos";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                projetos.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos: " + e.getMessage());
        }
        return projetos;
    }


    public List<String> buscarEquipes() {
        List<String> equipes = new ArrayList<>();
        // CORRIGIDO: Nome da coluna padronizado para 'nome'
        String sql = "SELECT NomeEquipe FROM equipes";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                equipes.add(rs.getString("NomeEquipe"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar equipes: " + e.getMessage());
        }
        return equipes;
    }


    public boolean salvarAlocacao(int id_Equipe, int id_Projeto) {
        String sql = "INSERT INTO alocacoes (id_equipe, id_projeto) VALUES (?, ?)";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_Equipe);
            stmt.setInt(2, id_Projeto);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar a alocação: " + e.getMessage());
            return false;
        }
    }
}
