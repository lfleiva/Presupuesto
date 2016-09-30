/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.control.AdicionJpaController;
import com.presupuesto.control.AdicionRubroJpaController;
import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Adicion;
import com.presupuesto.modelo.AdicionRubro;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.TipoRubro;
import com.presupuesto.modelo.Vigencia;
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
public class Adicion_Presupuestal extends javax.swing.JInternalFrame {

    //***** Atributos de la clase *****//
    Home home;
    Vigencia vigencia;
    AccesoDatos accesoDatos;
    AdicionRubroJpaController adicionRubroController;

    /**
     * Creates new form Adicion_Presupuestal
     */
    public Adicion_Presupuestal(Home parent) {
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
        accesoDatos = new AccesoDatos();
        vigencia = new Vigencia();
        vigencia.setActiva(true);
        vigencia = accesoDatos.consultarTodos(vigencia, Vigencia.class).get(0);
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

    private void cargarTablaAdiciones(Adicion adicion) {
        eliminarElementosTabla();

        if (!adicion.getAdicionRubroList().isEmpty()) {
            List<AdicionRubro> listaAdicionRubro = new ArrayList<AdicionRubro>();
            listaAdicionRubro = adicion.getAdicionRubroList();

            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaAdicion.getModel();
            for (AdicionRubro adicionRubro : listaAdicionRubro) {
                model.addRow(new Object[]{adicionRubro.getRubro().getCodigo() + " - " + adicionRubro.getRubro().getNombre(), "$" + formatoNumeroDecimales(adicionRubro.getValor().toString())});
            }
        }
    }

    private void eliminarElementosTabla() {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaAdicion.getModel();

        int numeroFilas = model.getRowCount();

        while (numeroFilas > 0) {
            model.removeRow(0);
            model = (DefaultTableModel) tablaAdicion.getModel();
            numeroFilas = model.getRowCount();
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

    private Boolean verificarRubro(String codigo) {
        Boolean verificacionExitosa = true;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaAdicion.getModel();
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

    private BigDecimal obtenerValorRubro(String valor) {
        valor = valor.trim();
        valor = valor.replace(",", "");
        valor = valor.replace("$", "");
        BigDecimal valorNumerico = BigDecimal.valueOf(Double.parseDouble(valor));;
        return valorNumerico;
    }

    private void reiniciarFormulario() {
        frNoDocumento.setText("");
        frNoDocumento.requestFocus();
        frFecha.setDate(null);
        frListaRubros.setSelectedIndex(0);
        frValor.setText("$0.0");
        frDescripcion.setText("");

        // Se eliminan filas de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaAdicion.getModel();
        int numeroFilas = model.getRowCount();

        if (numeroFilas > 0) {
            for (int i = numeroFilas - 1; i >= 0; i--) {
                model.removeRow(i);
            }
        }
    }

    private Boolean validarFormulario() {
        Boolean validacionExitosa = true;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaAdicion.getModel();
        int numeroFilas = model.getRowCount();

        if (frNoDocumento.getText().trim().equals("") || frFecha.getDate() == null || numeroFilas == 0) {
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

    private void eliminarAdicionRubro(Adicion adicion) {
        while (!adicion.getAdicionRubroList().isEmpty()) {
            try {
                adicionRubroController = new AdicionRubroJpaController();
                AdicionRubro adicionRubro = new AdicionRubro();
                adicionRubro = adicion.getAdicionRubroList().get(0);
                adicionRubroController.destroy(adicionRubro);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Adicion_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarAdicionSeleccionada(String documento) {
        accesoDatos = new AccesoDatos();
        Adicion adicion = new Adicion();
        adicion.setVigencia(vigencia);
        adicion.setDocumento(documento);
        adicion = accesoDatos.consultarObjetoPorVigencia(Adicion.class, adicion, vigencia).get(0);

        frNoDocumento.setText(adicion.getDocumento());
        frFecha.setDate(adicion.getFecha());
        frListaRubros.setSelectedItem(0);
        frValor.setText("$0.0");
        if (adicion.getDescripcion() != null) {
            if (!adicion.getDescripcion().trim().equals("")) {
                frDescripcion.setText(adicion.getDescripcion());
            }
        }
        cargarTablaAdiciones(adicion);
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
        listaAdiciones = new javax.swing.JLabel();
        imprimirAdicion = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        eliminarRegistro = new javax.swing.JLabel();
        labelDocumento = new javax.swing.JLabel();
        frNoDocumento = new javax.swing.JTextField();
        labelFecha = new javax.swing.JLabel();
        frFecha = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAdicion = new javax.swing.JTable();
        frListaRubros = new javax.swing.JComboBox<>();
        labelRubro = new javax.swing.JLabel();
        labelValor = new javax.swing.JLabel();
        frValor = new javax.swing.JTextField();
        botonRegistrar = new javax.swing.JButton();
        botonQuitar = new javax.swing.JButton();
        labelDescripcion = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        frDescripcion = new javax.swing.JTextArea();
        barraMenu = new javax.swing.JMenuBar();
        menuAdicion = new javax.swing.JMenu();
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

        listaAdiciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        listaAdiciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/lista.png"))); // NOI18N
        listaAdiciones.setAlignmentX(0.5F);
        listaAdiciones.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        listaAdiciones.setMaximumSize(new java.awt.Dimension(25, 20));
        listaAdiciones.setMinimumSize(new java.awt.Dimension(25, 20));
        listaAdiciones.setPreferredSize(new java.awt.Dimension(25, 20));
        listaAdiciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaAdicionesMousePressed(evt);
            }
        });
        barraHerramientas.add(listaAdiciones);

        imprimirAdicion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imprimirAdicion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/imprimir.png"))); // NOI18N
        imprimirAdicion.setAlignmentX(0.5F);
        imprimirAdicion.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        imprimirAdicion.setMaximumSize(new java.awt.Dimension(25, 20));
        imprimirAdicion.setMinimumSize(new java.awt.Dimension(25, 20));
        imprimirAdicion.setPreferredSize(new java.awt.Dimension(25, 20));
        imprimirAdicion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                imprimirAdicionMousePressed(evt);
            }
        });
        barraHerramientas.add(imprimirAdicion);
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

        tablaAdicion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rubro", "Adicion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaAdicion.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaAdicion);
        if (tablaAdicion.getColumnModel().getColumnCount() > 0) {
            tablaAdicion.getColumnModel().getColumn(1).setMinWidth(200);
            tablaAdicion.getColumnModel().getColumn(1).setPreferredWidth(200);
            tablaAdicion.getColumnModel().getColumn(1).setMaxWidth(200);
        }

        frListaRubros.setToolTipText("");

        labelRubro.setText("Rubro");

        labelValor.setText("Valor");

        frValor.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
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

        botonQuitar.setText("Quitar");
        botonQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonQuitarActionPerformed(evt);
            }
        });

