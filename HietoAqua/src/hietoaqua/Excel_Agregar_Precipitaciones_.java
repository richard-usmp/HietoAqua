/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hietoaqua;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class Excel_Agregar_Precipitaciones_ extends javax.swing.JFrame {
    ArrayList<Double> listaPrecipitaciones = new ArrayList<>();
    ArrayList <Double> precipitación_Acumulada = new ArrayList<>();
    String hora1_S,hora2_S;
    static int rango = 0;
    JFileChooser SelectArchivo=new JFileChooser();
    File archivo;
    int contador=0;
    String fecha_precipi/*PDF*/, medida, horas_S, nombre_esta/*PDF*/;
    int hora, maxima_hora=0, menor_hora=30;
    DefaultTableModel tblPre, tblPre2;
    /**
     * Creates new form Agregar_precipitaciones
     */
    public Excel_Agregar_Precipitaciones_() {
        initComponents();
        this.setLocationRelativeTo(null);
        txtHora_recib1.setVisible(false);
        txtHora_recib2.setVisible(false); 
        txtHora_recib1_0.setVisible(false);
        txtHora_recib2_0.setVisible(false);
        lblMed_Precipi_recibi.setVisible(false);
    }
    Inicio inicio = new Inicio();
    funciones f = new funciones();
    
    Workbook book;
    public void AgregarFiltro(){
        SelectArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xls)","xls"));
        SelectArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xlsx)","xlsx"));
    }
    public String Importar(File archivo, JTable tabla){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHorasss = new SimpleDateFormat("HH:mm:ss");   
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH");
        String mensaje="Error en la Importacion";
        DefaultTableModel modelo=new DefaultTableModel();
        tabla.setModel(modelo);
        String fecha_temp = "a";
        
        try {
            //CREA ARCHIVO CON EXTENSION XLS Y XLSX
            book=WorkbookFactory.create(new FileInputStream(archivo));
            Sheet hoja=book.getSheetAt(0);
            Iterator FilaIterator=hoja.rowIterator();
            int IndiceFila=-1;
            //VA SER VERDADERO SI EXISTEN FILAS POR RECORRER
            while (FilaIterator.hasNext()) {                
                //INDICE FILA AUMENTA 1 POR CADA RECORRIDO
                IndiceFila++;
                Row fila=(Row)FilaIterator.next();
                //RECORRE LAS COLUMNAS O CELDAS DE UNA FILA YA CREADA
                Iterator ColumnaIterator=fila.cellIterator();
                //ASIGNAMOS EL MAXIMO DE COLUMNA PERMITIDO
                Object[]ListaColumna=new Object[9999];
                int IndiceColumna=-1;
                //VA SER VERDADERO SI EXISTEN COLUMNAS POR RECORRER
                while (ColumnaIterator.hasNext()) {                    
                    //INDICE COLUMNA AUMENTA 1 POR CADA RECORRIDO
                    IndiceColumna++;
                    Cell celda=(Cell)ColumnaIterator.next();                                       
                    
                    if(IndiceFila==0 && celda.getStringCellValue().equals("FECHA")){
                        modelo.addColumn(celda.getStringCellValue());
                    }else{
                        if(celda!=null && IndiceColumna==0){
                            switch(celda.getCellType()){
                                case Cell.CELL_TYPE_NUMERIC:
                                    ListaColumna[0] = sdf.format(celda.getDateCellValue());
                                    fecha_temp = (String)ListaColumna[0];
                                    fecha_precipi = sdf.format(celda.getDateCellValue());
                                    break;
                                case Cell.CELL_TYPE_BLANK:
                                    ListaColumna[0]=celda.getStringCellValue();
                                    fecha_temp = (String)ListaColumna[0];
                                default:
                                    System.out.println("DEFAULT DE SWITCH Fecha");
                                    break;
                            }
                        }
                    }
                    
                    if(IndiceFila==0 && celda.getStringCellValue().equals("HORA")){
                        modelo.addColumn(celda.getStringCellValue());
                    }else{
                        if(celda!=null && IndiceColumna==1){
                            Object[]ListaHoras=new Object[9999];                                               
                            switch(celda.getCellType()){
                                case Cell.CELL_TYPE_NUMERIC:
                                    if(!fecha_temp.equals("")){
                                        ListaColumna[1]=sdfHorasss.format(celda.getDateCellValue());                                  
                                        ListaHoras[1] = sdfHora.format(celda.getDateCellValue());                                   
                                        for(int i=0; i<20; i++){
                                            horas_S = (String) ListaHoras[i];
                                            if(horas_S != null){
                                                hora = Integer.parseInt(horas_S);
                                                if(hora>maxima_hora){
                                                    maxima_hora = hora;
                                                    System.out.println("maxima_hora: "+ maxima_hora);
                                                }
                                                if(hora<menor_hora){
                                                    menor_hora=hora;
                                                    System.out.println("menor_hora: "+ menor_hora);
                                                }
                                            }                                       
                                        }
                                    }
                                    break;
                                default:
                                    System.out.println("DEFAULT DE SWITCH hora");
                                    break;
                            }
                        }
                    }
                    
                    //SI INDICE FILA ES IGUAL A "0" Y LA CELDA CONTIENE LA PALABRA VALOR, ENTONCES SE AGREGA UNA COLUMNA
                    if(IndiceFila==0 && celda.getStringCellValue().equals("VALOR DE PRECIPITACIÓN")){
                        modelo.addColumn(celda.getStringCellValue()); 
                    }else{
                        if(celda!=null && IndiceColumna==2){                                
                            switch (celda.getCellType()){
                                case Cell.CELL_TYPE_NUMERIC:
                                    if(!fecha_temp.equals("")){
                                        listaPrecipitaciones.add(celda.getNumericCellValue());
                                        ListaColumna[2]=celda.getNumericCellValue();
                                    }else{
                                        System.out.println("hola como estas");
                                    }                                 
                                    break;
                                default:
                                    System.out.println("DEFAULT DE SWITCH Valor");
                                    break;
                            }  
                            //System.out.println("listaPrecipitaciones: "+listaPrecipitaciones);
                        }
                    }  
                    
                    if(IndiceFila==0 && celda.getStringCellValue().equals("MEDIDA")){
                        modelo.addColumn(celda.getStringCellValue());
                    }else{
                        if(IndiceColumna==3){
                            switch(celda.getCellType()){
                                case Cell.CELL_TYPE_STRING:
                                    if(!fecha_temp.equals("")){
                                        ListaColumna[3]=celda.getStringCellValue();                                   
                                        medida = celda.getStringCellValue();
                                    }
                                    break;
                                default:
                                    System.out.println("DEFAULT DE SWITCH Medida");
                                    break;
                            }
                        }
                    }                   
                    
                    if(IndiceFila==0 && celda.getStringCellValue().equals("ESTACIÓN")){
                        System.out.println("aver: "+ celda.getStringCellValue());
                    }else{
                        if(celda!=null && IndiceColumna==4){
                            switch(celda.getCellType()){
                                case Cell.CELL_TYPE_STRING:
                                    nombre_esta = celda.getStringCellValue();
                                    break;
                                default:
                                    System.out.println("DEFAULT DE SWITCH estación");
                                    break;
                            }
                        }
                    }
                    
                }
                
                if(IndiceFila!=0)modelo.addRow(ListaColumna);
            }
            mensaje="Importacion Exitosa";
            lblMed_Precipi_recibi.setText(medida);
            txtHora_recib2.setText(""+maxima_hora);
            txtHora_recib2_0.setText(""+maxima_hora);
            txtHora_recib1.setText(""+menor_hora);
            txtHora_recib1_0.setText(""+menor_hora);
            System.out.println("listaPrecipitaciones: " + listaPrecipitaciones);
        } catch (Exception e) {
            return e+"";
        }
        return mensaje;
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblMed_Precipi_recibi = new javax.swing.JLabel();
        txtHora_recib1 = new javax.swing.JTextField();
        txtHora_recib2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAcumulado = new javax.swing.JTable();
        txtHora_recib1_0 = new javax.swing.JTextField();
        txtHora_recib2_0 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_maximos = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtHora_recib1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHora_recib1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHora_recib1ActionPerformed(evt);
            }
        });
        txtHora_recib1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHora_recib1KeyTyped(evt);
            }
        });

        txtHora_recib2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHora_recib2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHora_recib2KeyTyped(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setText("Calcular");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Acumulado por horas");

        tblAcumulado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblAcumulado.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblAcumulado);

        txtHora_recib1_0.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHora_recib1_0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHora_recib1_0ActionPerformed(evt);
            }
        });
        txtHora_recib1_0.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHora_recib1_0KeyTyped(evt);
            }
        });

        txtHora_recib2_0.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHora_recib2_0.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHora_recib2_0KeyTyped(evt);
            }
        });

        tbl_maximos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbl_maximos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "            "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_maximos.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbl_maximos);
        if (tbl_maximos.getColumnModel().getColumnCount() > 0) {
            tbl_maximos.getColumnModel().getColumn(0).setResizable(false);
            tbl_maximos.getColumnModel().getColumn(0).setPreferredWidth(140);
        }

        jButton3.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jButton3.setText("← Atrás");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton4.setText("Gráficos →");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jButton5.setText("Importar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(196, 196, 196)
                                .addComponent(jLabel5))
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHora_recib1_0, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMed_Precipi_recibi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(226, 226, 226)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHora_recib2_0, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtHora_recib1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtHora_recib2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblMed_Precipi_recibi, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton5)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtHora_recib1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtHora_recib1_0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(47, 47, 47)
                        .addComponent(txtHora_recib2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHora_recib2_0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtHora_recib1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHora_recib1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHora_recib1KeyTyped

    private void txtHora_recib2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHora_recib2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHora_recib2KeyTyped

    private void txtHora_recib1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHora_recib1ActionPerformed
    }//GEN-LAST:event_txtHora_recib1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
            tblPre = (DefaultTableModel)tblAcumulado.getModel();
            tblPre2 = (DefaultTableModel)tbl_maximos.getModel();
            //TableColumnModel tblAcumulado_f = tblAcumulado.getColumnModel();
            tblPre.setRowCount(0);
            tblPre2.setRowCount(0);
            tblPre.setColumnCount(0);
            tblPre2.setColumnCount(0);
            DecimalFormat df = new DecimalFormat("0.000");

            precipitación_Acumulada.add(listaPrecipitaciones.get(0));
            
            double profundidad_maxima1 = listaPrecipitaciones.get(0);
            double profundidad_maxima2 = Math.round(((listaPrecipitaciones.get(0) + listaPrecipitaciones.get(1))*100)/100);
            double profundidad_maxima3 = 0;
            double profundidad_maxima4 = 0;           
            hora1_S = txtHora_recib1_0.getText();
            hora2_S = txtHora_recib2_0.getText(); 
            int hora1 = Integer.parseInt(hora1_S);
            int hora2 = Integer.parseInt(hora2_S);
            rango = hora2-hora1+1;
            System.out.println("rango: "+ rango);
            for(int i=0;i<rango-1;i++){
                switch (i) {
                    case 0:
                        tblPre.addColumn("Precipitación\n" + " acumulada");
                        tblPre.addColumn("Total en 1 hora");
                        tblPre.addColumn("Total en 2 horas"); 
                        Object row1[] = {df.format(precipitación_Acumulada.get(i)), df.format(listaPrecipitaciones.get(i))};
                        precipitación_Acumulada.add((precipitación_Acumulada.get(i) + listaPrecipitaciones.get(i+1)));
                        Object row2[] = {df.format(precipitación_Acumulada.get(i+1)), df.format(listaPrecipitaciones.get(i+1)), df.format(listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1))};
                        
                        tblPre2.addColumn("");
                        tblPre2.addColumn("En 1 hora");
                        tblPre2.addColumn("En 2 horas");
                        if(listaPrecipitaciones.get(i)>profundidad_maxima1){
                            profundidad_maxima1 = listaPrecipitaciones.get(i);
                        }
                        if(listaPrecipitaciones.get(i+1)>profundidad_maxima1){
                            profundidad_maxima1 = listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1)>profundidad_maxima2){
                            profundidad_maxima2 = listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1);
                        }    
                        
                        tblPre.addRow(row1);
                        tblPre.addRow(row2);                       
                        break;
                    case 1:
                        profundidad_maxima3 = listaPrecipitaciones.get(0) + listaPrecipitaciones.get(1) + listaPrecipitaciones.get(2);
                        tblPre.addColumn("Total en 3 horas");
                        precipitación_Acumulada.add((precipitación_Acumulada.get(i) + listaPrecipitaciones.get(i+1)));
                        Object row3[] = {df.format(precipitación_Acumulada.get(i+1)), df.format(listaPrecipitaciones.get(i+1)), df.format(listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)),
                            df.format(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1))};
                        
                        tblPre2.addColumn("En 3 horas");
                        if(listaPrecipitaciones.get(i+1)>profundidad_maxima1){
                            profundidad_maxima1 = listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1)>profundidad_maxima2){
                            profundidad_maxima2 = listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)>profundidad_maxima3){
                            profundidad_maxima3 = listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1);
                        }
                        
                        tblPre.addRow(row3);
                        break;
                    case 2:
                        precipitación_Acumulada.add((precipitación_Acumulada.get(i) + listaPrecipitaciones.get(i+1)));
                        Object row4[] = {df.format(precipitación_Acumulada.get(i+1)), df.format(listaPrecipitaciones.get(i+1)), df.format(listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)),
                            df.format(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1))};
                        
                        if(listaPrecipitaciones.get(i+1)>profundidad_maxima1){
                            profundidad_maxima1 = listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1)>profundidad_maxima2){
                            profundidad_maxima2 = listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)>profundidad_maxima3){
                            profundidad_maxima3 = listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1);
                        }
                        
                        tblPre.addRow(row4);
                        break;
                    case 3:
                        precipitación_Acumulada.add((precipitación_Acumulada.get(i) + listaPrecipitaciones.get(i+1)));
                        Object row5[] = {df.format(precipitación_Acumulada.get(i+1)), df.format(listaPrecipitaciones.get(i+1)), df.format(listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)),
                            df.format(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1))};
                        
                        if(listaPrecipitaciones.get(i+1)>profundidad_maxima1){
                            profundidad_maxima1 = listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1)>profundidad_maxima2){
                            profundidad_maxima2 = listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)>profundidad_maxima3){
                            profundidad_maxima3 = listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1);
                        }
                        
                        tblPre.addRow(row5);
                        break;
                    case 4:
                        precipitación_Acumulada.add((precipitación_Acumulada.get(i) + listaPrecipitaciones.get(i+1)));
                        profundidad_maxima4 = listaPrecipitaciones.get(0) + listaPrecipitaciones.get(1) + listaPrecipitaciones.get(2) + listaPrecipitaciones.get(3)+ 
                                listaPrecipitaciones.get(4)+ listaPrecipitaciones.get(5);
                        tblPre.addColumn("Total en 6 horas");
                        Object row6[] = {df.format(precipitación_Acumulada.get(i+1)), df.format(listaPrecipitaciones.get(i+1)), df.format(listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)),
                            df.format(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)),
                            df.format(listaPrecipitaciones.get(i-4) + listaPrecipitaciones.get(i-3) + listaPrecipitaciones.get(i-2) + listaPrecipitaciones.get(i-1) +
                                    listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1))};
                        
                        tblPre2.addColumn("En 6 horas");
                        if(listaPrecipitaciones.get(i+1)>profundidad_maxima1){
                            profundidad_maxima1 = listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1)>profundidad_maxima2){
                            profundidad_maxima2 = listaPrecipitaciones.get(i)+ listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)>profundidad_maxima3){
                            profundidad_maxima3 = listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1);
                        }
                        if(listaPrecipitaciones.get(i-4) + listaPrecipitaciones.get(i-3) + listaPrecipitaciones.get(i-2) + listaPrecipitaciones.get(i-1) + 
                                listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)>profundidad_maxima4){
                            profundidad_maxima4 = listaPrecipitaciones.get(i-4) + listaPrecipitaciones.get(i-3) + listaPrecipitaciones.get(i-2) + listaPrecipitaciones.get(i-1) +
                                    listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1);
                        }
                        
                        tblPre.addRow(row6);
                        break;                   
                    default:
                        {
                            precipitación_Acumulada.add((precipitación_Acumulada.get(i) + listaPrecipitaciones.get(i+1)));
                            Object row7[] = {df.format(precipitación_Acumulada.get(i+1)), df.format(listaPrecipitaciones.get(i+1)), df.format(listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)),
                                df.format(listaPrecipitaciones.get(i-1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1)),
                                df.format(listaPrecipitaciones.get(i-4) + listaPrecipitaciones.get(i-3) + listaPrecipitaciones.get(i-2) + listaPrecipitaciones.get(i-1) +
                                        listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i+1))};
                            
                            if (listaPrecipitaciones.get(i + 1) > profundidad_maxima1) {
                                profundidad_maxima1 = listaPrecipitaciones.get(i + 1);
                            }
                            if (listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i + 1) > profundidad_maxima2) {
                                profundidad_maxima2 = listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i + 1);
                            }
                            if (listaPrecipitaciones.get(i - 1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i + 1) > profundidad_maxima3) {
                                profundidad_maxima3 = listaPrecipitaciones.get(i - 1) + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i + 1);
                            }
                            if (listaPrecipitaciones.get(i - 4) + listaPrecipitaciones.get(i - 3) + listaPrecipitaciones.get(i - 2) + listaPrecipitaciones.get(i - 1)
                                    + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i + 1) > profundidad_maxima4) {
                                profundidad_maxima4 = listaPrecipitaciones.get(i - 4) + listaPrecipitaciones.get(i - 3) + listaPrecipitaciones.get(i - 2) + listaPrecipitaciones.get(i - 1)
                                        + listaPrecipitaciones.get(i) + listaPrecipitaciones.get(i + 1);
                            }
                            
                            tblPre.addRow(row7);
                            break;
                        }
                }               
            }     
            double maximo_intensidad1 = profundidad_maxima1/1.0;
            double maximo_intensidad2 = profundidad_maxima2/2.0;
            double maximo_intensidad3 = profundidad_maxima3/3.0;
            double maximo_intensidad4 = profundidad_maxima4/6.0;
            Object rrooww1[] = {"Profundidad Máxima", df.format(profundidad_maxima1), df.format(profundidad_maxima2), df.format(profundidad_maxima3), df.format(profundidad_maxima4)};
            Object rrooww2[]={"Maximo intensidad", df.format(maximo_intensidad1), df.format(maximo_intensidad2), df.format(maximo_intensidad3), df.format(maximo_intensidad4)};
            tblPre2.addRow(rrooww1);
            tblPre2.addRow(rrooww2);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        inicio.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int width = (int) PageSize.A4.getWidth();
        int height = (int) (PageSize.A4.getHeight());
        Rectangle pagesize = new Rectangle( width, height );
        Document documento = new Document(pagesize, 50, 50, 50, 50);
        try{
            String ruta= System.getProperty("user.home");
            String medidas=lblMed_Precipi_recibi.getText();
            hora1_S = txtHora_recib1_0.getText();
            int hora1 = Integer.parseInt(hora1_S);
            DefaultCategoryDataset ds = new DefaultCategoryDataset();            
            for(int i=0; i<rango; i++){
                ds.addValue(listaPrecipitaciones.get(i), (hora1+i)+":00", "");
            }    
            JFreeChart jf = ChartFactory.createBarChart("Hietograma-Lluvia", "Tiempo (hrs)", "Precipitación " +"("+ medidas+")", ds, PlotOrientation.VERTICAL, true, true, true); 
            
            DefaultCategoryDataset ds2 = new DefaultCategoryDataset();            
            for(int i=0; i<rango; i++){
                ds2.addValue(precipitación_Acumulada.get(i), (hora1+i)+":00", "");
            }    
            JFreeChart jf2 = ChartFactory.createBarChart("Hietograma-Acumulado", "Tiempo (hrs)", "Precipitación " +"("+ medidas+")", ds2, PlotOrientation.VERTICAL, true, true, true);
            /*-----------------PDF----------------------*/
                PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(ruta+"\\Desktop"+"\\"+nombre_esta+".pdf"));
                BaseFont consolas_B = BaseFont.createFont("..\\src\\fonts\\consolas\\CONSOLA.ttf", "Cp1252",  true);
                //BaseFont consolas_B = BaseFont.createFont("fonts\\consolas\\CONSOLA.ttf", "Cp1252",  true); 
                Font consolas = new Font(consolas_B);
                Font font_nomColum = new Font(FontFactory.getFont(BaseFont.HELVETICA, 12));
                
                Image logo = Image.getInstance("https://i.ibb.co/SsB7NgC/logo-hietoaqua.png");
                logo.scaleToFit(120, 120);
                logo.setAlignment(Chunk.ALIGN_LEFT);
                
                Paragraph parrafo = new Paragraph();
                parrafo.setAlignment(Paragraph.ALIGN_CENTER);
                parrafo.setFont(FontFactory.getFont(BaseFont.HELVETICA, 20, Font.BOLD));
                parrafo.add(" \n"+nombre_esta.toUpperCase()+"\n\n\n\n\n");   
                //parrafo.add(" \n"+"Latitud: "+lat+ "  Longitud: "+lon+" \n\n\n\n\n"); //latitud y longitud
                
                Paragraph subtitulo = new Paragraph();
                subtitulo.setFont(FontFactory.getFont(BaseFont.HELVETICA, 17));       
                subtitulo.add("Acumulado por horas: \n\n\n\n");
                
                Paragraph fecha = new Paragraph();
                fecha.setAlignment(Paragraph.ALIGN_RIGHT);
                fecha.setFont(FontFactory.getFont(BaseFont.COURIER, 17));
                fecha.add("                                                  "+fecha_precipi);
                
                documento.open();
                documento.add(fecha);
                logo.setAbsolutePosition(25, 689);
                documento.add(logo);
                documento.add(parrafo); 
                documento.add(subtitulo);
                documento.addTitle("Hietograma_"+nombre_esta);
                //una pagina
                //tabla 1  
                PdfPTable table = new PdfPTable(tblPre.getColumnCount());
                int cols = tblPre.getColumnCount();
                int fils = tblPre.getRowCount();
                for(int j=0; j<cols; j++){
                    BaseColor myColor = WebColors.getRGBColor("#38acd0");
                    PdfPCell cell = new PdfPCell(new Phrase(tblPre.getColumnName(j), font_nomColum));  
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                }
                for(int i=0; i<fils; i++) {
                    for(int j=0; j<cols; j++){
                        PdfPCell cell = new PdfPCell(new Phrase((String) tblPre.getValueAt(i,j), consolas));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                    }
                }
           
                //tabla 2
                PdfPTable table2 = new PdfPTable(tblPre2.getColumnCount());
                int cols2 = tblPre2.getColumnCount();
                int fils2 = tblPre2.getRowCount();
                for(int j=0; j<cols2; j++){
                    PdfPCell cell = new PdfPCell(new Phrase(" "));
                    cell.setBackgroundColor(BaseColor.BLACK);
                    table2.addCell(cell);
                }
                for(int i=0; i<fils2; i++) {
                    for(int j=0; j<cols2; j++){
                        PdfPCell cell = new PdfPCell(new Phrase((String) tblPre2.getValueAt(i,j), consolas));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table2.addCell(cell);
                    }
                }
                documento.add(table);  
                documento.add(table2);
                
                //OTRA PAGINA
                documento.newPage();
                PdfContentByte cb = writer.getDirectContent();
                PdfTemplate tp = cb.createTemplate( width, height );
                Graphics2D g2 = tp.createGraphics( width, height, new DefaultFontMapper() );
                Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, ((height/2)-1) );
                jf.draw(g2, r2D);
                g2.dispose();
                cb.addTemplate(tp, 0, 0); 

                PdfContentByte cb2 = writer.getDirectContent();
                PdfTemplate tp2 = cb2.createTemplate( width, height );
                Graphics2D g22 = tp2.createGraphics( width, height, new DefaultFontMapper() );
                Rectangle2D r2D2 = new Rectangle2D.Double(0, 422, width, ((height/2)-1) );
                jf2.draw(g22, r2D2);
                g22.dispose();
                cb.addTemplate(tp2, 0, 0); 
                
                documento.close();
            /*-----------------FIN PDF----------------------*/
            ChartFrame f = new ChartFrame("Gráfico H-Lluvia", jf);
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            
            ChartFrame f2 = new ChartFrame("Gráfico H-Acumulado", jf2);
            f2.setSize(1000, 600);
            f2.setLocationRelativeTo(f);
            f2.setVisible(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"No se ha importado el archivo de Excel: " + e);
        }                     
        //System.out.println("Rango en graficos: " + rango);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    contador++;
    if(contador==1)AgregarFiltro();
        
    if(SelectArchivo.showDialog(null, "Seleccionar Archivo")==JFileChooser.APPROVE_OPTION){
        archivo=SelectArchivo.getSelectedFile();
        //ALT + 124 ||
        if(archivo.getName().endsWith("xls")||archivo.getName().endsWith("xlsx")){
            tblPre2 = (DefaultTableModel)tbl_maximos.getModel();
            tblPre2.setRowCount(0);
            tblPre2.setColumnCount(0);
            JOptionPane.showMessageDialog(null, Importar(archivo,tblAcumulado));
        }else{
            JOptionPane.showMessageDialog(null, "Seleccionar formato Valido");
        }
    }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtHora_recib2_0KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHora_recib2_0KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHora_recib2_0KeyTyped

    private void txtHora_recib1_0KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHora_recib1_0KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHora_recib1_0KeyTyped

    private void txtHora_recib1_0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHora_recib1_0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHora_recib1_0ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Excel_Agregar_Precipitaciones_.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Excel_Agregar_Precipitaciones_.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Excel_Agregar_Precipitaciones_.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Excel_Agregar_Precipitaciones_.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Excel_Agregar_Precipitaciones_().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JLabel lblMed_Precipi_recibi;
    private javax.swing.JTable tblAcumulado;
    private javax.swing.JTable tbl_maximos;
    public static javax.swing.JTextField txtHora_recib1;
    public static javax.swing.JTextField txtHora_recib1_0;
    public static javax.swing.JTextField txtHora_recib2;
    public static javax.swing.JTextField txtHora_recib2_0;
    // End of variables declaration//GEN-END:variables
}
