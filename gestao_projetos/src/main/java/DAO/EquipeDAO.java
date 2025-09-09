package DAO;

import Utilidade.ConexaoBancoDeDados;
import model.Equipe;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    public void salvarEquipeComMembros(Equipe equipe) throws SQLException {

        final String SQL_INSERT_EQUIPE = "INSERT INTO equipes (NomeEquipe, descricao) VALUES (?, ?)";
        final String SQL_INSERT_MEMBRO = "INSERT INTO equipe_membros (id_equipe, id_usuario) VALUES (?, ?)";

        Connection conn = null;

        try {
            conn = ConexaoBancoDeDados.getConnection();
            conn.setAutoCommit(false);
            int idNovaEquipe = 0;

            try (PreparedStatement stmtEquipe = conn.prepareStatement(SQL_INSERT_EQUIPE, Statement.RETURN_GENERATED_KEYS)) {
                stmtEquipe.setString(1, equipe.getNomeEquipe());
                stmtEquipe.setString(2, equipe.getDescricao());

                int linhasAfetadas = stmtEquipe.executeUpdate();
                if (linhasAfetadas == 0) {
                    throw new SQLException("A criação da equipe falhou, nenhuma linha foi afetada.");
                }

                try (ResultSet generatedKeys = stmtEquipe.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idNovaEquipe = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("A criação da equipe falhou, não foi possível obter o ID.");
                    }
                }
            }

            if (equipe.getMembros() != null && !equipe.getMembros().isEmpty()) {
                try (PreparedStatement stmtMembro = conn.prepareStatement(SQL_INSERT_MEMBRO)) {
                    for (Usuario membro : equipe.getMembros()) {
                        stmtMembro.setInt(1, idNovaEquipe);
                        stmtMembro.setInt(2, membro.getId());
                        stmtMembro.addBatch();
                    }
                    stmtMembro.executeBatch();
                }
            }

            conn.commit();
            System.out.println("SUCESSO: Equipe e membros salvos com sucesso!");

        } catch (SQLException e) {
            System.err.println("ERRO: Ocorreu um erro ao salvar a equipe. Fazendo rollback...");
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("ERRO CRÍTICO: Falha ao tentar reverter a transação.");
                    ex.printStackTrace();
                }
            }
            throw e;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public List<Equipe> buscarTodasEquipes() throws SQLException {

        List<Equipe> equipes = new ArrayList<>();

        String sql = "SELECT id, NomeEquipe, descricao FROM equipes ORDER BY NomeEquipe";


        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {


            while (rs.next()) {

                Equipe equipe = new Equipe();

                equipe.setIdEquipe(rs.getInt("id"));
                equipe.setNomeEquipe(rs.getString("NomeEquipe"));
                equipe.setDescricao(rs.getString("descricao"));

                equipes.add(equipe);
            }
        }
        // 6. Retorna a lista completa de equipes
        return equipes;
    }
}