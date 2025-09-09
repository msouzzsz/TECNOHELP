package gui;


import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    public static void createAndShowGUI() {

        JFrame frame = new JFrame("TECNOHELP - Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 800);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(5, 1, 0, 10));

        JButton btnCadastrar_Usuarios = new JButton("Cadastrar Usuários");
        JButton btnExibir_Usuarios = new JButton("Exibir Usuários");
        JButton btnCadastrar_Projetos = new JButton("Cadastrar Projetos");
        JButton btnExibir_Projetos = new JButton("Exibir Projetos");
        JButton btnCadastrar_Equipes = new JButton("Cadastrar Equipes");
        JButton btnExibir_Equipes = new JButton("Exibir Equipes");
        JButton btnCadastrar_Tarefas = new JButton("Cadastrar Tarefas");
        JButton btnExibir_Tarefas = new JButton("Exibir Tarefas");
        JButton btnAlocacao = new JButton("Alocação de Equipes a Tarefas");
        JButton btnRelatorios = new JButton("Relatórios");


        btnCadastrar_Usuarios.addActionListener(e -> {
            CadastroUsuarioGUI cadastroFrame = new CadastroUsuarioGUI();
            cadastroFrame.setVisible(true);
        });

        btnCadastrar_Projetos.addActionListener(e -> {
            CadastroProjetoGUI cadastroFrame = new CadastroProjetoGUI();
            cadastroFrame.setVisible(true);
        });

        btnExibir_Usuarios.addActionListener(e -> {
            ExibicaoUsuariosGUI cadastroFrame = new ExibicaoUsuariosGUI();
            cadastroFrame.setVisible(true);
        });

        btnCadastrar_Equipes.addActionListener(e -> {
            EquipesGUI cadastroFrame = new EquipesGUI();
            cadastroFrame.setVisible(true);
        });

        btnCadastrar_Tarefas.addActionListener(e -> {
            CadastroTarefaGUI cadastroFrame = new CadastroTarefaGUI();
            cadastroFrame.setVisible(true);
        });

        btnAlocacao.addActionListener(e -> {
            AlocacaoEquipeProjetoGUI cadastroFrame = new AlocacaoEquipeProjetoGUI();
            cadastroFrame.setVisible(true);
        });

        btnRelatorios.addActionListener(e -> {
            RelatorioGUI cadastroFrame = new RelatorioGUI();
            cadastroFrame.setVisible(true);
        });

        btnExibir_Projetos.addActionListener(e -> {
            ExibirProjetosGUI cadastroFrame = new ExibirProjetosGUI();
            cadastroFrame.setVisible(true);
        });

        btnExibir_Equipes.addActionListener(e -> {
            ExibirEquipesGUI cadastroFrame = new ExibirEquipesGUI();
            cadastroFrame.setVisible(true);
        });

        btnExibir_Tarefas.addActionListener(e -> {
            ExibirTarefasGUI cadastroFrame = new ExibirTarefasGUI();
            cadastroFrame.setVisible(true);
        });

        panel.add(btnCadastrar_Usuarios);
        panel.add(btnExibir_Usuarios);
        panel.add(btnCadastrar_Projetos);
        panel.add(btnExibir_Projetos);
        panel.add(btnCadastrar_Equipes);
        panel.add(btnExibir_Equipes);
        panel.add(btnCadastrar_Tarefas);
        panel.add(btnExibir_Tarefas);
        panel.add(btnAlocacao);
        panel.add(btnRelatorios);

        frame.add(panel);

        frame.setVisible(true);
    }
}