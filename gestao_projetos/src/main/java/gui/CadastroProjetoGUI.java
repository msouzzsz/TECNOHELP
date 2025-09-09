package gui;

import DAO.ProjetoDAO;
import DAO.UsuarioDAO;
import model.Projeto;
import model.StatusProjeto;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CadastroProjetoGUI extends JFrame {


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private JTextField txtNome;
    private JTextArea txtDescricao;
    private JTextField txtDataInicioPrevista;
    private JTextField txtDataInicioReal;
    private JTextField txtDataTerminoPrevista;
    private JTextField txtDataTerminoReal;
    private JComboBox<StatusProjeto> comboStatus;
    private JComboBox<Usuario> comboGerente;
    private JButton btnSalvar;


    private Projeto projetoAtual;

    public CadastroProjetoGUI() {
        this(null);
    }

    public CadastroProjetoGUI(Projeto projetoParaEditar) {
        this.projetoAtual = projetoParaEditar;

        setTitle(projetoAtual == null ? "Cadastro de Novo Projeto" : "Editando Projeto: " + projetoAtual.getNome());
        setSize(550, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nome:", SwingConstants.CENTER));
        txtNome = new JTextField();
        panel.add(txtNome);

        panel.add(new JLabel("Descrição:" ,SwingConstants.CENTER));
        txtDescricao = new JTextArea(3, 20);
        panel.add(new JScrollPane(txtDescricao));

        panel.add(new JLabel("Data de Início Prevista (AAAA-MM-DD):",SwingConstants.CENTER));
        txtDataInicioPrevista = new JTextField();
        panel.add(txtDataInicioPrevista);

        panel.add(new JLabel("Data de Início Real (AAAA-MM-DD):",SwingConstants.CENTER));
        txtDataInicioReal= new JTextField();
        panel.add(txtDataInicioReal);

        panel.add(new JLabel("Data de Término Prevista (AAAA-MM-DD):",SwingConstants.CENTER));
        txtDataTerminoPrevista = new JTextField();
        panel.add(txtDataTerminoPrevista);

        panel.add(new JLabel("Data de Término Real (AAAA-MM-DD):",SwingConstants.CENTER));
        txtDataTerminoReal = new JTextField();
        panel.add(txtDataTerminoReal);


        panel.add(new JLabel("Status:",SwingConstants.CENTER));
        comboStatus = new JComboBox<>(StatusProjeto.values());
        panel.add(comboStatus);

        panel.add(new JLabel("Gerente Responsável:",SwingConstants.CENTER));
        comboGerente = new JComboBox<>();
        panel.add(comboGerente);

        panel.add(new JLabel(""));
        btnSalvar = new JButton("Salvar");
        panel.add(btnSalvar);

        add(panel);

        carregarGerentes();

        if (projetoAtual != null) {
            carregarDadosDoProjeto();
        }

        btnSalvar.addActionListener(e -> salvarProjeto());

        setVisible(true);
    }

    private void carregarDadosDoProjeto() {
        txtNome.setText(projetoAtual.getNome());
        txtDescricao.setText(projetoAtual.getDescricao());
        // Usa o método correto getDataInicioPrevista()
        txtDataInicioPrevista.setText(projetoAtual.getDataInicioPrevista().format(DATE_FORMATTER)); // <-- CORRIGIDO
        txtDataTerminoPrevista.setText(projetoAtual.getDataTerminoPrevista().format(DATE_FORMATTER));
        comboStatus.setSelectedItem(projetoAtual.getStatus());

        for (int i = 0; i < comboGerente.getItemCount(); i++) {
            if (comboGerente.getItemAt(i).getId() == projetoAtual.getGerenteResponsavel().getId()) {
                comboGerente.setSelectedIndex(i);
                break;
            }
        }

        if (projetoAtual.getDataInicioReal() != null) {
            txtDataInicioReal.setText(projetoAtual.getDataInicioReal().format(DATE_FORMATTER));
        }

        if (projetoAtual.getDataTerminoReal() != null) { // <-- CORRIGIDO
            txtDataTerminoReal.setText(projetoAtual.getDataTerminoReal().format(DATE_FORMATTER)); // <-- CORRIGIDO
        }
    }

    private void carregarGerentes() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> gerentes = usuarioDAO.buscarGerentes();
            for (Usuario gerente : gerentes) {
                comboGerente.addItem(gerente);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar gerentes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void salvarProjeto() {
        try {
            if (comboGerente.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione um gerente responsável.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StatusProjeto novoStatus = (StatusProjeto) comboStatus.getSelectedItem();
            Projeto projetoParaSalvar = (projetoAtual == null) ? new Projeto() : projetoAtual;

            projetoParaSalvar.setNome(txtNome.getText());
            projetoParaSalvar.setDescricao(txtDescricao.getText());
            projetoParaSalvar.setDataInicioPrevista(LocalDate.parse(txtDataInicioPrevista.getText(), DATE_FORMATTER)); // <-- CORRIGIDO
            projetoParaSalvar.setDataTerminoPrevista(LocalDate.parse(txtDataTerminoPrevista.getText(), DATE_FORMATTER));
            projetoParaSalvar.setGerenteResponsavel((Usuario) comboGerente.getSelectedItem());

            if (novoStatus == StatusProjeto.EM_ANDAMENTO && projetoParaSalvar.getDataInicioReal() == null) {
                projetoParaSalvar.setDataInicioReal(LocalDate.now());
            }

            if (novoStatus == StatusProjeto.CONCLUIDO && projetoParaSalvar.getDataTerminoReal() == null) {
                projetoParaSalvar.setDataTerminoReal(LocalDate.now());
            }

            projetoParaSalvar.setStatus(novoStatus);

            ProjetoDAO dao = new ProjetoDAO();
            if (projetoParaSalvar.getId() == 0) {
                dao.salvarProjeto(projetoParaSalvar);
                JOptionPane.showMessageDialog(this, "Projeto salvo com sucesso!");
            } else {
                dao.atualizarProjeto(projetoParaSalvar);
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!");
            }

            this.dispose();

        } catch (SQLException | java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar projeto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}