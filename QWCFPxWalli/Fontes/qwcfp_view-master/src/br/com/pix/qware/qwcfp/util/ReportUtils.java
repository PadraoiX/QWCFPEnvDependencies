package br.com.pix.qware.qwcfp.util;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
 
/**
 * Classe com mtodos utilitrios para executar e abrir relatrios.
 *
 * @author David Buzatto
 */
public class ReportUtils {
 
    /**
     * Abre um relatrio usando uma conexo como datasource.
     *
     * @param titulo Ttulo usado na janela do relatrio.
     * @param inputStream InputStream que contm o relatrio.
     * @param parametros Parmetros utilizados pelo relatrio.
     * @param conexao Conexo utilizada para a execuo da query.
     * @throws JRException Caso ocorra algum problema na execuo do relatrio
     */
    public static void openReport( String titulo, InputStream inputStream, Map parametros,
            Connection conexao ) throws JRException {
 
        /*
         * Cria um JasperPrint, que  a verso preenchida do relatrio,
         * usando uma conexo.
         */
        JasperPrint print = JasperFillManager.fillReport(
                inputStream, parametros, conexao );
 
        // abre o JasperPrint em um JFrame
        viewReportFrame( titulo, print );
 
    }
 
    /**
     * Abre um relatrio usando um datasource genrico.
     *
     * @param titulo Titulo usado na janela do relatario.
     * @param inputStream InputStream que contem o relatario.
     * @param parametros Parametros utilizados pelo relatorio.
     * @param dataSource Datasource a ser utilizado pelo relatrio.
     * @throws JRException Caso ocorra algum problema na execuo do relatrio
     */
    public static void openReport(
            String titulo,
            InputStream inputStream,
            Map parametros,
            JRDataSource dataSource ) throws JRException {
 
        /*
         * Cria um JasperPrint, que  a verso preenchida do relatrio,
         * usando um datasource gerico.
         */
        JasperPrint print = JasperFillManager.fillReport(
                inputStream, parametros, dataSource );
 
        // abre o JasperPrint em um JFrame
        viewReportFrame( titulo, print );
 
    }
 
    /**
     * Cria um JFrame para exibir o relatrio representado pelo JasperPrint.
     *
     * @param titulo Ttulo do JFrame.
     * @param print JasperPrint do relatrio.
     */
    private static void viewReportFrame( String titulo, JasperPrint print ) {
 
        /*
         * Cria um JRViewer para exibir o relatrio.
         * Um JRViewer  uma JPanel.
         */
        JRViewer viewer = new JRViewer( print );
 
        // cria o JFrame
        JFrame frameRelatorio = new JFrame( titulo );
 
        // adiciona o JRViewer no JFrame
        frameRelatorio.add( viewer, BorderLayout.CENTER );
 
        // configura o tamanho padro do JFrame
        frameRelatorio.setSize( 500, 500 );
 
        // maximiza o JFrame para ocupar a tela toda.
        frameRelatorio.setExtendedState( JFrame.MAXIMIZED_BOTH );
 
        // configura a operao padro quando o JFrame for fechado.
        frameRelatorio.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
 
        // exibe o JFrame
        frameRelatorio.setVisible( true );
 
    }
 
}