/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.modelo.Ops;
import com.presupuesto.modelo.Vigencia;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Lista_Ops extends javax.swing.JDialog {

    public static Orden_Prestacion_Servicio_Presupuestal opsPresupuestal;

    AccesoDatos accesoDatos;
    Vigencia vigencia;
    
    /**
     * Creates new form Lista_Ops
     */
    public Lista_Ops(Orden_Prestacion_Servicio_Presupuestal parent, boolean modal) {
        this.opsPresupuestal = parent;
        this.setModal(modal);
        initComponents();
        consultarVigencia();
        cargarOps();
        
        // Icono
        setIconImage(new ImageIcon(getClass().getResource("/com/presupuesto/img/Icono.png")).getImage());
    }
    
    private void consultarVigencia() {
        accesoDatos = new AccesoDatos();
        vigencia = new Vigencia();
        vigencia.setActiva(true);
        vigencia = accesoDatos.consultarTodos(vigencia, Vigencia.class).get(0);
    }
    
    private void cargarOps() {
        accesoDatos = new AccesoDatos();
        List<Ops> listaOps = new ArrayList<Ops>();
                     
        listaOps = accesoDatos.consultarTodosPorVigencia(Ops.class, vigencia);
        
        if (!listaOps.isEmpty()) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaOps.getModel();

            for (Ops opsIterado : listaOps) {
                model.addRow(new Object[]{opsIterado.getConsecutivo(), opsIterado.getDisponibilidad().getConsecutivo(), opsIterado.getDisponibilidad().getBeneficiario().getNombre(), opsIterado.getFecha(), opsIterado.getObjeto()});
            }
        }
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
        tablaOps = new javax.swing.JTable();
        labelListaOps = new javax.swing.JLabel();
        botonSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(700, 400));
        setMinimumSize(new java.awt.Dimension(700, 400));

        tablaOps.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ops", "Disponibilidad", "Beneficiario", "Fecha", "Objeto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaOps);

        labelListaOps.setText("Ops registradas");

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
                    .addComponent(labelListaOps)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(labelListaOps)
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
        model = (DefaultTableModel) tablaOps.getModel();

        int filaSeleccionada = tablaOps.getSelectedRow();

        if (filaSeleccionada != -1) {
            String opsSeleccionado = model.getValueAt(filaSeleccionada, 0).toString();
            opsPresupuestal.cargarOpsSeleccionado(opsSeleccionado);
        }

        this.setVisible(false);
    }//GEN-LAST:event_botonSeleccionarActionPerformed

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
            java.util.logging.Logger.getLogger(Lista_Ops.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lista_Ops.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lista_Ops.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lista_Ops.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Lista_Ops dialog = new Lista_Ops(opsPresupuestal, true);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelListaOps;
    private javax.swing.JTable tablaOps;
    // End of variables declaration//GEN-END:variables
}
