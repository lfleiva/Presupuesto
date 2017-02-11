/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "ops")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ops.findAll", query = "SELECT o FROM Ops o"),
    @NamedQuery(name = "Ops.findByIdOps", query = "SELECT o FROM Ops o WHERE o.idOps = :idOps"),
    @NamedQuery(name = "Ops.findByConsecutivo", query = "SELECT o FROM Ops o WHERE o.consecutivo = :consecutivo"),
    @NamedQuery(name = "Ops.findByFecha", query = "SELECT o FROM Ops o WHERE o.fecha = :fecha"),
    @NamedQuery(name = "Ops.findByObjeto", query = "SELECT o FROM Ops o WHERE o.objeto = :objeto"),
    @NamedQuery(name = "Ops.findByPlazo", query = "SELECT o FROM Ops o WHERE o.plazo = :plazo"),
    @NamedQuery(name = "Ops.findByFechaFirma", query = "SELECT o FROM Ops o WHERE o.fechaFirma = :fechaFirma"),
    @NamedQuery(name = "Ops.findByFechaInicio", query = "SELECT o FROM Ops o WHERE o.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Ops.findByFechaFinal", query = "SELECT o FROM Ops o WHERE o.fechaFinal = :fechaFinal")})
public class Ops implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "consecutivo")
    private BigDecimal consecutivo;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ops")
    private BigDecimal idOps;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "objeto")
    private String objeto;
    @Column(name = "plazo")
    private String plazo;
    @Column(name = "fecha_firma")
    @Temporal(TemporalType.DATE)
    private Date fechaFirma;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @JoinColumn(name = "disponibilidad", referencedColumnName = "id_disponibilidad")
    @ManyToOne
    private Disponibilidad disponibilidad;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;

    public Ops() {
    }

    public Ops(BigDecimal idOps) {
        this.idOps = idOps;
    }

    public BigDecimal getIdOps() {
        return idOps;
    }

    public void setIdOps(BigDecimal idOps) {
        this.idOps = idOps;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Vigencia getVigencia() {
        return vigencia;
    }

    public void setVigencia(Vigencia vigencia) {
        this.vigencia = vigencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOps != null ? idOps.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ops)) {
            return false;
        }
        Ops other = (Ops) object;
        if ((this.idOps == null && other.idOps != null) || (this.idOps != null && !this.idOps.equals(other.idOps))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Ops[ idOps=" + idOps + " ]";
    }

    public BigDecimal getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(BigDecimal consecutivo) {
        this.consecutivo = consecutivo;
    }
    
}
