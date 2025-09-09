package DAO;

import Utilidade.ConexaoBancoDeDados;
import model.Projeto;
import model.StatusProjeto;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

    public void salvarProjeto(Projeto projeto) throws SQLException {
        if (projeto == null || projeto.getDataInicioPrevista() == null || projeto.getDataTerminoPrevista() == null || projeto.getStatus() == null || projeto.getGerenteResponsavel() == null) {
            throw new IllegalArgumentException("Projeto ou seus campos essenciais (datas, status, gerente) não podem ser nulos.");
        }

        String sql = "INSERT INTO projetos (nome, descricao, data_inicio_prevista, data_termino_prevista, status, gerente_responsavel_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, java.sql.Date.valueOf(projeto.getDataInicioPrevista()));
            stmt.setDate(4, java.sql.Date.valueOf(projeto.getDataTerminoPrevista()));
            stmt.setString(5, projeto.getStatus().name());
            stmt.setInt(6, projeto.getGerenteResponsavel().getId());

            stmt.executeUpdate();
            System.out.println("Projeto salvo com sucesso!");
        }
    }

    public List<Projeto> buscarTodosProjetos() throws SQLException {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT p.*, " +
                "u.ID AS gerente_id, u.nome_completo AS gerente_nome, u.email AS gerente_email " +
                "FROM projetos p " +
                "JOIN usuarios u ON p.gerente_responsavel_id = u.ID";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                projetos.add(mapearResultSetParaProjeto(rs));
            }
        }
        return projetos;
    }

    public Projeto buscarProjetoPorId(int id) throws SQLException {
        Projeto projeto = null;
        String sql = "SELECT p.*, " +
                "u.ID AS gerente_id, u.nome_completo AS gerente_nome, u.email AS gerente_email " +
                "FROM projetos p JOIN usuarios u ON p.gerente_responsavel_id = u.ID WHERE p.id = ?";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    projeto = mapearResultSetParaProjeto(rs);
                }
            }
        }
        return projeto;
    }

    public void atualizarProjeto(Projeto projeto) throws SQLException {
        if (projeto == null || projeto.getDataInicioPrevista() == null || projeto.getDataTerminoPrevista() == null || projeto.getStatus() == null || projeto.getGerenteResponsavel() == null) {
            throw new IllegalArgumentException("Projeto ou seus campos essenciais (datas, status, gerente) não podem ser nulos.");
        }

        String sql = "UPDATE projetos SET nome = ?, descricao = ?, data_inicio_prevista = ?, data_termino_prevista = ?, " +
                "status = ?, gerente_responsavel_id = ?, data_inicio_real = ?, data_termino_real = ? WHERE id = ?";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, java.sql.Date.valueOf(projeto.getDataInicioPrevista()));
            stmt.setDate(4, java.sql.Date.valueOf(projeto.getDataTerminoPrevista()));
            stmt.setString(5, projeto.getStatus().name());
            stmt.setInt(6, projeto.getGerenteResponsavel().getId());

            if (projeto.getDataInicioReal() != null) {
                stmt.setDate(7, java.sql.Date.valueOf(projeto.getDataInicioReal()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (projeto.getDataTerminoReal() != null) {
                stmt.setDate(8, java.sql.Date.valueOf(projeto.getDataTerminoReal()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            stmt.setInt(9, projeto.getId());
            stmt.executeUpdate();
            System.out.println("Projeto atualizado com sucesso!");
        }
    }

    private Projeto mapearResultSetParaProjeto(ResultSet rs) throws SQLException {
        Projeto projeto = new Projeto();
        projeto.setId(rs.getInt("id"));
        projeto.setNome(rs.getString("nome"));
        projeto.setDescricao(rs.getString("descricao"));

        java.sql.Date dataInicioPrevistaSql = rs.getDate("data_inicio_prevista");
        if (dataInicioPrevistaSql != null) {
            projeto.setDataInicioPrevista(dataInicioPrevistaSql.toLocalDate());
        }

        java.sql.Date dataTerminoPrevistaSql = rs.getDate("data_termino_prevista");
        if (dataTerminoPrevistaSql != null) {
            projeto.setDataTerminoPrevista(dataTerminoPrevistaSql.toLocalDate());
        }

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                projeto.setStatus(StatusProjeto.valueOf(statusStr));
            } catch (IllegalArgumentException e) {
                System.err.println("AVISO: Status inválido ('" + statusStr + "') encontrado no banco para o projeto ID: " + projeto.getId());
            }
        }

        java.sql.Date dataInicioRealSql = rs.getDate("data_inicio_real");
        if (dataInicioRealSql != null) {
            projeto.setDataInicioReal(dataInicioRealSql.toLocalDate());
        }

        java.sql.Date dataFinalRealSql = rs.getDate("data_termino_real");
        if (dataFinalRealSql != null) {
            projeto.setDataTerminoReal(dataFinalRealSql.toLocalDate());
        }


        Usuario gerente = new Usuario();
        gerente.setId(rs.getInt("gerente_id"));
        gerente.setNomeCompleto(rs.getString("gerente_nome"));
        gerente.setEmail(rs.getString("gerente_email"));
        projeto.setGerenteResponsavel(gerente);

        return projeto;
    }
}