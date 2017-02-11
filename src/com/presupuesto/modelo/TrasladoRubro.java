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
@Table(name = "traslado_rubro", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TrasladoRubro.findAll", query = "SELECT t FROM TrasladoRubro t"),
    @NamedQuery(name = "TrasladoRubro.findByIdTrasladoRubro", query = "SELECT t FROM TrasladoRubro t WHERE t.idTrasladoRubro = :idTrasladoRubro"),
    @NamedQuery(name = "TrasladoRubro.findByTipo", query = "SELECT t FROM TrasladoRubro t WHERE t.tipo = :tipo"),
    @NamedQuery(name = "TrasladoRubro.findByValor", query = "SELECT t FROM TrasladoRubro t WHERE t.valor = :valor")})
public class TrasladoRubro implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_traslado_rubro", nullable = false, precision = 22)
    private BigDecimal idTrasladoRubro;
    @Column(name = "tipo", length = 500)
    private String tipo;
    @JoinColumn(name = "traslado", referencedColumnName = "id_traslado")
    @ManyToOne
    private Traslado traslado;
    @JoinColumn(name = "rubro", referencedColumnName = "id_rubro")
    @ManyToOne
    private Rubro rubro;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;

    public TrasladoRubro() {
    }

    public TrasladoRubro(BigDecimal idTrasladoRubro) {
        this.idTrasladoRubro = idTrasladoRubro;
    }

    public BigDecimal getIdTrasladoRubro() {
        return idTrasladoRubro;
    }

    public void setIdTrasladoRubro(BigDecimal idTrasladoRubro) {
        this.idTrasladoRubro = idTrasladoRubro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public Traslado getTraslado() {
        return traslado;
    }

    public void setTraslado(Traslado traslado) {
        this.traslado = traslado;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
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
        hash += (idTrasladoRubro != null ? idTrasladoRubro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TrasladoRubro)) {
            return false;
        }
        TrasladoRubro other = (TrasladoRubro) object;
        if ((this.idTrasladoRubro == null && other.idTrasladoRubro != null) || (this.idTrasladoRubro != null && !this.idTrasladoRubro.equals(other.idTrasladoRubro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.TrasladoRubro[ idTrasladoRubro=" + idTrasladoRubro + " ]";
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
}
