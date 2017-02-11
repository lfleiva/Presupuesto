/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.vista;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.modelo.AdicionRubro;
import com.presupuesto.modelo.DisponibilidadRubro;
import com.presupuesto.modelo.Ejecucion;
import com.presupuesto.modelo.Presupuesto;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.utilidades.Generar_Reportes;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Ejecucion_Presupuestal extends javax.swing.JInternalFrame implements Runnable{

    //***** Atributos de la clase *****//
    Home home;
    Vigencia vigencia;
    AccesoDatos accesoDatos;

    /**
     * Creates new form Ejecucion_Presupuestal
     */
    public Ejecucion_Presupuestal(Home parent) {
        super();
        this.home = parent;

        // Elimina el la decoracion en la ventana interna
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);

        initComponents();
        consultarVigencia();
        
        Date fecha = new Date();
        
        frMesInicial.setMonth(0);
        frMesFinal.setMonth(fecha.getMonth());
        
        frMesInicial.setEnabled(false);
        //frMesFinal.setEnabled(false);
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

  

    private void inicializarVentanaProcesos() {                
//        Loading_Proceso generarEjecucion = new Loading_Proceso(this, false); // Modal debe ser false para que se ejecute el hilo        
//        generarEjecucion.setLocationRelativeTo(null);
//        generarEjecucion.setVisible(true);
        // Deshabilito boton generar
        botonGenerar.setEnabled(false);

        // Aqui se desarrolla todo el proceso de actualizar la ejecucion //        
        eliminarEjecucionActual();
        accesoDatos = new AccesoDatos();
        List<Presupuesto> listaPresupuesto = new ArrayList<Presupuesto>();

        listaPresupuesto = accesoDatos.consultarTodosPorVigencia(Presupuesto.class, vigencia);

        if (!listaPresupuesto.isEmpty()) {
            Ejecucion ejecucionCuenta = new Ejecucion();
            Ejecucion ejecucionSubcuenta = new Ejecucion();
           
            for (Presupuesto presupuestoIterado : listaPresupuesto) {

                switch (presupuestoIterado.getRubro().getTipoRubro().getTipoRubro()) {
                    case "Cuenta":
                        if (ejecucionCuenta.getIdEjecucion() != null) {
                            ejecucionCuenta = new Ejecucion();
                        }

                        // Rubro
                        ejecucionCuenta.setRubro(presupuestoIterado.getRubro());
                        // Presupuesto inicial
                        ejecucionCuenta.setPresupuestoInicial(presupuestoIterado.getRubro().getValor());
                        // Adiciones
                        ejecucionCuenta.setAdiciones(calcularAdiciones(presupuestoIterado.getRubro()));
                        // Creditos
                        ejecucionCuenta.setCreditos(calcularCreditos(presupuestoIterado.getRubro()));
                        // Contracreditos
                        ejecucionCuenta.setContracreditos(calcularContracreditos(presupuestoIterado.getRubro()));
                        // Presupuesto Final
                        ejecucionCuenta.setPresupuestoFinal(calcularPresupuestoFinal(ejecucionCuenta));
                        // Gastos Totales
                        ejecucionCuenta.setGastoTotal(calcularGastos(presupuestoIterado.getRubro()));
                        // Saldo
                        ejecucionCuenta.setSaldo(calcularSaldo(ejecucionCuenta));
                        // Vigencia
                        ejecucionCuenta.setVigencia(vigencia);

                        break;
                    case "Subcuenta":
                        if (ejecucionSubcuenta.getIdEjecucion() != null) {
                            ejecucionSubcuenta = new Ejecucion();
                        }
                        // Rubro
                        ejecucionSubcuenta.setRubro(presupuestoIterado.getRubro());
                        // Presupuesto inicial
                        ejecucionSubcuenta.setPresupuestoInicial(presupuestoIterado.getRubro().getValor());
                        // Adiciones
                        ejecucionSubcuenta.setAdiciones(calcularAdiciones(presupuestoIterado.getRubro()));
                        // Creditos
                        ejecucionSubcuenta.setCreditos(calcularCreditos(presupuestoIterado.getRubro()));
                        // Contracreditos
                        ejecucionSubcuenta.setContracreditos(calcularContracreditos(presupuestoIterado.getRubro()));
                        // Presupuesto Final
                        ejecucionSubcuenta.setPresupuestoFinal(calcularPresupuestoFinal(ejecucionSubcuenta));
                        // Gastos Totales
                        ejecucionSubcuenta.setGastoTotal(calcularGastos(presupuestoIterado.getRubro()));
                        // Saldo
                        ejecucionSubcuenta.setSaldo(calcularSaldo(ejecucionSubcuenta));
                        // Vigencia
                        ejecucionSubcuenta.setVigencia(vigencia);
                        break;
                    case "Auxiliar":
                        Ejecucion ejecucion = new Ejecucion();
                        // Rubro
                        ejecucion.setRubro(presupuestoIterado.getRubro());
                        // Presupuesto inicial
                        ejecucion.setPresupuestoInicial(presupuestoIterado.getRubro().getValor());
                        // Adiciones
                        ejecucion.setAdiciones(calcularAdiciones(presupuestoIterado.getRubro()));
                        // Creditos
                        ejecucion.setCreditos(calcularCreditos(presupuestoIterado.getRubro()));
                        // Contracreditos
                        ejecucion.setContracreditos(calcularContracreditos(presupuestoIterado.getRubro()));
                        // Presupuesto Final
                        ejecucion.setPresupuestoFinal(calcularPresupuestoFinal(ejecucion));
                        // Gastos Totales
                        ejecucion.setGastoTotal(calcularGastos(presupuestoIterado.getRubro()));
                        // Saldo
                        ejecucion.setSaldo(calcularSaldo(ejecucion));
                        // Vigencia
                        ejecucion.setVigencia(vigencia);

                        ejecucionCuenta = actualizarEjecucion(ejecucion, ejecucionCuenta);
                        ejecucionSubcuenta = actualizarEjecucion(ejecucion, ejecucionSubcuenta);

                        ejecucionCuenta = accesoDatos.persistirActualizar(ejecucionCuenta);
                        ejecucionSubcuenta = accesoDatos.persistirActualizar(ejecucionSubcuenta);
                        ejecucion = accesoDatos.persistirActualizar(ejecucion);

                        break;
                    default:
                        break;
                }
            }
        }

        // Se consulta la ejecucion actualizada
        List<Ejecucion> listaEjecucion = new ArrayList<Ejecucion>();

        listaEjecucion = accesoDatos.consultarTodosPorVigencia(Ejecucion.class, vigencia);

        eliminarElementosTabla();
        
        if (!listaEjecucion.isEmpty()) {            
            
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) tablaEjecucion.getModel();

            for (Ejecucion ejecucionIterada : listaEjecucion) {
                model.addRow(new Object[]{ejecucionIterada.getRubro().getCodigo() + " - " + ejecucionIterada.getRubro().getNombre(), "$" + formatoNumeroDecimales(ejecucionIterada.getPresupuestoInicial().toString()), "$" + formatoNumeroDecimales(ejecucionIterada.getAdiciones().toString()), "$" + formatoNumeroDecimales(ejecucionIterada.getCreditos().toString()), "$" + formatoNumeroDecimales(ejecucionIterada.getContracreditos().toString()), "$" + formatoNumeroDecimales(ejecucionIterada.getPresupuestoFinal().toString()), "$" + formatoNumeroDecimales(ejecucionIterada.getGastoTotal().toString()), "$" + formatoNumeroDecimales(ejecucionIterada.getSaldo().toString())});
            }
        }

        // Ocullto loading y habilito el boton generar         
        //generarEjecucion.setVisible(false);
        botonGenerar.setEnabled(true);
    }

    private void eliminarEjecucionActual() {
        accesoDatos = new AccesoDatos();
        accesoDatos.limpiarTablaVigencia("ejecucion", vigencia);
    }

    private BigDecimal calcularAdiciones(Rubro rubro) {
        BigDecimal adiciones = new BigDecimal(0);

        if (!rubro.getAdicionRubroList().isEmpty()) {
            for (AdicionRubro adicionRubro : rubro.getAdicionRubroList()) {
                adiciones = adiciones.add(adicionRubro.getValor());
            }
        }

        return adiciones;
    }

    private BigDecimal calcularCreditos(Rubro rubro) {
        BigDecimal creditos = new BigDecimal(0);

        if (!rubro.getTrasladoRubroList().isEmpty()) {

        }

        return creditos;
    }

    private BigDecimal calcularContracreditos(Rubro rubro) {
        BigDecimal contraCreditos = new BigDecimal(0);

        if (!rubro.getTrasladoRubroList().isEmpty()) {

        }

        return contraCreditos;
    }

    private BigDecimal calcularPresupuestoFinal(Ejecucion ejecucion) {
        BigDecimal presupuestoFinal = new BigDecimal(0);

        presupuestoFinal = presupuestoFinal.add(ejecucion.getPresupuestoInicial());
        presupuestoFinal = presupuestoFinal.add(ejecucion.getAdiciones());
        presupuestoFinal = presupuestoFinal.add(ejecucion.getCreditos());
        presupuestoFinal = presupuestoFinal.subtract(ejecucion.getContracreditos());

        return presupuestoFinal;
    }

    private BigDecimal calcularGastos(Rubro rubro) {
        BigDecimal gastos = new BigDecimal(0);

        if (!rubro.getDisponibilidadRubroList().isEmpty()) {
            for (DisponibilidadRubro disponibilidadRubro : rubro.getDisponibilidadRubroList()) {
                int mesDisponibilidad = disponibilidadRubro.getDisponibilidad().getFecha().getMonth();
                if(frMesInicial.getMonth() <= disponibilidadRubro.getDisponibilidad().getFecha().getMonth() && frMesFinal.getMonth() >= disponibilidadRubro.getDisponibilidad().getFecha().getMonth()) {
                    gastos = gastos.add(disponibilidadRubro.getValor());
                }                
            }
        }

        return gastos;
    }

    private BigDecimal calcularSaldo(Ejecucion ejecucion) {
        BigDecimal saldo = new BigDecimal(0);

        saldo = saldo.add(ejecucion.getPresupuestoFinal());
        saldo = saldo.subtract(ejecucion.getGastoTotal());

        return saldo;
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

    private Ejecucion actualizarEjecucion(Ejecucion ejecucion, Ejecucion ejecucionActualizar) {
        // Presupuesto inicial
        BigDecimal presupuestoInicial = new BigDecimal(ejecucionActualizar.getPresupuestoInicial().toString());
        presupuestoInicial = presupuestoInicial.add(ejecucion.getPresupuestoInicial());
        // Adiciones
        BigDecimal adiciones = new BigDecimal(ejecucionActualizar.getAdiciones().toString());
        adiciones = adiciones.add(ejecucion.getAdiciones());
        // Creditos
        BigDecimal creditos = new BigDecimal(ejecucionActualizar.getCreditos().toString());
        creditos = creditos.add(ejecucion.getCreditos());
        // Contracreditos
        BigDecimal contraCreditos = new BigDecimal(ejecucionActualizar.getContracreditos().toString());
        contraCreditos = contraCreditos.add(ejecucion.getContracreditos());
        // Presupuesto final
        BigDecimal presupuestoFinal = new BigDecimal(ejecucionActualizar.getPresupuestoFinal().toString());
        presupuestoFinal = presupuestoFinal.add(ejecucion.getPresupuestoFinal());
        // gastos
        BigDecimal gastos = new BigDecimal(ejecucionActualizar.getGastoTotal().toString());
        gastos = gastos.add(ejecucion.getGastoTotal());
        // saldo
        BigDecimal saldo = new BigDecimal(ejecucionActualizar.getSaldo().toString());
        saldo = saldo.add(ejecucion.getSaldo());
        
        ejecucionActualizar.setPresupuestoInicial(presupuestoInicial);
        ejecucionActualizar.setAdiciones(adiciones);
        ejecucionActualizar.setCreditos(creditos);
        ejecucionActualizar.setContracreditos(contraCreditos);
        ejecucionActualizar.setPresupuestoFinal(presupuestoFinal);
        ejecucionActualizar.setGastoTotal(gastos);
        ejecucionActualizar.setSaldo(saldo);

        return ejecucionActualizar;
    }
    
    private void eliminarElementosTabla() {
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tablaEjecucion.getModel();

        int numeroFilas = model.getRowCount();

        while (numeroFilas > 0) {
            model.removeRow(0);
            model = (DefaultTableModel) tablaEjecucion.getModel();
            numeroFilas = model.getRowCount();
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

        frMesInicial = new com.toedter.calendar.JMonthChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        frMesFinal = new com.toedter.calendar.JMonthChooser();
        botonGenerar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEjecucion = new javax.swing.JTable();
        barraHerramientas = new javax.swing.JToolBar();
        imprimirAdicion = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        menuAdicion = new javax.swing.JMenu();
        itemCerrar = new javax.swing.JMenuItem();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setMaximumSize(new java.awt.Dimension(895, 474));
        setMinimumSize(new java.awt.Dimension(895, 474));
        setPreferredSize(new java.awt.Dimension(895, 474));

        jLabel1.setText("Mes Inicial");

        jLabel2.setText("Mes Final");

        botonGenerar.setText("Generar");
        botonGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGenerarActionPerformed(evt);
            }
        });

        tablaEjecucion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rubro", "Presupuesto Inicial", "Adicion", "Credito", "Contracredito", "Presupuesto Final", "Gastos", "Saldo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaEjecucion);

        barraHerramientas.setBackground(new java.awt.Color(255, 255, 255));
        barraHerramientas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        barraHerramientas.setFloatable(false);
        barraHerramientas.setRollover(true);

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

        barraMenu.setBackground(new java.awt.Color(255, 255, 255));

        menuAdicion.setText("Ejecucion");

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 835, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(botonGenerar)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(frMesInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(frMesFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 628, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
            .addComponent(barraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(frMesInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(frMesFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonGenerar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGenerarActionPerformed
        // Creo el hilo
        Loading_Proceso loadig = new Loading_Proceso(this, false);     
        
        Ejecucion_Presupuestal ejecucion = new Ejecucion_Presupuestal(home);
        Thread thread1 = new Thread(loadig);
        Thread thread2 = new Thread(ejecucion);
        loadig.setHilo(thread2);
        thread1.start();
        thread2.start();        
    }//GEN-LAST:event_botonGenerarActionPerformed

    private void itemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCerrarActionPerformed
        home.getVentanaPrincipal().remove(this);
    }//GEN-LAST:event_itemCerrarActionPerformed

    private void imprimirAdicionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirAdicionMousePressed
        Generar_Reportes reportes = new Generar_Reportes();
        reportes.runReporteEjecucion(vigencia);
    }//GEN-LAST:event_imprimirAdicionMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JButton botonGenerar;
    private com.toedter.calendar.JMonthChooser frMesFinal;
    private com.toedter.calendar.JMonthChooser frMesInicial;
    private javax.swing.JLabel imprimirAdicion;
    private javax.swing.JMenuItem itemCerrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuAdicion;
    private javax.swing.JTable tablaEjecucion;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        inicializarVentanaProcesos();
    }
}
