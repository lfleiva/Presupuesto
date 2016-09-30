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
@Table(name = "adicion_rubro", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdicionRubro.findAll", query = "SELECT a FROM AdicionRubro a"),
    @NamedQuery(name = "AdicionRubro.findByIdAdicionRubro", query = "SELECT a FROM AdicionRubro a WHERE a.idAdicionRubro = :idAdicionRubro"),
    @NamedQuery(name = "AdicionRubro.findByValor", query = "SELECT a FROM AdicionRubro a WHERE a.valor = :valor")})
public class AdicionRubro implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_adicion_rubro", nullable = false, precision = 22)
    private BigDecimal idAdicionRubro;
    @JoinColumn(name = "rubro", referencedColumnName = "id_rubro")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Rubro rubro;    
    @JoinColumn(name = "adicion", referencedColumnName = "id_adicion")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Adicion adicion;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Vigencia vigencia;

    public AdicionRubro() {
    }

    public AdicionRubro(BigDecimal idAdicionRubro) {
        this.idAdicionRubro = idAdicionRubro;
    }

    public BigDecimal getIdAdicionRubro() {
        return idAdicionRubro;
    }

    public void setIdAdicionRubro(BigDecimal idAdicionRubro) {
        this.idAdicionRubro = idAdicionRubro;
    }


    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public Adicion getAdicion() {
        return adicion;
    }

    public void setAdicion(Adicion adicion) {
        this.adicion = adicion;
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
        hash += (idAdicionRubro != null ? idAdicionRubro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdicionRubro)) {
            return false;
        }
        AdicionRubro other = (AdicionRubro) object;
        if ((this.idAdicionRubro == null && other.idAdicionRubro != null) || (this.idAdicionRubro != null && !this.idAdicionRubro.equals(other.idAdicionRubro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.AdicionRubro[ idAdicionRubro=" + idAdicionRubro + " ]";
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
}
