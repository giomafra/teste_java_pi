package sptech.fynx.dao;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class ConexaoBD {

    private DataSource conexaoBD;

    public ConexaoBD() {
        DriverManagerDataSource driver = new DriverManagerDataSource();

        String host = "localhost";  // Host fixo
        String port = System.getenv("DB_PORT");
        String database = System.getenv("DB_DATABASE");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (host == null || port == null || database == null || user == null || password == null) {
            throw new IllegalArgumentException("Alguma variável de ambiente do banco de dados não está definida");
        }

        String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8",
                host, port, database
        );

        driver.setUrl(url);
        driver.setUsername(user);
        driver.setPassword(password);
        driver.setDriverClassName("com.mysql.cj.jdbc.Driver");

        this.conexaoBD = driver;
    }

    public DataSource getConexaoBD() {
        return this.conexaoBD;
    }
}
