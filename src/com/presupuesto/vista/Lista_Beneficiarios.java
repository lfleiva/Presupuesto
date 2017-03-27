/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.modelo.Beneficiario;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Lista_Beneficiarios extends javax.swing.JDialog {

    /**
     * *** Constantes de la clase ****
     */
    public static Beneficiario_Presupuesto beneficiarioPresupuestal;

    /**
     * *** Atributos de la clase ****
     */
    AccesoDatos accesoDatos;

    /**
     * Creates new form Lista_Beneficiarios
     */
    public Lista_Beneficiarios(Beneficiario_Presupuesto parent, boolean modal) {
        //super(parent, modal);
        this.beneficiarioPresupuestal = parent;
        this.setModal(modal);

        initComponents();
        cargarBeneficiariosRegistrados();
        
        // Icono
        setIconImage(new ImageIcon(getClass().getResource("/com/presupuesto/img/Icono.png")).getImage());
    }

    private void cargarBeneficiariosRegistrados() {
        accesoDatos = new AccesoDatos();
        Beneficiario beneficiario = new Beneficiario();
        List<Beneficiario> listaBeneficiario = new ArrayList<Beneficiario>();

        listaBeneficiario = accesoDatos.consultarTodos(beneficiario, Beneficiario.class);

        if (!listaBeneficiario.isEmpty()) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaBeneficiarios.getModel();

            for (Beneficiario beneficiarioIterado : listaBeneficiario) {
                model.addRow(new Object[]{beneficiarioIterado.getIdentificacion(), beneficiarioIterado.getNombre(), beneficiarioIterado.getDireccion(), beneficiarioIterado.getTelefono()});
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
        tablaBeneficiarios = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        botonSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(700, 400));
        setMinimumSize(new java.awt.Dimension(700, 400));

        tablaBeneficiarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificacion", "Nombre", "Direccion", "Telefono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaBeneficiarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaBeneficiariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaBeneficiarios);

        jLabel1.setText("Beneficiarios Registrados");

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
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonSeleccionar)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(botonSeleccionar)
                .addGap(40, 40, 40))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSeleccionarActionPerformed
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaBeneficiarios.getModel();

        int filaSeleccionada = tablaBeneficiarios.getSelectedRow();

        if (filaSeleccionada != -1) {
            String beneficiarioSeleccionado = model.getValueAt(filaSeleccionada, 0).toString();
            beneficiarioPresupuestal.cargarBeneficiarioSeleccionado(beneficiarioSeleccionado);
        }

        this.setVisible(false);
    }//GEN-LAST:event_botonSeleccionarActionPerformed

    private void tablaBeneficiariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaBeneficiariosMouseClicked
        if (evt.getClickCount() == 2) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaBeneficiarios.getModel();
            int filaSeleccionada = tablaBeneficiarios.getSelectedRow();

            if (filaSeleccionada != -1) {
                String beneficiarioSeleccionado = model.getValueAt(filaSeleccionada, 0).toString();
                beneficiarioPresupuestal.cargarBeneficiarioSeleccionado(beneficiarioSeleccionado);
            }

            this.setVisible(false);
        }
    }//GEN-LAST:event_tablaBeneficiariosMouseClicked

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
            java.util.logging.Logger.getLogger(Lista_Beneficiarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lista_Beneficiarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lista_Beneficiarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lista_Beneficiarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Lista_Beneficiarios dialog = new Lista_Beneficiarios(beneficiarioPresupuestal, true);
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
    private javax.swing.JTable tablaBeneficiarios;
    // End of variables declaration//GEN-END:variables
}
