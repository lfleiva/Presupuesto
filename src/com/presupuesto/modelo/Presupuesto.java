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

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "presupuesto", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Presupuesto.findAll", query = "SELECT p FROM Presupuesto p"),
    @NamedQuery(name = "Presupuesto.findByIdPresupuesto", query = "SELECT p FROM Presupuesto p WHERE p.idPresupuesto = :idPresupuesto"),
    @NamedQuery(name = "Presupuesto.findByValor", query = "SELECT p FROM Presupuesto p WHERE p.valor = :valor"),
    @NamedQuery(name = "Presupuesto.findByOrden", query = "SELECT p FROM Presupuesto p WHERE p.orden = :orden")})
public class Presupuesto implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "orden")
    private BigDecimal orden;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_presupuesto", nullable = false, precision = 22)
    private BigDecimal idPresupuesto;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;
    @JoinColumn(name = "rubro", referencedColumnName = "id_rubro")
    @ManyToOne
    private Rubro rubro;

    public Presupuesto() {
    }

    public Presupuesto(BigDecimal idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public BigDecimal getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(BigDecimal idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }


    public Vigencia getVigencia() {
        return vigencia;
    }

    public void setVigencia(Vigencia vigencia) {
        this.vigencia = vigencia;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPresupuesto != null ? idPresupuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Presupuesto)) {
            return false;
        }
        Presupuesto other = (Presupuesto) object;
        if ((this.idPresupuesto == null && other.idPresupuesto != null) || (this.idPresupuesto != null && !this.idPresupuesto.equals(other.idPresupuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Presupuesto[ idPresupuesto=" + idPresupuesto + " ]";
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getOrden() {
        return orden;
    }

    public void setOrden(BigDecimal orden) {
        this.orden = orden;
    }
    
}
