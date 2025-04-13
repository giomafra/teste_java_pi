package sptech.fynx.dao;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class ConexaoBD {

    //DataSource --> interface pro H2 --> como vai se conctar com o banco atraves de um driver
    private DataSource conexaoBD;

    public ConexaoBD() {

        // DriverManagerDataSource --> vai ensinar o jdbc como se conectar com o banco
        DriverManagerDataSource driver = new DriverManagerDataSource();


        String host = "localhost"; // Substitua pelo endereço do seu servidor de banco de dados
        String port = "3306"; // Substitua pela porta do seu banco de dados (exemplo para MySQL)
        String database = "teste"; // Substitua pelo nome do seu banco de dados
        String user = "root"; // Substitua pelo seu usuário do banco de dados
        String password = "admin"; // Substitua pela senha do seu banco de dados

        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", host, port, database);

        driver.setUrl(url);
        driver.setUsername(user);
        driver.setPassword(password);
        driver.setDriverClassName("com.mysql.cj.jdbc.Driver");

;

        /* driver.setUsername("sa");
        driver.setPassword("");
        driver.setUrl("jdbc:h2:file:./banco-de-dados");
        driver.setDriverClassName("org.h2.Driver"); */

        this.conexaoBD = driver;
    }

    public DataSource getConexaoBD(){
        return this.conexaoBD;
    }
}
