/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.modelo.Descuento;
import com.presupuesto.modelo.Vigencia;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Descuentos_Egreso extends javax.swing.JDialog {

    /**
     * *** Constantes de la clase ****
     */
    public static Comprobante_Egreso comprobanteEgreso;

    /**
     * *** Atributos de la clase ****
     */
    AccesoDatos accesoDatos;
    Vigencia vigencia;

    /**
     * Creates new form Descuentos_Egreso
     */
    public Descuentos_Egreso(Comprobante_Egreso parent, boolean modal) {
        this.comprobanteEgreso = parent;
        this.setModal(modal);
        initComponents();
        
        consultarVigencia();        
        
        // Icono
        setIconImage(new ImageIcon(getClass().getResource("/com/presupuesto/img/Icono.png")).getImage());
    }
    
    private void consultarVigencia() {
        accesoDatos = new AccesoDatos();
        vigencia = new Vigencia();
        vigencia.setActiva(true);
        vigencia = accesoDatos.consultarTodos(vigencia, Vigencia.class).get(0);
    }

    private boolean validarFormulario() {
        boolean validacionExitosa = true;

        if (frNombreDescuento.getText().equals("")) {
            validacionExitosa = false;
        }
        
        return validacionExitosa;
    }
    
    private void reiniciarFormulario() {
        frNombreDescuento.setText("");
        frPorcentaje.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barraHerramientas = new javax.swing.JToolBar();
        nuevoRegistro = new javax.swing.JLabel();
        guardarRegistro = new javax.swing.JLabel();
        listaRegistros = new javax.swing.JLabel();
        labelDecuento = new javax.swing.JLabel();
        frNombreDescuento = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        frPorcentaje = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(450, 160));
        setMinimumSize(new java.awt.Dimension(450, 160));
        setPreferredSize(new java.awt.Dimension(450, 160));

        barraHerramientas.setBackground(new java.awt.Color(255, 255, 255));
        barraHerramientas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        barraHerramientas.setFloatable(false);
        barraHerramientas.setRollover(true);

        nuevoRegistro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nuevoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/nuevo_registro.png"))); // NOI18N
        nuevoRegistro.setAlignmentX(0.5F);
        nuevoRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        nuevoRegistro.setMaximumSize(new java.awt.Dimension(25, 20));
        nuevoRegistro.setMinimumSize(new java.awt.Dimension(25, 20));
        nuevoRegistro.setPreferredSize(new java.awt.Dimension(25, 20));
        nuevoRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nuevoRegistroMousePressed(evt);
            }
        });
        barraHerramientas.add(nuevoRegistro);

        guardarRegistro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guardarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/guardar.png"))); // NOI18N
        guardarRegistro.setAlignmentX(0.5F);
        guardarRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        guardarRegistro.setMaximumSize(new java.awt.Dimension(25, 20));
        guardarRegistro.setMinimumSize(new java.awt.Dimension(25, 20));
        guardarRegistro.setPreferredSize(new java.awt.Dimension(25, 20));
        guardarRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                guardarRegistroMousePressed(evt);
            }
        });
        barraHerramientas.add(guardarRegistro);

        listaRegistros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        listaRegistros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/lista.png"))); // NOI18N
        listaRegistros.setAlignmentX(0.5F);
        listaRegistros.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        listaRegistros.setMaximumSize(new java.awt.Dimension(25, 20));
        listaRegistros.setMinimumSize(new java.awt.Dimension(25, 20));
        listaRegistros.setPreferredSize(new java.awt.Dimension(25, 20));
        listaRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaRegistrosMousePressed(evt);
            }
        });
        barraHerramientas.add(listaRegistros);

        labelDecuento.setText("Nombre Descuento");

        jLabel1.setText("Porcentaje (%)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(labelDecuento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(frPorcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frNombreDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDecuento)
                    .addComponent(frNombreDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(frPorcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 49, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nuevoRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoRegistroMousePressed
        reiniciarFormulario();
    }//GEN-LAST:event_nuevoRegistroMousePressed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<Descuento> listaDescuentos = new ArrayList<Descuento>();
            
            Descuento descuento = new Descuento();
            descuento.setNombre(frNombreDescuento.getText());
            
            listaDescuentos = accesoDatos.consultarObjetoPorVigencia(Descuento.class, descuento, vigencia);
            
            if(!listaDescuentos.isEmpty()){
                descuento = listaDescuentos.get(0);
            }
            
            descuento.setTipoDescuento(new BigDecimal(1));
            descuento.setPorcentaje(new BigDecimal(frPorcentaje.getText()));
            descuento.setVigencia(vigencia);
            
            descuento = accesoDatos.persistirActualizar(descuento);

            reiniciarFormulario();
            
            comprobanteEgreso.cargarListaDescuentos();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void listaRegistrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaRegistrosMousePressed
        // Abrir Lista
        Lista_Descuentos listaDescuentos = new Lista_Descuentos(this, true);
        listaDescuentos.setLocationRelativeTo(null);
        listaDescuentos.setVisible(true);
    }//GEN-LAST:event_listaRegistrosMousePressed

    public void cargarDescuento(String descuentoSeleccionado){
        accesoDatos = new AccesoDatos();
        Descuento descuento = new Descuento();
        descuento.setNombre(descuentoSeleccionado);
        
        List<Descuento> listaDescuento = new ArrayList<Descuento>();        
        listaDescuento = accesoDatos.consultarObjetoPorVigencia(Descuento.class, descuento, vigencia);
        
        if(!listaDescuento.isEmpty()){            
            descuento = listaDescuento.get(0);
            frNombreDescuento.setText(descuento.getNombre());
            
            if(descuento.getPorcentaje() != null){
                frPorcentaje.setText(descuento.getPorcentaje().toString());
            }
        }
    }
    
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
            java.util.logging.Logger.getLogger(Descuentos_Egreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Descuentos_Egreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Descuentos_Egreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Descuentos_Egreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Descuentos_Egreso dialog = new Descuentos_Egreso(comprobanteEgreso, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JTextField frNombreDescuento;
    private javax.swing.JTextField frPorcentaje;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelDecuento;
    private javax.swing.JLabel listaRegistros;
    private javax.swing.JLabel nuevoRegistro;
    // End of variables declaration//GEN-END:variables
}
