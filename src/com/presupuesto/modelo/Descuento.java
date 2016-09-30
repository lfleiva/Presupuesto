/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "descuento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Descuento.findAll", query = "SELECT d FROM Descuento d"),
    @NamedQuery(name = "Descuento.findByIdDescuento", query = "SELECT d FROM Descuento d WHERE d.idDescuento = :idDescuento"),
    @NamedQuery(name = "Descuento.findByTipoDescuento", query = "SELECT d FROM Descuento d WHERE d.tipoDescuento = :tipoDescuento"),
    @NamedQuery(name = "Descuento.findByNombre", query = "SELECT d FROM Descuento d WHERE d.nombre = :nombre")})
public class Descuento implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_descuento")
    private BigDecimal idDescuento;
    @Column(name = "tipo_descuento")
    private BigDecimal tipoDescuento;
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;
    @OneToMany(mappedBy = "descuento")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<EgresoDescuento> egresoDescuentoList;

    public Descuento() {
    }

    public Descuento(BigDecimal idDescuento) {
        this.idDescuento = idDescuento;
    }

    public BigDecimal getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(BigDecimal idDescuento) {
        this.idDescuento = idDescuento;
    }

    public BigDecimal getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(BigDecimal tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Vigencia getVigencia() {
        return vigencia;
    }

    public void setVigencia(Vigencia vigencia) {
        this.vigencia = vigencia;
    }

    @XmlTransient
    public List<EgresoDescuento> getEgresoDescuentoList() {
        return egresoDescuentoList;
    }

    public void setEgresoDescuentoList(List<EgresoDescuento> egresoDescuentoList) {
        this.egresoDescuentoList = egresoDescuentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDescuento != null ? idDescuento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Descuento)) {
            return false;
        }
        Descuento other = (Descuento) object;
        if ((this.idDescuento == null && other.idDescuento != null) || (this.idDescuento != null && !this.idDescuento.equals(other.idDescuento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Descuento[ idDescuento=" + idDescuento + " ]";
    }
    
}
