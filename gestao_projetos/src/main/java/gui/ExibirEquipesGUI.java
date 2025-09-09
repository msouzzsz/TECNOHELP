package gui;

import DAO.EquipeDAO;
import DAO.MembrosEquipeDAO;
import model.Equipe;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ExibirEquipesGUI extends JFrame {

    private JTable tabelaEquipes;
    private JList<Usuario> listaMembros;
    private DefaultTableModel tableModel;
    private DefaultListModel<Usuario> listModel;

    private EquipeDAO equipeDAO;
    private MembrosEquipeDAO membrosEquipeDAO;

    public ExibirEquipesGUI() {
        setTitle("Consulta de Equipes e Membros");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        equipeDAO = new EquipeDAO();
        membrosEquipeDAO = new MembrosEquipeDAO();


        String[] colunas = {"ID", "Nome da Equipe", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaEquipes = new JTable(tableModel);
        tabelaEquipes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabela = new JScrollPane(tabelaEquipes);


        listModel = new DefaultListModel<>();
        listaMembros = new JList<>(listModel);
        listaMembros.setBorder(BorderFactory.createTitledBorder("Membros da Equipe Selecionada"));
        JScrollPane scrollLista = new JScrollPane(listaMembros);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabela, scrollLista);
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);


        tabelaEquipes.getSelectionModel().addListSelectionListener(event -> {

            if (!event.getValueIsAdjusting()) {
                int linhaSelecionada = tabelaEquipes.getSelectedRow();
                if (linhaSelecionada != -1) {

                    int idEquipe = (int) tableModel.getValueAt(linhaSelecionada, 0);
                    carregarMembrosDaEquipe(idEquipe);
                }
            }
        });


        carregarEquipesNaTabela();
    }

    private void carregarEquipesNaTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        try {
            List<Equipe> equipes = equipeDAO.buscarTodasEquipes();
            for (Equipe equipe : equipes) {
                tableModel.addRow(new Object[]{
                        equipe.getIdEquipe(),
                        equipe.getNomeEquipe(),
                        equipe.getDescricao()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar equipes: " + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void carregarMembrosDaEquipe(int idEquipe) {
        listModel.clear(); // Limpa a lista de membros
        try {
            List<Usuario> membros = membrosEquipeDAO.buscarMembrosPorEquipeId(idEquipe);
            if (membros.isEmpty()) {

            } else {
                for (Usuario membro : membros) {
                    listModel.addElement(membro);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar membros da equipe: " + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExibirEquipesGUI().setVisible(true);
        });
    }
}