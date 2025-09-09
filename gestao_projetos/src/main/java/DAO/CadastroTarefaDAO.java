package DAO;

import Utilidade.ConexaoBancoDeDados;
import model.Tarefa;
import model.StatusTarefa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CadastroTarefaDAO {

    public boolean salvarTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO tarefas (titulo, descricao, projeto_vinculado_id, responsavel_id, " +
                "status, data_inicio_prevista, data_fim_prevista, data_inicio_real, data_fim_real) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("Tentando salvar a tarefa: " + tarefa.getTitulo());

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setInt(3, tarefa.getProjetoVinculadoId());
            stmt.setInt(4, tarefa.getResponsavelId());
            stmt.setString(5, tarefa.getStatus().name());

            // Tratamento para datas nulas
            stmt.setDate(6, tarefa.getDataInicioPrevista() != null ? Date.valueOf(tarefa.getDataInicioPrevista()) : null);
            stmt.setDate(7, tarefa.getDataFimPrevista() != null ? Date.valueOf(tarefa.getDataFimPrevista()) : null);
            stmt.setDate(8, tarefa.getDataInicioReal() != null ? Date.valueOf(tarefa.getDataInicioReal()) : null);
            stmt.setDate(9, tarefa.getDataFimReal() != null ? Date.valueOf(tarefa.getDataFimReal()) : null);

            stmt.executeUpdate();
            System.out.println("Tarefa salva com sucesso!");
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar tarefa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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

    public List<String> buscarUsuarios() {
        List<String> usuarios = new ArrayList<>();
        String sql = "SELECT nome_completo FROM usuarios";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(rs.getString("nome_completo"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuários: " + e.getMessage());
        }
        return usuarios;
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

    public int buscarUsuarioIdPorNome(String nome) {
        String sql = "SELECT id FROM usuarios WHERE nome_completo = ?";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ID do usuário: " + e.getMessage());
        }
        return -1;
    }

    public List<Tarefa> buscarTodasTarefas() {
        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT " +
                "t.id, t.titulo, t.descricao, t.status, " +
                "t.data_inicio_prevista, t.data_fim_prevista, " +
                "t.data_inicio_real, t.data_fim_real, " +
                "p.nome AS nome_projeto, " +
                "u.nome_completo AS nome_responsavel " +
                "FROM tarefas t " +
                "LEFT JOIN projetos p ON t.projeto_vinculado_id = p.id " +
                "LEFT JOIN usuarios u ON t.responsavel_id = u.id " +
                "ORDER BY t.id";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(rs.getInt("id"));
                tarefa.setTitulo(rs.getString("titulo"));
                tarefa.setDescricao(rs.getString("descricao"));

                // Converte a String do banco de volta para o enum
                if (rs.getString("status") != null) {
                    tarefa.setStatus(StatusTarefa.valueOf(rs.getString("status").toUpperCase()));
                }

                // Mapeia as datas, verificando se são nulas
                Date dataInicioPrev = rs.getDate("data_inicio_prevista");
                if (dataInicioPrev != null) tarefa.setDataInicioPrevista(dataInicioPrev.toLocalDate());

                Date dataFimPrev = rs.getDate("data_fim_prevista");
                if (dataFimPrev != null) tarefa.setDataFimPrevista(dataFimPrev.toLocalDate());

                Date dataInicioReal = rs.getDate("data_inicio_real");
                if (dataInicioReal != null) tarefa.setDataInicioReal(dataInicioReal.toLocalDate());

                Date dataFimReal = rs.getDate("data_fim_real");
                if (dataFimReal != null) tarefa.setDataFimReal(dataFimReal.toLocalDate());

                // Popula os novos campos com os nomes vindos dos JOINs
                tarefa.setNomeProjeto(rs.getString("nome_projeto"));
                tarefa.setNomeResponsavel(rs.getString("nome_responsavel"));

                tarefas.add(tarefa);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as tarefas: " + e.getMessage());
            e.printStackTrace();
        }
        return tarefas;
    }
}
