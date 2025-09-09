package gui;

import DAO.UsuarioDAO;
import model.Usuario;
import model.Perfil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CadastroUsuarioGUI extends JFrame {

    private JTextField txtNomeCompleto;
    private JTextField txtCpf;
    private JTextField txtEmail;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<Perfil> comboPerfil;
    private JButton btnSalvar;

    public CadastroUsuarioGUI() {
        setTitle("Cadastro de Usuário");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));

        add(new JLabel("Nome Completo:",SwingConstants.CENTER));
        txtNomeCompleto = new JTextField();
        add(txtNomeCompleto);

        add(new JLabel("CPF:",SwingConstants.CENTER));
        txtCpf = new JTextField();
        add(txtCpf);

        add(new JLabel("Email:",SwingConstants.CENTER));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Login:",SwingConstants.CENTER));
        txtLogin = new JTextField();
        add(txtLogin);

        add(new JLabel("Senha:",SwingConstants.CENTER));
        txtSenha = new JPasswordField();
        add(txtSenha);

        add(new JLabel("Perfil:",SwingConstants.CENTER));
        comboPerfil = new JComboBox<>(Perfil.values());
        add(comboPerfil);

        btnSalvar = new JButton("Salvar");
        add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarUsuario();
            }
        });

        setVisible(true);
    }

    private void salvarUsuario() {
        try {

            Usuario novoUsuario = new Usuario(
                    txtNomeCompleto.getText(),
                    txtCpf.getText(),
                    txtEmail.getText(),
                    txtLogin.getText(),
                    new String(txtSenha.getPassword()),
                    (Perfil) comboPerfil.getSelectedItem());

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.salvarUsuario(novoUsuario);

            JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");

            txtNomeCompleto.setText("");
            txtCpf.setText("");
            txtEmail.setText("");
            txtLogin.setText("");
            txtSenha.setText("");
            comboPerfil.setSelectedIndex(0);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CadastroUsuarioGUI();
            }
        });
    }
}
