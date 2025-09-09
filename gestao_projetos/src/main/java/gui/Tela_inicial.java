package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class Tela_inicial {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Gesrenciador de Projetos TECNOHELP");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 800);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 1));

            JLabel label = new JLabel("Bem-vindo ao sistema TECNOHELP");
            label.setHorizontalAlignment(JLabel.CENTER);

            JButton button = new JButton("Clique aqui para acessar o sistema");

            button.addActionListener(e -> {
                frame.setVisible(false);
                Menu.createAndShowGUI();
            });

            panel.add(label);
            panel.add(button);

            frame.add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}