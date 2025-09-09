package gui;

import DAO.RelatorioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class RelatorioGUI extends JFrame {

    private RelatorioDAO relatorioDAO;
    private JTabbedPane tabbedPane;

    public RelatorioGUI() {
        super("Relatórios e Dashboard");
        relatorioDAO = new RelatorioDAO();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        JPanel panelProjetosEmAndamento = criarPainelProjetosEmAndamento();
        JPanel panelDesempenhoUsuarios = criarPainelDesempenhoUsuarios();
        JPanel panelProjetosComAtraso = criarPainelProjetosComAtraso();

        tabbedPane.addTab("Projetos em Andamento", panelProjetosEmAndamento);
        tabbedPane.addTab("Desempenho de Usuários", panelDesempenhoUsuarios);
        tabbedPane.addTab("Projetos com Atraso", panelProjetosComAtraso);

        add(tabbedPane);
    }

    private JPanel criarPainelProjetosEmAndamento() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Projetos em Andamento", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome do Projeto", "Descrição"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        List<Map<String, Object>> projetos = relatorioDAO.getProjetosEmAndamento();
        if (projetos != null) {
            for (Map<String, Object> projeto : projetos) {
                model.addRow(new Object[]{
                        projeto.get("id"),
                        projeto.get("nome"),
                        projeto.get("descricao")
                });
            }
        }
        return panel;
    }

    private JPanel criarPainelDesempenhoUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Desempenho de Usuários", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"Usuário", "Total de Tarefas Atribuídas", "Tarefas Concluídas"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        List<Map<String, Object>> desempenho = relatorioDAO.getDesempenhoUsuarios();
        if (desempenho != null) {
            for (Map<String, Object> usuario : desempenho) {
                model.addRow(new Object[]{
                        usuario.get("nome"),
                        usuario.get("total_tarefas"),
                        usuario.get("tarefas_concluidas")
                });
            }
        }
        return panel;
    }

    private JPanel criarPainelProjetosComAtraso() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Projetos com Risco de Atraso", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome do Projeto", "Início Previsto", "Fim Previsto"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        List<Map<String, Object>> projetosAtraso = relatorioDAO.getProjetosComAtraso();
        if (projetosAtraso != null) {
            for (Map<String, Object> projeto : projetosAtraso) {
                model.addRow(new Object[]{
                        projeto.get("id"),
                        projeto.get("nome"),
                        projeto.get("data_inicio_prevista"),
                        projeto.get("data_fim_prevista")
                });
            }
        }
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RelatorioGUI().setVisible(true));
    }
}
