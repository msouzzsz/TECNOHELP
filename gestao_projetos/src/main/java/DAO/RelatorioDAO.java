package DAO;

import Utilidade.ConexaoBancoDeDados;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RelatorioDAO {


    public List<Map<String, Object>> getProjetosEmAndamento() {
        List<Map<String, Object>> projetos = new ArrayList<>();
        String sql = "SELECT DISTINCT p.id, p.nome, p.descricao FROM projetos p " +
                "JOIN tarefas t ON p.id = t.projeto_vinculado_id " +
                "WHERE t.status = 'EM_EXECUCAO'";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> projeto = new HashMap<>();
                projeto.put("id", rs.getInt("id"));
                projeto.put("nome", rs.getString("nome"));
                projeto.put("descricao", rs.getString("descricao"));
                projetos.add(projeto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos em andamento: " + e.getMessage());
        }
        return projetos;
    }

    public List<Map<String, Object>> getDesempenhoUsuarios() {
        List<Map<String, Object>> desempenho = new ArrayList<>();
        String sql = "SELECT u.nome, " +
                "COUNT(t.id) AS total_tarefas, " +
                "SUM(CASE WHEN t.status = 'CONCLUIDA' THEN 1 ELSE 0 END) AS tarefas_concluidas " +
                "FROM usuarios u " +
                "LEFT JOIN tarefas t ON u.ID = t.responsavel_id " +
                "GROUP BY u.ID, u.nome";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> usuario = new HashMap<>();
                usuario.put("nome", rs.getString("nome"));
                usuario.put("total_tarefas", rs.getInt("total_tarefas"));
                usuario.put("tarefas_concluidas", rs.getInt("tarefas_concluidas"));
                desempenho.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar desempenho dos usuários: " + e.getMessage());
        }
        return desempenho;
    }


    public List<Map<String, Object>> getProjetosComAtraso() {
        List<Map<String, Object>> projetosAtraso = new ArrayList<>();
        String sql = "SELECT id, nome, data_inicio_prevista, data_fim_prevista FROM projetos " +
                "WHERE data_fim_real IS NULL AND data_fim_prevista < CURDATE()";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> projeto = new HashMap<>();
                projeto.put("id", rs.getInt("id"));
                projeto.put("nome", rs.getString("nome"));
                projeto.put("data_inicio_prevista", rs.getDate("data_inicio_prevista"));
                projeto.put("data_fim_prevista", rs.getDate("data_fim_prevista"));
                projetosAtraso.add(projeto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos com atraso: " + e.getMessage());
        }
        return projetosAtraso;
    }
}
