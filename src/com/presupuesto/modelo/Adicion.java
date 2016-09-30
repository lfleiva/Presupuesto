/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "adicion", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Adicion.findAll", query = "SELECT a FROM Adicion a"),
    @NamedQuery(name = "Adicion.findByIdAdicion", query = "SELECT a FROM Adicion a WHERE a.idAdicion = :idAdicion"),
    @NamedQuery(name = "Adicion.findByFecha", query = "SELECT a FROM Adicion a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "Adicion.findByDocumento", query = "SELECT a FROM Adicion a WHERE a.documento = :documento"),
    @NamedQuery(name = "Adicion.findByDescripcion", query = "SELECT a FROM Adicion a WHERE a.descripcion = :descripcion")})
public class Adicion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_adicion", nullable = false, precision = 22)
    private BigDecimal idAdicion;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "documento", length = 100)
    private String documento;
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    @OneToMany(mappedBy = "adicion")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<AdicionRubro> adicionRubroList;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;

    public Adicion() {
    }

    public Adicion(BigDecimal idAdicion) {
        this.idAdicion = idAdicion;
    }

    public BigDecimal getIdAdicion() {
        return idAdicion;
    }

    public void setIdAdicion(BigDecimal idAdicion) {
        this.idAdicion = idAdicion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<AdicionRubro> getAdicionRubroList() {
        return adicionRubroList;
    }

    public void setAdicionRubroList(List<AdicionRubro> adicionRubroList) {
        this.adicionRubroList = adicionRubroList;
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
        hash += (idAdicion != null ? idAdicion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adicion)) {
            return false;
        }
        Adicion other = (Adicion) object;
        if ((this.idAdicion == null && other.idAdicion != null) || (this.idAdicion != null && !this.idAdicion.equals(other.idAdicion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Adicion[ idAdicion=" + idAdicion + " ]";
    }
    
}
