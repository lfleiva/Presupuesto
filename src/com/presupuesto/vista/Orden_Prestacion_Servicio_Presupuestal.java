/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.control.OpsJpaController;
import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.Ops;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.utilidades.Generar_Reportes;
import java.math.BigDecimal;
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
public class Orden_Prestacion_Servicio_Presupuestal extends javax.swing.JInternalFrame {

    Home home;
    AccesoDatos accesoDatos;
    Vigencia vigencia;
    OpsJpaController opsController;

    /**
     * Creates new form Orden_Prestacion_Servicio_Presupuestal
     */
    public Orden_Prestacion_Servicio_Presupuestal(Home parent) {
        super();
        this.home = parent;

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);
        consultarVigencia();
        initComponents();

        frOPS.setText(consultarConsecutivo().toString());
        cargarListaDisponibilidades();
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

    private BigDecimal consultarConsecutivo() {
        BigDecimal consecutivo = new BigDecimal(0);
        opsController = new OpsJpaController();

        consecutivo = opsController.consultarMaximoRegistro(vigencia).add(new BigDecimal(1));

        return consecutivo;
    }

    private void cargarListaDisponibilidades() {
        accesoDatos = new AccesoDatos();
        List<Disponibilidad> listaDisponibilidad = new ArrayList<Disponibilidad>();
        listaDisponibilidad = accesoDatos.consultarTodosPorVigencia(Disponibilidad.class, vigencia);

        if (!listaDisponibilidad.isEmpty()) {
            for (Disponibilidad disponibilidadIterado : listaDisponibilidad) {
                frListaDisponibilidad.addItem(disponibilidadIterado.getConsecutivo() + " - " + disponibilidadIterado.getBeneficiario().getNombre());
            }
        }
    }

    private void reiniciarFormulario() {
        frOPS.setText(consultarConsecutivo().toString());
        if (frListaDisponibilidad.getItemCount() > 0) {
            frListaDisponibilidad.setSelectedIndex(0);
        }
        frFecha.setDate(null);
        frObjeto.setText("");
        frFechaFin.setDate(null);
        frFechaFirma.setDate(null);
        frFechaInicio.setDate(null);
        frPlazo.setText("");
        frOPS.requestFocus();
    }

    private boolean validarFormulario() {
        boolean validacionExitosa = true;

        if (frOPS.getText().trim().equals("") || frFecha.getDate() == null || frObjeto.getText().trim().equals("") || frFechaFin.getDate() == null || frFechaFirma.getDate() == null || frFechaInicio.getDate() == null || frPlazo.getText().equals("")) {
            validacionExitosa = false;
        }

        return validacionExitosa;
    }

    private Disponibilidad consultarDisponibilidad() {
        Disponibilidad disponibilidad = new Disponibilidad();

        String disponibilidadSeleccionada = frListaDisponibilidad.getSelectedItem().toString();
        String consecutivo = disponibilidadSeleccionada.substring(0, disponibilidadSeleccionada.indexOf("-") - 1);

        disponibilidad.setConsecutivo(new BigDecimal(consecutivo));
        disponibilidad.setVigencia(vigencia);

        disponibilidad = accesoDatos.consultarObjetoPorVigencia(Disponibilidad.class, disponibilidad, vigencia).get(0);

        return disponibilidad;
    }

