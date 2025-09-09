package gui;

import DAO.ProjetoDAO;
import model.Projeto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;


public class ExibirProjetosGUI extends JFrame {

    private JTable tabelaProjetos;
    private DefaultTableModel tableModel;
    private ProjetoDAO projetoDAO;

    public ExibirProjetosGUI() {
        setTitle("Consulta de Projetos Cadastrados");

        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        projetoDAO = new ProjetoDAO();

        setLayout(new BorderLayout(10, 10));

        String[] colunas = {
                "ID", "Nome do Projeto", "Gerente Responsável", "Status",
                "Início Previsto", "Término Previsto", "Início Real", "Final Real"
        };


        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        tabelaProjetos = new JTable(tableModel);
        tabelaProjetos.setFillsViewportHeight(true);
        tabelaProjetos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaProjetos.setRowHeight(25);
        tabelaProjetos.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tabelaProjetos);

        add(scrollPane, BorderLayout.CENTER);

        carregarProjetosNaTabela();
    }

    private void carregarProjetosNaTabela() {

        tableModel.setRowCount(0);

        try {

            List<Projeto> projetos = projetoDAO.buscarTodosProjetos();

            for (Projeto projeto : projetos) {
                tableModel.addRow(new Object[]{
                        projeto.getId(),
                        projeto.getNome(),
                        projeto.getGerenteResponsavel().getNomeCompleto(),
                        projeto.getStatus(),
                        projeto.getDataInicioPrevista(),
                        projeto.getDataTerminoPrevista(),
                        projeto.getDataInicioReal(),
                        projeto.getDataTerminoReal()
                });
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar projetos do banco de dados: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExibirProjetosGUI().setVisible(true);
        });
    }
}
