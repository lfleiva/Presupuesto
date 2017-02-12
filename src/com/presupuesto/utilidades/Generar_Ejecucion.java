/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.utilidades;

import com.presupuesto.control.AccesoDatos;
import com.presupuesto.modelo.AdicionRubro;
import com.presupuesto.modelo.DisponibilidadRubro;
import com.presupuesto.modelo.Ejecucion;
import com.presupuesto.modelo.Presupuesto;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.Vigencia;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Generar_Ejecucion implements Runnable{

    AccesoDatos accesoDatos;
    Vigencia vigencia;
    int fechaInicio;
    int fechaFinal;
    
    public Generar_Ejecucion(int fechaInicio, int fechaFinal){
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }
    
    /**
     * Metodo para eliominar registro de ejecucion de la vigencia actual
     */
    private void eliminarRegistrosEjecucionVigencia(){
        accesoDatos = new AccesoDatos();
        accesoDatos.limpiarTablaVigencia("ejecucion", consultarVigencia());
    }
    
    /**
     * Metodo que consulta la vigencia activa
     */
    private Vigencia consultarVigencia() {
        accesoDatos = new AccesoDatos();
        vigencia = new Vigencia();
        vigencia.setActiva(true);
        vigencia = accesoDatos.consultarTodos(vigencia, Vigencia.class).get(0);
        return vigencia;
    }
    
    /**
     * Metodo que consulta tabla presupuesto y calcula la ejecucion
     */
    private void calcularEjecucion(int mesInicial, int mesFinal){
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
                        ejecucionCuenta.setGastoTotal(calcularGastos(presupuestoIterado.getRubro(), mesInicial, mesFinal));
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
                        ejecucionSubcuenta.setGastoTotal(calcularGastos(presupuestoIterado.getRubro(), mesInicial, mesFinal));
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
                        ejecucion.setGastoTotal(calcularGastos(presupuestoIterado.getRubro(), mesInicial, mesFinal));
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

    private BigDecimal calcularGastos(Rubro rubro, int mesInicial, int mesFinal) {
        BigDecimal gastos = new BigDecimal(0);

        if (!rubro.getDisponibilidadRubroList().isEmpty()) {
            for (DisponibilidadRubro disponibilidadRubro : rubro.getDisponibilidadRubroList()) {
                int mesDisponibilidad = disponibilidadRubro.getDisponibilidad().getFecha().getMonth();
                if(mesInicial <= disponibilidadRubro.getDisponibilidad().getFecha().getMonth() && mesFinal >= disponibilidadRubro.getDisponibilidad().getFecha().getMonth()) {
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
   
    @Override
    public void run() {
        
        // Elimino los registros de la base de datos
        eliminarRegistrosEjecucionVigencia();
        System.out.println("Elimino registros");
        
        // Se calcula ejecucion presupuestal 
        calcularEjecucion(fechaInicio, fechaFinal);
        System.out.println("Calculo Ejecucion");
    }    

    public int getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(int fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(int fechaFinal) {
        this.fechaFinal = fechaFinal;
    }            
}