    public void cargarOpsSeleccionado(String consecutivoOps) {
        if (!consecutivoOps.trim().equals("")) {
            accesoDatos = new AccesoDatos();
            List<Ops> listaOps = new ArrayList<Ops>();
            Ops ops = new Ops();
            ops.setConsecutivo(new BigDecimal(consecutivoOps));
            ops.setVigencia(vigencia);

            listaOps = accesoDatos.consultarObjetoPorVigencia(Ops.class, ops, vigencia);

            if (!listaOps.isEmpty()) {
                ops = listaOps.get(0);
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedItem(ops.getDisponibilidad().getConsecutivo() + " - " + ops.getDisponibilidad().getBeneficiario().getNombre());
                }
                frOPS.setText(consecutivoOps);
                frFecha.setDate(ops.getFecha());
                frObjeto.setText(ops.getObjeto());
                frFechaInicio.setDate(ops.getFechaInicio());
                frFechaFin.setDate(ops.getFechaFinal());
                frFechaFirma.setDate(ops.getFechaFirma());
                frPlazo.setText(ops.getPlazo().toString());
            } else {
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedIndex(0);
                }
                frFecha.setDate(null);
                frObjeto.setText("");
                frFechaFin.setDate(null);
                frFechaFirma.setDate(null);
                frFechaInicio.setDate(null);
                frPlazo.setText("");
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

        barraHerramientas = new javax.swing.JToolBar();
        nuevoRegistro = new javax.swing.JLabel();
        guardarRegistro = new javax.swing.JLabel();
        listaRegistros = new javax.swing.JLabel();
        imprimirRegistro = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        eliminarRegistro = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        frObjeto = new javax.swing.JTextArea();
        labelObjeto = new javax.swing.JLabel();
        frListaDisponibilidad = new javax.swing.JComboBox<>();
        frOPS = new javax.swing.JTextField();
        labelDisponibilidad = new javax.swing.JLabel();
        labelNoOPS = new javax.swing.JLabel();
        frFecha = new com.toedter.calendar.JDateChooser();
        labelFecha = new javax.swing.JLabel();
        labelFechaFirma = new javax.swing.JLabel();
        frFechaFirma = new com.toedter.calendar.JDateChooser();
        labelFechaInicio = new javax.swing.JLabel();
        frFechaInicio = new com.toedter.calendar.JDateChooser();
        labelFechaFin = new javax.swing.JLabel();
        frFechaFin = new com.toedter.calendar.JDateChooser();
        labelPlazo = new javax.swing.JLabel();
        frPlazo = new javax.swing.JTextField();
        barraMenu = new javax.swing.JMenuBar();
        menuTraslado = new javax.swing.JMenu();
        itemNuevo = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        itemListaAdiciones = new javax.swing.JMenuItem();
        itemCerrar = new javax.swing.JMenuItem();
        menuEditar = new javax.swing.JMenu();
        itemEliminar = new javax.swing.JMenuItem();

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

        imprimirRegistro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imprimirRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/imprimir.png"))); // NOI18N
        imprimirRegistro.setAlignmentX(0.5F);
        imprimirRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        imprimirRegistro.setMaximumSize(new java.awt.Dimension(25, 20));
        imprimirRegistro.setMinimumSize(new java.awt.Dimension(25, 20));
        imprimirRegistro.setPreferredSize(new java.awt.Dimension(25, 20));
        imprimirRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                imprimirRegistroMousePressed(evt);
            }
        });
        barraHerramientas.add(imprimirRegistro);
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

        frObjeto.setColumns(20);
        frObjeto.setLineWrap(true);
        frObjeto.setRows(5);
        jScrollPane1.setViewportView(frObjeto);

        labelObjeto.setText("Objeto");

        frOPS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frOPSFocusLost(evt);
            }
        });

        labelDisponibilidad.setText("Disponibilidad");

        labelNoOPS.setText("No. OPS");

        labelFecha.setText("Fecha");

        labelFechaFirma.setText("Fecha Firma");

        labelFechaInicio.setText("Fecha Inicio");

        labelFechaFin.setText("Fecha Fin");

        labelPlazo.setText("Plazo (Meses)");

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuTraslado.setText("Inicio");

        itemNuevo.setText("Nuevo Registro");
        itemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNuevoActionPerformed(evt);
            }
        });
        menuTraslado.add(itemNuevo);

        itemGuardar.setText("Guardar");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuTraslado.add(itemGuardar);

        itemListaAdiciones.setText("Lista OPS Presupuestal");
        itemListaAdiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemListaAdicionesActionPerformed(evt);
            }
        });
        menuTraslado.add(itemListaAdiciones);

        itemCerrar.setText("Cerrar");
        itemCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCerrarActionPerformed(evt);
            }
        });
        menuTraslado.add(itemCerrar);

        barraMenu.add(menuTraslado);

        menuEditar.setText("Editar");

        itemEliminar.setText("Eliminar");
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelFechaFin)
                            .addComponent(labelPlazo))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(frFechaFin, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(frPlazo)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelFechaInicio)
                            .addComponent(labelFechaFirma)
                            .addComponent(labelObjeto)
                            .addComponent(labelFecha)
                            .addComponent(labelDisponibilidad)
                            .addComponent(labelNoOPS))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(frOPS, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frListaDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(frFechaFirma, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(frFechaInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(450, 450, 450))
                            .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frOPS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNoOPS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDisponibilidad)
                    .addComponent(frListaDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelFecha)
                    .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelObjeto)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelFechaFirma)
                    .addComponent(frFechaFirma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelFechaInicio)
                    .addComponent(frFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelFechaFin)
                    .addComponent(frFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frPlazo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPlazo))
                .addGap(0, 30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevoActionPerformed
        reiniciarFormulario();
    }//GEN-LAST:event_itemNuevoActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        if (validarFormulario()) {
            List<Ops> listaOps = new ArrayList<Ops>();
            Ops ops = new Ops();
            ops.setConsecutivo(new BigDecimal(frOPS.getText().trim()));
            ops.setVigencia(vigencia);

            listaOps = accesoDatos.consultarObjetoPorVigencia(Ops.class, ops, vigencia);

            if (!listaOps.isEmpty()) {
                ops = listaOps.get(0);
            }

            ops.setFecha(frFecha.getDate());
            ops.setDisponibilidad(consultarDisponibilidad());
            ops.setObjeto(frObjeto.getText());
            ops.setVigencia(vigencia);
            ops.setFechaFinal(frFechaFin.getDate());
            ops.setFechaFirma(frFechaFirma.getDate());
            ops.setFechaInicio(frFechaInicio.getDate());
            ops.setPlazo(frPlazo.getText().trim());

            ops = accesoDatos.persistirActualizar(ops);

            reiniciarFormulario();
        }
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemListaAdicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemListaAdicionesActionPerformed
        Lista_Ops listaOps = new Lista_Ops(this, true);
        listaOps.setLocationRelativeTo(null);
        listaOps.setVisible(true);
    }//GEN-LAST:event_itemListaAdicionesActionPerformed

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void itemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEliminarActionPerformed
        accesoDatos = new AccesoDatos();
        opsController = new OpsJpaController();
        List<Ops> listaOps = new ArrayList<Ops>();
        Ops ops = new Ops();
        ops.setConsecutivo(new BigDecimal(frOPS.getText()));
        ops.setVigencia(vigencia);

        listaOps = accesoDatos.consultarObjetoPorVigencia(Ops.class, ops, vigencia);

        if (!listaOps.isEmpty()) {

            try {
                ops = listaOps.get(0);
                opsController.destroy(ops);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Orden_Prestacion_Servicio_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        reiniciarFormulario();
    }//GEN-LAST:event_itemEliminarActionPerformed

    private void nuevoRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoRegistroMousePressed
        reiniciarFormulario();
    }//GEN-LAST:event_nuevoRegistroMousePressed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        if (validarFormulario()) {
            List<Ops> listaOps = new ArrayList<Ops>();
            Ops ops = new Ops();
            ops.setConsecutivo(new BigDecimal(frOPS.getText().trim()));
            ops.setVigencia(vigencia);

            listaOps = accesoDatos.consultarObjetoPorVigencia(Ops.class, ops, vigencia);

            if (!listaOps.isEmpty()) {
                ops = listaOps.get(0);
            }

            ops.setFecha(frFecha.getDate());
            ops.setDisponibilidad(consultarDisponibilidad());
            ops.setObjeto(frObjeto.getText());
            ops.setVigencia(vigencia);
            ops.setFechaFinal(frFechaFin.getDate());
            ops.setFechaFirma(frFechaFirma.getDate());
            ops.setFechaInicio(frFechaInicio.getDate());
            ops.setPlazo(frPlazo.getText().trim());

            ops = accesoDatos.persistirActualizar(ops);

            reiniciarFormulario();
        }
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void listaRegistrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaRegistrosMousePressed
        Lista_Ops listaOps = new Lista_Ops(this, true);
        listaOps.setLocationRelativeTo(null);
        listaOps.setVisible(true);
    }//GEN-LAST:event_listaRegistrosMousePressed

    private void imprimirRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirRegistroMousePressed
        Generar_Reportes reportes = new Generar_Reportes();

        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<Ops> listaOps = new ArrayList<Ops>();
            Ops ops = new Ops();
            ops.setConsecutivo(new BigDecimal(frOPS.getText()));
            ops.setVigencia(vigencia);

            listaOps = accesoDatos.consultarObjetoPorVigencia(Ops.class, ops, vigencia);

            if (!listaOps.isEmpty()) {
                ops = listaOps.get(0);
                reportes.runReporteOpsPresupuestal(vigencia, ops);
            } else {
                JOptionPane.showMessageDialog(null, "La OPS no existe", "Verificación OPS", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_imprimirRegistroMousePressed

    private void eliminarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarRegistroMousePressed
        accesoDatos = new AccesoDatos();
        opsController = new OpsJpaController();
        List<Ops> listaOps = new ArrayList<Ops>();
        Ops ops = new Ops();
        ops.setConsecutivo(new BigDecimal(frOPS.getText()));
        ops.setVigencia(vigencia);

        listaOps = accesoDatos.consultarObjetoPorVigencia(Ops.class, ops, vigencia);

        if (!listaOps.isEmpty()) {

            try {
                ops = listaOps.get(0);
                opsController.destroy(ops);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Orden_Prestacion_Servicio_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        reiniciarFormulario();
    }//GEN-LAST:event_eliminarRegistroMousePressed

    private void frOPSFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frOPSFocusLost
        if (!frOPS.getText().trim().equals("")) {
            accesoDatos = new AccesoDatos();
            List<Ops> listaOps = new ArrayList<Ops>();
            Ops ops = new Ops();
            ops.setConsecutivo(new BigDecimal(frOPS.getText()));
            ops.setVigencia(vigencia);

            listaOps = accesoDatos.consultarObjetoPorVigencia(Ops.class, ops, vigencia);

            if (!listaOps.isEmpty()) {
                ops = listaOps.get(0);
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedItem(ops.getDisponibilidad().getConsecutivo() + " - " + ops.getDisponibilidad().getBeneficiario().getNombre());
                }
                frFecha.setDate(ops.getFecha());
                frObjeto.setText(ops.getObjeto());
                frFechaInicio.setDate(ops.getFechaInicio());
                frFechaFin.setDate(ops.getFechaFinal());
                frFechaFirma.setDate(ops.getFechaFirma());
                frPlazo.setText(ops.getPlazo().toString());
            } else {
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedIndex(0);
                }
                frFecha.setDate(null);
                frObjeto.setText("");
                frFechaFin.setDate(null);
                frFechaFirma.setDate(null);
                frFechaInicio.setDate(null);
                frPlazo.setText("");
            }
        }
    }//GEN-LAST:event_frOPSFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JLabel eliminarRegistro;
    private com.toedter.calendar.JDateChooser frFecha;
    private com.toedter.calendar.JDateChooser frFechaFin;
    private com.toedter.calendar.JDateChooser frFechaFirma;
    private com.toedter.calendar.JDateChooser frFechaInicio;
    private javax.swing.JComboBox<String> frListaDisponibilidad;
    private javax.swing.JTextField frOPS;
    private javax.swing.JTextArea frObjeto;
    private javax.swing.JTextField frPlazo;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JLabel imprimirRegistro;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemEliminar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemListaAdiciones;
    private javax.swing.JMenuItem itemNuevo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel labelDisponibilidad;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JLabel labelFechaFin;
    private javax.swing.JLabel labelFechaFirma;
    private javax.swing.JLabel labelFechaInicio;
    private javax.swing.JLabel labelNoOPS;
    private javax.swing.JLabel labelObjeto;
    private javax.swing.JLabel labelPlazo;
    private javax.swing.JLabel listaRegistros;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JMenu menuTraslado;
    private javax.swing.JLabel nuevoRegistro;
    // End of variables declaration//GEN-END:variables
}
