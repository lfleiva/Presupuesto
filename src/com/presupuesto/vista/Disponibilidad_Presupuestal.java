/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.control.DisponibilidadJpaController;
import com.presupuesto.control.DisponibilidadRubroJpaController;
import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Beneficiario;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.DisponibilidadRubro;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.TipoRubro;
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
public class Disponibilidad_Presupuestal extends javax.swing.JInternalFrame {

    Home home;
    Vigencia vigencia;
    AccesoDatos accesoDatos;
    DisponibilidadJpaController disponibilidadController;
    DisponibilidadRubroJpaController disponibilidadRubroController;

    /**
     * Creates new form Disponibilidad_Presupuestal
     */
    public Disponibilidad_Presupuestal(Home parent) {
        super();
        this.home = parent;

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);

        consultarVigencia();
        initComponents();
        cargarListaBeneficiarios();
        cargarListaRubros();

        frDisponibilidad.setText(consultarConsecutivo().toString());
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

    private void cargarListaBeneficiarios() {
        accesoDatos = new AccesoDatos();
        List<Beneficiario> listaBeneficiario = new ArrayList<Beneficiario>();

        listaBeneficiario = accesoDatos.consultarTodos(Beneficiario.class, true, 0, 0);

        if (!listaBeneficiario.isEmpty()) {
            frListaBeneficiarios.addItem("-- Seleccione --");
            for (Beneficiario beneficiarioIterado : listaBeneficiario) {
                frListaBeneficiarios.addItem(beneficiarioIterado.getIdentificacion() + " - " + beneficiarioIterado.getNombre());
            }
        }
    }

    private void cargarListaRubros() {
        accesoDatos = new AccesoDatos();
        List<Rubro> listaRubros = new ArrayList<Rubro>();
        Rubro rubro = new Rubro();
        rubro.setTipoRubro(new TipoRubro(new BigDecimal(3)));

        listaRubros = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia);