        labelDescripcion.setText("Descripcion");

        frDescripcion.setColumns(20);
        frDescripcion.setLineWrap(true);
        frDescripcion.setRows(3);
        frDescripcion.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane2.setViewportView(frDescripcion);

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuAdicion.setText("Adicion");

        itemNuevo.setText("Nuevo Registro");
        itemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNuevoActionPerformed(evt);
            }
        });
        menuAdicion.add(itemNuevo);

        itemGuardar.setText("Guardar");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuAdicion.add(itemGuardar);

        itemListaAdiciones.setText("Lista Adiciones");
        itemListaAdiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemListaAdicionesActionPerformed(evt);
            }
        });
        menuAdicion.add(itemListaAdiciones);

        itemCerrar.setText("Cerrar");
        itemCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCerrarActionPerformed(evt);
            }
        });
        menuAdicion.add(itemCerrar);

        barraMenu.add(menuAdicion);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelFecha)
                                    .addComponent(labelDocumento)
                                    .addComponent(labelDescripcion))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelValor)
                                    .addComponent(labelRubro))
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frNoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frListaRubros, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frValor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonQuitar)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonRegistrar))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frNoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDocumento))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFecha))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelDescripcion)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(frListaRubros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelRubro))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(frValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelValor))))
                .addGap(18, 18, 18)
                .addComponent(botonRegistrar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonQuitar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void itemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEliminarActionPerformed
        AdicionJpaController adicionController = new AdicionJpaController();
        accesoDatos = new AccesoDatos();
        Adicion adicion = new Adicion();
        List<Adicion> listaAdicion = new ArrayList<Adicion>();

        if (!frNoDocumento.getText().trim().equals("")) {
            adicion.setVigencia(vigencia);
            adicion.setDocumento(frNoDocumento.getText().trim());

            listaAdicion = accesoDatos.consultarObjetoPorVigencia(Adicion.class, adicion, vigencia);

            if (!listaAdicion.isEmpty()) {
                try {
                    adicion = listaAdicion.get(0);
                    adicionController.destroy(adicion);
                    reiniciarFormulario();
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Adicion_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_itemEliminarActionPerformed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            Adicion adicion = new Adicion();
            List<Adicion> listaAdicion = new ArrayList<Adicion>();
            List<AdicionRubro> listaAdicionRubro = new ArrayList<AdicionRubro>();

            // Se registra adicion           
            if (!frNoDocumento.getText().trim().equals("")) {
                adicion.setVigencia(vigencia);
                adicion.setDocumento(frNoDocumento.getText().trim());

                listaAdicion = accesoDatos.consultarObjetoPorVigencia(Adicion.class, adicion, vigencia);

                if (!listaAdicion.isEmpty()) {
                    adicion = listaAdicion.get(0);

                    if (!adicion.getAdicionRubroList().isEmpty()) {
                        eliminarAdicionRubro(adicion);
                    }
                }

                adicion.setDocumento(frNoDocumento.getText().trim());
                adicion.setFecha(frFecha.getDate());
                adicion.setVigencia(vigencia);
                if (!frDescripcion.getText().trim().equals("")) {
                    adicion.setDescripcion(frDescripcion.getText().trim());
                }

                adicion = accesoDatos.persistirActualizar(adicion);

                // Se registra adicion - rubro
                DefaultTableModel model = new DefaultTableModel();
                model = (DefaultTableModel) tablaAdicion.getModel();
                int numeroFilas = model.getRowCount();
                if (numeroFilas > 0) {
                    listaAdicionRubro = new ArrayList<AdicionRubro>();
                    accesoDatos = new AccesoDatos();
                    for (int i = 0; i < numeroFilas; i++) {
                        Rubro rubro = new Rubro();
                        AdicionRubro adicionRubro = new AdicionRubro();
                        String rubroTabla = model.getValueAt(i, 0).toString();
                        String valorAdicion = model.getValueAt(i, 1).toString();
                        // Se consulta el rubroregitrado en la tabla
                        String codigoRubro = rubroTabla.substring(0, rubroTabla.indexOf("-") - 1);
                        rubro = consultarRubro(codigoRubro);
                        BigDecimal valorAR = obtenerValorRubro(valorAdicion);
                        adicionRubro.setAdicion(adicion);
                        adicionRubro.setRubro(rubro);
                        adicionRubro.setValor(valorAR);
                        adicionRubro.setVigencia(vigencia);
                        listaAdicionRubro.add(adicionRubro);
                    }
                    accesoDatos.persistirActualizar(listaAdicionRubro);
                }

                reiniciarFormulario();
            }

        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void eliminarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarRegistroMousePressed
        AdicionJpaController adicionController = new AdicionJpaController();
        accesoDatos = new AccesoDatos();
        Adicion adicion = new Adicion();
        List<Adicion> listaAdicion = new ArrayList<Adicion>();

        if (!frNoDocumento.getText().trim().equals("")) {
            adicion.setVigencia(vigencia);
            adicion.setDocumento(frNoDocumento.getText().trim());

            listaAdicion = accesoDatos.consultarObjetoPorVigencia(Adicion.class, adicion, vigencia);

            if (!listaAdicion.isEmpty()) {
                try {
                    adicion = listaAdicion.get(0);
                    adicionController.destroy(adicion);
                    reiniciarFormulario();
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Adicion_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_eliminarRegistroMousePressed

    private void nuevoRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoRegistroMousePressed
        reiniciarFormulario();
    }//GEN-LAST:event_nuevoRegistroMousePressed

    private void listaAdicionesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaAdicionesMousePressed
        // Abrir Lista
        Lista_Adicion listaAdicion = new Lista_Adicion(this, true);
        listaAdicion.setLocationRelativeTo(null);
        listaAdicion.setVisible(true);
    }//GEN-LAST:event_listaAdicionesMousePressed

    private void imprimirAdicionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirAdicionMousePressed

    }//GEN-LAST:event_imprimirAdicionMousePressed

    private void frNoDocumentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frNoDocumentoFocusLost
        accesoDatos = new AccesoDatos();
        Adicion adicion = new Adicion();
        List<Adicion> listaAdicion = new ArrayList<Adicion>();

        if (!frNoDocumento.getText().trim().equals("")) {
            adicion.setVigencia(vigencia);
            adicion.setDocumento(frNoDocumento.getText().trim());

            listaAdicion = accesoDatos.consultarObjetoPorVigencia(Adicion.class, adicion, vigencia);

            if (!listaAdicion.isEmpty()) {
                adicion = listaAdicion.get(0);
                frFecha.setDate(adicion.getFecha());
                frListaRubros.setSelectedItem(0);
                frValor.setText("$0.0");
                if (adicion.getDescripcion() != null) {
                    if (!adicion.getDescripcion().equals("")) {
                        frDescripcion.setText(adicion.getDescripcion());
                    }
                }
                cargarTablaAdiciones(adicion);
            }
        }
    }//GEN-LAST:event_frNoDocumentoFocusLost

    private void botonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegistrarActionPerformed
        String rubroSeleccionado = frListaRubros.getSelectedItem().toString();

        if (!rubroSeleccionado.equals("-- Seleccionar --")) {
            if (!frValor.getText().trim().equals("") && !frValor.getText().trim().equals("$0.0")) {
                if (verificarRubro(rubroSeleccionado)) {
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) tablaAdicion.getModel();
                    model.addRow(new Object[]{rubroSeleccionado, frValor.getText()});
                    frValor.setText("$0.0");
                    frListaRubros.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(null, "El rubro ya fue registrado en la tabla", "Verificación Adicion", 0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe digitar un valor para la adicion", "Verificación Adicion", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un rubro de la lista", "Verificación Adicion", 0);
        }
    }//GEN-LAST:event_botonRegistrarActionPerformed

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

    private void botonQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonQuitarActionPerformed
        int filaSeleccionada = tablaAdicion.getSelectedRow();

        if (filaSeleccionada != -1) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaAdicion.getModel();
            // Se elimina de la tabla
            model.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(null, "Para eliminar un registro debe seleccionar una fila", "Eliminar Registro", 0);
        }
    }//GEN-LAST:event_botonQuitarActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            Adicion adicion = new Adicion();
            List<Adicion> listaAdicion = new ArrayList<Adicion>();
            List<AdicionRubro> listaAdicionRubro = new ArrayList<AdicionRubro>();

            // Se registra adicion           
            if (!frNoDocumento.getText().trim().equals("")) {
                adicion.setVigencia(vigencia);
                adicion.setDocumento(frNoDocumento.getText().trim());

                listaAdicion = accesoDatos.consultarObjetoPorVigencia(Adicion.class, adicion, vigencia);

                if (!listaAdicion.isEmpty()) {
                    adicion = listaAdicion.get(0);

                    if (!adicion.getAdicionRubroList().isEmpty()) {
                        eliminarAdicionRubro(adicion);
                    }
                }

                adicion.setDocumento(frNoDocumento.getText().trim());
                adicion.setFecha(frFecha.getDate());
                adicion.setVigencia(vigencia);
                if (!frDescripcion.getText().trim().equals("")) {
                    adicion.setDescripcion(frDescripcion.getText().trim());
                }

                adicion = accesoDatos.persistirActualizar(adicion);

                // Se registra adicion - rubro
                DefaultTableModel model = new DefaultTableModel();
                model = (DefaultTableModel) tablaAdicion.getModel();
                int numeroFilas = model.getRowCount();
                if (numeroFilas > 0) {
                    listaAdicionRubro = new ArrayList<AdicionRubro>();
                    accesoDatos = new AccesoDatos();
                    for (int i = 0; i < numeroFilas; i++) {
                        Rubro rubro = new Rubro();
                        AdicionRubro adicionRubro = new AdicionRubro();
                        String rubroTabla = model.getValueAt(i, 0).toString();
                        String valorAdicion = model.getValueAt(i, 1).toString();
                        // Se consulta el rubroregitrado en la tabla
                        String codigoRubro = rubroTabla.substring(0, rubroTabla.indexOf("-") - 1);
                        rubro = consultarRubro(codigoRubro);
                        BigDecimal valorAR = obtenerValorRubro(valorAdicion);
                        adicionRubro.setAdicion(adicion);
                        adicionRubro.setRubro(rubro);
                        adicionRubro.setValor(valorAR);
                        adicionRubro.setVigencia(vigencia);
                        listaAdicionRubro.add(adicionRubro);
                    }
                    accesoDatos.persistirActualizar(listaAdicionRubro);
                }

                reiniciarFormulario();
            }

        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevoActionPerformed
        reiniciarFormulario();
    }//GEN-LAST:event_itemNuevoActionPerformed

    private void itemListaAdicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemListaAdicionesActionPerformed
        Lista_Adicion listaAdicion = new Lista_Adicion(this, true);
        listaAdicion.setLocationRelativeTo(null);
        listaAdicion.setVisible(true);
    }//GEN-LAST:event_itemListaAdicionesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JButton botonQuitar;
    private javax.swing.JButton botonRegistrar;
    private javax.swing.JLabel eliminarRegistro;
    private javax.swing.JTextArea frDescripcion;
    private com.toedter.calendar.JDateChooser frFecha;
    private javax.swing.JComboBox<String> frListaRubros;
    private javax.swing.JTextField frNoDocumento;
    private javax.swing.JTextField frValor;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JLabel imprimirAdicion;
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
    private javax.swing.JLabel labelValor;
    private javax.swing.JLabel listaAdiciones;
    private javax.swing.JMenu menuAdicion;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JLabel nuevoRegistro;
    private javax.swing.JTable tablaAdicion;
    // End of variables declaration//GEN-END:variables
}
