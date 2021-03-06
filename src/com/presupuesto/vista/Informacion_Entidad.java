/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.modelo.Entidad;
import java.math.BigDecimal;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Informacion_Entidad extends javax.swing.JInternalFrame {

    Home home;
    AccesoDatos accesoDatos;
    BigDecimal idEntidad;

    /**
     * Creates new form Informacion_Entidad
     */
    public Informacion_Entidad(Home parent) {
        super();
        this.home = parent;
        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);
        
        initComponents();
        labelMensaje.setText("");
        
        idEntidad = new BigDecimal(0);
        
        consultarEntidad();
    }

    private void consultarEntidad() {
        accesoDatos = new AccesoDatos();
        Entidad entidad = new Entidad();
        entidad = accesoDatos.consultarTodos(entidad, Entidad.class).get(0);

        frCargoGerente.setText(entidad.getCargoGerente());
        frCargoTesorero.setText(entidad.getCargoTesorero());
        frCelular.setText(entidad.getCelular());
        frCiudad.setText(entidad.getCiudad());
        frDepartamento.setText(entidad.getDepartamento());
        frDireccion.setText(entidad.getDireccion());
        frGerente.setText(entidad.getGerente());
        frNit.setText(entidad.getNit());
        frNombreCortoEntidad.setText(entidad.getNombreCorto());
        frNombreEntidad.setText(entidad.getNombre());
        frTelefono.setText(entidad.getTelefono());
        frTesorero.setText(entidad.getTesorero());
        
        idEntidad = entidad.getIdEntidad();
    }

    private boolean validarFormulario() {
        boolean transaccionExitosa = true;

        if (frCargoGerente.getText().trim().equals("") || frCargoTesorero.getText().trim().equals("") || frCelular.getText().trim().equals("")
                || frCiudad.getText().trim().equals("") || frDepartamento.getText().trim().equals("") || frDireccion.getText().trim().equals("") 
                || frGerente.getText().trim().equals("") || frNit.getText().trim().equals("") || frNombreCortoEntidad.getText().trim().equals("") 
                || frNombreEntidad.getText().trim().equals("") || frTelefono.getText().trim().equals("") || frTesorero.getText().trim().equals("")) {
            
            transaccionExitosa = false;
            
        }
        
        return transaccionExitosa;
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
        guardarRegistro = new javax.swing.JLabel();
        labelNombre = new javax.swing.JLabel();
        labelNombreCorto = new javax.swing.JLabel();
        frNombreEntidad = new javax.swing.JTextField();
        frNombreCortoEntidad = new javax.swing.JTextField();
        frNit = new javax.swing.JTextField();
        frDepartamento = new javax.swing.JTextField();
        frCiudad = new javax.swing.JTextField();
        frDireccion = new javax.swing.JTextField();
        frTelefono = new javax.swing.JTextField();
        frCelular = new javax.swing.JTextField();
        frGerente = new javax.swing.JTextField();
        frCargoGerente = new javax.swing.JTextField();
        frTesorero = new javax.swing.JTextField();
        frCargoTesorero = new javax.swing.JTextField();
        labelNit = new javax.swing.JLabel();
        labelDepartamento = new javax.swing.JLabel();
        labelCiudad = new javax.swing.JLabel();
        labelDireccion = new javax.swing.JLabel();
        labelTelefono = new javax.swing.JLabel();
        labelCelular = new javax.swing.JLabel();
        labelGenrente = new javax.swing.JLabel();
        labelGenrenteCargo = new javax.swing.JLabel();
        labelTesorero = new javax.swing.JLabel();
        labelTesoreroCargo = new javax.swing.JLabel();
        labelMensaje = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        menuAdicion = new javax.swing.JMenu();
        itemGuardar = new javax.swing.JMenuItem();
        itemCerrar = new javax.swing.JMenuItem();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(895, 474));
        setMinimumSize(new java.awt.Dimension(895, 474));
        setPreferredSize(new java.awt.Dimension(895, 474));

        barraHerramientas.setBackground(new java.awt.Color(255, 255, 255));
        barraHerramientas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        barraHerramientas.setFloatable(false);
        barraHerramientas.setRollover(true);

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

        labelNombre.setText("Nombre de la entidad");

        labelNombreCorto.setText("Nombre corto");

        labelNit.setText("Nit");

        labelDepartamento.setText("Departamento");

        labelCiudad.setText("Ciudad");

        labelDireccion.setText("Direccion");

        labelTelefono.setText("Telefono");

        labelCelular.setText("Celular");

        labelGenrente.setText("Gerente");

        labelGenrenteCargo.setText("Cargo");

        labelTesorero.setText("Tesorero(a)");

        labelTesoreroCargo.setText("Cargo");

        labelMensaje.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelMensaje.setForeground(new java.awt.Color(0, 204, 0));

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuAdicion.setText("Inicio");

        itemGuardar.setText("Guardar");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuAdicion.add(itemGuardar);

        itemCerrar.setText("Cerrar");
        itemCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCerrarActionPerformed(evt);
            }
        });
        menuAdicion.add(itemCerrar);

        barraMenu.add(menuAdicion);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelNombre)
                    .addComponent(labelNit)
                    .addComponent(labelDepartamento)
                    .addComponent(labelCiudad)
                    .addComponent(labelDireccion)
                    .addComponent(labelTelefono)
                    .addComponent(labelCelular)
                    .addComponent(labelGenrente)
                    .addComponent(labelTesorero))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(frTesorero, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                            .addComponent(frNombreEntidad)
                            .addComponent(frCelular)
                            .addComponent(frTelefono)
                            .addComponent(frDireccion)
                            .addComponent(frCiudad)
                            .addComponent(frDepartamento)
                            .addComponent(frNit, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frGerente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelNombreCorto, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelGenrenteCargo, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelTesoreroCargo, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frNombreCortoEntidad, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(frCargoTesorero, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                                .addComponent(frCargoGerente, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(74, 74, 74))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombre)
                    .addComponent(frNombreEntidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frNombreCortoEntidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNombreCorto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frNit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDepartamento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCiudad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDireccion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTelefono))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCelular))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frGerente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frCargoGerente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelGenrente)
                    .addComponent(labelGenrenteCargo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frTesorero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frCargoTesorero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTesorero)
                    .addComponent(labelTesoreroCargo))
                .addGap(36, 36, 36)
                .addComponent(labelMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            Entidad entidad = new Entidad();
            
            entidad.setCargoGerente(frCargoGerente.getText().trim());
            entidad.setCargoTesorero(frCargoTesorero.getText().trim());
            entidad.setCelular(frCelular.getText().trim());
            entidad.setCiudad(frCiudad.getText().trim());
            entidad.setDepartamento(frDepartamento.getText().trim());
            entidad.setDireccion(frDireccion.getText().trim());
            entidad.setGerente(frGerente.getText().trim());            
            entidad.setNit(frNit.getText().trim());
            entidad.setNombre(frNombreEntidad.getText().trim());
            entidad.setNombreCorto(frNombreCortoEntidad.getText().trim());
            entidad.setTelefono(frTelefono.getText().trim());
            entidad.setTesorero(frTesorero.getText().trim());
            entidad.setIdEntidad(idEntidad);
            
            entidad = accesoDatos.persistirActualizar(entidad);
            
            labelMensaje.setText("La información se actualizo correctamente");
        }
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            Entidad entidad = new Entidad();
            
            entidad.setCargoGerente(frCargoGerente.getText().trim());
            entidad.setCargoTesorero(frCargoTesorero.getText().trim());
            entidad.setCelular(frCelular.getText().trim());
            entidad.setCiudad(frCiudad.getText().trim());
            entidad.setDepartamento(frDepartamento.getText().trim());
            entidad.setDireccion(frDireccion.getText().trim());
            entidad.setGerente(frGerente.getText().trim());            
            entidad.setNit(frNit.getText().trim());
            entidad.setNombre(frNombreEntidad.getText().trim());
            entidad.setNombreCorto(frNombreCortoEntidad.getText().trim());
            entidad.setTelefono(frTelefono.getText().trim());
            entidad.setTesorero(frTesorero.getText().trim());
            entidad.setIdEntidad(idEntidad);
            
            entidad = accesoDatos.persistirActualizar(entidad);
            
            labelMensaje.setText("La información se actualizo correctamente");
        }
    }//GEN-LAST:event_guardarRegistroMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JTextField frCargoGerente;
    private javax.swing.JTextField frCargoTesorero;
    private javax.swing.JTextField frCelular;
    private javax.swing.JTextField frCiudad;
    private javax.swing.JTextField frDepartamento;
    private javax.swing.JTextField frDireccion;
    private javax.swing.JTextField frGerente;
    private javax.swing.JTextField frNit;
    private javax.swing.JTextField frNombreCortoEntidad;
    private javax.swing.JTextField frNombreEntidad;
    private javax.swing.JTextField frTelefono;
    private javax.swing.JTextField frTesorero;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JLabel labelCelular;
    private javax.swing.JLabel labelCiudad;
    private javax.swing.JLabel labelDepartamento;
    private javax.swing.JLabel labelDireccion;
    private javax.swing.JLabel labelGenrente;
    private javax.swing.JLabel labelGenrenteCargo;
    private javax.swing.JLabel labelMensaje;
    private javax.swing.JLabel labelNit;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel labelNombreCorto;
    private javax.swing.JLabel labelTelefono;
    private javax.swing.JLabel labelTesorero;
    private javax.swing.JLabel labelTesoreroCargo;
    private javax.swing.JMenu menuAdicion;
    // End of variables declaration//GEN-END:variables
}
