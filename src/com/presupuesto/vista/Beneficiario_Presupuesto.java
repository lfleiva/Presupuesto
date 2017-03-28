/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.control.BeneficiarioJpaController;
import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Beneficiario;
import com.presupuesto.modelo.Vigencia;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Beneficiario_Presupuesto extends javax.swing.JInternalFrame {

    Home home;
    Vigencia vigencia;
    AccesoDatos accesoDatos;
    BeneficiarioJpaController beneficiarioController;

    /**
     * Creates new form Beneficiario_Presupuesto
     */
    public Beneficiario_Presupuesto(Home parent) {
        super();
        this.home = parent;

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);

        initComponents();
    }

    private Boolean validarFormulario() {
        Boolean validacionExitosa = true;

        if (frIdentificacion.getText().equals("") || frNombre.getText().equals("")) {
            validacionExitosa = false;
        }

        return validacionExitosa;
    }

    private void reiniciarFormulario() {
        frIdentificacion.setText("");
        frDireccion.setText("");
        frNombre.setText("");
        frTelefono.setText("");
        frEmail.setText("");
        frIdentificacion.requestFocus();
    }

    public void cargarBeneficiarioSeleccionado(String identificacion) {
        accesoDatos = new AccesoDatos();
        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setIdentificacion(identificacion);
        beneficiario = accesoDatos.consultarTodos(beneficiario, Beneficiario.class).get(0);

        frIdentificacion.setText(beneficiario.getIdentificacion());
        frNombre.setText(beneficiario.getNombre());
        frDireccion.setText(beneficiario.getDireccion());
        frTelefono.setText(beneficiario.getTelefono());
        frEmail.setText(beneficiario.getEmail());
    }

    private void eliminarBeneficiario() {
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<Beneficiario> listaBeneficiario = new ArrayList<Beneficiario>();
            Beneficiario beneficiario = new Beneficiario();
            beneficiario.setIdentificacion(frIdentificacion.getText());

            listaBeneficiario = accesoDatos.consultarTodos(beneficiario, Beneficiario.class);

            if (!listaBeneficiario.isEmpty()) {
                beneficiario = listaBeneficiario.get(0);
                if (beneficiario.getDisponibilidadList().isEmpty()) {
                    try {
                        beneficiarioController = new BeneficiarioJpaController();
                        beneficiarioController.destroy(beneficiario);
                        reiniciarFormulario();
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(Beneficiario_Presupuesto.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    frMensaje.setForeground(Color.RED);
                    frMensaje.setText("El beneficiario no se puede eliminar, tiene disponibilidades a su nombre");                    
                }
            } else {
                frMensaje.setForeground(Color.RED);
                frMensaje.setText("Por favor verificar que exista el registro del beneficiario a eliminar");                
            }
        } else {
            frMensaje.setForeground(Color.RED);
            frMensaje.setText("Por favor verificar que el formulario de registro este completo");       
        }
    }
    
    private void guardarBeneficiario() {
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<Beneficiario> listaBeneficarios = new ArrayList<Beneficiario>();
            Beneficiario beneficiario = new Beneficiario();
            beneficiario.setIdentificacion(frIdentificacion.getText());

            listaBeneficarios = accesoDatos.consultarTodos(beneficiario, Beneficiario.class);

            if (!listaBeneficarios.isEmpty()) {
                beneficiario = listaBeneficarios.get(0);
            }

            beneficiario.setNombre(frNombre.getText());
            beneficiario.setDireccion(frDireccion.getText());
            beneficiario.setTelefono(frTelefono.getText());
            beneficiario.setEmail(frEmail.getText());

            beneficiario = accesoDatos.persistirActualizar(beneficiario);

            reiniciarFormulario();
            
            frMensaje.setForeground(Color.DARK_GRAY);
            frMensaje.setText("El beneficiario se registro correctamente");
        } else {
            frMensaje.setForeground(Color.RED);
            frMensaje.setText("Por favor verificar que el formulario de registro este completo");
        }
    }
    
    private void abrirListaBeneficiarios() {
        // Abrir Lista
        Lista_Beneficiarios listaBeneficiarios = new Lista_Beneficiarios(this, true);
        listaBeneficiarios.setLocationRelativeTo(null);
        listaBeneficiarios.setVisible(true);
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
        jSeparator1 = new javax.swing.JToolBar.Separator();
        eliminarRegistro = new javax.swing.JLabel();
        labelIdentificacion = new javax.swing.JLabel();
        frIdentificacion = new javax.swing.JTextField();
        labelNombre = new javax.swing.JLabel();
        frNombre = new javax.swing.JTextField();
        frDireccion = new javax.swing.JTextField();
        labelDireccion = new javax.swing.JLabel();
        frTelefono = new javax.swing.JTextField();
        frEmail = new javax.swing.JTextField();
        labelTelefono = new javax.swing.JLabel();
        labelEmail = new javax.swing.JLabel();
        frMensaje = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        menuAdicion = new javax.swing.JMenu();
        itemNuevo = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        itemLista = new javax.swing.JMenuItem();
        itemCerrar = new javax.swing.JMenuItem();
        menuEditar = new javax.swing.JMenu();
        itemEliminar = new javax.swing.JMenuItem();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(895, 474));
        setMinimumSize(new java.awt.Dimension(895, 474));
        setPreferredSize(new java.awt.Dimension(895, 474));

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
        barraHerramientas.add(jSeparator1);

        eliminarRegistro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eliminarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/eliminar.png"))); // NOI18N
        eliminarRegistro.setAlignmentX(0.5F);
        eliminarRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        eliminarRegistro.setMaximumSize(new java.awt.Dimension(25, 20));
        eliminarRegistro.setMinimumSize(new java.awt.Dimension(25, 20));
        eliminarRegistro.setPreferredSize(new java.awt.Dimension(25, 20));
        eliminarRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                eliminarRegistroMousePressed(evt);
            }
        });
        barraHerramientas.add(eliminarRegistro);

        labelIdentificacion.setText("Identificacion");

        frIdentificacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frIdentificacionFocusLost(evt);
            }
        });

        labelNombre.setText("Nombre");

        labelDireccion.setText("Direccion");

        labelTelefono.setText("Telefono");

        labelEmail.setText("Email");

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuAdicion.setText("Inicio");

        itemNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        itemNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/nuevo_registro.png"))); // NOI18N
        itemNuevo.setText("Nuevo Beneficiario");
        itemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNuevoActionPerformed(evt);
            }
        });
        menuAdicion.add(itemNuevo);

        itemGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        itemGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/guardar.png"))); // NOI18N
        itemGuardar.setText("Guardar Beneficiario");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuAdicion.add(itemGuardar);

        itemLista.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        itemLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/lista.png"))); // NOI18N
        itemLista.setText("Lista Beneficiarios");
        itemLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemListaActionPerformed(evt);
            }
        });
        menuAdicion.add(itemLista);

        itemCerrar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        itemCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/Cerrar.png"))); // NOI18N
        itemCerrar.setText("Cerrar");
        itemCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCerrarActionPerformed(evt);
            }
        });
        menuAdicion.add(itemCerrar);

        barraMenu.add(menuAdicion);

        menuEditar.setText("Editar");

        itemEliminar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        itemEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/eliminar.png"))); // NOI18N
        itemEliminar.setText("Eliminar Beneficiario");
        itemEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemEliminarActionPerformed(evt);
            }
        });
        menuEditar.add(itemEliminar);

        barraMenu.add(menuEditar);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelIdentificacion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelNombre, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelDireccion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelTelefono, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelEmail, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(frIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(frNombre)
                        .addComponent(frDireccion)
                        .addComponent(frTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(frEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(frMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelIdentificacion)
                    .addComponent(frIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombre)
                    .addComponent(frNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDireccion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTelefono))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(frMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 201, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevoActionPerformed
        frIdentificacion.setText("");
        frNombre.setText("");
        frDireccion.setText("");
        frTelefono.setText("");
        frEmail.setText("");
        frIdentificacion.requestFocus();
    }//GEN-LAST:event_itemNuevoActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        guardarBeneficiario();
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemListaActionPerformed
        abrirListaBeneficiarios();
    }//GEN-LAST:event_itemListaActionPerformed

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void itemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEliminarActionPerformed
        eliminarBeneficiario();
    }//GEN-LAST:event_itemEliminarActionPerformed

    private void nuevoRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoRegistroMousePressed
        frIdentificacion.setText("");
        frNombre.setText("");
        frDireccion.setText("");
        frTelefono.setText("");
        frEmail.setText("");
        frIdentificacion.requestFocus();
        frMensaje.setText("");
    }//GEN-LAST:event_nuevoRegistroMousePressed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        guardarBeneficiario();
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void listaRegistrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaRegistrosMousePressed
        abrirListaBeneficiarios();
    }//GEN-LAST:event_listaRegistrosMousePressed

    private void eliminarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarRegistroMousePressed
        eliminarBeneficiario();
    }//GEN-LAST:event_eliminarRegistroMousePressed

    private void frIdentificacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frIdentificacionFocusLost
        if (!frIdentificacion.getText().equals("")) {
            List<Beneficiario> listaBeneficarios = new ArrayList<Beneficiario>();
            accesoDatos = new AccesoDatos();
            Beneficiario beneficiario = new Beneficiario();
            beneficiario.setIdentificacion(frIdentificacion.getText());

            listaBeneficarios = accesoDatos.consultarTodos(beneficiario, Beneficiario.class);

            if (!listaBeneficarios.isEmpty()) {
                beneficiario = listaBeneficarios.get(0);
                frNombre.setText(beneficiario.getNombre());
                frDireccion.setText(beneficiario.getDireccion());
                frTelefono.setText(beneficiario.getTelefono());
                frEmail.setText(beneficiario.getEmail());
            }
        }
    }//GEN-LAST:event_frIdentificacionFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JLabel eliminarRegistro;
    private javax.swing.JTextField frDireccion;
    private javax.swing.JTextField frEmail;
    private javax.swing.JTextField frIdentificacion;
    private javax.swing.JLabel frMensaje;
    private javax.swing.JTextField frNombre;
    private javax.swing.JTextField frTelefono;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemEliminar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemLista;
    private javax.swing.JMenuItem itemNuevo;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel labelDireccion;
    private javax.swing.JLabel labelEmail;
    private javax.swing.JLabel labelIdentificacion;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel labelTelefono;
    private javax.swing.JLabel listaRegistros;
    private javax.swing.JMenu menuAdicion;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JLabel nuevoRegistro;
    // End of variables declaration//GEN-END:variables
}
