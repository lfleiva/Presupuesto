/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.control.TrasladoRubroJpaController;
import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Adicion;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.TipoRubro;
import com.presupuesto.modelo.Traslado;
import com.presupuesto.modelo.TrasladoRubro;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.utilidades.Generar_Reportes;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Traslado_Presupuestal extends javax.swing.JInternalFrame {

    //***** Atributos de la clase *****//
    Home home;
    Vigencia vigencia;
    AccesoDatos accesoDatos;
    TrasladoRubroJpaController trasladoRubroController;

    /**
     * Creates new form Traslado_Presupuestal
     */
    public Traslado_Presupuestal(Home parent) {
        super();
        this.home = parent;

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);

        initComponents();
        consultarVigencia();
        consultarRubrosRegistrados();
        frValor.setText("$0.0");
    }

    /**
     * Metodo que consulta la vigencia activa
     */
    private void consultarVigencia() {        
        vigencia = home.getVigencia();        
    }

    private void consultarRubrosRegistrados() {
        frListaRubros.removeAllItems();
        accesoDatos = new AccesoDatos();
        List<Rubro> listaRubros = new ArrayList<Rubro>();
        Rubro rubro = new Rubro();
        rubro.setVigencia(vigencia);
        rubro.setTipoRubro(new TipoRubro(new BigDecimal(3)));
        listaRubros = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia);

        if (listaRubros != null && !listaRubros.isEmpty()) {
            frListaRubros.removeAllItems();
            frListaRubros.addItem("-- Seleccione --");
            for (Rubro rubroIterado : listaRubros) {
                frListaRubros.addItem(rubroIterado.getCodigo() + " - " + rubroIterado.getNombre().trim());
            }
        }
    }

    private void cargarTablaTraslado(Traslado traslado) {
        eliminarElementosTabla();

        if (!traslado.getTrasladoRubroList().isEmpty()) {
            List<TrasladoRubro> listaTrasladoRubro = new ArrayList<TrasladoRubro>();
            listaTrasladoRubro = traslado.getTrasladoRubroList();

            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaTraslado.getModel();
            for (TrasladoRubro trasladoRubro : listaTrasladoRubro) {
                model.addRow(new Object[]{trasladoRubro.getRubro().getCodigo() + " - " + trasladoRubro.getRubro().getNombre(), trasladoRubro.getTipo(),"$" + formatoNumeroDecimales(trasladoRubro.getValor().toString())});
            }
        }
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

    private void eliminarElementosTabla() {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaTraslado.getModel();

        int numeroFilas = model.getRowCount();

        while (numeroFilas > 0) {
            model.removeRow(0);
            model = (DefaultTableModel) tablaTraslado.getModel();
            numeroFilas = model.getRowCount();
        }
    }

    private Boolean verificarRubro(String codigo) {
        Boolean verificacionExitosa = true;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaTraslado.getModel();
        int numeroFinal = model.getRowCount();

        for (int i = 0; i < numeroFinal; i++) {
            String primeraColumna = model.getValueAt(i, 0).toString();
            if (codigo.equals(primeraColumna)) {
                verificacionExitosa = false;
                break;
            }
        }

        return verificacionExitosa;
    }

    private boolean validarFormulario() {
        boolean validacionExitosa = true;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaTraslado.getModel();
        int numeroFilas = model.getRowCount();

        if (frNoDocumento.getText().trim().equals("") || frFecha.getDate() == null || frDescripcion.getText().trim().equals("") || numeroFilas <= 0) {
            validacionExitosa = false;
        }

        return validacionExitosa;
    }
    
    private Rubro consultarRubro(String codigo) {
        accesoDatos = new AccesoDatos();
        Rubro rubro = new Rubro();
        rubro.setCodigo(codigo.trim());
        rubro.setVigencia(vigencia);

        List<Rubro> listaRubro = new ArrayList<Rubro>();

        listaRubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia);

        if (!listaRubro.isEmpty()) {
            rubro = listaRubro.get(0);
        }
        return rubro;
    }
    
    private BigDecimal obtenerValorRubro(String valor) {
        valor = valor.trim();
        valor = valor.replace(",", "");
        valor = valor.replace("$", "");
        BigDecimal valorNumerico = BigDecimal.valueOf(Double.parseDouble(valor));;
        return valorNumerico;
    }
    
    private void reiniciarFormulario() {
        frNoDocumento.setText("");
        frFecha.setDate(null);
        frDescripcion.setText("");
        frListaRubros.setSelectedIndex(0);
        frListaTipoTraslado.setSelectedIndex(0);
        frValor.setText("$0.0");
        eliminarElementosTabla();
        frNoDocumento.requestFocus();
    }
    
    private void eliminarTrasladodRubro(Traslado traslado) {
        while (!traslado.getTrasladoRubroList().isEmpty()) {
            try {
                trasladoRubroController = new TrasladoRubroJpaController();
                TrasladoRubro trasladoRubro = new TrasladoRubro();
                trasladoRubro = traslado.getTrasladoRubroList().get(0);

                trasladoRubroController.destroy(trasladoRubro);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Disponibilidad_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
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
        listaTraslado = new javax.swing.JLabel();
        imprimirTraslado = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        eliminarRegistro = new javax.swing.JLabel();
        labelDocumento = new javax.swing.JLabel();
        frNoDocumento = new javax.swing.JTextField();
        labelFecha = new javax.swing.JLabel();
        frFecha = new com.toedter.calendar.JDateChooser();
        labelDescripcion = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        frDescripcion = new javax.swing.JTextArea();
        labelRubro = new javax.swing.JLabel();
        frListaRubros = new javax.swing.JComboBox<>();
        labelRubro1 = new javax.swing.JLabel();
        frListaTipoTraslado = new javax.swing.JComboBox<>();
        labelRubro2 = new javax.swing.JLabel();
        frValor = new javax.swing.JTextField();
        botonRegistrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTraslado = new javax.swing.JTable();
        botonQuitar = new javax.swing.JButton();
        barraMenu = new javax.swing.JMenuBar();
        menuTraslado = new javax.swing.JMenu();
        itemNuevo = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        itemListaAdiciones = new javax.swing.JMenuItem();
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

        listaTraslado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        listaTraslado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/lista.png"))); // NOI18N
        listaTraslado.setAlignmentX(0.5F);
        listaTraslado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        listaTraslado.setMaximumSize(new java.awt.Dimension(25, 20));
        listaTraslado.setMinimumSize(new java.awt.Dimension(25, 20));
        listaTraslado.setPreferredSize(new java.awt.Dimension(25, 20));
        listaTraslado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaTrasladoMousePressed(evt);
            }
        });
        barraHerramientas.add(listaTraslado);

        imprimirTraslado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imprimirTraslado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/imprimir.png"))); // NOI18N
        imprimirTraslado.setAlignmentX(0.5F);
        imprimirTraslado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        imprimirTraslado.setMaximumSize(new java.awt.Dimension(25, 20));
        imprimirTraslado.setMinimumSize(new java.awt.Dimension(25, 20));
        imprimirTraslado.setPreferredSize(new java.awt.Dimension(25, 20));
        imprimirTraslado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                imprimirTrasladoMousePressed(evt);
            }
        });
        barraHerramientas.add(imprimirTraslado);
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

        labelDocumento.setText("No. Documento");

        frNoDocumento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frNoDocumentoFocusLost(evt);
            }
        });

        labelFecha.setText("fecha");

        labelDescripcion.setText("Descripcion");

        frDescripcion.setColumns(20);
        frDescripcion.setLineWrap(true);
        frDescripcion.setRows(3);
        frDescripcion.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane2.setViewportView(frDescripcion);

        labelRubro.setText("Rubro");

        frListaRubros.setToolTipText("");

        labelRubro1.setText("Tipo de traslado");

        frListaTipoTraslado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Credito", "Contracredito" }));

        labelRubro2.setText("Valor");

        frValor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                frValorFocusGained(evt);
            }
        });
        frValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                frValorKeyTyped(evt);
            }
        });

        botonRegistrar.setText("Registrar");
        botonRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRegistrarActionPerformed(evt);
            }
        });

        tablaTraslado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rubro", "Tipo Traslado", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaTraslado);

        botonQuitar.setText("Quitar");
        botonQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonQuitarActionPerformed(evt);
            }
        });

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuTraslado.setText("Inicio");

        itemNuevo.setText("Nuevo Traslado");
        itemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNuevoActionPerformed(evt);
            }
        });
        menuTraslado.add(itemNuevo);

        itemGuardar.setText("Guardar Traslado");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuTraslado.add(itemGuardar);

        itemListaAdiciones.setText("Lista Traslados");
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

        itemEliminar.setText("Eliminar Traslado");
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
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelRubro, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelDescripcion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelFecha, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelDocumento, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonQuitar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(botonRegistrar)
                                    .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(frNoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(frListaRubros, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(labelRubro1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(frListaTipoTraslado, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(labelRubro2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(frValor, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane2))
                                .addGap(0, 1, Short.MAX_VALUE)))
                        .addGap(95, 95, 95))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frNoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDocumento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelDescripcion)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRubro)
                    .addComponent(frListaRubros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRubro1)
                    .addComponent(frListaTipoTraslado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRubro2)
                    .addComponent(frValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(botonRegistrar)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonQuitar)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevoActionPerformed

    }//GEN-LAST:event_itemNuevoActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed

    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemListaAdicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemListaAdicionesActionPerformed

    }//GEN-LAST:event_itemListaAdicionesActionPerformed

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void itemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEliminarActionPerformed

    }//GEN-LAST:event_itemEliminarActionPerformed

    private void nuevoRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoRegistroMousePressed
        frDescripcion.setText("");
        frFecha.setDate(null);
        frListaRubros.setSelectedIndex(0);
        frListaTipoTraslado.setSelectedIndex(0);
        frNoDocumento.setText("");
        frValor.setText("$0.0");
        eliminarElementosTabla();
        frNoDocumento.requestFocus();
    }//GEN-LAST:event_nuevoRegistroMousePressed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<Traslado> listaTraslado = new ArrayList<Traslado>();
            String documento = frNoDocumento.getText().trim();
            Traslado traslado = new Traslado();
            traslado.setDocumento(documento);

            listaTraslado = accesoDatos.consultarObjetoPorVigencia(Traslado.class, traslado, vigencia);

            if (!listaTraslado.isEmpty()) {
                traslado = listaTraslado.get(0);
                if(!traslado.getTrasladoRubroList().isEmpty()) {
                    eliminarTrasladodRubro(traslado);
                }
            }

            traslado.setFecha(frFecha.getDate());
            traslado.setDescripcion(frDescripcion.getText().trim());
            traslado.setVigencia(vigencia);

            traslado = accesoDatos.persistirActualizar(traslado);

            // Se registra traslado - rubro
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaTraslado.getModel();
            int numeroFilas = model.getRowCount();
            List<TrasladoRubro> listaTrasladoRubro = new ArrayList<TrasladoRubro>();
            if (numeroFilas > 0) {
                listaTrasladoRubro = new ArrayList<TrasladoRubro>();
                accesoDatos = new AccesoDatos();
                for (int i = 0; i < numeroFilas; i++) {
                    Rubro rubro = new Rubro();
                    TrasladoRubro trasladoRubro = new TrasladoRubro();
                    String rubroTabla = model.getValueAt(i, 0).toString();
                    String tipoTraslado = model.getValueAt(i, 1).toString();
                    String valorTraslado = model.getValueAt(i, 2).toString();
                    // Se consulta el rubroregitrado en la tabla
                    String codigoRubro = rubroTabla.substring(0, rubroTabla.indexOf("-") - 1);
                    rubro = consultarRubro(codigoRubro);
                    BigDecimal valorTR = obtenerValorRubro(valorTraslado);
                    trasladoRubro.setTraslado(traslado);
                    trasladoRubro.setRubro(rubro);
                    trasladoRubro.setTipo(tipoTraslado);
                    trasladoRubro.setValor(valorTR);
                    trasladoRubro.setVigencia(vigencia);
                    listaTrasladoRubro.add(trasladoRubro);
                }
                accesoDatos.persistirActualizar(listaTrasladoRubro);
                reiniciarFormulario();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void listaTrasladoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaTrasladoMousePressed
        // Abrir Lista
        Lista_Traslados listaTraslados = new Lista_Traslados(this, true);
        listaTraslados.setLocationRelativeTo(null);
        listaTraslados.setVisible(true);
    }//GEN-LAST:event_listaTrasladoMousePressed

    private void imprimirTrasladoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirTrasladoMousePressed
        Generar_Reportes reportes = new Generar_Reportes();
        
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            Traslado traslado = new Traslado();
            List<Traslado> listaTraslado = new ArrayList<Traslado>();

            if (!frNoDocumento.getText().trim().equals("")) {
                traslado.setVigencia(vigencia);
                traslado.setDocumento(frNoDocumento.getText().trim());

                listaTraslado = accesoDatos.consultarObjetoPorVigencia(Traslado.class, traslado, vigencia);

                if (!listaTraslado.isEmpty()) {
                    traslado = listaTraslado.get(0);
                    reportes.runReporteTraslado(vigencia, traslado);
                }
            }
        }
    }//GEN-LAST:event_imprimirTrasladoMousePressed

    private void eliminarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarRegistroMousePressed

    }//GEN-LAST:event_eliminarRegistroMousePressed

    private void frNoDocumentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frNoDocumentoFocusLost
        accesoDatos = new AccesoDatos();
        List<Traslado> listaTraslado = new ArrayList<Traslado>();
        String documento = frNoDocumento.getText().trim();
        Traslado traslado = new Traslado();
        traslado.setDocumento(documento);

        listaTraslado = accesoDatos.consultarObjetoPorVigencia(Traslado.class, traslado, vigencia);

        if (!listaTraslado.isEmpty()) {
            traslado = listaTraslado.get(0);

            frFecha.setDate(traslado.getFecha());
            frDescripcion.setText(traslado.getDescripcion());

            cargarTablaTraslado(traslado);
        } else {
            frFecha.setDate(null);
            frDescripcion.setText("");
            frListaRubros.setSelectedIndex(0);
            frListaTipoTraslado.setSelectedIndex(0);
            frValor.setText("$0.0");
            eliminarElementosTabla();
        }
    }//GEN-LAST:event_frNoDocumentoFocusLost

    private void botonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegistrarActionPerformed
        String rubroSeleccionado = frListaRubros.getSelectedItem().toString();

        if (!rubroSeleccionado.equals("-- Seleccionar --")) {
            if (!frValor.getText().trim().equals("") && !frValor.getText().trim().equals("$0.0")) {
                if (verificarRubro(rubroSeleccionado)) {
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) tablaTraslado.getModel();
                    model.addRow(new Object[]{rubroSeleccionado, frListaTipoTraslado.getSelectedItem().toString(), frValor.getText()});
                    frValor.setText("$0.0");
                    frListaRubros.setSelectedIndex(0);
                    frListaTipoTraslado.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(null, "El rubro ya fue registrado en la tabla", "Verificación Traslado", 0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe digitar un valor para el traslado", "Verificación Traslado", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un rubro de la lista", "Verificación Traslado", 0);
        }
    }//GEN-LAST:event_botonRegistrarActionPerformed

    private void botonQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonQuitarActionPerformed
        int filaSeleccionada = tablaTraslado.getSelectedRow();

        if (filaSeleccionada != -1) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaTraslado.getModel();
            // Se elimina de la tabla
            model.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(null, "Para eliminar un registro debe seleccionar una fila", "Eliminar Registro", 0);
        }
    }//GEN-LAST:event_botonQuitarActionPerformed

    private void frValorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_frValorKeyTyped
        char tecla = evt.getKeyChar();

        if ((tecla < '0' || tecla > '9') && (tecla != '.' && tecla != ',')) {
            evt.consume();
        } else {
            String valor = frValor.getText();
            valor = valor.replace("$", "");
            valor = valor + tecla;
            if (tecla != '.') {
                valor = formatoNumeroDecimales(valor);
            }
            valor = "$" + valor;
            frValor.setText(valor);
            evt.consume();
        }
    }//GEN-LAST:event_frValorKeyTyped

    private void frValorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frValorFocusGained
        if (frValor.getText().equals("$0.0")) {
            frValor.setText("");
        }
    }//GEN-LAST:event_frValorFocusGained

    public void cargarTrasladoSeleccionado(String documento) {
        accesoDatos = new AccesoDatos();
        Traslado traslado = new Traslado();        
        traslado.setVigencia(vigencia);
        traslado.setDocumento(documento);
        traslado = accesoDatos.consultarObjetoPorVigencia(Traslado.class, traslado, vigencia).get(0);

        frNoDocumento.setText(traslado.getDocumento());
        frFecha.setDate(traslado.getFecha());
        frListaRubros.setSelectedItem(0);
        frValor.setText("$0.0");
        if (traslado.getDescripcion() != null) {
            if (!traslado.getDescripcion().trim().equals("")) {
                frDescripcion.setText(traslado.getDescripcion());
            }
        }
        cargarTablaTraslado(traslado);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JButton botonQuitar;
    private javax.swing.JButton botonRegistrar;
    private javax.swing.JLabel eliminarRegistro;
    private javax.swing.JTextArea frDescripcion;
    private com.toedter.calendar.JDateChooser frFecha;
    private javax.swing.JComboBox<String> frListaRubros;
    private javax.swing.JComboBox<String> frListaTipoTraslado;
    private javax.swing.JTextField frNoDocumento;
    private javax.swing.JTextField frValor;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JLabel imprimirTraslado;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemEliminar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemListaAdiciones;
    private javax.swing.JMenuItem itemNuevo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelDocumento;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JLabel labelRubro;
    private javax.swing.JLabel labelRubro1;
    private javax.swing.JLabel labelRubro2;
    private javax.swing.JLabel listaTraslado;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JMenu menuTraslado;
    private javax.swing.JLabel nuevoRegistro;
    private javax.swing.JTable tablaTraslado;
    // End of variables declaration//GEN-END:variables
}
