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
@Table(name = "traslado", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Traslado.findAll", query = "SELECT t FROM Traslado t"),
    @NamedQuery(name = "Traslado.findByIdTraslado", query = "SELECT t FROM Traslado t WHERE t.idTraslado = :idTraslado"),
    @NamedQuery(name = "Traslado.findByDocumento", query = "SELECT t FROM Traslado t WHERE t.documento = :documento"),
    @NamedQuery(name = "Traslado.findByFecha", query = "SELECT t FROM Traslado t WHERE t.fecha = :fecha"),
    @NamedQuery(name = "Traslado.findByDescripcion", query = "SELECT t FROM Traslado t WHERE t.descripcion = :descripcion")})
public class Traslado implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_traslado", nullable = false, precision = 22)
    private BigDecimal idTraslado;
    @Column(name = "documento", length = 500)
    private String documento;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "descripcion", length = 5000)
    private String descripcion;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;
    @OneToMany(mappedBy = "traslado")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<TrasladoRubro> trasladoRubroList;

    public Traslado() {
    }

    public Traslado(BigDecimal idTraslado) {
        this.idTraslado = idTraslado;
    }

    public BigDecimal getIdTraslado() {
        return idTraslado;
    }

    public void setIdTraslado(BigDecimal idTraslado) {
        this.idTraslado = idTraslado;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Vigencia getVigencia() {
        return vigencia;
    }

    public void setVigencia(Vigencia vigencia) {
        this.vigencia = vigencia;
    }

    @XmlTransient
    public List<TrasladoRubro> getTrasladoRubroList() {
        return trasladoRubroList;
    }

    public void setTrasladoRubroList(List<TrasladoRubro> trasladoRubroList) {
        this.trasladoRubroList = trasladoRubroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTraslado != null ? idTraslado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Traslado)) {
            return false;
        }
        Traslado other = (Traslado) object;
        if ((this.idTraslado == null && other.idTraslado != null) || (this.idTraslado != null && !this.idTraslado.equals(other.idTraslado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Traslado[ idTraslado=" + idTraslado + " ]";
    }
    
}
