/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.modelo.Presupuesto;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.TipoRubro;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.utilidades.Generar_Reportes;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Presupuesto_Inicial extends javax.swing.JInternalFrame {

    //***** Atributos de la clase *****//
    Home home;
    Vigencia vigencia;
    AccesoDatos accesoDatos;

    /**
     * Creates new form Presupuesto
     */
    public Presupuesto_Inicial(Home parent) {
        super();
        this.home = parent;
        accesoDatos = new AccesoDatos();

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);

        initComponents();
        consultarVigencia();
        consultarCuentasRegistradas();
        cargarPresupuestoRegistrado();
    }

    /**
     * Metodo que consulta la vigencia activa
     */
    private void consultarVigencia() {
        vigencia = home.getVigencia();
    }

    /**
     * *
     * Metodo que carga las cuentas registradas en la vigencia actual
     */
    private void consultarCuentasRegistradas() {
        frListaCuentas.removeAllItems();

        List<Rubro> listaCuentas = new ArrayList<Rubro>();
        Rubro cuenta = new Rubro();
        cuenta.setVigencia(vigencia);
        cuenta.setTipoRubro(new TipoRubro(new BigDecimal(1)));
        listaCuentas = accesoDatos.consultarObjetoPorVigencia(Rubro.class, cuenta, vigencia);

        if (listaCuentas != null && !listaCuentas.isEmpty()) {
            frListaCuentas.removeAllItems();
            frListaCuentas.addItem("-- Seleccione --");
            for (Rubro cuentaIterada : listaCuentas) {
                frListaCuentas.addItem(cuentaIterada.getCodigo() + " - " + cuentaIterada.getNombre().trim());
            }
        }
    }

    /**
     * Metodo que carga el presupuesto de la vigencia actual en la tabla
     */
    private void cargarPresupuestoRegistrado() {
        List<Presupuesto> listaPresupuesto = new ArrayList<Presupuesto>();

        listaPresupuesto = accesoDatos.consultarTodosPorVigencia(Presupuesto.class, vigencia);

        if (!listaPresupuesto.isEmpty()) {
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaPresupuesto.getModel();

            DefaultTableCellRenderer AlinearDerecha = new DefaultTableCellRenderer();
            AlinearDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
            tablaPresupuesto.getColumnModel().getColumn(2).setCellRenderer(AlinearDerecha);

            DefaultTableCellRenderer AlinearCentro = new DefaultTableCellRenderer();
            AlinearCentro.setHorizontalAlignment(SwingConstants.CENTER);
            tablaPresupuesto.getColumnModel().getColumn(3).setCellRenderer(AlinearCentro);

            for (Presupuesto presupuesto : listaPresupuesto) {
                model.addRow(new Object[]{presupuesto.getRubro().getCodigo(), presupuesto.getRubro().getNombre(), "$" + formatoNumeroDecimales(presupuesto.getValor().toString()), presupuesto.getRubro().getTipoRubro().getTipoRubro(), (presupuesto.getRubro().getCuenta() != null) ? presupuesto.getRubro().getCuenta().getNombre() : "", (presupuesto.getRubro().getSubcuenta() != null) ? presupuesto.getRubro().getSubcuenta().getNombre() : ""});
            }
        }
    }

    private Rubro consultarSubcuentaSeleccionada() {
        accesoDatos = new AccesoDatos();
        Rubro rubro = new Rubro();

        if (frListaSubcuentas.getItemCount() > 0) {
            String subCuentaSeleccionada = frListaSubcuentas.getSelectedItem().toString();
            if (!subCuentaSeleccionada.equals("-- Seleccione --")) {
                String codigo = subCuentaSeleccionada.substring(0, subCuentaSeleccionada.indexOf("-") - 1).trim();
                rubro.setCodigo(codigo);
                rubro.setTipoRubro(new TipoRubro(new BigDecimal(2)));
                rubro.setVigencia(vigencia);
                rubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia).get(0);
            }
        }

        return rubro;
    }

    private Rubro consultarCuentaSeleccionada() {
        accesoDatos = new AccesoDatos();
        Rubro rubro = new Rubro();

        if (frListaCuentas.getItemCount() > 0) {
            String cuentaSeleccionada = frListaCuentas.getSelectedItem().toString();

            if (!cuentaSeleccionada.equals("-- Seleccione --") && !cuentaSeleccionada.equals("")) {
                String codigo = cuentaSeleccionada.substring(0, cuentaSeleccionada.indexOf("-") - 1).trim();
                rubro.setCodigo(codigo);
                rubro.setTipoRubro(new TipoRubro(new BigDecimal(1)));
                rubro.setVigencia(vigencia);
                rubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia).get(0);
            }
        }

        return rubro;
    }

    private Boolean verificarRubro(String codigoRubro) {
        Boolean verificacionExitosa = true;

        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaPresupuesto.getModel();
        int numeroFinal = model.getRowCount();

        for (int i = 0; i < numeroFinal; i++) {
            String primeraColumna = model.getValueAt(i, 0).toString();
            if (codigoRubro.equals(primeraColumna)) {
                verificacionExitosa = false;
                break;
            }
        }

        return verificacionExitosa;
    }

    private void registrarRubroEnTable(Rubro rubro) {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaPresupuesto.getModel();

        model.addRow(new Object[]{rubro.getCodigo(), rubro.getNombre(), "$" + formatoNumeroDecimales(rubro.getValor().toString()), rubro.getTipoRubro().getTipoRubro(), (rubro.getCuenta() != null) ? rubro.getCuenta().getNombre() : "", (rubro.getSubcuenta() != null) ? rubro.getSubcuenta().getNombre() : ""});
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

    private void restarValor(Rubro rubro, BigDecimal valor) {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaPresupuesto.getModel();
        int numeroFinal = model.getRowCount();

        for (int i = 0; i < numeroFinal; i++) {
            String primeraColumna = model.getValueAt(i, 0).toString();
            if (rubro.getCodigo().equals(primeraColumna)) {
                String valorActual = model.getValueAt(i, 2).toString();
                BigDecimal valorNumerico = obtenerValorRubro(valorActual);
                valorNumerico = valorNumerico.subtract(valor);
                model.setValueAt("$" + formatoNumeroDecimales(valorNumerico.toString()), i, 2);
                // Si el valor de la resta es cero se debe eliminar de la lista
                if (valorNumerico.equals(new BigDecimal(0))) {
                    model.removeRow(i);
                }

                break;
            }
        }
    }

    private BigDecimal obtenerValorRubro(String valor) {
        valor = valor.replace(",", "");
        valor = valor.replace("$", "");
        BigDecimal valorNumerico = BigDecimal.valueOf(Double.parseDouble(valor));;
        return valorNumerico;
    }

    private void eliminarPresupuesto() {
        accesoDatos = new AccesoDatos();
        accesoDatos.limpiarTablaVigencia("presupuesto", vigencia);
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

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            inicializarVentanaProcesos();
        }
    };

    private void inicializarVentanaProcesos() {
        Loading_Proceso generarPresupuesto = new Loading_Proceso(this, false); // Modal debe ser false para que se ejecute el hilo
        generarPresupuesto.setLocationRelativeTo(null);
        generarPresupuesto.setVisible(true);
        // Deshabilito boton generar
        guardarRegistro.setEnabled(false);

        // Aqui se desarrolla todo el proceso de actualizar el presupuesto //        
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaPresupuesto.getModel();
        int numeroFilas = model.getRowCount();

        if (numeroFilas > 0) {
            eliminarPresupuesto();
            Presupuesto presupuesto = new Presupuesto();
            for (int i = 0; i < numeroFilas; i++) {
                String codigoRubro = model.getValueAt(i, 0).toString();
                String valorRubro = model.getValueAt(i, 2).toString();
                presupuesto.setRubro(consultarRubro(codigoRubro));
                presupuesto.setOrden(new BigDecimal(i));
                presupuesto.setVigencia(vigencia);
                presupuesto.setValor(obtenerValorRubro(valorRubro));
                accesoDatos.persistirActualizar(presupuesto);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No existen registro en la tabla", "Verificación de formulario", 0);
        }

        // Ocullto loading y habilito el boton generar         
        generarPresupuesto.setVisible(false);
        guardarRegistro.setEnabled(true);
    }

    private void quitarRubroTabla() {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaPresupuesto.getModel();

        int filaSeleccionada = tablaPresupuesto.getSelectedRow();

        if (filaSeleccionada != -1) {
            String codigoRubro = model.getValueAt(filaSeleccionada, 0).toString();
            accesoDatos = new AccesoDatos();
            Rubro rubro = new Rubro();
            rubro.setCodigo(codigoRubro);
            rubro.setVigencia(vigencia);
            rubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia).get(0);
            
            // Verificar si el rubro ya hace parte del presupuesto
            if(rubro.getAdicionRubroList().isEmpty() && rubro.getDisponibilidadRubroList().isEmpty() && 
                        rubro.getTrasladoRubroList().isEmpty()){
                            
                model.removeRow(filaSeleccionada);            
            } else {
                frMensaje.setText("El rubro no se puede eliminar del presupuesto.");
            }
                        
        } else {
            JOptionPane.showMessageDialog(null, "Para eliminar un registro debe seleccionar una fila", "Eliminar Registro", 0);
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

        jLabel1 = new javax.swing.JLabel();
        barraHerramientas = new javax.swing.JToolBar();
        guardarRegistro = new javax.swing.JLabel();
        imprimirPresupuesto = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        eliminarRegistro = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPresupuesto = new javax.swing.JTable();
        botonSubir = new javax.swing.JButton();
        botonBajar = new javax.swing.JButton();
        botonQuitar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        frListaCuentas = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        frListaSubcuentas = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        frListaAuxiliares = new javax.swing.JComboBox<>();
        botonRegistrar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        frMensaje = new javax.swing.JLabel();
        frMensajeQuitar = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        menuPresupuesto = new javax.swing.JMenu();
        itemGuardar = new javax.swing.JMenuItem();
        itemCerrar = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        itemEliminar = new javax.swing.JMenuItem();

        jLabel1.setText("jLabel1");

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

        imprimirPresupuesto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imprimirPresupuesto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/imprimir.png"))); // NOI18N
        imprimirPresupuesto.setAlignmentX(0.5F);
        imprimirPresupuesto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        imprimirPresupuesto.setMaximumSize(new java.awt.Dimension(25, 20));
        imprimirPresupuesto.setMinimumSize(new java.awt.Dimension(25, 20));
        imprimirPresupuesto.setPreferredSize(new java.awt.Dimension(25, 20));
        imprimirPresupuesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                imprimirPresupuestoMousePressed(evt);
            }
        });
        barraHerramientas.add(imprimirPresupuesto);
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

        tablaPresupuesto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rubro", "Nombre", "Valor", "Tipo", "Cuenta", "Subcuenta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaPresupuesto);
        if (tablaPresupuesto.getColumnModel().getColumnCount() > 0) {
            tablaPresupuesto.getColumnModel().getColumn(0).setMinWidth(70);
            tablaPresupuesto.getColumnModel().getColumn(0).setPreferredWidth(70);
            tablaPresupuesto.getColumnModel().getColumn(0).setMaxWidth(70);
            tablaPresupuesto.getColumnModel().getColumn(2).setMinWidth(100);
            tablaPresupuesto.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaPresupuesto.getColumnModel().getColumn(2).setMaxWidth(100);
            tablaPresupuesto.getColumnModel().getColumn(3).setMinWidth(70);
            tablaPresupuesto.getColumnModel().getColumn(3).setPreferredWidth(70);
            tablaPresupuesto.getColumnModel().getColumn(3).setMaxWidth(70);
        }

        botonSubir.setText("Subir");
        botonSubir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSubirActionPerformed(evt);
            }
        });

        botonBajar.setText("Bajar");
        botonBajar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBajarActionPerformed(evt);
            }
        });

        botonQuitar.setText("Quitar");
        botonQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonQuitarActionPerformed(evt);
            }
        });

        jLabel2.setText("Cuenta");

        frListaCuentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frListaCuentasActionPerformed(evt);
            }
        });

        jLabel3.setText("Subcuenta");

        frListaSubcuentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frListaSubcuentasActionPerformed(evt);
            }
        });

        jLabel4.setText("Auxiliar");

        botonRegistrar.setText("Registrar");
        botonRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRegistrarActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("* Guardar para actualizar las modificaciones al presupuesto");

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuPresupuesto.setText("Inicio");

        itemGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        itemGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/guardar.png"))); // NOI18N
        itemGuardar.setText("Guardar");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuPresupuesto.add(itemGuardar);

        itemCerrar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        itemCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/Cerrar.png"))); // NOI18N
        itemCerrar.setText("Cerrar");
        itemCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCerrarActionPerformed(evt);
            }
        });
        menuPresupuesto.add(itemCerrar);

        barraMenu.add(menuPresupuesto);

        jMenu1.setText("Editar");

        itemEliminar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        itemEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/eliminar.png"))); // NOI18N
        itemEliminar.setText("Eliminar");
        itemEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemEliminarActionPerformed(evt);
            }
        });
        jMenu1.add(itemEliminar);

        barraMenu.add(jMenu1);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonQuitar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frMensajeQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel5)
                        .addGap(0, 26, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonBajar)
                    .addComponent(botonSubir))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonRegistrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(frListaCuentas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(frListaAuxiliares, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(frListaSubcuentas, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonSubir)
                        .addGap(18, 18, 18)
                        .addComponent(botonBajar)
                        .addGap(106, 106, 106))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(frListaSubcuentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(frListaCuentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(frListaAuxiliares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(botonRegistrar)
                                    .addComponent(frMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonQuitar)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(frMensajeQuitar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void itemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEliminarActionPerformed
        quitarRubroTabla();
    }//GEN-LAST:event_itemEliminarActionPerformed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        // Creo el hilo
        Thread thread1 = new Thread(runnable);
        thread1.start();
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void eliminarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarRegistroMousePressed
        quitarRubroTabla();
    }//GEN-LAST:event_eliminarRegistroMousePressed

    private void frListaCuentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frListaCuentasActionPerformed
        accesoDatos = new AccesoDatos();
        frListaSubcuentas.removeAllItems();
        Rubro cuentaSeleccionada = new Rubro();
        cuentaSeleccionada = consultarCuentaSeleccionada();
        if (cuentaSeleccionada.getIdRubro() != null && !cuentaSeleccionada.getRubroList1().isEmpty()) {
            List<Rubro> listaSubcuentas = new ArrayList<Rubro>();
            listaSubcuentas = cuentaSeleccionada.getRubroList1();
            frListaSubcuentas.setEnabled(true);
            frListaSubcuentas.addItem("-- Seleccione --");
            for (Rubro subCuentasIteradas : listaSubcuentas) {
                if (subCuentasIteradas.getTipoRubro().getTipoRubro().equals("Subcuenta")) {
                    frListaSubcuentas.addItem(subCuentasIteradas.getCodigo() + " - " + subCuentasIteradas.getNombre());
                }
            }
        } else {
            frListaSubcuentas.addItem("-- Seleccione --");
        }
    }//GEN-LAST:event_frListaCuentasActionPerformed

    private void frListaSubcuentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frListaSubcuentasActionPerformed
        Rubro subcuenta = new Rubro();
        subcuenta = consultarSubcuentaSeleccionada();
        frListaAuxiliares.removeAllItems();
        if (subcuenta.getIdRubro() != null && !subcuenta.getRubroList().isEmpty()) {
            frListaAuxiliares.addItem("-- Seleccione --");
            for (Rubro auxiliares : subcuenta.getRubroList()) {
                frListaAuxiliares.addItem(auxiliares.getCodigo() + " - " + auxiliares.getNombre());
            }
        } else {
            frListaAuxiliares.addItem("-- Seleccione --");
        }
    }//GEN-LAST:event_frListaSubcuentasActionPerformed

    private void botonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegistrarActionPerformed

        String rubroSeleccionado = frListaAuxiliares.getSelectedItem().toString();

        if (!rubroSeleccionado.equals("-- Seleccionar --")) {

            rubroSeleccionado = rubroSeleccionado.substring(0, rubroSeleccionado.indexOf("-") - 1);

            // Retorna true cuando no se encuentra en la tabla
            if (verificarRubro(rubroSeleccionado)) {
                frMensaje.setText("");

                accesoDatos = new AccesoDatos();
                Rubro rubro = new Rubro();
                rubro.setCodigo(rubroSeleccionado);
                rubro.setVigencia(vigencia);
                rubro.setTipoRubro(new TipoRubro(new BigDecimal(3)));
                rubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia).get(0);

                // verificar si la cuenta esta en la tabla. Retorna true cuando no esta en la tabla
                if (verificarRubro(rubro.getCuenta().getCodigo())) {
                    // Registro la cuenta, despues la subcuenta y por ultimo el auxiliar                    
                    registrarRubroEnTable(rubro.getCuenta());
                    registrarRubroEnTable(rubro.getSubcuenta());
                    registrarRubroEnTable(rubro);
                } else if (verificarRubro(rubro.getSubcuenta().getCodigo())) {
                    registrarRubroEnTable(rubro.getSubcuenta());
                    registrarRubroEnTable(rubro);
                } else {
                    // Al valor de la cuenta y subcuenta registrado en la tabla se debe sumar el valor del rubro                   
                    registrarRubroEnTable(rubro);
                }

            } else {
                frMensaje.setForeground(Color.RED);
                frMensaje.setText("El rubro ya fue registrado en la tabla");
            }
        }

    }//GEN-LAST:event_botonRegistrarActionPerformed

    /**
     * MEtodo que elimina fila seleccionada en la tabla
     *
     * @param evt
     */
    private void botonQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonQuitarActionPerformed
        quitarRubroTabla();
    }//GEN-LAST:event_botonQuitarActionPerformed

    private void botonSubirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSubirActionPerformed
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaPresupuesto.getModel();

        int filaSeleccionada = tablaPresupuesto.getSelectedRow();

        if (filaSeleccionada != -1 && filaSeleccionada != 0) {
            model.moveRow(filaSeleccionada, filaSeleccionada, filaSeleccionada - 1);
            tablaPresupuesto.setRowSelectionInterval(filaSeleccionada - 1, filaSeleccionada - 1);
        }
    }//GEN-LAST:event_botonSubirActionPerformed

    private void botonBajarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBajarActionPerformed
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaPresupuesto.getModel();

        int filaSeleccionada = tablaPresupuesto.getSelectedRow();

        if (filaSeleccionada != -1 && filaSeleccionada != (model.getRowCount() - 1)) {

            model.moveRow(filaSeleccionada, filaSeleccionada, filaSeleccionada + 1);
            tablaPresupuesto.setRowSelectionInterval(filaSeleccionada + 1, filaSeleccionada + 1);
        }
    }//GEN-LAST:event_botonBajarActionPerformed

    private void imprimirPresupuestoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirPresupuestoMousePressed
        Generar_Reportes reportes = new Generar_Reportes();
        reportes.runReportePresupuestoInicial(vigencia);
    }//GEN-LAST:event_imprimirPresupuestoMousePressed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        // Creo el hilo
        Thread thread1 = new Thread(runnable);
        thread1.start();
    }//GEN-LAST:event_itemGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JButton botonBajar;
    private javax.swing.JButton botonQuitar;
    private javax.swing.JButton botonRegistrar;
    private javax.swing.JButton botonSubir;
    private javax.swing.JLabel eliminarRegistro;
    private javax.swing.JComboBox<String> frListaAuxiliares;
    private javax.swing.JComboBox<String> frListaCuentas;
    private javax.swing.JComboBox<String> frListaSubcuentas;
    private javax.swing.JLabel frMensaje;
    private javax.swing.JLabel frMensajeQuitar;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JLabel imprimirPresupuesto;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemEliminar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JMenu menuPresupuesto;
    private javax.swing.JTable tablaPresupuesto;
    // End of variables declaration//GEN-END:variables
}
