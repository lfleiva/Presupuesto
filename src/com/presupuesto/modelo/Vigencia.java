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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "vigencia", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vigencia.findAll", query = "SELECT v FROM Vigencia v"),
    @NamedQuery(name = "Vigencia.findByIdVigencia", query = "SELECT v FROM Vigencia v WHERE v.idVigencia = :idVigencia"),
    @NamedQuery(name = "Vigencia.findByVigencia", query = "SELECT v FROM Vigencia v WHERE v.vigencia = :vigencia"),
    @NamedQuery(name = "Vigencia.findByDescripcion", query = "SELECT v FROM Vigencia v WHERE v.descripcion = :descripcion"),
    @NamedQuery(name = "Vigencia.findByActiva", query = "SELECT v FROM Vigencia v WHERE v.activa = :activa")})
public class Vigencia implements Serializable {

    @OneToMany(mappedBy = "vigencia")
    private List<OrdenSuministro> ordenSuministroList;
    @OneToMany(mappedBy = "vigencia")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Suministro> suministroList;
    @OneToMany(mappedBy = "vigencia")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Descuento> descuentoList;
    @OneToMany(mappedBy = "vigencia")
    private List<ComprobanteEgreso> comprobanteEgresoList;
    @OneToMany(mappedBy = "vigencia")
    private List<Ops> opsList;
    @OneToMany(mappedBy = "vigencia")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<EgresoDescuento> egresoDescuentoList;

    @OneToMany(mappedBy = "vigencia")
    private List<Registro> registroList;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "id_vigencia", nullable = false, precision = 22)
    private BigDecimal idVigencia;
    @Basic(optional = false)
    @Column(name = "vigencia", nullable = false, length = 20)
    private String vigencia;
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    @Column(name = "activa")
    private Boolean activa;
    @OneToMany(mappedBy = "vigencia")
    private List<Presupuesto> presupuestoList;
    @OneToMany(mappedBy = "vigencia")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<AdicionRubro> adicionRubroList;
    @OneToMany(mappedBy = "vigencia")
    private List<Traslado> trasladoList;
    @OneToMany(mappedBy = "vigencia")
    private List<Ejecucion> ejecucionList;
    @OneToMany(mappedBy = "vigencia")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<DisponibilidadRubro> disponibilidadRubroList;
    @OneToMany(mappedBy = "vigencia")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Disponibilidad> disponibilidadList;
    @OneToMany(mappedBy = "vigencia")
    private List<Adicion> adicionList;
    @OneToMany(mappedBy = "vigencia")
    private List<Rubro> rubroList;
    @OneToMany(mappedBy = "vigencia")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<TrasladoRubro> trasladoRubroList;

    public Vigencia() {
    }

    public Vigencia(BigDecimal idVigencia) {
        this.idVigencia = idVigencia;
    }

    public Vigencia(BigDecimal idVigencia, String vigencia) {
        this.idVigencia = idVigencia;
        this.vigencia = vigencia;
    }

    public BigDecimal getIdVigencia() {
        return idVigencia;
    }

    public void setIdVigencia(BigDecimal idVigencia) {
        this.idVigencia = idVigencia;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    @XmlTransient
    public List<Presupuesto> getPresupuestoList() {
        return presupuestoList;
    }

    public void setPresupuestoList(List<Presupuesto> presupuestoList) {
        this.presupuestoList = presupuestoList;
    }

    @XmlTransient
    public List<AdicionRubro> getAdicionRubroList() {
        return adicionRubroList;
    }

    public void setAdicionRubroList(List<AdicionRubro> adicionRubroList) {
        this.adicionRubroList = adicionRubroList;
    }

    @XmlTransient
    public List<Traslado> getTrasladoList() {
        return trasladoList;
    }

    public void setTrasladoList(List<Traslado> trasladoList) {
        this.trasladoList = trasladoList;
    }

    @XmlTransient
    public List<Ejecucion> getEjecucionList() {
        return ejecucionList;
    }

    public void setEjecucionList(List<Ejecucion> ejecucionList) {
        this.ejecucionList = ejecucionList;
    }

    @XmlTransient
    public List<DisponibilidadRubro> getDisponibilidadRubroList() {
        return disponibilidadRubroList;
    }

    public void setDisponibilidadRubroList(List<DisponibilidadRubro> disponibilidadRubroList) {
        this.disponibilidadRubroList = disponibilidadRubroList;
    }

    @XmlTransient
    public List<Disponibilidad> getDisponibilidadList() {
        return disponibilidadList;
    }

    public void setDisponibilidadList(List<Disponibilidad> disponibilidadList) {
        this.disponibilidadList = disponibilidadList;
    }

    @XmlTransient
    public List<Adicion> getAdicionList() {
        return adicionList;
    }

    public void setAdicionList(List<Adicion> adicionList) {
        this.adicionList = adicionList;
    }

    @XmlTransient
    public List<Rubro> getRubroList() {
        return rubroList;
    }

    public void setRubroList(List<Rubro> rubroList) {
        this.rubroList = rubroList;
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
        hash += (idVigencia != null ? idVigencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vigencia)) {
            return false;
        }
        Vigencia other = (Vigencia) object;
        if ((this.idVigencia == null && other.idVigencia != null) || (this.idVigencia != null && !this.idVigencia.equals(other.idVigencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Vigencia[ idVigencia=" + idVigencia + " ]";
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
    public List<Suministro> getSuministroList() {
        return suministroList;
    }

    public void setSuministroList(List<Suministro> suministroList) {
        this.suministroList = suministroList;
    }

    @XmlTransient
    public List<Descuento> getDescuentoList() {
        return descuentoList;
    }

    public void setDescuentoList(List<Descuento> descuentoList) {
        this.descuentoList = descuentoList;
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

    @XmlTransient
    public List<EgresoDescuento> getEgresoDescuentoList() {
        return egresoDescuentoList;
    }

    public void setEgresoDescuentoList(List<EgresoDescuento> egresoDescuentoList) {
        this.egresoDescuentoList = egresoDescuentoList;
    }
    
}
