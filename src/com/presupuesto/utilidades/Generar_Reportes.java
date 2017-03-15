/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.utilidades;

import com.presupuesto.modelo.Adicion;
import com.presupuesto.modelo.ComprobanteEgreso;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.Ops;
import com.presupuesto.modelo.OrdenSuministro;
import com.presupuesto.modelo.Registro;
import com.presupuesto.modelo.Traslado;
import com.presupuesto.modelo.Vigencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.*;

/**
 *
 * @author Luis Fernando Leiva
 */
public class Generar_Reportes {

    private Connection conn;
    private final String login = "root"; //usuario de acceso a MySQL
    private final String password = "leiva"; //contraseña de usuario
    private String url = "jdbc:mysql://localhost/presupuesto";
    private String id_contact;

    public Generar_Reportes() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //se carga el driver
            conn = DriverManager.getConnection(url, login, password);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que despliega el reporte de presupuesto
     *
     * @param vigencia
     */
    public void runReportePresupuestoInicial(Vigencia vigencia) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Presupuesto_Inicial.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Presupuesto Inicial.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Presupuesto Inicial");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }

    public void runReporteEjecucion(Vigencia vigencia) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Ejecucion_Presupuestal.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Ejecucion Presupuestal.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("Vigencia", vigencia.getIdVigencia());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Ejecucion Presupuestal");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }

    public void runReporteDisponibilidadPresupuestal(Vigencia vigencia, Disponibilidad disponibilidad) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Disponibilidad_Presupuestal.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Disponibilidad Presupuestal.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());
            parametro.put("disponibilidad", disponibilidad.getIdDisponibilidad());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Disponibilidad Presupuestal");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }

    public void runReporteRegistroPresupuestal(Vigencia vigencia, Registro registro) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Registro_Presupuestal.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Registro Presupuestal.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());
            parametro.put("registro", registro.getConsecutivo());
            parametro.put("disponibilidad", registro.getDisponibilidad().getConsecutivo());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Registro Presupuestal");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }

    public void runReporteOpsPresupuestal(Vigencia vigencia, Ops ops) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Orden_Prestacion_Servicio.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Registro Presupuestal.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());
            parametro.put("ops", ops.getIdOps());
            parametro.put("disponibilidad", ops.getDisponibilidad().getIdDisponibilidad());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Orden de Prestacion de Servicios");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }

    public void runReporteComprobante(Vigencia vigencia, ComprobanteEgreso comprobante) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Comprobante_Egreso.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Registro Presupuestal.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());
            parametro.put("comprobante", comprobante.getIdComprobanteEgreso());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Comprobante Egreso");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }

    public void runReporteOrdenSuministro(Vigencia vigencia, OrdenSuministro orden) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Orden_Suministro.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Orden de Suministro.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());
            parametro.put("orden_suministro", orden.getIdOrdenSuministro());
            
            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Orden de Suministro");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }
    
    public void runReporteRubro(Vigencia vigencia) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Rubro_Presupuesto.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Rubro Presupuestal.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Rubros Presupuestales");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }
    
    public void runReporteAdicion(Vigencia vigencia, Adicion adicion) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Adicion_Presupuestal.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Adicion Presupuestal.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());
            parametro.put("adicion", adicion.getIdAdicion());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Adición Presupuestal");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }
    
    public void runReporteTraslado(Vigencia vigencia, Traslado traslado) {
        try {
//            String master = System.getProperty("user.dir")
//                    + "/informes/Presupuesto_Inicial.jasper";    

            String master = "C:\\Program Files\\Presupuesto\\Reportes/Traslado_Presupuestal.jasper";

            System.out.println("master" + master);
            if (master == null) {
                System.out.println("No encuentro el archivo de Adicion Presupuestal.");
            }

            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(master);
            } catch (JRException e) {
                System.out.println("Error cargando el reporte maestro: " + e.getMessage());
            }

            //este es el parámetro, se pueden agregar más parámetros
            //basta con poner mas parametro.put
            Map parametro = new HashMap();
            parametro.put("vigencia", vigencia.getIdVigencia());
            parametro.put("traslado", traslado.getIdTraslado());

            //Reporte diseñado y compilado con iReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);

            //Se lanza el Viewer de Jasper, no termina aplicación al salir
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("Traslado Presupuestal");
            jviewer.setVisible(true);
        } catch (Exception j) {
            System.out.println("Mensaje de Error:" + j.getMessage());
        }
    }
}
