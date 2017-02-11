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
@Table(name = "orden_suministro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenSuministro.findAll", query = "SELECT o FROM OrdenSuministro o"),
    @NamedQuery(name = "OrdenSuministro.findByIdOrdenSuministro", query = "SELECT o FROM OrdenSuministro o WHERE o.idOrdenSuministro = :idOrdenSuministro"),
    @NamedQuery(name = "OrdenSuministro.findByConsecutivo", query = "SELECT o FROM OrdenSuministro o WHERE o.consecutivo = :consecutivo"),
    @NamedQuery(name = "OrdenSuministro.findByFecha", query = "SELECT o FROM OrdenSuministro o WHERE o.fecha = :fecha"),
    @NamedQuery(name = "OrdenSuministro.findByObjeto", query = "SELECT o FROM OrdenSuministro o WHERE o.objeto = :objeto")})
public class OrdenSuministro implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "consecutivo")
    private BigDecimal consecutivo;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_orden_suministro")
    private BigDecimal idOrdenSuministro;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "objeto")
    private String objeto;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;
    @JoinColumn(name = "disponibilidad", referencedColumnName = "id_disponibilidad")
    @ManyToOne
    private Disponibilidad disponibilidad;
    @OneToMany(mappedBy = "ordenSuministro")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Suministro> suministroList;

    public OrdenSuministro() {
    }

    public OrdenSuministro(BigDecimal idOrdenSuministro) {
        this.idOrdenSuministro = idOrdenSuministro;
    }

    public BigDecimal getIdOrdenSuministro() {
        return idOrdenSuministro;
    }

    public void setIdOrdenSuministro(BigDecimal idOrdenSuministro) {
        this.idOrdenSuministro = idOrdenSuministro;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public Vigencia getVigencia() {
        return vigencia;
    }

    public void setVigencia(Vigencia vigencia) {
        this.vigencia = vigencia;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    @XmlTransient
    public List<Suministro> getSuministroList() {
        return suministroList;
    }

    public void setSuministroList(List<Suministro> suministroList) {
        this.suministroList = suministroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrdenSuministro != null ? idOrdenSuministro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenSuministro)) {
            return false;
        }
        OrdenSuministro other = (OrdenSuministro) object;
        if ((this.idOrdenSuministro == null && other.idOrdenSuministro != null) || (this.idOrdenSuministro != null && !this.idOrdenSuministro.equals(other.idOrdenSuministro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.OrdenSuministro[ idOrdenSuministro=" + idOrdenSuministro + " ]";
    }

    public BigDecimal getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(BigDecimal consecutivo) {
        this.consecutivo = consecutivo;
    }
    
}
