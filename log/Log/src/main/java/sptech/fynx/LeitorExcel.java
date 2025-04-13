package sptech.fynx;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sptech.fynx.dao.DadosDAO;
import sptech.fynx.dao.DadosModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeitorExcel {

    public void lerPlanilha(String arquivoExcel, DadosDAO dadosDAO) {
        FileInputStream fis = null;
        Workbook workbook = null;
        Connection conn = dadosDAO.getConexao(); // Recupera a conexão para dar commit
        int contador = 0;

        try {
            conn.setAutoCommit(false); // Desativa o autocommit para controle manual

            fis = new FileInputStream(arquivoExcel);
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // Loop para processar cada linha da planilha
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Ignora a primeira linha (cabeçalho)

                DadosModel dados = new DadosModel();

                // Preenche os dados a partir das células da planilha
                dados.setDataContratacao(getDateCellValue(row.getCell(0)));
                dados.setValorOperacao(getNumericCellValue(row.getCell(1)));
                dados.setValorDesenbolsado(getNumericCellValue(row.getCell(2)));
                dados.setFonteRecurso(getStringCellValue(row.getCell(3)));
                dados.setCustoFinanceiro(getStringCellValue(row.getCell(4)));
                dados.setJuros(getFloatCellValue(row.getCell(5)));
                dados.setPrazoCarencia(getIntCellValue(row.getCell(6)));
                dados.setPrazoAmortizacao(getIntCellValue(row.getCell(7)));
                dados.setProduto(getStringCellValue(row.getCell(8)));
                dados.setSetorCnae(getStringCellValue(row.getCell(9)));
                dados.setSubsetorCnae(getStringCellValue(row.getCell(10)));
                dados.setCnae(getStringCellValue(row.getCell(11)));
                dados.setSituacaoOperacao(getStringCellValue(row.getCell(12)));

                // Insere os dados no banco
                dadosDAO.inserirDados(dados);
                contador++;

                // Realiza o commit a cada 1000 linhas
                if (contador % 1000 == 0) {
                    conn.commit(); // Confirma as transações após 1000 inserções
                    System.out.println("Commit realizado em " + contador + " linhas");
                }
            }

            // Commit final após processar todas as linhas
            conn.commit();
            System.out.println("Commit final após " + contador + " linhas");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Realiza rollback em caso de erro
                    System.err.println("Rollback executado por erro.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (workbook != null) workbook.close();
                if (fis != null) fis.close();
                if (conn != null) conn.setAutoCommit(true); // Restaura o autocommit para a conexão
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para obter o valor de células do tipo String
    private String getStringCellValue(Cell cell) {
        return (cell != null && cell.getCellType() == CellType.STRING) ? cell.getStringCellValue() : "";
    }

    // Método para obter o valor numérico das células
    private double getNumericCellValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return Double.parseDouble(cell.getStringCellValue().replaceAll(",", "."));
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }
        return 0.0;
    }

    // Método para obter o valor flutuante das células
    private float getFloatCellValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (float) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return Float.parseFloat(cell.getStringCellValue().replaceAll(",", "."));
                } catch (NumberFormatException e) {
                    return 0.0f;
                }
            }
        }
        return 0.0f;
    }

    // Método para obter o valor inteiro das células
    private int getIntCellValue(Cell cell) {
        return (cell != null && cell.getCellType() == CellType.NUMERIC) ? (int) cell.getNumericCellValue() : 0;
    }

    // Método para obter o valor de data das células
    private java.sql.Date getDateCellValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING) {
                String dateStr = cell.getStringCellValue();
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parsedDate = format.parse(dateStr);
                    return new java.sql.Date(parsedDate.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return new java.sql.Date(cell.getDateCellValue().getTime());
            }
        }
        return null;
    }
}
