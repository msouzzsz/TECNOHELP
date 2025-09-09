package Utilidade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBancoDeDados {
    // Informações de conexão (podem ser estáticas também)
    private static final String URL = "jdbc:mysql://localhost:3306/gestao_projetos";
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}