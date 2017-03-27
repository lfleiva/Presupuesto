/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.control.RubroJpaController;
import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.TipoRubro;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.utilidades.Generar_Reportes;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
public class Rubro_Presupuestal extends javax.swing.JInternalFrame {

    //***** Atributos de la clase *****//
    Home home;
    Vigencia vigencia;
    AccesoDatos accesoDatos;
    TipoRubro tipoRubro;

    /**
     * Creates new form Rubro_Presupuestal
     */
    public Rubro_Presupuestal(Home parent) {
        super();
        this.home = parent;

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);

        initComponents();

        consultarVigencia();
        cargarTiposRubro();
        iniciarFormulario();
    }

    //***** Metodos privados *****//
    /**
     * Metodo que consulta la vigencia activa
     */
    private void consultarVigencia() {
        vigencia = home.getVigencia();
    }

    private void cargarTiposRubro() {
        accesoDatos = new AccesoDatos();
        TipoRubro tipoRubro = new TipoRubro();
        List<TipoRubro> listaTipoRubro = new ArrayList<TipoRubro>();
        listaTipoRubro = accesoDatos.consultarTodos(tipoRubro, TipoRubro.class);

        for (TipoRubro tipo : listaTipoRubro) {
            frListaTipoRubro.addItem(tipo.getTipoRubro());
        }
    }

    private void iniciarFormulario() {
        frListaTipoRubro.setSelectedIndex(0);
        frListaCuentas.removeAllItems();
        frListaSubcuentas.removeAllItems();
        frListaCuentas.setEnabled(false);
        frListaSubcuentas.setEnabled(false);
        frCodigoPresupuestal.setText("");
        frNombreRubro.setText("");
        frValor.setText("$0.0");
        frCodigoPresupuestal.requestFocus();
        frMensaje.setText("");
    }

    private void consultarCuentasRegistradas() {
        // Elimino los datos que esten
        frListaCuentas.removeAllItems();
        accesoDatos = new AccesoDatos();
        List<Rubro> listaCuentas = new ArrayList<Rubro>();
        Rubro cuenta = new Rubro();
        cuenta.setVigencia(vigencia);
        cuenta.setTipoRubro(new TipoRubro(new BigDecimal(1)));
        listaCuentas = accesoDatos.consultarObjetoPorVigencia(Rubro.class, cuenta, vigencia);

        if (listaCuentas != null && !listaCuentas.isEmpty()) {
            frListaCuentas.addItem("-- Seleccione --");
            for (Rubro cuentaIterada : listaCuentas) {
                frListaCuentas.addItem(cuentaIterada.getCodigo() + " - " + cuentaIterada.getNombre().trim());
            }
        }
    }

    private void consultarSubcuentasRegistradas() {
        // Elimino los datos que esten
        frListaSubcuentas.removeAllItems();
        accesoDatos = new AccesoDatos();
        List<Rubro> listaSubcuentas = new ArrayList<Rubro>();
        Rubro subCuenta = new Rubro();
        subCuenta.setVigencia(vigencia);
        subCuenta.setTipoRubro(new TipoRubro(new BigDecimal(2)));
        listaSubcuentas = accesoDatos.consultarObjetoPorVigencia(Rubro.class, subCuenta, vigencia);

        if (listaSubcuentas != null && !listaSubcuentas.isEmpty()) {
            frListaSubcuentas.addItem("-- Seleccione --");
            for (Rubro subCuentaIterada : listaSubcuentas) {
                frListaSubcuentas.addItem(subCuentaIterada.getCodigo() + " - " + subCuentaIterada.getNombre().trim());
            }
        }
    }

    private boolean validarFormulario() {
        boolean validacionExitosa = true;

        String tipo = frListaTipoRubro.getSelectedItem().toString();

        switch (tipo) {
            case "Cuenta":
                if (frCodigoPresupuestal.getText().trim().equals("") || frNombreRubro.getText().trim().equals("") || frValor.getText().trim().equals("$0.0") || frValor.getText().trim().equals("")) {
                    validacionExitosa = false;
                }
                break;
            case "Subcuenta":
                if (frCodigoPresupuestal.getText().trim().equals("") || frNombreRubro.getText().trim().equals("") || frListaCuentas.getSelectedItem().toString().equals("-- Seleccionar --") || frValor.getText().trim().equals("$0.0") || frValor.getText().trim().equals("")) {
                    validacionExitosa = false;
                }
                break;
            case "Auxiliar":
                if (frCodigoPresupuestal.getText().trim().equals("") || frNombreRubro.getText().trim().equals("") || frListaCuentas.getSelectedItem().toString().equals("-- Seleccionar --") || frListaSubcuentas.getSelectedItem().toString().equals("-- Seleccionar --") || frValor.getText().trim().equals("$0.0") || frValor.getText().trim().equals("")) {
                    validacionExitosa = false;
                }
                break;
        }

        return validacionExitosa;
    }

    private Rubro registrarRubro() {
        boolean modificacion = false;
        if (validarFormulario()) {
            Rubro rubro = new Rubro();
            rubro.setCodigo(frCodigoPresupuestal.getText().trim());
            rubro.setVigencia(vigencia);

            List<Rubro> listaRubro = new ArrayList<Rubro>();
            listaRubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia);

            if (!listaRubro.isEmpty()) {
                rubro = listaRubro.get(0);
                modificacion = true;
            }

            TipoRubro tipoRubro = new TipoRubro();
            String tipo = frListaTipoRubro.getSelectedItem().toString();

            if (modificacion == false) {

                rubro = registrarActualizarRubro(rubro, tipo);
                
                frMensaje.setForeground(Color.DARK_GRAY);
                frMensaje.setText("El Rubro se registro correctamente");
                return rubro;
                
            } else if(modificacion == true && rubro.getPresupuestoList().isEmpty()
                    && rubro.getDisponibilidadRubroList().isEmpty()
                    && rubro.getEjecucionList().isEmpty()
                    && rubro.getPresupuestoList().isEmpty()
                    && rubro.getTrasladoRubroList().isEmpty()
                    && rubro.getAdicionRubroList().isEmpty()){
                
                rubro = registrarActualizarRubro(rubro, tipo);
                frMensaje.setForeground(Color.DARK_GRAY);
                frMensaje.setText("El Rubro se actualizo correctamente");
                return rubro;
            
            } else {                
                frMensaje.setForeground(Color.RED);
                frMensaje.setText("El Rubro se encuentra registrado en el Presupuesto. No se puede modificar.");
                return rubro;
            }

        } else {
            frMensaje.setForeground(Color.RED);
            frMensaje.setText("Error en el formulario de registro");
            return null;
        }
    }

    private Rubro registrarActualizarRubro(Rubro rubro, String tipo) {
        switch (tipo) {
            case "Cuenta":
                tipoRubro = consultarTipoRubro("Cuenta");
                rubro.setTipoRubro(tipoRubro);
                rubro.setVigencia(vigencia);
                rubro.setCodigo(frCodigoPresupuestal.getText().trim());
                rubro.setNombre(frNombreRubro.getText().trim());
                rubro.setCuenta(null);
                rubro.setSubcuenta(null);
                rubro.setValor(obtenerValorRubro());
                guardarRubro(rubro);
                iniciarFormulario();
                break;

            case "Subcuenta":
                tipoRubro = consultarTipoRubro("Subcuenta");
                rubro.setTipoRubro(tipoRubro);
                rubro.setVigencia(vigencia);
                rubro.setCodigo(frCodigoPresupuestal.getText().trim());
                rubro.setNombre(frNombreRubro.getText().trim());
                rubro.setCuenta(consultarCuentaSeleccionada());
                rubro.setSubcuenta(null);
                rubro.setValor(obtenerValorRubro());
                guardarRubro(rubro);
                iniciarFormulario();
                break;
            case "Auxiliar":
                tipoRubro = consultarTipoRubro("Auxiliar");
                rubro.setTipoRubro(tipoRubro);
                rubro.setVigencia(vigencia);
                rubro.setCodigo(frCodigoPresupuestal.getText().trim());
                rubro.setNombre(frNombreRubro.getText().trim());
                rubro.setCuenta(consultarCuentaSeleccionada());
                rubro.setSubcuenta(consultarSubcuentaSeleccionada());
                rubro.setValor(obtenerValorRubro());
                rubro = guardarRubro(rubro);
                iniciarFormulario();
                break;
        }
        
        return rubro;
    }

    private void eliminarRubro() {
        if (validarFormulario()) {
            accesoDatos = new AccesoDatos();
            RubroJpaController rubroController = new RubroJpaController();
            Rubro rubro = new Rubro();
            rubro.setCodigo(frCodigoPresupuestal.getText().trim());
            rubro.setVigencia(vigencia);

            List<Rubro> listaRubro = new ArrayList<Rubro>();
            listaRubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia);

            if (!listaRubro.isEmpty()) {
                try {
                    rubro = listaRubro.get(0);
                    if (rubro.getPresupuestoList().isEmpty() && rubro.getAdicionRubroList().isEmpty()
                            && rubro.getDisponibilidadRubroList().isEmpty()
                            && rubro.getEjecucionList().isEmpty()
                            && rubro.getTrasladoRubroList().isEmpty()) {
                        rubroController.destroy(rubro);
                        iniciarFormulario();
                    } else {
                        frMensaje.setForeground(Color.RED);
                        frMensaje.setText("Rubro registrado en presupuesto. No se puede eliminar.");
                    }
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Rubro_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
                    frMensaje.setForeground(Color.RED);
                    frMensaje.setText("Error al intentar eliminar el rubro");
                }
            } else {
                frMensaje.setForeground(Color.RED);
                frMensaje.setText("El rubro no existe");
            }
        } else {
            frMensaje.setForeground(Color.RED);
            frMensaje.setText("Error en el formulario");
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
        listaRubros = new javax.swing.JLabel();
        imprimirAdicion = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        eliminarRegistro = new javax.swing.JLabel();
        labelListaTipoRubro = new javax.swing.JLabel();
        frListaTipoRubro = new javax.swing.JComboBox<>();
        labelCodigo = new javax.swing.JLabel();
        frCodigoPresupuestal = new javax.swing.JTextField();
        labelNombre = new javax.swing.JLabel();
        frNombreRubro = new javax.swing.JTextField();
        labelCuenta = new javax.swing.JLabel();
        frListaCuentas = new javax.swing.JComboBox<>();
        labelSubcuenta = new javax.swing.JLabel();
        frListaSubcuentas = new javax.swing.JComboBox<>();
        labelValor = new javax.swing.JLabel();
        frValor = new javax.swing.JTextField();
        frMensaje = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        menuRubro = new javax.swing.JMenu();
        itemNuevoRegistro = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        itemLista = new javax.swing.JMenuItem();
        itemCerrar = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
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
        nuevoRegistro.setMaximumSize(new java.awt.Dimension(25, 20));
        nuevoRegistro.setMinimumSize(new java.awt.Dimension(25, 20));
        nuevoRegistro.setName(""); // NOI18N
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

        listaRubros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        listaRubros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/lista.png"))); // NOI18N
        listaRubros.setAlignmentX(0.5F);
        listaRubros.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        listaRubros.setMaximumSize(new java.awt.Dimension(25, 20));
        listaRubros.setMinimumSize(new java.awt.Dimension(25, 20));
        listaRubros.setPreferredSize(new java.awt.Dimension(25, 20));
        listaRubros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaRubrosMousePressed(evt);
            }
        });
        barraHerramientas.add(listaRubros);

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

        labelListaTipoRubro.setText("Tipo de Rubro");

        frListaTipoRubro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frListaTipoRubroActionPerformed(evt);
            }
        });

        labelCodigo.setText("Codigo");

        frCodigoPresupuestal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frCodigoPresupuestalFocusLost(evt);
            }
        });
        frCodigoPresupuestal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                frCodigoPresupuestalKeyTyped(evt);
            }
        });

        labelNombre.setText("Nombre");

        labelCuenta.setText("Cuenta");

        frListaCuentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frListaCuentasActionPerformed(evt);
            }
        });

        labelSubcuenta.setText("Subcuenta");

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

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuRubro.setText("Inicio");
        menuRubro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRubroActionPerformed(evt);
            }
        });

        itemNuevoRegistro.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        itemNuevoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/nuevo_registro.png"))); // NOI18N
        itemNuevoRegistro.setText("Nuevo Rubro");
        itemNuevoRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNuevoRegistroActionPerformed(evt);
            }
        });
        menuRubro.add(itemNuevoRegistro);

        itemGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        itemGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/guardar.png"))); // NOI18N
        itemGuardar.setText("Guardar Rubro");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuRubro.add(itemGuardar);

        itemLista.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        itemLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/lista.png"))); // NOI18N
        itemLista.setText("Lista Rubros");
        itemLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemListaActionPerformed(evt);
            }
        });
        menuRubro.add(itemLista);

        itemCerrar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        itemCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/Cerrar.png"))); // NOI18N
        itemCerrar.setText("Cerrar");
        itemCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCerrarActionPerformed(evt);
            }
        });
        menuRubro.add(itemCerrar);

        barraMenu.add(menuRubro);

        jMenu1.setText("Editar");

        itemEliminar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        itemEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/presupuesto/img/eliminar.png"))); // NOI18N
        itemEliminar.setText("Eliminar Rubro");
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
            .addComponent(barraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelCodigo)
                    .addComponent(labelSubcuenta)
                    .addComponent(labelCuenta)
                    .addComponent(labelValor)
                    .addComponent(labelNombre)
                    .addComponent(labelListaTipoRubro))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(frListaCuentas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(frListaSubcuentas, 0, 300, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(frListaTipoRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(47, 47, 47)))
                    .addComponent(frValor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frCodigoPresupuestal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frNombreRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frCodigoPresupuestal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCodigo))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frNombreRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNombre))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frListaTipoRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelListaTipoRubro))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCuenta)
                    .addComponent(frListaCuentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSubcuenta)
                    .addComponent(frListaSubcuentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelValor)
                    .addComponent(frValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(frMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 123, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void frListaTipoRubroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frListaTipoRubroActionPerformed
        String tipo = frListaTipoRubro.getSelectedItem().toString();

        switch (tipo) {
            case "Cuenta":
                frListaCuentas.removeAllItems();
                frListaCuentas.setEnabled(false);
                frListaSubcuentas.removeAllItems();
                frListaSubcuentas.setEnabled(false);
                frValor.setText("$0.0");
                break;
            case "Subcuenta":
                frListaCuentas.removeAllItems();
                frListaCuentas.setEnabled(true);
                consultarCuentasRegistradas();
                frListaSubcuentas.removeAllItems();
                frListaSubcuentas.setEnabled(false);
                frValor.setText("$0.0");
                break;
            case "Auxiliar":
                frListaCuentas.removeAllItems();
                frListaCuentas.setEnabled(true);
                consultarCuentasRegistradas();
                frListaSubcuentas.removeAllItems();
                frListaSubcuentas.setEnabled(false);
                frValor.setText("$0.0");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_frListaTipoRubroActionPerformed

    private void nuevoRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoRegistroMousePressed
        iniciarFormulario();
    }//GEN-LAST:event_nuevoRegistroMousePressed

    private void guardarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarRegistroMousePressed
        Rubro rubro = new Rubro();
        rubro = registrarRubro();
    }//GEN-LAST:event_guardarRegistroMousePressed

    private void frListaCuentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frListaCuentasActionPerformed
        accesoDatos = new AccesoDatos();

        // Si el tipo de rubro a registrar es un auxiliar, se deben cargar las subcuentas de la cuenta seleccionada. 
        if (frListaTipoRubro.getSelectedItem().toString().equals("Auxiliar")) {
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
        }
    }//GEN-LAST:event_frListaCuentasActionPerformed

    private void frCodigoPresupuestalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_frCodigoPresupuestalKeyTyped
        char tecla = evt.getKeyChar();

        if (tecla < '0' || tecla > '9') {
            evt.consume();
        }
    }//GEN-LAST:event_frCodigoPresupuestalKeyTyped

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

    private void frCodigoPresupuestalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frCodigoPresupuestalFocusLost
        if (!frCodigoPresupuestal.getText().trim().equals("")) {
            // Se borra mensaje de formulario
            frMensaje.setText("");
            accesoDatos = new AccesoDatos();
            Rubro rubro = new Rubro();
            rubro.setCodigo(frCodigoPresupuestal.getText().trim());
            rubro.setVigencia(vigencia);

            List<Rubro> listaRubro = new ArrayList<Rubro>();

            listaRubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia);

            if (!listaRubro.isEmpty()) {
                rubro = listaRubro.get(0);

                if (rubro != null && rubro.getIdRubro() != null) {
                    if (rubro.getNombre() != null && !rubro.getNombre().trim().equals("")) {
                        frNombreRubro.setText(rubro.getNombre());
                    }
                    if (rubro.getTipoRubro() != null && rubro.getTipoRubro().getIdTipoRubro() != null) {

                        frListaTipoRubro.setSelectedItem(rubro.getTipoRubro().getTipoRubro());

                        switch (rubro.getTipoRubro().getTipoRubro()) {
                            case "Cuenta":
                                frListaCuentas.removeAllItems();
                                frListaCuentas.setEnabled(false);
                                frListaSubcuentas.removeAllItems();
                                frListaSubcuentas.setEnabled(false);
                                frValor.setText("$" + formatoNumeroDecimales(rubro.getValor().toString()));
                                break;
                            case "Subcuenta":
                                frListaCuentas.removeAllItems();
                                consultarCuentasRegistradas();
                                frListaCuentas.setSelectedItem(rubro.getCuenta().getCodigo() + " - " + rubro.getCuenta().getNombre());
                                frListaCuentas.setEnabled(true);
                                frListaSubcuentas.removeAllItems();
                                frListaSubcuentas.setEnabled(false);
                                frValor.setText("$" + formatoNumeroDecimales(rubro.getValor().toString()));
                                break;
                            case "Auxiliar":
                                frListaCuentas.removeAllItems();
                                consultarCuentasRegistradas();
                                frListaCuentas.setSelectedItem(rubro.getCuenta().getCodigo() + " - " + rubro.getCuenta().getNombre());
                                frListaCuentas.setEnabled(true);
                                frListaSubcuentas.removeAllItems();

                                if (!rubro.getCuenta().getRubroList1().isEmpty()) {
                                    frListaSubcuentas.addItem("-- Seleccione --");
                                    for (Rubro subcuentaIterada : rubro.getCuenta().getRubroList1()) {
                                        if (subcuentaIterada.getTipoRubro().getTipoRubro().equals("Subcuenta")) {
                                            frListaSubcuentas.addItem(subcuentaIterada.getCodigo() + " - " + subcuentaIterada.getNombre());
                                        }
                                    }
                                }

                                frListaSubcuentas.setSelectedItem(rubro.getSubcuenta().getCodigo() + " - " + rubro.getSubcuenta().getNombre());
                                frListaSubcuentas.setEnabled(true);

                                frValor.setText("$" + formatoNumeroDecimales(rubro.getValor().toString()));
                                break;
                        }
                    }
                }
            } else {
                frNombreRubro.setText("");
                frListaTipoRubro.setSelectedIndex(0);
                frValor.setText("$0.0");
            }
        }
    }//GEN-LAST:event_frCodigoPresupuestalFocusLost

    private void itemNuevoRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevoRegistroActionPerformed
        iniciarFormulario();
    }//GEN-LAST:event_itemNuevoRegistroActionPerformed

    private void menuRubroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRubroActionPerformed
        if (validarFormulario()) {
            Rubro rubro = new Rubro();
            rubro.setCodigo(frCodigoPresupuestal.getText().trim());
            rubro.setVigencia(vigencia);

            List<Rubro> listaRubro = new ArrayList<Rubro>();
            listaRubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia);

            if (!listaRubro.isEmpty()) {
                rubro = listaRubro.get(0);
            }

            TipoRubro tipoRubro = new TipoRubro();
            String tipo = frListaTipoRubro.getSelectedItem().toString();

            switch (tipo) {
                case "Cuenta":
                    tipoRubro = consultarTipoRubro("Cuenta");
                    rubro.setTipoRubro(tipoRubro);
                    rubro.setVigencia(vigencia);
                    rubro.setCodigo(frCodigoPresupuestal.getText().trim());
                    rubro.setNombre(frNombreRubro.getText().trim());
                    rubro.setCuenta(null);
                    rubro.setSubcuenta(null);
                    rubro.setValor(obtenerValorRubro());
                    guardarRubro(rubro);
                    iniciarFormulario();
                    break;

                case "Subcuenta":
                    tipoRubro = consultarTipoRubro("Subcuenta");
                    rubro.setTipoRubro(tipoRubro);
                    rubro.setVigencia(vigencia);
                    rubro.setCodigo(frCodigoPresupuestal.getText().trim());
                    rubro.setNombre(frNombreRubro.getText().trim());
                    rubro.setCuenta(consultarCuentaSeleccionada());
                    rubro.setSubcuenta(null);
                    rubro.setValor(obtenerValorRubro());
                    guardarRubro(rubro);
                    iniciarFormulario();
                    break;
                case "Auxiliar":
                    tipoRubro = consultarTipoRubro("Auxiliar");
                    rubro.setTipoRubro(tipoRubro);
                    rubro.setVigencia(vigencia);
                    rubro.setCodigo(frCodigoPresupuestal.getText().trim());
                    rubro.setNombre(frNombreRubro.getText().trim());
                    rubro.setCuenta(consultarCuentaSeleccionada());
                    rubro.setSubcuenta(consultarSubcuentaSeleccionada());
                    rubro.setValor(obtenerValorRubro());
                    guardarRubro(rubro);
                    iniciarFormulario();
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Formulario incompleto", "Validacion Formulario", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_menuRubroActionPerformed

    private void eliminarRegistroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminarRegistroMousePressed
        // Eliminar Registro
        eliminarRubro();
    }//GEN-LAST:event_eliminarRegistroMousePressed

    private void listaRubrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaRubrosMousePressed
        // Abrir Lista
        Lista_Rubros listaRubros = new Lista_Rubros(this, true);
        listaRubros.setLocationRelativeTo(null);
        listaRubros.setVisible(true);
    }//GEN-LAST:event_listaRubrosMousePressed

    private void itemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEliminarActionPerformed
        // Eliminar Registro
        eliminarRubro();
    }//GEN-LAST:event_itemEliminarActionPerformed

    private void itemListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemListaActionPerformed
        // Abrir Lista
        Lista_Rubros listaRubros = new Lista_Rubros(this, true);
        listaRubros.setLocationRelativeTo(null);
        listaRubros.setVisible(true);
    }//GEN-LAST:event_itemListaActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        Rubro rubro = new Rubro();
        rubro = registrarRubro();
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void imprimirAdicionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirAdicionMousePressed
        Generar_Reportes reportes = new Generar_Reportes();
        reportes.runReporteRubro(vigencia);
    }//GEN-LAST:event_imprimirAdicionMousePressed

    private Rubro guardarRubro(Rubro rubro) {
        accesoDatos = new AccesoDatos();
        try {
            rubro = accesoDatos.persistirActualizar(rubro);
        } catch (Exception e) {
            System.out.println("Error al guardar o actualizar rubro: " + e);
        }

        return rubro;
    }

    private TipoRubro consultarTipoRubro(String tipo) {
        accesoDatos = new AccesoDatos();
        TipoRubro tipoRubro = new TipoRubro();
        tipoRubro.setTipoRubro(tipo);
        tipoRubro = accesoDatos.consultarTodos(tipoRubro, TipoRubro.class).get(0);

        return tipoRubro;
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

    private Rubro consultarSubcuentaSeleccionada() {
        accesoDatos = new AccesoDatos();
        Rubro rubro = new Rubro();
        String subCuentaSeleccionada = frListaSubcuentas.getSelectedItem().toString();
        String codigo = subCuentaSeleccionada.substring(0, subCuentaSeleccionada.indexOf("-") - 1).trim();
        rubro.setCodigo(codigo);
        rubro.setTipoRubro(new TipoRubro(new BigDecimal(2)));
        rubro.setVigencia(vigencia);
        rubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia).get(0);
        return rubro;
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

    private BigDecimal obtenerValorRubro() {
        String valor = frValor.getText().trim();
        valor = valor.replace(",", "");
        valor = valor.replace("$", "");
        BigDecimal valorNumerico = BigDecimal.valueOf(Double.parseDouble(valor));;
        return valorNumerico;
    }

    public void cargarRubroSeleccionado(String codigoRubro) {
        accesoDatos = new AccesoDatos();
        Rubro rubro = new Rubro();
        rubro.setCodigo(codigoRubro);
        rubro.setVigencia(vigencia);
        rubro = accesoDatos.consultarObjetoPorVigencia(Rubro.class, rubro, vigencia).get(0);

        if (rubro.getCodigo() != null && !rubro.getCodigo().trim().equals("")) {
            frCodigoPresupuestal.setText(rubro.getCodigo());
        }

        if (rubro.getNombre() != null && !rubro.getNombre().trim().equals("")) {
            frNombreRubro.setText(rubro.getNombre());
        }

        if (rubro.getTipoRubro() != null && rubro.getTipoRubro().getIdTipoRubro() != null) {

            frListaTipoRubro.setSelectedItem(rubro.getTipoRubro().getTipoRubro());

            switch (rubro.getTipoRubro().getTipoRubro()) {
                case "Cuenta":
                    frListaCuentas.removeAllItems();
                    frListaCuentas.setEnabled(false);
                    frListaSubcuentas.removeAllItems();
                    frListaSubcuentas.setEnabled(false);
                    frValor.setText("$" + formatoNumeroDecimales(rubro.getValor().toString()));
                    break;
                case "Subcuenta":
                    frListaCuentas.removeAllItems();
                    consultarCuentasRegistradas();
                    frListaCuentas.setSelectedItem(rubro.getCuenta().getCodigo() + " - " + rubro.getCuenta().getNombre());
                    frListaCuentas.setEnabled(true);
                    frListaSubcuentas.removeAllItems();
                    frListaSubcuentas.setEnabled(false);
                    frValor.setText("$" + formatoNumeroDecimales(rubro.getValor().toString()));
                    break;
                case "Auxiliar":
                    frListaCuentas.removeAllItems();
                    consultarCuentasRegistradas();
                    frListaCuentas.setSelectedItem(rubro.getCuenta().getCodigo() + " - " + rubro.getCuenta().getNombre());
                    frListaCuentas.setEnabled(true);
                    frListaSubcuentas.removeAllItems();

                    consultarSubcuentasRegistradas();

                    if (!rubro.getCuenta().getRubroList().isEmpty()) {
                        frListaSubcuentas.addItem("-- Seleccione --");
                        for (Rubro subcuentaIterada : rubro.getSubcuenta().getRubroList()) {
                            if (subcuentaIterada.getTipoRubro().getTipoRubro().equals("Subcuenta")) {
                                frListaSubcuentas.addItem(subcuentaIterada.getCodigo() + " - " + subcuentaIterada.getNombre());
                            }
                        }
                    }

                    frListaSubcuentas.setSelectedItem(rubro.getSubcuenta().getCodigo() + " - " + rubro.getSubcuenta().getNombre());
                    frListaSubcuentas.setEnabled(true);

                    frValor.setText("$" + formatoNumeroDecimales(rubro.getValor().toString()));
                    break;
            }
        }
    }

    public Vigencia getVigencia() {
        return vigencia;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JLabel eliminarRegistro;
    private javax.swing.JTextField frCodigoPresupuestal;
    private javax.swing.JComboBox<String> frListaCuentas;
    private javax.swing.JComboBox<String> frListaSubcuentas;
    private javax.swing.JComboBox<String> frListaTipoRubro;
    private javax.swing.JLabel frMensaje;
    private javax.swing.JTextField frNombreRubro;
    private javax.swing.JTextField frValor;
    private javax.swing.JLabel guardarRegistro;
    private javax.swing.JLabel imprimirAdicion;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JMenuItem itemEliminar;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemLista;
    private javax.swing.JMenuItem itemNuevoRegistro;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel labelCodigo;
    private javax.swing.JLabel labelCuenta;
    private javax.swing.JLabel labelListaTipoRubro;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel labelSubcuenta;
    private javax.swing.JLabel labelValor;
    private javax.swing.JLabel listaRubros;
    private javax.swing.JMenu menuRubro;
    private javax.swing.JLabel nuevoRegistro;
    // End of variables declaration//GEN-END:variables
}
