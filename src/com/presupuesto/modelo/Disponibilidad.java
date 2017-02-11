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
@Table(name = "disponibilidad", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disponibilidad.findAll", query = "SELECT d FROM Disponibilidad d"),
    @NamedQuery(name = "Disponibilidad.findByIdDisponibilidad", query = "SELECT d FROM Disponibilidad d WHERE d.idDisponibilidad = :idDisponibilidad"),
    @NamedQuery(name = "Disponibilidad.findByConsecutivo", query = "SELECT d FROM Disponibilidad d WHERE d.consecutivo = :consecutivo"),
    @NamedQuery(name = "Disponibilidad.findByFecha", query = "SELECT d FROM Disponibilidad d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "Disponibilidad.findByObjeto", query = "SELECT d FROM Disponibilidad d WHERE d.objeto = :objeto")})
public class Disponibilidad implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "consecutivo")
    private BigDecimal consecutivo;
    @OneToMany(mappedBy = "disponibilidad")
    private List<OrdenSuministro> ordenSuministroList;
    @OneToMany(mappedBy = "disponibilidad")
    private List<ComprobanteEgreso> comprobanteEgresoList;
    @OneToMany(mappedBy = "disponibilidad")
    private List<Ops> opsList;
    @OneToMany(mappedBy = "disponibilidad")
    private List<Registro> registroList;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_disponibilidad", nullable = false, precision = 22)
    private BigDecimal idDisponibilidad;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "objeto", length = 5000)
    private String objeto;
    @OneToMany(mappedBy = "disponibilidad")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<DisponibilidadRubro> disponibilidadRubroList;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id_beneficiario")
    @ManyToOne
    private Beneficiario beneficiario;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;

    public Disponibilidad() {
    }

    public Disponibilidad(BigDecimal idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }

    public BigDecimal getIdDisponibilidad() {
        return idDisponibilidad;
    }

    public void setIdDisponibilidad(BigDecimal idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
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

    @XmlTransient
    public List<DisponibilidadRubro> getDisponibilidadRubroList() {
        return disponibilidadRubroList;
    }

    public void setDisponibilidadRubroList(List<DisponibilidadRubro> disponibilidadRubroList) {
        this.disponibilidadRubroList = disponibilidadRubroList;
    }

    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
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
        hash += (idDisponibilidad != null ? idDisponibilidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Disponibilidad)) {
            return false;
        }
        Disponibilidad other = (Disponibilidad) object;
        if ((this.idDisponibilidad == null && other.idDisponibilidad != null) || (this.idDisponibilidad != null && !this.idDisponibilidad.equals(other.idDisponibilidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Disponibilidad[ idDisponibilidad=" + idDisponibilidad + " ]";
    }


    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }


    @XmlTransient
    public List<OrdenSuministro> getOrdenSuministroList() {
        return ordenSuministroList;
    }

    public void setOrdenSuministroList(List<OrdenSuministro> ordenSuministroList) {
        this.ordenSuministroList = ordenSuministroList;
    }

    @XmlTransient
    public List<ComprobanteEgreso> getComprobanteEgresoList() {
        return comprobanteEgresoList;
    }

    public void setComprobanteEgresoList(List<ComprobanteEgreso> comprobanteEgresoList) {
        this.comprobanteEgresoList = comprobanteEgresoList;
    }

    @XmlTransient
    public List<Ops> getOpsList() {
        return opsList;
    }

    public void setOpsList(List<Ops> opsList) {
        this.opsList = opsList;
    }

    public BigDecimal getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(BigDecimal consecutivo) {
        this.consecutivo = consecutivo;
    }
    
}
