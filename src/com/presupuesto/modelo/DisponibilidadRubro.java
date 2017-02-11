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
import javax.persistence.FetchType;
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
@Table(name = "disponibilidad_rubro", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DisponibilidadRubro.findAll", query = "SELECT d FROM DisponibilidadRubro d"),
    @NamedQuery(name = "DisponibilidadRubro.findByIdDisponibilidadRubro", query = "SELECT d FROM DisponibilidadRubro d WHERE d.idDisponibilidadRubro = :idDisponibilidadRubro"),
    @NamedQuery(name = "DisponibilidadRubro.findByValor", query = "SELECT d FROM DisponibilidadRubro d WHERE d.valor = :valor")})
public class DisponibilidadRubro implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_disponibilidad_rubro", nullable = false, precision = 22)
    private BigDecimal idDisponibilidadRubro;
    @JoinColumn(name = "rubro", referencedColumnName = "id_rubro")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Rubro rubro;
    @JoinColumn(name = "disponibilidad", referencedColumnName = "id_disponibilidad")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Disponibilidad disponibilidad;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Vigencia vigencia;

    public DisponibilidadRubro() {
    }

    public DisponibilidadRubro(BigDecimal idDisponibilidadRubro) {
        this.idDisponibilidadRubro = idDisponibilidadRubro;
    }

    public BigDecimal getIdDisponibilidadRubro() {
        return idDisponibilidadRubro;
    }

    public void setIdDisponibilidadRubro(BigDecimal idDisponibilidadRubro) {
        this.idDisponibilidadRubro = idDisponibilidadRubro;
    }


    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
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
        hash += (idDisponibilidadRubro != null ? idDisponibilidadRubro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DisponibilidadRubro)) {
            return false;
        }
        DisponibilidadRubro other = (DisponibilidadRubro) object;
        if ((this.idDisponibilidadRubro == null && other.idDisponibilidadRubro != null) || (this.idDisponibilidadRubro != null && !this.idDisponibilidadRubro.equals(other.idDisponibilidadRubro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.DisponibilidadRubro[ idDisponibilidadRubro=" + idDisponibilidadRubro + " ]";
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
}
