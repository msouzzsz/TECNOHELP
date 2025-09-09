package gui;

import DAO.CadastroTarefaDAO;
import model.Tarefa;
import model.StatusTarefa;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CadastroTarefaGUI extends JFrame {

    private JLabel labelTitulo, labelDescricao, labelProjeto, labelResponsavel,
            labelStatus, labelDataInicioPrevista, labelDataFimPrevista,
            labelDataInicioReal, labelDataFimReal, labelStatusMensagem;

    private JTextField txtTitulo, txtDataInicioPrevista, txtDataFimPrevista,
            txtDataInicioReal, txtDataFimReal;

    private JTextArea txtDescricao;
    private JComboBox<String> cmbProjeto, cmbResponsavel, cmbStatus;
    private JButton btnSalvar;

    private CadastroTarefaDAO tarefaDAO;

    public CadastroTarefaGUI() {
        super("Cadastro de Tarefa");

        tarefaDAO = new CadastroTarefaDAO();

        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        labelTitulo = new JLabel("Título:",SwingConstants.CENTER);
        txtTitulo = new JTextField(20);

        labelDescricao = new JLabel("Descrição:",SwingConstants.CENTER);
        txtDescricao = new JTextArea(4, 20);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);

        labelProjeto = new JLabel("Projeto Vinculado:",SwingConstants.CENTER);
        cmbProjeto = new JComboBox<>();

        labelResponsavel = new JLabel("Responsável:",SwingConstants.CENTER);
        cmbResponsavel = new JComboBox<>();

        labelStatus = new JLabel("Status:",SwingConstants.CENTER);
        cmbStatus = new JComboBox<>(new String[]{"PENDENTE", "EM_EXECUCAO", "CONCLUIDA"});

        labelDataInicioPrevista = new JLabel("Data de Início Prevista (AAAA-MM-DD):",SwingConstants.CENTER);
        txtDataInicioPrevista = new JTextField(10);

        labelDataFimPrevista = new JLabel("Data de Fim Prevista (AAAA-MM-DD):",SwingConstants.CENTER);
        txtDataFimPrevista = new JTextField(10);

        labelDataInicioReal = new JLabel("Data de Início Real (AAAA-MM-DD):",SwingConstants.CENTER);
        txtDataInicioReal = new JTextField(10);

        labelDataFimReal = new JLabel("Data de Fim Real (AAAA-MM-DD):",SwingConstants.CENTER);
        txtDataFimReal = new JTextField(10);

        btnSalvar = new JButton("Salvar Tarefa");
        labelStatusMensagem = new JLabel("Aguardando...", SwingConstants.CENTER);

        carregarProjetos();
        carregarUsuarios();

        panelForm.add(labelTitulo);
        panelForm.add(txtTitulo);
        panelForm.add(labelDescricao);
        panelForm.add(scrollDescricao);
        panelForm.add(labelProjeto);
        panelForm.add(cmbProjeto);
        panelForm.add(labelResponsavel);
        panelForm.add(cmbResponsavel);
        panelForm.add(labelStatus);
        panelForm.add(cmbStatus);
        panelForm.add(labelDataInicioPrevista);
        panelForm.add(txtDataInicioPrevista);
        panelForm.add(labelDataFimPrevista);
        panelForm.add(txtDataFimPrevista);
        panelForm.add(labelDataInicioReal);
        panelForm.add(txtDataInicioReal);
        panelForm.add(labelDataFimReal);
        panelForm.add(txtDataFimReal);


        panelBotoes.add(btnSalvar);

        add(labelStatusMensagem, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvarTarefa());
    }

    private void carregarProjetos() {
        List<String> projetos = tarefaDAO.buscarProjetos();
        if (projetos != null && !projetos.isEmpty()) {
            for (String projeto : projetos) {
                cmbProjeto.addItem(projeto);
            }
        } else {
            System.err.println("Nenhum projeto encontrado. O ComboBox estará vazio.");
            labelStatusMensagem.setText("Erro: Nenhum projeto encontrado no banco de dados.");
            labelStatusMensagem.setForeground(Color.RED);
        }
    }

    private void carregarUsuarios() {
        List<String> usuarios = tarefaDAO.buscarUsuarios();
        if (usuarios != null && !usuarios.isEmpty()) {
            for (String usuario : usuarios) {
                cmbResponsavel.addItem(usuario);
            }
        } else {
            System.err.println("Nenhum usuário encontrado. O ComboBox estará vazio.");
            labelStatusMensagem.setText("Erro: Nenhum usuário encontrado no banco de dados.");
            labelStatusMensagem.setForeground(Color.RED);
        }
    }

    private void salvarTarefa() {

        if (txtTitulo.getText().trim().isEmpty() ||
                cmbProjeto.getSelectedItem() == null ||
                cmbResponsavel.getSelectedItem() == null) {
            labelStatusMensagem.setText("Preencha todos os campos obrigatórios!");
            labelStatusMensagem.setForeground(Color.RED);
            return;
        }

        try {
            String tituloProjetoSelecionado = (String) cmbProjeto.getSelectedItem();
            String nomeUsuarioSelecionado = (String) cmbResponsavel.getSelectedItem();

            int projetoId = tarefaDAO.buscarProjetoIdPorNome(tituloProjetoSelecionado);
            int usuarioId = tarefaDAO.buscarUsuarioIdPorNome(nomeUsuarioSelecionado);


            if (projetoId == -1) {
                labelStatusMensagem.setText("Erro: Projeto selecionado não encontrado no banco de dados.");
                labelStatusMensagem.setForeground(Color.RED);
                return;
            }
            if (usuarioId == -1) {
                labelStatusMensagem.setText("Erro: Usuário selecionado não encontrado no banco de dados.");
                labelStatusMensagem.setForeground(Color.RED);
                return;
            }


            LocalDate dataInicioPrevista = null;
            if (!txtDataInicioPrevista.getText().trim().isEmpty()) {
                dataInicioPrevista = LocalDate.parse(txtDataInicioPrevista.getText());
            }
            LocalDate dataFimPrevista = null;
            if (!txtDataFimPrevista.getText().trim().isEmpty()) {
                dataFimPrevista = LocalDate.parse(txtDataFimPrevista.getText());
            }
            LocalDate dataInicioReal = null;
            if (!txtDataInicioReal.getText().trim().isEmpty()) {
                dataInicioReal = LocalDate.parse(txtDataInicioReal.getText());
            }
            LocalDate dataFimReal = null;
            if (!txtDataFimReal.getText().trim().isEmpty()) {
                dataFimReal = LocalDate.parse(txtDataFimReal.getText());
            }

            Tarefa novaTarefa = new Tarefa(txtTitulo.getText(), txtDescricao.getText(),
                    projetoId, usuarioId,
                    StatusTarefa.valueOf((String) cmbStatus.getSelectedItem()),
                    dataInicioPrevista, dataFimPrevista, dataInicioReal, dataFimReal);

            if (tarefaDAO.salvarTarefa(novaTarefa)) {
                labelStatusMensagem.setText("Tarefa salva com sucesso!");
                labelStatusMensagem.setForeground(Color.GREEN);
            } else {
                labelStatusMensagem.setText("Falha ao salvar a tarefa. Verifique a sua conexão com o banco e os dados!");
                labelStatusMensagem.setForeground(Color.RED);
            }

        } catch (DateTimeParseException ex) {
            labelStatusMensagem.setText("Erro: Verifique o formato das datas (AAAA-MM-DD).");
            labelStatusMensagem.setForeground(Color.RED);
            System.err.println("Erro na conversão de data:");
            ex.printStackTrace();
        } catch (Exception ex) {
            labelStatusMensagem.setText("Erro inesperado. Verifique o console para mais detalhes.");
            labelStatusMensagem.setForeground(Color.RED);
            System.err.println("Erro inesperado:");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CadastroTarefaGUI().setVisible(true);
        });
    }
}
