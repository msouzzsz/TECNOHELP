package gui;

import DAO.AlocacaoEquipeProjetoDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class AlocacaoEquipeProjetoGUI extends JFrame {

    private JLabel labelProjeto, labelEquipe, labelStatus;
    private JComboBox<String> cmbProjetos, cmbEquipes;
    private JButton btnSalvar;

    private AlocacaoEquipeProjetoDAO alocacaoDAO;

    public AlocacaoEquipeProjetoGUI() {
        super("Alocação de Equipes a Projetos");

        alocacaoDAO = new AlocacaoEquipeProjetoDAO();

        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 5, 5));

        labelProjeto = new JLabel("Selecione um Projeto:",SwingConstants.CENTER);
        cmbProjetos = new JComboBox<>();

        labelEquipe = new JLabel("Selecione uma Equipe:",SwingConstants.CENTER);
        cmbEquipes = new JComboBox<>();

        labelStatus = new JLabel("Aguardando...", SwingConstants.CENTER);

        btnSalvar = new JButton("Salvar Alocação");

        carregarProjetos();
        carregarEquipes();

        panelForm.add(labelProjeto);
        panelForm.add(cmbProjetos);
        panelForm.add(labelEquipe);
        panelForm.add(cmbEquipes);

        add(labelStatus, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvarAlocacao());
    }

    private void carregarProjetos() {
        List<String> projetos = alocacaoDAO.buscarProjetos();
        if (projetos != null && !projetos.isEmpty()) {
            for (String projeto : projetos) {
                cmbProjetos.addItem(projeto);
            }
        } else {
            labelStatus.setText("Erro: Nenhum projeto encontrado no banco de dados.");
            labelStatus.setForeground(Color.RED);
            System.err.println("Nenhum projeto encontrado. O ComboBox de projetos estará vazio.");
        }
    }

    private void carregarEquipes() {
        List<String> equipes = alocacaoDAO.buscarEquipes();
        if (equipes != null && !equipes.isEmpty()) {
            for (String equipe : equipes) {
                cmbEquipes.addItem(equipe);
            }
        } else {
            labelStatus.setText("Erro: Nenhuma equipe encontrada no banco de dados.");
            labelStatus.setForeground(Color.RED);
            System.err.println("Nenhuma equipe encontrada. O ComboBox de equipes estará vazio.");
        }
    }

    private void salvarAlocacao() {

        if (cmbProjetos.getSelectedItem() == null || cmbEquipes.getSelectedItem() == null) {
            labelStatus.setText("Selecione um projeto e uma equipe!");
            labelStatus.setForeground(Color.RED);
            return;
        }

        String projetoSelecionado = (String) cmbProjetos.getSelectedItem();
        String equipeSelecionada = (String) cmbEquipes.getSelectedItem();


        int idProjeto = alocacaoDAO.buscarProjetoIdPorNome(projetoSelecionado);
        int idEquipe = alocacaoDAO.buscarEquipeIdPorNome(equipeSelecionada);


        if (idProjeto == -1 || idEquipe == -1) {
            labelStatus.setText("Erro ao buscar IDs. Verifique se os dados existem.");
            labelStatus.setForeground(Color.RED);
            return;
        }


        if (alocacaoDAO.salvarAlocacao(idEquipe, idProjeto)) {
            labelStatus.setText("Alocação salva com sucesso!");
            labelStatus.setForeground(Color.GREEN);
        } else {
            labelStatus.setText("Falha ao salvar a alocação. Verifique se ela já existe.");
            labelStatus.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AlocacaoEquipeProjetoGUI().setVisible(true);
        });
    }
}