        if (!listaRubros.isEmpty()) {
            for (Rubro rubroIterado : listaRubros) {
                frListaRubros.addItem(rubroIterado.getCodigo() + " - " + rubroIterado.getNombre());
            }
        }
    }

    private void eliminarElementosTabla() {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaRubros.getModel();

        int numeroFilas = model.getRowCount();

        while (numeroFilas > 0) {
            model.removeRow(0);
            model = (DefaultTableModel) tablaRubros.getModel();
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

    private Boolean validarFormulario() {
        Boolean validacionExitosa = true;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaRubros.getModel();

        int numeroFilas = model.getRowCount();

        if (frDisponibilidad.getText().equals("") || frFecha.getDate() == null || frObjeto.getText().equals("") || numeroFilas <= 0 || frListaBeneficiarios.getSelectedItem().toString().equals("-- Seleccione --")) {
            validacionExitosa = false;
        }

        return validacionExitosa;
    }

    private void reiniciarFormulario() {
        frDisponibilidad.setText(consultarConsecutivo().toString());
        frDisponibilidad.requestFocus();
        frListaBeneficiarios.setSelectedIndex(0);
        frFecha.setDate(null);
        frObjeto.setText("");
        frListaRubros.setSelectedIndex(0);
        frValor.setText("$0.0");
        eliminarElementosTabla();
    }

    private BigDecimal consultarConsecutivo() {
        disponibilidadController = new DisponibilidadJpaController();
        BigDecimal maximoConsecutivo = disponibilidadController.consultarMaximoDisponibilidad(vigencia);

        maximoConsecutivo = maximoConsecutivo.add(new BigDecimal(1));

        return maximoConsecutivo;
    }

    private Boolean verificarRubro(String codigo) {
        Boolean verificacionExitosa = true;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaRubros.getModel();
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

    private Beneficiario consultarBeneficiario() {
        Beneficiario beneficiario = new Beneficiario();

        String beneficiarioSeleccionado = frListaBeneficiarios.getSelectedItem().toString();

        if (!beneficiarioSeleccionado.equals("-- Seleccione --")) {
            String identificacion = beneficiarioSeleccionado.substring(0, beneficiarioSeleccionado.indexOf("-") - 1);
            beneficiario.setIdentificacion(identificacion);
            beneficiario = accesoDatos.consultarTodos(beneficiario, Beneficiario.class).get(0);
        }

        return beneficiario;
    }

    private void eliminarDisponibilidadRubro(Disponibilidad disponibilidad) {
        while (!disponibilidad.getDisponibilidadRubroList().isEmpty()) {
            try {
                disponibilidadRubroController = new DisponibilidadRubroJpaController();
                DisponibilidadRubro disponibilidadRubro = new DisponibilidadRubro();
                disponibilidadRubro = disponibilidad.getDisponibilidadRubroList().get(0);

                disponibilidadRubroController.destroy(disponibilidadRubro);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Disponibilidad_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    public void cargarDisponibilidadSeleccionada(String consecutivoDisponibilidad) {
        accesoDatos = new AccesoDatos();
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setConsecutivo(new BigDecimal(consecutivoDisponibilidad));

        disponibilidad = accesoDatos.consultarObjetoPorVigencia(Disponibilidad.class, disponibilidad, vigencia).get(0);

        frDisponibilidad.setText(disponibilidad.getConsecutivo().toString());
        frFecha.setDate(disponibilidad.getFecha());
        frListaBeneficiarios.setSelectedItem(disponibilidad.getBeneficiario().getIdentificacion() + " - " + disponibilidad.getBeneficiario().getNombre());
        frObjeto.setText(disponibilidad.getObjeto());
        frListaRubros.setSelectedIndex(0);
        frValor.setText("$0.0");

        if (!disponibilidad.getDisponibilidadRubroList().isEmpty()) {
            List<DisponibilidadRubro> listaDisponibilidadRubro = new ArrayList<DisponibilidadRubro>();
            listaDisponibilidadRubro = disponibilidad.getDisponibilidadRubroList();
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaRubros.getModel();

            for (DisponibilidadRubro disponibilidadRubro : listaDisponibilidadRubro) {
                model.addRow(new Object[]{disponibilidadRubro.getRubro().getCodigo() + " - " + disponibilidadRubro.getRubro().getNombre(), "$" + formatoNumeroDecimales(disponibilidadRubro.getValor().toString())});
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
        listaAdiciones = new javax.swing.JLabel();
        imprimirAdicion = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        eliminarRegistro = new javax.swing.JLabel();
        frDisponibilidad = new javax.swing.JTextField();
        frListaBeneficiarios = new javax.swing.JComboBox<>();
        frFecha = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        frObjeto = new javax.swing.JTextArea();
        frListaRubros = new javax.swing.JComboBox<>();
        frValor = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaRubros = new javax.swing.JTable();
        botonRegistrar = new javax.swing.JButton();
        botonQuitar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        menuDisponibilidad = new javax.swing.JMenu();
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

        frDisponibilidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frDisponibilidadFocusLost(evt);
            }
        });

        frObjeto.setColumns(20);
        frObjeto.setLineWrap(true);
        frObjeto.setRows(2);
        jScrollPane1.setViewportView(frObjeto);

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

        jLabel1.setText("No. Disponibilidad");

        jLabel3.setText("Fecha");

        jLabel4.setText("Objeto");

        jLabel5.setText("Rubro");

        jLabel6.setText("Valor");

        tablaRubros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rubro", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaRubros);
        if (tablaRubros.getColumnModel().getColumnCount() > 0) {
            tablaRubros.getColumnModel().getColumn(1).setMinWidth(200);
            tablaRubros.getColumnModel().getColumn(1).setPreferredWidth(200);
            tablaRubros.getColumnModel().getColumn(1).setMaxWidth(200);
        }

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

        jLabel7.setText("Beneficiario");

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuDisponibilidad.setText("Inicio");

        itemNuevo.setText("Nuevo Registro");
        itemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNuevoActionPerformed(evt);
            }
        });
        menuDisponibilidad.add(itemNuevo);

        itemGuardar.setText("Guardar");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuDisponibilidad.add(itemGuardar);

        itemListaAdiciones.setText("Lista Disponibilidades");
        itemListaAdiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemListaAdicionesActionPerformed(evt);
            }
        });
        menuDisponibilidad.add(itemListaAdiciones);

        itemCerrar.setText("Cerrar");
        itemCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCerrarActionPerformed(evt);
            }
        });
        menuDisponibilidad.add(itemCerrar);

        barraMenu.add(menuDisponibilidad);

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
                .addGap(82, 82, 82)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonQuitar)
                    .addComponent(botonRegistrar)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(frListaRubros, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(frValor, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(frDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane1)
                        .addComponent(jScrollPane2)
                        .addComponent(frListaBeneficiarios, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frListaBeneficiarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(frListaRubros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(63, 63, 63)))
                .addComponent(botonRegistrar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonQuitar)
                .addGap(20, 20, 20))
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
        reiniciarFormulario();
    }//GEN-LAST:event_nuevoRegistroMousePressed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<Disponibilidad> listaDisponibilidad = new ArrayList<Disponibilidad>();
            Disponibilidad disponibilidad = new Disponibilidad();
            disponibilidad.setConsecutivo(new BigDecimal(frDisponibilidad.getText()));
            disponibilidad.setVigencia(vigencia);

            listaDisponibilidad = accesoDatos.consultarObjetoPorVigencia(Disponibilidad.class, disponibilidad, vigencia);

            if (!listaDisponibilidad.isEmpty()) {
                disponibilidad = listaDisponibilidad.get(0);

                if (!disponibilidad.getDisponibilidadRubroList().isEmpty()) {
                    eliminarDisponibilidadRubro(disponibilidad);
                }
            }

            disponibilidad.setFecha(frFecha.getDate());
            disponibilidad.setBeneficiario(consultarBeneficiario());
            disponibilidad.setObjeto(frObjeto.getText());

            disponibilidad = accesoDatos.persistirActualizar(disponibilidad);

            // Se registra disponibilidad - rubro
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaRubros.getModel();
            int numeroFilas = model.getRowCount();
            if (numeroFilas > 0) {
                List<DisponibilidadRubro> listaDisponibilidadRubro = new ArrayList<DisponibilidadRubro>();
                accesoDatos = new AccesoDatos();
                for (int i = 0; i < numeroFilas; i++) {
                    Rubro rubro = new Rubro();
                    DisponibilidadRubro disponibilidadRubro = new DisponibilidadRubro();
                    String rubroTabla = model.getValueAt(i, 0).toString();
                    String valorRubro = model.getValueAt(i, 1).toString();
                    // Se consulta el rubroregitrado en la tabla
                    String codigoRubro = rubroTabla.substring(0, rubroTabla.indexOf("-") - 1);
                    rubro = consultarRubro(codigoRubro);
                    BigDecimal valorDR = obtenerValorRubro(valorRubro);
                    disponibilidadRubro.setDisponibilidad(disponibilidad);
                    disponibilidadRubro.setRubro(rubro);
                    disponibilidadRubro.setValor(valorDR);
                    disponibilidadRubro.setVigencia(vigencia);
                    listaDisponibilidadRubro.add(disponibilidadRubro);
                }
                accesoDatos.persistirActualizar(listaDisponibilidadRubro);
            }

            reiniciarFormulario();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void listaAdicionesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaAdicionesMousePressed
        // Abrir Lista
        Lista_Disponibilidad listaDisponibilidad = new Lista_Disponibilidad(this, true);
        listaDisponibilidad.setLocationRelativeTo(null);
        listaDisponibilidad.setVisible(true);
    }//GEN-LAST:event_listaAdicionesMousePressed

    private void imprimirAdicionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirAdicionMousePressed
        Generar_Reportes reportes = new Generar_Reportes();

        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<Disponibilidad> listaDisponibilidad = new ArrayList<Disponibilidad>();
            Disponibilidad disponibilidad = new Disponibilidad();
            disponibilidad.setConsecutivo(new BigDecimal(frDisponibilidad.getText()));
            disponibilidad.setVigencia(vigencia);

            listaDisponibilidad = accesoDatos.consultarObjetoPorVigencia(Disponibilidad.class, disponibilidad, vigencia);

            if (!listaDisponibilidad.isEmpty()) {
                disponibilidad = listaDisponibilidad.get(0);
                reportes.runReporteDisponibilidadPresupuestal(vigencia, disponibilidad);
            } else {
                JOptionPane.showMessageDialog(null, "La Disponibilidad no existe", "Verificación Disponibilidad", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_imprimirAdicionMousePressed

    private void eliminarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarRegistroMousePressed
        accesoDatos = new AccesoDatos();
        disponibilidadController = new DisponibilidadJpaController();
        List<Disponibilidad> listaDisponibilidad = new ArrayList<Disponibilidad>();
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setConsecutivo(new BigDecimal(frDisponibilidad.getText()));
        disponibilidad.setVigencia(vigencia);

        listaDisponibilidad = accesoDatos.consultarObjetoPorVigencia(Disponibilidad.class, disponibilidad, vigencia);

        if (!listaDisponibilidad.isEmpty()) {
            try {
                disponibilidad = listaDisponibilidad.get(0);
                disponibilidadController.destroy(disponibilidad);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Disponibilidad_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        reiniciarFormulario();
    }//GEN-LAST:event_eliminarRegistroMousePressed

    private void frDisponibilidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frDisponibilidadFocusLost
        accesoDatos = new AccesoDatos();
        List<Disponibilidad> listaDisponibilidad = new ArrayList<Disponibilidad>();
        Disponibilidad disponibilidad = new Disponibilidad();
        if (!frDisponibilidad.getText().equals("")) {
            BigDecimal consecutivofr = new BigDecimal(frDisponibilidad.getText());
            disponibilidad.setConsecutivo(consecutivofr);
            listaDisponibilidad = accesoDatos.consultarObjetoPorVigencia(Disponibilidad.class, disponibilidad, vigencia);

            if (!listaDisponibilidad.isEmpty()) {
                disponibilidad = listaDisponibilidad.get(0);
                frListaBeneficiarios.setSelectedItem(disponibilidad.getBeneficiario().getIdentificacion() + " - " + disponibilidad.getBeneficiario().getNombre());
                frFecha.setDate(disponibilidad.getFecha());
                frObjeto.setText(disponibilidad.getObjeto());
                frListaRubros.setSelectedIndex(0);
                frValor.setText("$0.0");

                eliminarElementosTabla();

                if (!disponibilidad.getDisponibilidadRubroList().isEmpty()) {
                    List<DisponibilidadRubro> listaDisponibilidadRubro = new ArrayList<DisponibilidadRubro>();
                    listaDisponibilidadRubro = disponibilidad.getDisponibilidadRubroList();
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) tablaRubros.getModel();

                    for (DisponibilidadRubro disponibilidadRubro : listaDisponibilidadRubro) {
                        model.addRow(new Object[]{disponibilidadRubro.getRubro().getCodigo() + " - " + disponibilidadRubro.getRubro().getNombre(), "$" + formatoNumeroDecimales(disponibilidadRubro.getValor().toString())});
                    }
                }
            } else {
                if(frListaBeneficiarios.getItemCount() > 0) {
                    frListaBeneficiarios.setSelectedIndex(0);
                }                
                frFecha.setDate(null);
                frObjeto.setText("");
                frListaRubros.setSelectedIndex(0);
                frValor.setText("$0.0");
                eliminarElementosTabla();
            }
        }
    }//GEN-LAST:event_frDisponibilidadFocusLost

    private void frValorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frValorFocusGained
        if (frValor.getText().equals("$0.0")) {
            frValor.setText("");
        }
    }//GEN-LAST:event_frValorFocusGained

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

    private void botonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegistrarActionPerformed
        String rubroSeleccionado = frListaRubros.getSelectedItem().toString();

        if (!rubroSeleccionado.equals("-- Seleccionar --")) {
            if (!frValor.getText().trim().equals("") && !frValor.getText().trim().equals("$0.0")) {
                if (verificarRubro(rubroSeleccionado)) {
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) tablaRubros.getModel();
                    model.addRow(new Object[]{rubroSeleccionado, frValor.getText()});
                    frValor.setText("$0.0");
                    frListaRubros.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(null, "El rubro ya fue registrado en la tabla", "Verificación Rubro", 0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe digitar un valor para el rubro", "Verificación Rubro", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un rubro de la lista", "Verificación Rubro", 0);
        }
    }//GEN-LAST:event_botonRegistrarActionPerformed

    private void botonQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonQuitarActionPerformed
        int filaSeleccionada = tablaRubros.getSelectedRow();

        if (filaSeleccionada != -1) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaRubros.getModel();
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
    private javax.swing.JLabel eliminarRegistro;
    private javax.swing.JTextField frDisponibilidad;
    private com.toedter.calendar.JDateChooser frFecha;
    private javax.swing.JComboBox<String> frListaBeneficiarios;
    private javax.swing.JComboBox<String> frListaRubros;
    private javax.swing.JTextArea frObjeto;
    private javax.swing.JTextField frValor;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JLabel imprimirAdicion;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemEliminar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemListaAdiciones;
    private javax.swing.JMenuItem itemNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel listaAdiciones;
    private javax.swing.JMenu menuDisponibilidad;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JLabel nuevoRegistro;
    private javax.swing.JTable tablaRubros;
    // End of variables declaration//GEN-END:variables
}
