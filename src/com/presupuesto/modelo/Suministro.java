/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "suministro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Suministro.findAll", query = "SELECT s FROM Suministro s"),
    @NamedQuery(name = "Suministro.findByIdSuministro", query = "SELECT s FROM Suministro s WHERE s.idSuministro = :idSuministro"),
    @NamedQuery(name = "Suministro.findByDetalle", query = "SELECT s FROM Suministro s WHERE s.detalle = :detalle"),
    @NamedQuery(name = "Suministro.findByUnidad", query = "SELECT s FROM Suministro s WHERE s.unidad = :unidad"),
    @NamedQuery(name = "Suministro.findByCantidad", query = "SELECT s FROM Suministro s WHERE s.cantidad = :cantidad")})
public class Suministro implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private BigDecimal cantidad;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_suministro")
    private BigDecimal idSuministro;
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "unidad")
    private String unidad;
    @JoinColumn(name = "orden_suministro", referencedColumnName = "id_orden_suministro")
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private OrdenSuministro ordenSuministro;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Vigencia vigencia;

    public Suministro() {
    }

    public Suministro(BigDecimal idSuministro) {
        this.idSuministro = idSuministro;
    }

    public BigDecimal getIdSuministro() {
        return idSuministro;
    }

    public void setIdSuministro(BigDecimal idSuministro) {
        this.idSuministro = idSuministro;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }


    public OrdenSuministro getOrdenSuministro() {
        return ordenSuministro;
    }

    public void setOrdenSuministro(OrdenSuministro ordenSuministro) {
        this.ordenSuministro = ordenSuministro;
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
        hash += (idSuministro != null ? idSuministro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Suministro)) {
            return false;
        }
        Suministro other = (Suministro) object;
        if ((this.idSuministro == null && other.idSuministro != null) || (this.idSuministro != null && !this.idSuministro.equals(other.idSuministro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Suministro[ idSuministro=" + idSuministro + " ]";
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
    
}
