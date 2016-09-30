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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "tipo_rubro", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoRubro.findAll", query = "SELECT t FROM TipoRubro t"),
    @NamedQuery(name = "TipoRubro.findByIdTipoRubro", query = "SELECT t FROM TipoRubro t WHERE t.idTipoRubro = :idTipoRubro"),
    @NamedQuery(name = "TipoRubro.findByTipoRubro", query = "SELECT t FROM TipoRubro t WHERE t.tipoRubro = :tipoRubro")})
public class TipoRubro implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_rubro", nullable = false, precision = 22)
    private BigDecimal idTipoRubro;
    @Basic(optional = false)
    @Column(name = "tipo_rubro", nullable = false, length = 100)
    private String tipoRubro;
    @OneToMany(mappedBy = "tipoRubro")
    private List<Rubro> rubroList;

    public TipoRubro() {
    }

    public TipoRubro(BigDecimal idTipoRubro) {
        this.idTipoRubro = idTipoRubro;
    }

    public TipoRubro(BigDecimal idTipoRubro, String tipoRubro) {
        this.idTipoRubro = idTipoRubro;
        this.tipoRubro = tipoRubro;
    }

    public BigDecimal getIdTipoRubro() {
        return idTipoRubro;
    }

    public void setIdTipoRubro(BigDecimal idTipoRubro) {
        this.idTipoRubro = idTipoRubro;
    }

    public String getTipoRubro() {
        return tipoRubro;
    }

    public void setTipoRubro(String tipoRubro) {
        this.tipoRubro = tipoRubro;
    }

    @XmlTransient
    public List<Rubro> getRubroList() {
        return rubroList;
    }

    public void setRubroList(List<Rubro> rubroList) {
        this.rubroList = rubroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoRubro != null ? idTipoRubro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoRubro)) {
            return false;
        }
        TipoRubro other = (TipoRubro) object;
        if ((this.idTipoRubro == null && other.idTipoRubro != null) || (this.idTipoRubro != null && !this.idTipoRubro.equals(other.idTipoRubro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.TipoRubro[ idTipoRubro=" + idTipoRubro + " ]";
    }
    
}
