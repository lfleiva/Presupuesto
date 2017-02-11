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
@Table(name = "egreso_descuento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EgresoDescuento.findAll", query = "SELECT e FROM EgresoDescuento e"),
    @NamedQuery(name = "EgresoDescuento.findByIdEgresoDescuento", query = "SELECT e FROM EgresoDescuento e WHERE e.idEgresoDescuento = :idEgresoDescuento"),
    @NamedQuery(name = "EgresoDescuento.findByPorcentaje", query = "SELECT e FROM EgresoDescuento e WHERE e.porcentaje = :porcentaje"),
    @NamedQuery(name = "EgresoDescuento.findByValor", query = "SELECT e FROM EgresoDescuento e WHERE e.valor = :valor")})
public class EgresoDescuento implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "valor")
    private BigDecimal valor;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_egreso_descuento")
    private BigDecimal idEgresoDescuento;
    @JoinColumn(name = "descuento", referencedColumnName = "id_descuento")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Descuento descuento;
    @JoinColumn(name = "comprobante_egreso", referencedColumnName = "id_comprobante_egreso")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private ComprobanteEgreso comprobanteEgreso;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Vigencia vigencia;

    public EgresoDescuento() {
    }

    public EgresoDescuento(BigDecimal idEgresoDescuento) {
        this.idEgresoDescuento = idEgresoDescuento;
    }

    public BigDecimal getIdEgresoDescuento() {
        return idEgresoDescuento;
    }

    public void setIdEgresoDescuento(BigDecimal idEgresoDescuento) {
        this.idEgresoDescuento = idEgresoDescuento;
    }


    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public ComprobanteEgreso getComprobanteEgreso() {
        return comprobanteEgreso;
    }

    public void setComprobanteEgreso(ComprobanteEgreso comprobanteEgreso) {
        this.comprobanteEgreso = comprobanteEgreso;
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
        hash += (idEgresoDescuento != null ? idEgresoDescuento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EgresoDescuento)) {
            return false;
        }
        EgresoDescuento other = (EgresoDescuento) object;
        if ((this.idEgresoDescuento == null && other.idEgresoDescuento != null) || (this.idEgresoDescuento != null && !this.idEgresoDescuento.equals(other.idEgresoDescuento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.EgresoDescuento[ idEgresoDescuento=" + idEgresoDescuento + " ]";
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
}
