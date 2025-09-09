package DAO;

import Utilidade.ConexaoBancoDeDados;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembrosEquipeDAO {

    public List<Usuario> buscarMembrosPorEquipeId(int idEquipe) throws SQLException {
        List<Usuario> membros = new ArrayList<>();
        String sql = "SELECT u.ID, u.nome_completo FROM usuarios u " +
                "JOIN equipe_membros em ON u.ID = em.id_membro " +
                "WHERE em.id_equipe = ?";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEquipe);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario membro = new Usuario();
                    membro.setId(rs.getInt("id"));
                    membro.setNomeCompleto(rs.getString("nome_completo"));
                    membros.add(membro);
                }
            }
        }
        return membros;
    }
}