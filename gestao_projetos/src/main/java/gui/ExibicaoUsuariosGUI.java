package gui;

import DAO.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;


public class ExibicaoUsuariosGUI extends JFrame {

    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;
    private UsuarioDAO usuarioDAO;

    public ExibicaoUsuariosGUI() {
        setTitle("Consulta de Usuários do Sistema");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela

        usuarioDAO = new UsuarioDAO();


        setLayout(new BorderLayout(10, 10));


        String[] colunas = {"ID", "Nome Completo", "Email", "Perfil"};


        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        tabelaUsuarios = new JTable(tableModel);
        tabelaUsuarios.setFillsViewportHeight(true); // Faz a tabela ocupar toda a altura disponível
        tabelaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaUsuarios.setRowHeight(25);


        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);

        add(scrollPane, BorderLayout.CENTER);

        carregarUsuariosNaTabela();
    }


    private void carregarUsuariosNaTabela() {

        tableModel.setRowCount(0);

        try {

            List<Usuario> usuarios = usuarioDAO.listarTodos();

            for (Usuario usuario : usuarios) {
                tableModel.addRow(new Object[]{
                        usuario.getId(),
                        usuario.getNomeCompleto(),
                        usuario.getEmail(),
                        usuario.getPerfil()
                });
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar usuários do banco de dados: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new ExibicaoUsuariosGUI().setVisible(true);
        });
    }
}