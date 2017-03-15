/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.modelo.Traslado;
import com.presupuesto.modelo.TrasladoRubro;
import com.presupuesto.modelo.Vigencia;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Lista_Traslados extends javax.swing.JDialog {

    /**
     * *** Constantes de la clase ****
     */
    public static Traslado_Presupuestal trasladoPresupuestal;

    /**
     * *** Atributos de la clase ****
     */
    AccesoDatos accesoDatos;
    Vigencia vigencia;

    /**
     * Creates new form Lista_Adicion
     */
    public Lista_Traslados(Traslado_Presupuestal parent, boolean modal) {
        //super(parent, modal);
        this.trasladoPresupuestal = parent;
        this.setModal(modal);
        initComponents();
        consultarVigencia();
        cargarTrasladosRegistrados();

        // Icono
        setIconImage(new ImageIcon(getClass().getResource("/com/presupuesto/img/Icono.png")).getImage());
    }

    private void cargarTrasladosRegistrados() {
        accesoDatos = new AccesoDatos();
        List<Traslado> listaTraslados = new ArrayList<Traslado>();
        Traslado traslado = new Traslado();
        traslado.setVigencia(vigencia);
        listaTraslados = accesoDatos.consultarTodosPorVigencia(Traslado.class, vigencia);

        if (!listaTraslados.isEmpty()) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaTraslados.getModel();

            for (Traslado trasladoIterado : listaTraslados) {
                BigDecimal valorTraslado = new BigDecimal(0);
                if (!trasladoIterado.getTrasladoRubroList().isEmpty()) {
                    for (TrasladoRubro trasladoRubroIterado : trasladoIterado.getTrasladoRubroList()) {
                        valorTraslado = valorTraslado.add(trasladoRubroIterado.getValor());
                    }
                }

                model.addRow(new Object[]{trasladoIterado.getDocumento(), trasladoIterado.getDescripcion(), "$" + formatoNumeroDecimales(valorTraslado.toString())});
            }
        }
    }

    /**
     * Metodo que consulta la vigencia activa
     */
    private void consultarVigencia() {
        accesoDatos = new AccesoDatos();
        vigencia = new Vigencia();
        vigencia.setActiva(true);
        vigencia = accesoDatos.consultarTodos(vigencia, Vigencia.class).get(0);
    }

    private String formatoNumeroDecimales(String valor) {
        DecimalFormat formateador = new DecimalFormat("###,###,###.##");
        valor = valor.replace(",", "");
        double decimales = Double.parseDouble(valor);
        String valorFormateado = formateador.format(decimales);
        valorFormateado = valorFormateado.replace(",", "#");
        valorFormateado = valorFormateado.replace(".", ",");
        valorFormateado = valorFormateado.replace("#", ".");
        return valorFormateado;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTraslados = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        botonSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 400));
        setResizable(false);

        tablaTraslados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Documento", "Detalle", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaTraslados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaTrasladosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaTraslados);

        jLabel1.setText("Traslados Registradas");

        botonSeleccionar.setText("Seleccionar");
        botonSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSeleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonSeleccionar)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(botonSeleccionar)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSeleccionarActionPerformed
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaTraslados.getModel();

        int filaSeleccionada = tablaTraslados.getSelectedRow();

        if (filaSeleccionada != -1) {
            String trasladoSeleccionado = model.getValueAt(filaSeleccionada, 0).toString();
            trasladoPresupuestal.cargarTrasladoSeleccionado(trasladoSeleccionado);
        }

        this.setVisible(false);
    }//GEN-LAST:event_botonSeleccionarActionPerformed

    private void tablaTrasladosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaTrasladosMouseClicked
        if (evt.getClickCount() == 2) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaTraslados.getModel();
            int filaSeleccionada = tablaTraslados.getSelectedRow();

            if (filaSeleccionada != -1) {
                String trasladoSeleccionado = model.getValueAt(filaSeleccionada, 0).toString();
                trasladoPresupuestal.cargarTrasladoSeleccionado(trasladoSeleccionado);
            }

            this.setVisible(false);
        }
    }//GEN-LAST:event_tablaTrasladosMouseClicked

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
            java.util.logging.Logger.getLogger(Lista_Traslados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lista_Traslados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lista_Traslados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lista_Traslados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Lista_Traslados dialog = new Lista_Traslados(trasladoPresupuestal, true);
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
    private javax.swing.JButton botonSeleccionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaTraslados;
    // End of variables declaration//GEN-END:variables
}
