package gui;

import DAO.CadastroTarefaDAO;
import model.Tarefa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ExibirTarefasGUI extends JFrame {

    private JTable tabelaTarefas;
    private DefaultTableModel tableModel;
    private CadastroTarefaDAO tarefaDAO;

    public ExibirTarefasGUI() {
        setTitle("Consulta de Tarefas");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tarefaDAO = new CadastroTarefaDAO();
        setLayout(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Título da Tarefa", "Projeto", "Responsável", "Status", "Início Previsto", "Fim Previsto"};

        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaTarefas = new JTable(tableModel);
        tabelaTarefas.setFillsViewportHeight(true);
        tabelaTarefas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaTarefas.setRowHeight(25);
        tabelaTarefas.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tabelaTarefas);
        add(scrollPane, BorderLayout.CENTER);

        carregarTarefasNaTabela();
    }

    private void carregarTarefasNaTabela() {
        tableModel.setRowCount(0);

        try {

            List<Tarefa> tarefas = tarefaDAO.buscarTodasTarefas();

            for (Tarefa tarefa : tarefas) {
                tableModel.addRow(new Object[]{
                        tarefa.getId(),
                        tarefa.getTitulo(),
                        tarefa.getNomeProjeto() != null ? tarefa.getNomeProjeto() : "N/A",
                        tarefa.getNomeResponsavel() != null ? tarefa.getNomeResponsavel() : "N/A",
                        tarefa.getStatus(),
                        tarefa.getDataInicioPrevista(),
                        tarefa.getDataFimPrevista()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar tarefas do banco de dados: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExibirTarefasGUI().setVisible(true);
        });
    }
}