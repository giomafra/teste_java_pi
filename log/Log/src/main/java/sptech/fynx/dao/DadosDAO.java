package sptech.fynx.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DadosDAO {

    private final JdbcTemplate jdbcTemplate;
    private final Connection conexao;

    public DadosDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        Connection tempConexao = null;
        try {
            tempConexao = jdbcTemplate.getDataSource().getConnection(); // Obtém a conexão do DataSource
            criarTabela();
        } catch (SQLException e) {
            e.printStackTrace();  // Tratamento de exceção
            // Se desejar, pode lançar uma exceção personalizada ou fazer algum tratamento adequado aqui
        } finally {
            this.conexao = tempConexao;  // Atribui a conexão (null caso falhe)
        }
    }

    private void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS dados (
                idDados INT PRIMARY KEY AUTO_INCREMENT,
                data_contratacao DATE,
                valor_operacao DOUBLE,
                valor_desenbolsado DOUBLE,
                fonte_recurso VARCHAR(50),
                custo_financeiro VARCHAR(50),
                juros FLOAT,
                prazo_carencia INT,
                prazo_amortizacao INT,
                produto VARCHAR(50),
                setor_cnae VARCHAR(50),
                subsetor_cnae VARCHAR(50),
                cnae CHAR(7),
                situacao_operacao VARCHAR(50)
            )
        """;

        jdbcTemplate.execute(sql);
    }

    public void inserirDados(DadosModel dados) {
        String sql = """
            INSERT INTO dados 
            (data_contratacao, valor_operacao, valor_desenbolsado, fonte_recurso, custo_financeiro, juros, 
            prazo_carencia, prazo_amortizacao, produto, setor_cnae, subsetor_cnae, cnae, situacao_operacao) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                dados.getDataContratacao(),
                dados.getValorOperacao(),
                dados.getValorDesenbolsado(),
                dados.getFonteRecurso(),
                dados.getCustoFinanceiro(),
                dados.getJuros(),
                dados.getPrazoCarencia(),
                dados.getPrazoAmortizacao(),
                dados.getProduto(),
                dados.getSetorCnae(),
                dados.getSubsetorCnae(),
                dados.getCnae(),
                dados.getSituacaoOperacao()
        );
    }

    public Connection getConexao() {
        return conexao;
    }
}
