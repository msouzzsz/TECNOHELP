package gui;

import DAO.EquipeDAO;
import DAO.UsuarioDAO;
import model.Equipe;
import model.Usuario;


import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EquipesGUI extends JFrame {

    private final JTextField nomeEquipeField;
    private final JTextArea descricaoArea;
    private final JList<Usuario> membrosList;
    private final DefaultListModel<Usuario> membrosListModel;

    private final EquipeDAO equipesDAO;
    private final UsuarioDAO usuarioDAO;

    public EquipesGUI() {

        this.equipesDAO = new EquipeDAO();
        this.usuarioDAO = new UsuarioDAO();

        setTitle("Cadastro de Equipes");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome da Equipe:",SwingConstants.CENTER), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        nomeEquipeField = new JTextField(25);
        panel.add(nomeEquipeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Descrição:",SwingConstants.CENTER), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.5;
        descricaoArea = new JTextArea(5, 25);
        panel.add(new JScrollPane(descricaoArea), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Membros:",SwingConstants.CENTER), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        membrosListModel = new DefaultListModel<>();
        membrosList = new JList<>(membrosListModel);
        membrosList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panel.add(new JScrollPane(membrosList), gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.weighty = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JButton salvarButton = new JButton("Salvar Equipe");
        panel.add(salvarButton, gbc);

        salvarButton.addActionListener(e -> salvarEquipe());

        add(panel);
        carregarListaDeUsuarios();
    }

    private void carregarListaDeUsuarios() {
        try {
            List<Usuario> usuariosDisponiveis = usuarioDAO.listarTodos();
            membrosListModel.clear();
            for (Usuario u : usuariosDisponiveis) {
                membrosListModel.addElement(u);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Não foi possível carregar a lista de usuários.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void salvarEquipe() {

        String nomeEquipe = nomeEquipeField.getText().trim();
        String descricao = descricaoArea.getText().trim();
        List<Usuario> membrosSelecionados = membrosList.getSelectedValuesList();

        if (nomeEquipe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da equipe é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Equipe novaEquipe = new Equipe();
        novaEquipe.setNomeEquipe(nomeEquipe);
        novaEquipe.setDescricao(descricao);
        novaEquipe.setMembros(membrosSelecionados);

        try {
            EquipeDAO dao = new EquipeDAO();
            dao.salvarEquipeComMembros(novaEquipe);

            JOptionPane.showMessageDialog(this, "Equipe '" + nomeEquipe + "' salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            limparCampos();     // Limpa os campos após o sucesso
            this.dispose();     // Fecha a janela após o sucesso

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this, "Erro ao salvar a equipe no banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Mostra o erro detalhado no console para o desenvolvedor
        }
    }

    private void limparCampos() {
        nomeEquipeField.setText("");
        descricaoArea.setText("");
        membrosList.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            EquipesGUI tela = new EquipesGUI();
            tela.setVisible(true);
        });
    }
}