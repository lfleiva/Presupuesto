/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.control.ComprobanteEgresoJpaController;
import com.presupuesto.control.EgresoDescuentoJpaController;
import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.ComprobanteEgreso;
import com.presupuesto.modelo.Descuento;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.DisponibilidadRubro;
import com.presupuesto.modelo.EgresoDescuento;
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
public class Comprobante_Egreso extends javax.swing.JInternalFrame {

    Home home;
    AccesoDatos accesoDatos;
    Vigencia vigencia;
    ComprobanteEgresoJpaController comprobanteController;
    EgresoDescuentoJpaController egresoDescuentoController;

    /**
     * Creates new form Comprobante_Egreso
     */
    public Comprobante_Egreso(Home parent) {
        super();
        this.home = parent;

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);
        consultarVigencia();
        initComponents();

        frValorDescuento.setText("$0.0");
        frNetoPagar.setText("$0.0");
        cargarListaDisponibilidades();
        cargarListaDescuentos();
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
        comprobanteController = new ComprobanteEgresoJpaController();

        consecutivo = comprobanteController.consultarMaximoRegistro(vigencia).add(new BigDecimal(1));

        return consecutivo;
    }

    private void cargarListaDisponibilidades() {
        accesoDatos = new AccesoDatos();
        List<Disponibilidad> listaDisponibilidad = new ArrayList<Disponibilidad>();
        listaDisponibilidad = accesoDatos.consultarTodosPorVigencia(Disponibilidad.class, vigencia);

        if (!listaDisponibilidad.isEmpty()) {
            frListaDisponibilidad.addItem("-- Seleccionar --");
            for (Disponibilidad disponibilidadIterado : listaDisponibilidad) {
                frListaDisponibilidad.addItem(disponibilidadIterado.getConsecutivo() + " - " + disponibilidadIterado.getBeneficiario().getNombre());
            }
        }
    }

    private void eliminarElementosTabla() {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaDescuentos.getModel();

        int numeroFilas = model.getRowCount();

        while (numeroFilas > 0) {
            model.removeRow(0);
            model = (DefaultTableModel) tablaDescuentos.getModel();
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

    public void cargarListaDescuentos() {
        List<Descuento> listaDescuentos = new ArrayList<Descuento>();
        Descuento descuento = new Descuento();

        descuento.setVigencia(vigencia);

        listaDescuentos = accesoDatos.consultarObjetoPorVigencia(Descuento.class, descuento, vigencia);

        if (!listaDescuentos.isEmpty()) {
            frListaDecuentos.removeAllItems();
            frListaDecuentos.addItem("-- Seleccione --");
            for (Descuento descuentoIterado : listaDescuentos) {
                frListaDecuentos.addItem(descuentoIterado.getNombre() + " (" + descuentoIterado.getPorcentaje() + "%)");
            }
        }
    }

    private boolean validarFormulario() {
        boolean validacionExitosa = true;

        if (frCheque.getText().trim().equals("") || frComprobante.getText().trim().equals("") || frCuenta.getText().trim().equals("") || frDescripcion.getText().trim().equals("") || frFecha.getDate() == null || frNetoPagar.getText().trim().equals("") || frNoCuenta.getText().trim().equals("") || frValorLetras.getText().trim().equals("") || frListaDisponibilidad.getSelectedItem().toString().equals("-- Seleccionar --")) {
            validacionExitosa = false;
        }

        return validacionExitosa;
    }

    private BigDecimal obtenerValorRubro(String valor) {
        valor = valor.trim();
        valor = valor.replace(",", "");
        valor = valor.replace("$", "");
        BigDecimal valorNumerico = BigDecimal.valueOf(Double.parseDouble(valor));;
        return valorNumerico;
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

    private void reiniciarFormulario() {
        frComprobante.setText("");
        frFecha.setDate(null);
        frCheque.setText("");
        frDescripcion.setText("");
        frListaDecuentos.setSelectedIndex(0);
        frListaDisponibilidad.setSelectedIndex(0);
        frNoCuenta.setText("");
        frCuenta.setText("$0.0");
        frNetoPagar.setText("$0.0");
        frValorLetras.setText("");
        frComprobante.requestFocus();
        eliminarElementosTabla();
    }

    private Descuento consultarDescuento(String nombreDescuento) {
        List<Descuento> listaDescuento = new ArrayList<Descuento>();
        Descuento descuento = new Descuento();
        descuento.setNombre(nombreDescuento);
        descuento.setVigencia(vigencia);

        listaDescuento = accesoDatos.consultarObjetoPorVigencia(Descuento.class, descuento, vigencia);

        if (!listaDescuento.isEmpty()) {
            descuento = listaDescuento.get(0);
        }

        return descuento;
    }

    private void eliminarEgresoDescuento(ComprobanteEgreso comprobante) {

        while (!comprobante.getEgresoDescuentoList().isEmpty()) {
            try {
                egresoDescuentoController = new EgresoDescuentoJpaController();
                EgresoDescuento egresoDescuento = new EgresoDescuento();
                egresoDescuento = comprobante.getEgresoDescuentoList().get(0);

                egresoDescuentoController.destroy(egresoDescuento);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Disponibilidad_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cargarComprobanteSeleccionado(String comprobanteSeleccionado) {
        if (!comprobanteSeleccionado.trim().equals("")) {
            accesoDatos = new AccesoDatos();
            List<ComprobanteEgreso> listaComprobante = new ArrayList<ComprobanteEgreso>();
            ComprobanteEgreso comprobante = new ComprobanteEgreso();
            comprobante.setConsecutivo(new BigDecimal(comprobanteSeleccionado));
            comprobante.setVigencia(vigencia);

            listaComprobante = accesoDatos.consultarObjetoPorVigencia(ComprobanteEgreso.class, comprobante, vigencia);

            if (!listaComprobante.isEmpty()) {
                comprobante = listaComprobante.get(0);
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedItem(comprobante.getDisponibilidad().getConsecutivo() + " - " + comprobante.getDisponibilidad().getBeneficiario().getNombre());
                }
                frComprobante.setText(comprobante.getConsecutivo().toString());
                frFecha.setDate(comprobante.getFecha());
                frCheque.setText(comprobante.getCheque());
                frDescripcion.setText(comprobante.getDescripcion());
                frNoCuenta.setText(comprobante.getCuentaBanco());
                frCuenta.setText("$" + formatoNumeroDecimales(comprobante.getValorPagar().toString()));
                frNetoPagar.setText("$" + formatoNumeroDecimales(comprobante.getValorCuenta().toString()));
                frValorDescuento.setText("$0.0");
                frValorLetras.setText(comprobante.getValorLetras());

                if (frListaDecuentos.getItemCount() > 0) {
                    frListaDecuentos.setSelectedItem(0);
                }

                // Llenar tabla de decuentos
                eliminarElementosTabla();

                if (!comprobante.getEgresoDescuentoList().isEmpty()) {
                    List<EgresoDescuento> listaDescuentos = new ArrayList<EgresoDescuento>();
                    listaDescuentos = comprobante.getEgresoDescuentoList();
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) tablaDescuentos.getModel();

                    for (EgresoDescuento descuento : listaDescuentos) {
                        model.addRow(new Object[]{descuento.getDescuento().getNombre() + " (" + descuento.getDescuento().getPorcentaje() + "%)", "$" + formatoNumeroDecimales(descuento.getValor().toString())});
                    }
                }

            } else {
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedIndex(0);
                }
                frFecha.setDate(null);
                frCheque.setText("");
                frDescripcion.setText("");
                frNoCuenta.setText("");
                frCuenta.setText("");
                frNetoPagar.setText("");
                frValorDescuento.setText("$0.0");
                frValorLetras.setText("");

                if (frListaDecuentos.getItemCount() > 0) {
                    frListaDecuentos.setSelectedItem(0);
                }
            }
        }
    }

    private BigDecimal sumarDescuentos() {
        BigDecimal totalDescuentos = new BigDecimal(0);

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaDescuentos.getModel();
        int numeroFilas = model.getRowCount();
        if (numeroFilas > 0) {
            for (int i = 0; i < numeroFilas; i++) {
                String valorDescuentoTexto = model.getValueAt(i, 1).toString();

                BigDecimal valorDescuento = obtenerValorRubro(valorDescuentoTexto);
                totalDescuentos = totalDescuentos.add(valorDescuento);
            }
        }

        return totalDescuentos;
    }

    private BigDecimal obtenerPorcentaje(String descuentoSeleccionado) {
        String porcentajeDescuento = descuentoSeleccionado.substring(descuentoSeleccionado.indexOf("(") + 1, descuentoSeleccionado.length() - 2);
        return new BigDecimal(porcentajeDescuento);
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
        labelComprobante = new javax.swing.JLabel();
        frComprobante = new javax.swing.JTextField();
        labelFecha = new javax.swing.JLabel();
        frFecha = new com.toedter.calendar.JDateChooser();
        labelDisponibilidad = new javax.swing.JLabel();
        frListaDisponibilidad = new javax.swing.JComboBox<>();
        labelPagar = new javax.swing.JLabel();
        frCuenta = new javax.swing.JTextField();
        labelValorCuenta = new javax.swing.JLabel();
        frNetoPagar = new javax.swing.JTextField();
        labelValorLetras = new javax.swing.JLabel();
        frValorLetras = new javax.swing.JTextField();
        labelCuenta = new javax.swing.JLabel();
        frNoCuenta = new javax.swing.JTextField();
        labelCheque = new javax.swing.JLabel();
        frCheque = new javax.swing.JTextField();
        labelDescripcion = new javax.swing.JLabel();
        frDescripcion = new javax.swing.JTextField();
        panelDescuentos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDescuentos = new javax.swing.JTable();
        labelDescuento = new javax.swing.JLabel();
        frListaDecuentos = new javax.swing.JComboBox<>();
        labelValorDescuento = new javax.swing.JLabel();
        frValorDescuento = new javax.swing.JTextField();
        botonQuitar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        barraMenu = new javax.swing.JMenuBar();
        menuTraslado = new javax.swing.JMenu();
        itemNuevo = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        itemListaAdiciones = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
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

        labelComprobante.setText("No. Comprobante");

        frComprobante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frComprobanteFocusLost(evt);
            }
        });

        labelFecha.setText("Fecha");

        labelDisponibilidad.setText("Disponibilidad");

        frListaDisponibilidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frListaDisponibilidadActionPerformed(evt);
            }
        });

        labelPagar.setText("Valor Cuenta");

        frCuenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        frCuenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                frCuentaKeyTyped(evt);
            }
        });

        labelValorCuenta.setText("Neto a Pagar");

        frNetoPagar.setEnabled(false);

        labelValorLetras.setText("Valor en Letras");

        labelCuenta.setText("Cuenta No.");

        labelCheque.setText("No. Cheque");

        labelDescripcion.setText("Descripcion");

        panelDescuentos.setBackground(new java.awt.Color(255, 255, 255));
        panelDescuentos.setBorder(javax.swing.BorderFactory.createTitledBorder("Descuentos"));

        tablaDescuentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descuento", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaDescuentos);

        labelDescuento.setText("Descuento");

        labelValorDescuento.setText("Valor");

        frValorDescuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                frValorDescuentoFocusGained(evt);
            }
        });
        frValorDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                frValorDescuentoKeyTyped(evt);
            }
        });

        botonQuitar.setText("Quitar");
        botonQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonQuitarActionPerformed(evt);
            }
        });

        jButton1.setText("Registrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDescuentosLayout = new javax.swing.GroupLayout(panelDescuentos);
        panelDescuentos.setLayout(panelDescuentosLayout);
        panelDescuentosLayout.setHorizontalGroup(
            panelDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDescuentosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(panelDescuentosLayout.createSequentialGroup()
                        .addComponent(labelDescuento)
                        .addGap(18, 18, 18)
                        .addComponent(frListaDecuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelValorDescuento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(frValorDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDescuentosLayout.createSequentialGroup()
                        .addGroup(panelDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonQuitar)
                            .addComponent(jButton1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelDescuentosLayout.setVerticalGroup(
            panelDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDescuentosLayout.createSequentialGroup()
                .addGroup(panelDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDescuento)
                    .addComponent(frListaDecuentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frValorDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelValorDescuento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonQuitar))
        );

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

        itemListaAdiciones.setText("Lista Comprobantes");
        itemListaAdiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemListaAdicionesActionPerformed(evt);
            }
        });
        menuTraslado.add(itemListaAdiciones);

        jMenuItem1.setText("Descuentos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menuTraslado.add(jMenuItem1);

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
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelValorCuenta)
                            .addComponent(labelDisponibilidad)
                            .addComponent(labelCuenta)
                            .addComponent(labelDescripcion))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelDescuentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(frListaDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(labelPagar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(frCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(frNoCuenta, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                    .addComponent(frNetoPagar))
                                .addGap(106, 106, 106)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelValorLetras)
                                        .addGap(18, 18, 18)
                                        .addComponent(frValorLetras, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelCheque)
                                        .addGap(18, 18, 18)
                                        .addComponent(frCheque, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(frDescripcion)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelComprobante, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelFecha, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelComprobante))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelFecha)
                    .addComponent(frFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDisponibilidad)
                    .addComponent(frListaDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPagar)
                    .addComponent(frCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelValorCuenta)
                    .addComponent(frNetoPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelValorLetras)
                    .addComponent(frValorLetras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCuenta)
                    .addComponent(frNoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCheque)
                    .addComponent(frCheque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDescripcion)
                    .addComponent(frDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDescuentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevoActionPerformed
        reiniciarFormulario();
    }//GEN-LAST:event_itemNuevoActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        if (validarFormulario()) {
            List<ComprobanteEgreso> listaComprobante = new ArrayList<ComprobanteEgreso>();
            ComprobanteEgreso comprobante = new ComprobanteEgreso();
            comprobante.setConsecutivo(new BigDecimal(frComprobante.getText().trim()));
            comprobante.setVigencia(vigencia);

            listaComprobante = accesoDatos.consultarObjetoPorVigencia(ComprobanteEgreso.class, comprobante, vigencia);

            if (!listaComprobante.isEmpty()) {
                comprobante = listaComprobante.get(0);
                eliminarEgresoDescuento(comprobante);
            }

            comprobante.setDisponibilidad(consultarDisponibilidad());
            comprobante.setFecha(frFecha.getDate());
            comprobante.setCheque(frCheque.getText());
            comprobante.setConsecutivo(new BigDecimal(frComprobante.getText()));
            comprobante.setCuentaBanco(frNoCuenta.getText());
            comprobante.setDescripcion(frDescripcion.getText());
            comprobante.setValorCuenta(obtenerValorRubro(frCuenta.getText()));
            comprobante.setValorLetras(frValorLetras.getText());
            comprobante.setValorPagar(obtenerValorRubro(frNetoPagar.getText()));
            comprobante.setValorDescuentos(sumarDescuentos());

            comprobante = accesoDatos.persistirActualizar(comprobante);

            // Se registra disponibilidad - rubro
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaDescuentos.getModel();
            int numeroFilas = model.getRowCount();
            if (numeroFilas > 0) {
                for (int i = 0; i < numeroFilas; i++) {
                    EgresoDescuento egresoDescuento = new EgresoDescuento();
                    String nombreDescuento = model.getValueAt(i, 0).toString();
                    String porcentajeDescuento = model.getValueAt(i, 1).toString();
                    String valorDescuento = model.getValueAt(i, 2).toString();

                    egresoDescuento.setComprobanteEgreso(comprobante);
                    egresoDescuento.setDescuento(consultarDescuento(nombreDescuento));
                    egresoDescuento.setPorcentaje(new BigDecimal(porcentajeDescuento));
                    egresoDescuento.setValor(obtenerValorRubro(valorDescuento));
                    egresoDescuento.setVigencia(vigencia);
                    accesoDatos.persistirActualizar(egresoDescuento);
                }
            }

            reiniciarFormulario();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemListaAdicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemListaAdicionesActionPerformed
        Lista_Comprobante listaComprobante = new Lista_Comprobante(this, true);
        listaComprobante.setLocationRelativeTo(null);
        listaComprobante.setVisible(true);
    }//GEN-LAST:event_itemListaAdicionesActionPerformed

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void itemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEliminarActionPerformed
        accesoDatos = new AccesoDatos();
        comprobanteController = new ComprobanteEgresoJpaController();
        List<ComprobanteEgreso> listaComprobantes = new ArrayList<ComprobanteEgreso>();
        ComprobanteEgreso comprobante = new ComprobanteEgreso();
        comprobante.setConsecutivo(new BigDecimal(frComprobante.getText()));
        comprobante.setVigencia(vigencia);

        listaComprobantes = accesoDatos.consultarObjetoPorVigencia(ComprobanteEgreso.class, comprobante, vigencia);

        if (!listaComprobantes.isEmpty()) {

            try {
                comprobante = listaComprobantes.get(0);
                comprobanteController.destroy(comprobante);
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
            List<ComprobanteEgreso> listaComprobante = new ArrayList<ComprobanteEgreso>();
            ComprobanteEgreso comprobante = new ComprobanteEgreso();
            comprobante.setConsecutivo(new BigDecimal(frComprobante.getText().trim()));
            comprobante.setVigencia(vigencia);

            listaComprobante = accesoDatos.consultarObjetoPorVigencia(ComprobanteEgreso.class, comprobante, vigencia);

            if (!listaComprobante.isEmpty()) {
                comprobante = listaComprobante.get(0);
                eliminarEgresoDescuento(comprobante);
            }

            comprobante.setDisponibilidad(consultarDisponibilidad());
            comprobante.setFecha(frFecha.getDate());
            comprobante.setCheque(frCheque.getText());
            comprobante.setConsecutivo(new BigDecimal(frComprobante.getText()));
            comprobante.setCuentaBanco(frNoCuenta.getText());
            comprobante.setDescripcion(frDescripcion.getText());
            comprobante.setValorCuenta(obtenerValorRubro(frCuenta.getText()));
            comprobante.setValorLetras(frValorLetras.getText());
            comprobante.setValorPagar(obtenerValorRubro(frNetoPagar.getText()));
            comprobante.setValorDescuentos(sumarDescuentos());

            comprobante = accesoDatos.persistirActualizar(comprobante);

            // Se registra disponibilidad - rubro
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaDescuentos.getModel();
            int numeroFilas = model.getRowCount();
            if (numeroFilas > 0) {
                for (int i = 0; i < numeroFilas; i++) {
                    EgresoDescuento egresoDescuento = new EgresoDescuento();
                    String nombreDescuento = model.getValueAt(i, 0).toString();
                    String valorDescuento = model.getValueAt(i, 1).toString();

                    egresoDescuento.setComprobanteEgreso(comprobante);
                    egresoDescuento.setDescuento(consultarDescuento(nombreDescuento.substring(0, nombreDescuento.indexOf("(") - 1)));
                    egresoDescuento.setPorcentaje(obtenerPorcentaje(nombreDescuento));
                    egresoDescuento.setValor(obtenerValorRubro(valorDescuento));
                    egresoDescuento.setVigencia(vigencia);
                    accesoDatos.persistirActualizar(egresoDescuento);
                }
            }

            reiniciarFormulario();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void listaRegistrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaRegistrosMousePressed
        Lista_Comprobante listaComprobante = new Lista_Comprobante(this, true);
        listaComprobante.setLocationRelativeTo(null);
        listaComprobante.setVisible(true);
    }//GEN-LAST:event_listaRegistrosMousePressed

    private void imprimirRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirRegistroMousePressed
        Generar_Reportes reportes = new Generar_Reportes();

        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            List<ComprobanteEgreso> listaComprobante = new ArrayList<ComprobanteEgreso>();
            ComprobanteEgreso comprobante = new ComprobanteEgreso();
            comprobante.setConsecutivo(new BigDecimal(frComprobante.getText()));
            comprobante.setVigencia(vigencia);

            listaComprobante = accesoDatos.consultarObjetoPorVigencia(ComprobanteEgreso.class, comprobante, vigencia);

            if (!listaComprobante.isEmpty()) {
                comprobante = listaComprobante.get(0);
                reportes.runReporteComprobante(vigencia, comprobante);
            } else {
                JOptionPane.showMessageDialog(null, "La OPS no existe", "Verificación OPS", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor verificar el formulario de entrada", "Verificación Formulario", 0);
        }
    }//GEN-LAST:event_imprimirRegistroMousePressed

    private void eliminarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarRegistroMousePressed
        accesoDatos = new AccesoDatos();
        comprobanteController = new ComprobanteEgresoJpaController();
        List<ComprobanteEgreso> listaComprobantes = new ArrayList<ComprobanteEgreso>();
        ComprobanteEgreso comprobante = new ComprobanteEgreso();
        comprobante.setConsecutivo(new BigDecimal(frComprobante.getText()));
        comprobante.setVigencia(vigencia);

        listaComprobantes = accesoDatos.consultarObjetoPorVigencia(ComprobanteEgreso.class, comprobante, vigencia);

        if (!listaComprobantes.isEmpty()) {

            try {
                comprobante = listaComprobantes.get(0);
                comprobanteController.destroy(comprobante);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Orden_Prestacion_Servicio_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        reiniciarFormulario();
    }//GEN-LAST:event_eliminarRegistroMousePressed

    private void frComprobanteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frComprobanteFocusLost
        if (!frComprobante.getText().trim().equals("")) {
            accesoDatos = new AccesoDatos();
            List<ComprobanteEgreso> listaComprobante = new ArrayList<ComprobanteEgreso>();
            ComprobanteEgreso comprobante = new ComprobanteEgreso();
            comprobante.setConsecutivo(new BigDecimal(frComprobante.getText()));
            comprobante.setVigencia(vigencia);

            listaComprobante = accesoDatos.consultarObjetoPorVigencia(ComprobanteEgreso.class, comprobante, vigencia);

            if (!listaComprobante.isEmpty()) {
                comprobante = listaComprobante.get(0);
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedItem(comprobante.getDisponibilidad().getConsecutivo() + " - " + comprobante.getDisponibilidad().getBeneficiario().getNombre());
                }
                frFecha.setDate(comprobante.getFecha());
                frCheque.setText(comprobante.getCheque());
                frDescripcion.setText(comprobante.getDescripcion());
                frNoCuenta.setText(comprobante.getCuentaBanco());
                frCuenta.setText("$" + formatoNumeroDecimales(comprobante.getValorCuenta().toString()));
                frNetoPagar.setText("$" + formatoNumeroDecimales(comprobante.getValorPagar().toString()));
                frValorDescuento.setText("$0.0");
                frValorLetras.setText(comprobante.getValorLetras());

                if (frListaDecuentos.getItemCount() > 0) {
                    frListaDecuentos.setSelectedItem(0);
                }

                // Llenar tabla de decuentos
                eliminarElementosTabla();

                if (!comprobante.getEgresoDescuentoList().isEmpty()) {
                    List<EgresoDescuento> listaDescuentos = new ArrayList<EgresoDescuento>();
                    listaDescuentos = comprobante.getEgresoDescuentoList();
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) tablaDescuentos.getModel();

                    for (EgresoDescuento descuento : listaDescuentos) {
                        model.addRow(new Object[]{descuento.getDescuento().getNombre() + " (" + descuento.getDescuento().getPorcentaje() + "%)", "$" + formatoNumeroDecimales(descuento.getValor().toString())});
                    }
                }

            } else {
                if (frListaDisponibilidad.getItemCount() > 0) {
                    frListaDisponibilidad.setSelectedIndex(0);
                }
                frFecha.setDate(null);
                frCheque.setText("");
                frDescripcion.setText("");
                frNoCuenta.setText("");
                frCuenta.setText("");
                frNetoPagar.setText("");
                frValorDescuento.setText("$0.0");
                frValorLetras.setText("");

                if (frListaDecuentos.getItemCount() > 0) {
                    frListaDecuentos.setSelectedItem(0);
                }

                eliminarElementosTabla();
            }
        }
    }//GEN-LAST:event_frComprobanteFocusLost

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Abrir Lista
        Descuentos_Egreso registroDescuento = new Descuentos_Egreso(this, true);
        registroDescuento.setLocationRelativeTo(null);
        registroDescuento.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void frListaDisponibilidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frListaDisponibilidadActionPerformed

        if (!frListaDisponibilidad.getSelectedItem().toString().equals("-- Seleccionar --")) {
            String itemSeleccionado = frListaDisponibilidad.getSelectedItem().toString();
            String codigoDisponibilidad = itemSeleccionado.substring(0, itemSeleccionado.indexOf("-") - 1);

            List<Disponibilidad> listaDisponibilidad = new ArrayList<Disponibilidad>();
            Disponibilidad disponibilidad = new Disponibilidad();
            disponibilidad.setConsecutivo(new BigDecimal(codigoDisponibilidad));
            disponibilidad.setVigencia(vigencia);

            listaDisponibilidad = accesoDatos.consultarObjetoPorVigencia(Disponibilidad.class, disponibilidad, vigencia);

            if (!listaDisponibilidad.isEmpty()) {
                BigDecimal valorDisponibilidad = new BigDecimal(0);
                disponibilidad = listaDisponibilidad.get(0);

                List<DisponibilidadRubro> listaDisponibilidadRubros = new ArrayList<DisponibilidadRubro>();
                listaDisponibilidadRubros = disponibilidad.getDisponibilidadRubroList();

                for (DisponibilidadRubro disponibilidadRubro : listaDisponibilidadRubros) {
                    valorDisponibilidad = valorDisponibilidad.add(disponibilidadRubro.getValor());
                }

                frCuenta.setText("$" + formatoNumeroDecimales(valorDisponibilidad.toString()));
                frNetoPagar.setText("$" + formatoNumeroDecimales(valorDisponibilidad.toString()));
            }

            System.out.println(codigoDisponibilidad);

        } else {
            frCuenta.setText("$0.0");
        }
    }//GEN-LAST:event_frListaDisponibilidadActionPerformed

    private void frValorDescuentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_frValorDescuentoKeyTyped
        char tecla = evt.getKeyChar();

        if ((tecla < '0' || tecla > '9') && (tecla != '.' && tecla != ',')) {
            evt.consume();
        } else {
            String valor = frValorDescuento.getText();
            valor = valor.replace("$", "");
            valor = valor + tecla;
            if (tecla != '.') {
                valor = formatoNumeroDecimales(valor);
            }
            valor = "$" + valor;
            frValorDescuento.setText(valor);
            evt.consume();
        }
    }//GEN-LAST:event_frValorDescuentoKeyTyped

    private void frValorDescuentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frValorDescuentoFocusGained
        if (frValorDescuento.getText().equals("$0.0")) {
            frValorDescuento.setText("");
        }
    }//GEN-LAST:event_frValorDescuentoFocusGained

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String descuentoSeleccionado = frListaDecuentos.getSelectedItem().toString();

        if (!descuentoSeleccionado.equals("-- Seleccionar --")) {

            BigDecimal porcentajeDescuento = obtenerPorcentaje(descuentoSeleccionado);

            if (!frValorDescuento.getText().trim().equals("") && !frValorDescuento.getText().trim().equals("$0.0") && !porcentajeDescuento.equals("")) {
                DefaultTableModel model = new DefaultTableModel();
                model = (DefaultTableModel) tablaDescuentos.getModel();
                model.addRow(new Object[]{descuentoSeleccionado, frValorDescuento.getText()});

                // Se debe restar el valor del descuento para calcular neto a pagar
                BigDecimal valorDescuento = obtenerValorRubro(frValorDescuento.getText());
                BigDecimal netoPagar = obtenerValorRubro(frNetoPagar.getText());
                netoPagar = netoPagar.subtract(valorDescuento);
                frNetoPagar.setText("$" + formatoNumeroDecimales(netoPagar.toString()));

                //
                frValorDescuento.setText("$0.0");
                frListaDecuentos.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(null, "Debe digitar un valor para el Decusento", "Verificación Descuento", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un descuento de la lista", "Verificación Descuento", 0);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void botonQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonQuitarActionPerformed
        int filaSeleccionada = tablaDescuentos.getSelectedRow();

        if (filaSeleccionada != -1) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaDescuentos.getModel();

            String valorDescuentoSeleccionado = model.getValueAt(filaSeleccionada, 1).toString();
            BigDecimal valorDescuento = obtenerValorRubro(valorDescuentoSeleccionado);
            BigDecimal netoPagar = obtenerValorRubro(frNetoPagar.getText());
            netoPagar = netoPagar.add(valorDescuento);
            frNetoPagar.setText("$" + formatoNumeroDecimales(netoPagar.toString()));

            // Se elimina de la tabla
            model.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(null, "Para eliminar un registro debe seleccionar una fila", "Eliminar Registro", 0);
        }
    }//GEN-LAST:event_botonQuitarActionPerformed

    private void frCuentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_frCuentaKeyTyped
        char tecla = evt.getKeyChar();

        if ((tecla < '0' || tecla > '9') && (tecla != '.' && tecla != ',')) {
            evt.consume();
        } else {
            String valor = frCuenta.getText();
            valor = valor.replace("$", "");
            valor = valor + tecla;
            if (tecla != '.') {
                valor = formatoNumeroDecimales(valor);
            }
            valor = "$" + valor;
            frCuenta.setText(valor);
            frNetoPagar.setText(valor);
            evt.consume();
        }
    }//GEN-LAST:event_frCuentaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JButton botonQuitar;
    private javax.swing.JLabel eliminarRegistro;
    private javax.swing.JTextField frCheque;
    private javax.swing.JTextField frComprobante;
    private javax.swing.JTextField frCuenta;
    private javax.swing.JTextField frDescripcion;
    private com.toedter.calendar.JDateChooser frFecha;
    private javax.swing.JComboBox<String> frListaDecuentos;
    private javax.swing.JComboBox<String> frListaDisponibilidad;
    private javax.swing.JTextField frNetoPagar;
    private javax.swing.JTextField frNoCuenta;
    private javax.swing.JTextField frValorDescuento;
    private javax.swing.JTextField frValorLetras;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JLabel imprimirRegistro;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemEliminar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemListaAdiciones;
    private javax.swing.JMenuItem itemNuevo;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel labelCheque;
    private javax.swing.JLabel labelComprobante;
    private javax.swing.JLabel labelCuenta;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelDescuento;
    private javax.swing.JLabel labelDisponibilidad;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JLabel labelPagar;
    private javax.swing.JLabel labelValorCuenta;
    private javax.swing.JLabel labelValorDescuento;
    private javax.swing.JLabel labelValorLetras;
    private javax.swing.JLabel listaRegistros;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JMenu menuTraslado;
    private javax.swing.JLabel nuevoRegistro;
    private javax.swing.JPanel panelDescuentos;
    private javax.swing.JTable tablaDescuentos;
    // End of variables declaration//GEN-END:variables
}
