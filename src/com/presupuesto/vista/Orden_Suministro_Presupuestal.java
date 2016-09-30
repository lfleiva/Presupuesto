/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.control.OrdenSuministroJpaController;
import com.presupuesto.control.SuministroJpaController;
import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.Ops;
import com.presupuesto.modelo.OrdenSuministro;
import com.presupuesto.modelo.Suministro;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.utilidades.Generar_Reportes;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Orden_Suministro_Presupuestal extends javax.swing.JInternalFrame {

    Home home;
    AccesoDatos accesoDatos;
    Vigencia vigencia;
    OrdenSuministroJpaController ordenSuministroController;

    /**
     * Creates new form Orden_Suministro_Presupuestal
     */
    public Orden_Suministro_Presupuestal(Home parent) {
        super();
        this.home = parent;

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);
        consultarVigencia();
        initComponents();

        frOrden.setText(consultarConsecutivo().toString());
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
        ordenSuministroController = new OrdenSuministroJpaController();

        consecutivo = ordenSuministroController.consultarMaximoOrdenSuministro(vigencia).add(new BigDecimal(1));

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
        frOrden.setText(consultarConsecutivo().toString());
        if (frListaDisponibilidad.getItemCount() > 0) {
            frListaDisponibilidad.setSelectedIndex(0);
        }
        frFecha.setDate(null);
        frObjeto.setText("");
        frOrden.requestFocus();
        frDetalle.setText("");
        frUnidad.setText("");
        frCantidad.setValue(Integer.valueOf("0"));
        eliminarElementosTabla();
    }

    private boolean validarFormulario() {
        boolean validacionExitosa = true;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaSuministros.getModel();
        int numerofilas = model.getRowCount();

        if (frOrden.getText().trim().equals("") || frFecha.getDate() == null || frObjeto.getText().trim().equals("") || numerofilas <= 0) {
            validacionExitosa = false;
        }

        return validacionExitosa;
    }

    private void eliminarElementosTabla() {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaSuministros.getModel();

        int numeroFilas = model.getRowCount();

        while (numeroFilas > 0) {
            model.removeRow(0);
            model = (DefaultTableModel) tablaSuministros.getModel();
            numeroFilas = model.getRowCount();
        }
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

    private void eliminarSuministros(OrdenSuministro orden) {
        SuministroJpaController suministroController = new SuministroJpaController();

        while (!orden.getSuministroList().isEmpty()) {
            try {
                Suministro suministro = new Suministro();
                suministro = orden.getSuministroList().get(0);
                suministroController.destroy(suministro);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Orden_Suministro_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarOrdenSeleccionada(String ordenSeleccionada) {
        if (!ordenSeleccionada.trim().equals("")) {
            accesoDatos = new AccesoDatos();
            List<OrdenSuministro> listaOrdenes = new ArrayList<OrdenSuministro>();
            OrdenSuministro orden = new OrdenSuministro();

            orden.setConsecutivo(new BigDecimal(ordenSeleccionada.trim()));
            orden.setVigencia(vigencia);

            listaOrdenes = accesoDatos.consultarObjetoPorVigencia(OrdenSuministro.class, orden, vigencia);

            if (!listaOrdenes.isEmpty()) {
                orden = listaOrdenes.get(0);

                frOrden.setText(orden.getConsecutivo().toString());
                frCantidad.setValue(0);
                frDetalle.setText("");
                frFecha.setDate(orden.getFecha());
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedItem(orden.getDisponibilidad().getConsecutivo() + " - " + orden.getDisponibilidad().getBeneficiario().getNombre());
                }
                frObjeto.setText(orden.getObjeto());
                frUnidad.setText("");

                eliminarElementosTabla();

                if (!orden.getSuministroList().isEmpty()) {
                    List<Suministro> listaSuministros = new ArrayList<Suministro>();
                    listaSuministros = orden.getSuministroList();
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) tablaSuministros.getModel();

                    for (Suministro suministros : listaSuministros) {
                        model.addRow(new Object[]{suministros.getDetalle(), suministros.getUnidad(), suministros.getCantidad()});
                    }
                }
            } else {
                frCantidad.setValue(0);
                frDetalle.setText("");
                frFecha.setDate(null);
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedIndex(0);
                }
                frObjeto.setText("");
                frUnidad.setText("");

                eliminarElementosTabla();
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
        nuevaOrden = new javax.swing.JLabel();
        guardarOrden = new javax.swing.JLabel();
        mostrarOrdenes = new javax.swing.JLabel();
        imprimirOrden = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        eliminarOrden = new javax.swing.JLabel();
        frListaDisponibilidad = new javax.swing.JComboBox<>();
        frFecha = new com.toedter.calendar.JDateChooser();
        labelFecha = new javax.swing.JLabel();
        labelObjeto = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        frObjeto = new javax.swing.JTextArea();
        labelNoOrden = new javax.swing.JLabel();
        frOrden = new javax.swing.JTextField();
        labelDisponibilidad = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaSuministros = new javax.swing.JTable();
        botonRegistrar = new javax.swing.JButton();
        botonQuitar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        frDetalle = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        frUnidad = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        frCantidad = new javax.swing.JSpinner();
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

        nuevaOrden.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nuevaOrden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/nuevo_registro.png"))); // NOI18N
        nuevaOrden.setAlignmentX(0.5F);
        nuevaOrden.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        nuevaOrden.setMaximumSize(new java.awt.Dimension(25, 20));
        nuevaOrden.setMinimumSize(new java.awt.Dimension(25, 20));
        nuevaOrden.setPreferredSize(new java.awt.Dimension(25, 20));
        nuevaOrden.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nuevaOrdenMousePressed(evt);
            }
        });
        barraHerramientas.add(nuevaOrden);

        guardarOrden.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guardarOrden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/guardar.png"))); // NOI18N
        guardarOrden.setAlignmentX(0.5F);
        guardarOrden.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        guardarOrden.setMaximumSize(new java.awt.Dimension(25, 20));
        guardarOrden.setMinimumSize(new java.awt.Dimension(25, 20));
        guardarOrden.setPreferredSize(new java.awt.Dimension(25, 20));
        guardarOrden.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                guardarOrdenMousePressed(evt);
            }
        });
        barraHerramientas.add(guardarOrden);

        mostrarOrdenes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mostrarOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/lista.png"))); // NOI18N
        mostrarOrdenes.setAlignmentX(0.5F);
        mostrarOrdenes.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        mostrarOrdenes.setMaximumSize(new java.awt.Dimension(25, 20));
        mostrarOrdenes.setMinimumSize(new java.awt.Dimension(25, 20));
        mostrarOrdenes.setPreferredSize(new java.awt.Dimension(25, 20));
        mostrarOrdenes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mostrarOrdenesMousePressed(evt);
            }
        });
        barraHerramientas.add(mostrarOrdenes);

        imprimirOrden.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imprimirOrden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/imprimir.png"))); // NOI18N
        imprimirOrden.setAlignmentX(0.5F);
        imprimirOrden.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        imprimirOrden.setMaximumSize(new java.awt.Dimension(25, 20));
        imprimirOrden.setMinimumSize(new java.awt.Dimension(25, 20));
        imprimirOrden.setPreferredSize(new java.awt.Dimension(25, 20));
        imprimirOrden.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                imprimirOrdenMousePressed(evt);
            }
        });
        barraHerramientas.add(imprimirOrden);
        barraHerramientas.add(jSeparator1);

        eliminarOrden.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eliminarOrden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/eliminar.png"))); // NOI18N
        eliminarOrden.setAlignmentX(0.5F);
        eliminarOrden.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        eliminarOrden.setMaximumSize(new java.awt.Dimension(25, 20));
        eliminarOrden.setMinimumSize(new java.awt.Dimension(25, 20));
        eliminarOrden.setPreferredSize(new java.awt.Dimension(25, 20));
        eliminarOrden.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                eliminarOrdenMousePressed(evt);
            }
        });
        barraHerramientas.add(eliminarOrden);

        labelFecha.setText("Fecha");

        labelObjeto.setText("Objeto");

        frObjeto.setColumns(20);
        frObjeto.setLineWrap(true);
        frObjeto.setRows(5);
        frObjeto.setMinimumSize(new java.awt.Dimension(100, 20));
        jScrollPane1.setViewportView(frObjeto);

        labelNoOrden.setText("No. Orden");

        frOrden.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frOrdenFocusLost(evt);
            }
        });

        labelDisponibilidad.setText("Disponibilidad");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Suministros"));

        tablaSuministros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Detalle", "Unidad de Medida", "Cantidad"
            }
        ));
        jScrollPane2.setViewportView(tablaSuministros);

        botonRegistrar.setText("Registrar");
        botonRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRegistrarActionPerformed(evt);
            }
        });

        botonQuitar.setText("Quitar");
        botonQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonQuitarActionPerformed(evt);
            }
        });

        jLabel1.setText("Detalle");

        jLabel2.setText("Unidad");

        jLabel3.setText("Cantidad");

        frCantidad.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonQuitar)
                            .addComponent(botonRegistrar))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frDetalle)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(frDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(frUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(frCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonRegistrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonQuitar)
                .addGap(5, 5, 5))
        );

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuTraslado.setText("Orden de Suministro");

        itemNuevo.setText("Nueva Orden");
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

        itemListaAdiciones.setText("Lista Orden Suministro");
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
            .addComponent(barraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelObjeto, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelFecha, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelDisponibilidad, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelNoOrden, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(frOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frListaDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNoOrden))
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevoActionPerformed
        reiniciarFormulario();
    }//GEN-LAST:event_itemNuevoActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<OrdenSuministro> listaOrden = new ArrayList<OrdenSuministro>();
            OrdenSuministro orden = new OrdenSuministro();
            orden.setConsecutivo(new BigDecimal(frOrden.getText().trim()));
            orden.setVigencia(vigencia);

            listaOrden = accesoDatos.consultarObjetoPorVigencia(OrdenSuministro.class, orden, vigencia);

            if (!listaOrden.isEmpty()) {
                orden = listaOrden.get(0);

                if (!orden.getSuministroList().isEmpty()) {
                    eliminarSuministros(orden);
                }
            }

            orden.setFecha(frFecha.getDate());
            orden.setObjeto(frObjeto.getText().trim());
            orden.setDisponibilidad(consultarDisponibilidad());

            orden = accesoDatos.persistirActualizar(orden);

            // Registro de suministros
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaSuministros.getModel();

            int numeroFilas = model.getRowCount();

            if (numeroFilas > 0) {
                for (int i = 0; i < numeroFilas; i++) {
                    Suministro suministro = new Suministro();
                    suministro.setOrdenSuministro(orden);
                    suministro.setDetalle(model.getValueAt(i, 0).toString().trim());
                    suministro.setUnidad(model.getValueAt(i, 1).toString());
                    suministro.setCantidad(new BigDecimal(model.getValueAt(i, 2).toString()));
                    suministro.setVigencia(vigencia);

                    suministro = accesoDatos.persistirActualizar(suministro);
                }
            }

            reiniciarFormulario();
        } else {
            JOptionPane.showMessageDialog(null, "Validar que todos los campos del formulario esten diligenciados", "Verificación Orden de Suministro", 0);
        }
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemListaAdicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemListaAdicionesActionPerformed
        Lista_Orden_Suministro listaOrden = new Lista_Orden_Suministro(this, true);
        listaOrden.setLocationRelativeTo(null);
        listaOrden.setVisible(true);
    }//GEN-LAST:event_itemListaAdicionesActionPerformed

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void itemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEliminarActionPerformed
        if (validarFormulario()) {
            ordenSuministroController = new OrdenSuministroJpaController();
            accesoDatos = new AccesoDatos();
            List<OrdenSuministro> listaOrden = new ArrayList<OrdenSuministro>();
            OrdenSuministro orden = new OrdenSuministro();
            orden.setConsecutivo(new BigDecimal(frOrden.getText()));
            orden.setVigencia(vigencia);

            listaOrden = accesoDatos.consultarObjetoPorVigencia(OrdenSuministro.class, orden, vigencia);

            if (!listaOrden.isEmpty()) {
                try {
                    orden = listaOrden.get(0);
                    ordenSuministroController.destroy(orden);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Orden_Suministro_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "La Orden de Suministro no existe", "Verificación Orden Suministro", 0);
            }

            reiniciarFormulario();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_itemEliminarActionPerformed

    private void nuevaOrdenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevaOrdenMousePressed
        reiniciarFormulario();
    }//GEN-LAST:event_nuevaOrdenMousePressed

    private void guardarOrdenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarOrdenMousePressed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<OrdenSuministro> listaOrden = new ArrayList<OrdenSuministro>();
            OrdenSuministro orden = new OrdenSuministro();
            orden.setConsecutivo(new BigDecimal(frOrden.getText().trim()));
            orden.setVigencia(vigencia);

            listaOrden = accesoDatos.consultarObjetoPorVigencia(OrdenSuministro.class, orden, vigencia);

            if (!listaOrden.isEmpty()) {
                orden = listaOrden.get(0);

                if (!orden.getSuministroList().isEmpty()) {
                    eliminarSuministros(orden);
                }
            }

            orden.setFecha(frFecha.getDate());
            orden.setObjeto(frObjeto.getText().trim());
            orden.setDisponibilidad(consultarDisponibilidad());

            orden = accesoDatos.persistirActualizar(orden);

            // Registro de suministros
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaSuministros.getModel();

            int numeroFilas = model.getRowCount();

            if (numeroFilas > 0) {
                for (int i = 0; i < numeroFilas; i++) {
                    Suministro suministro = new Suministro();
                    suministro.setOrdenSuministro(orden);
                    suministro.setDetalle(model.getValueAt(i, 0).toString().trim());
                    suministro.setUnidad(model.getValueAt(i, 1).toString());
                    suministro.setCantidad(new BigDecimal(model.getValueAt(i, 2).toString()));
                    suministro.setVigencia(vigencia);

                    suministro = accesoDatos.persistirActualizar(suministro);
                }
            }

            reiniciarFormulario();
        } else {
            JOptionPane.showMessageDialog(null, "Validar que todos los campos del formulario esten diligenciados", "Verificación Orden de Suministro", 0);
        }
    }//GEN-LAST:event_guardarOrdenMousePressed

    private void mostrarOrdenesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mostrarOrdenesMousePressed
        Lista_Orden_Suministro listaOrden = new Lista_Orden_Suministro(this, true);
        listaOrden.setLocationRelativeTo(null);
        listaOrden.setVisible(true);
    }//GEN-LAST:event_mostrarOrdenesMousePressed

    private void imprimirOrdenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirOrdenMousePressed
        Generar_Reportes reportes = new Generar_Reportes();

        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<OrdenSuministro> listaOrden = new ArrayList<OrdenSuministro>();
            OrdenSuministro orden = new OrdenSuministro();
            orden.setConsecutivo(new BigDecimal(frOrden.getText()));
            orden.setVigencia(vigencia);

            listaOrden = accesoDatos.consultarObjetoPorVigencia(OrdenSuministro.class, orden, vigencia);

            if (!listaOrden.isEmpty()) {
                orden = listaOrden.get(0);
                reportes.runReporteOrdenSuministro(vigencia, orden);
            } else {
                JOptionPane.showMessageDialog(null, "La Orden de Suministro no existe", "Verificación Orden Suministro", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_imprimirOrdenMousePressed

    private void eliminarOrdenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarOrdenMousePressed

        if (validarFormulario()) {
            ordenSuministroController = new OrdenSuministroJpaController();
            accesoDatos = new AccesoDatos();
            List<OrdenSuministro> listaOrden = new ArrayList<OrdenSuministro>();
            OrdenSuministro orden = new OrdenSuministro();
            orden.setConsecutivo(new BigDecimal(frOrden.getText()));
            orden.setVigencia(vigencia);

            listaOrden = accesoDatos.consultarObjetoPorVigencia(OrdenSuministro.class, orden, vigencia);

            if (!listaOrden.isEmpty()) {
                try {
                    orden = listaOrden.get(0);
                    ordenSuministroController.destroy(orden);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Orden_Suministro_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "La Orden de Suministro no existe", "Verificación Orden Suministro", 0);
            }

            reiniciarFormulario();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_eliminarOrdenMousePressed

    private void frOrdenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frOrdenFocusLost
        if (!frOrden.getText().trim().equals("")) {
            accesoDatos = new AccesoDatos();
            List<OrdenSuministro> listaOrdenes = new ArrayList<OrdenSuministro>();
            OrdenSuministro orden = new OrdenSuministro();

            orden.setConsecutivo(new BigDecimal(frOrden.getText().trim()));
            orden.setVigencia(vigencia);

            listaOrdenes = accesoDatos.consultarObjetoPorVigencia(OrdenSuministro.class, orden, vigencia);

            if (!listaOrdenes.isEmpty()) {
                orden = listaOrdenes.get(0);

                frCantidad.setValue(0);
                frDetalle.setText("");
                frFecha.setDate(orden.getFecha());
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedItem(orden.getDisponibilidad().getConsecutivo() + " - " + orden.getDisponibilidad().getBeneficiario().getNombre());
                }
                frObjeto.setText(orden.getObjeto());
                frUnidad.setText("");

                eliminarElementosTabla();

                if (!orden.getSuministroList().isEmpty()) {
                    List<Suministro> listaSuministros = new ArrayList<Suministro>();
                    listaSuministros = orden.getSuministroList();
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) tablaSuministros.getModel();

                    for (Suministro suministros : listaSuministros) {
                        model.addRow(new Object[]{suministros.getDetalle(), suministros.getUnidad(), suministros.getCantidad()});
                    }
                }
            } else {
                frCantidad.setValue(0);
                frDetalle.setText("");
                frFecha.setDate(null);
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedIndex(0);
                }
                frObjeto.setText("");
                frUnidad.setText("");

                eliminarElementosTabla();
            }
        }
    }//GEN-LAST:event_frOrdenFocusLost

    private void botonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegistrarActionPerformed
        if (!frDetalle.getText().trim().equals("") && !frUnidad.getText().trim().equals("") && !frCantidad.getValue().toString().equals("0")) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaSuministros.getModel();

            model.addRow(new Object[]{frDetalle.getText().trim(), frUnidad.getText().trim(), frCantidad.getValue().toString().trim()});

            frDetalle.setText("");
            frUnidad.setText("");
            frCantidad.setValue(Integer.valueOf("0"));
        } else {
            JOptionPane.showMessageDialog(null, "Por favor ingrese toda la informacion del suministro", "Verificación Suministro", 0);
        }
    }//GEN-LAST:event_botonRegistrarActionPerformed

    private void botonQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonQuitarActionPerformed
        int filaSeleccionada = tablaSuministros.getSelectedRow();

        if (filaSeleccionada != -1) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaSuministros.getModel();
            // Se elimina de la tabla
            model.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(null, "Para eliminar un registro debe seleccionar una fila", "Eliminar Registro", 0);
        }
    }//GEN-LAST:event_botonQuitarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JButton botonQuitar;
    private javax.swing.JButton botonRegistrar;
    private javax.swing.JLabel eliminarOrden;
    private javax.swing.JSpinner frCantidad;
    private javax.swing.JTextField frDetalle;
    private com.toedter.calendar.JDateChooser frFecha;
    private javax.swing.JComboBox<String> frListaDisponibilidad;
    private javax.swing.JTextArea frObjeto;
    private javax.swing.JTextField frOrden;
    private javax.swing.JTextField frUnidad;
    private javax.swing.JLabel guardarOrden;
    private javax.swing.JLabel imprimirOrden;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemEliminar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemListaAdiciones;
    private javax.swing.JMenuItem itemNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel labelDisponibilidad;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JLabel labelNoOrden;
    private javax.swing.JLabel labelObjeto;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JMenu menuTraslado;
    private javax.swing.JLabel mostrarOrdenes;
    private javax.swing.JLabel nuevaOrden;
    private javax.swing.JTable tablaSuministros;
    // End of variables declaration//GEN-END:variables
}
