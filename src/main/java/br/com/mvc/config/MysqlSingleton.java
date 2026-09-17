package br.com.mvc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlSingleton {
    private static final String URL = "jdbc:mysql://localhost:3306/tkweb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234"; 
    private static MysqlSingleton instance;
    private Connection conexao;

    private MysqlSingleton() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL nao encontrado no projeto.", e);
        }
    }

    public static synchronized MysqlSingleton getInstance() {
        if (instance == null) {
            instance = new MysqlSingleton();
        }
        return instance;
    }

    public Connection obterConexao() throws SQLException {
        if (this.conexao == null || this.conexao.isClosed()) {
            this.conexao = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return this.conexao;
    }

    public static Connection getConnection() throws SQLException {
        return getInstance().obterConexao();
    }
}