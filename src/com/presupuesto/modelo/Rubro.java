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
@Table(name = "rubro", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rubro.findAll", query = "SELECT r FROM Rubro r"),
    @NamedQuery(name = "Rubro.findByIdRubro", query = "SELECT r FROM Rubro r WHERE r.idRubro = :idRubro"),
    @NamedQuery(name = "Rubro.findByCodigo", query = "SELECT r FROM Rubro r WHERE r.codigo = :codigo"),
    @NamedQuery(name = "Rubro.findByNombre", query = "SELECT r FROM Rubro r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Rubro.findByValor", query = "SELECT r FROM Rubro r WHERE r.valor = :valor")})
public class Rubro implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_rubro", nullable = false, precision = 22)
    private BigDecimal idRubro;
    @Basic(optional = false)
    @Column(name = "codigo", nullable = false, length = 100)
    private String codigo;
    @Basic(optional = false)
    @Column(name = "nombre", nullable = false, length = 1000)
    private String nombre;
    @OneToMany(mappedBy = "rubro")
    private List<Presupuesto> presupuestoList;
    @OneToMany(mappedBy = "rubro")
    private List<AdicionRubro> adicionRubroList;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "rubro")
    private List<Ejecucion> ejecucionList;
    @OneToMany(mappedBy = "rubro")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<DisponibilidadRubro> disponibilidadRubroList;
    @JoinColumn(name = "tipo_rubro", referencedColumnName = "id_tipo_rubro")
    @ManyToOne
    private TipoRubro tipoRubro;
    @OneToMany(mappedBy = "subcuenta")
    private List<Rubro> rubroList;
    @JoinColumn(name = "subcuenta", referencedColumnName = "id_rubro")
    @ManyToOne
    private Rubro subcuenta;
    @OneToMany(mappedBy = "cuenta")
    private List<Rubro> rubroList1;
    @JoinColumn(name = "cuenta", referencedColumnName = "id_rubro")
    @ManyToOne
    private Rubro cuenta;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;
    @OneToMany(mappedBy = "rubro")
    private List<TrasladoRubro> trasladoRubroList;

    public Rubro() {
    }

    public Rubro(BigDecimal idRubro) {
        this.idRubro = idRubro;
    }

    public Rubro(BigDecimal idRubro, String codigo, String nombre) {
        this.idRubro = idRubro;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public BigDecimal getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(BigDecimal idRubro) {
        this.idRubro = idRubro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public TipoRubro getTipoRubro() {
        return tipoRubro;
    }

    public void setTipoRubro(TipoRubro tipoRubro) {
        this.tipoRubro = tipoRubro;
    }

    @XmlTransient
    public List<Rubro> getRubroList() {
        return rubroList;
    }

    public void setRubroList(List<Rubro> rubroList) {
        this.rubroList = rubroList;
    }

    public Rubro getSubcuenta() {
        return subcuenta;
    }

    public void setSubcuenta(Rubro subcuenta) {
        this.subcuenta = subcuenta;
    }

    @XmlTransient
    public List<Rubro> getRubroList1() {
        return rubroList1;
    }

    public void setRubroList1(List<Rubro> rubroList1) {
        this.rubroList1 = rubroList1;
    }

    public Rubro getCuenta() {
        return cuenta;
    }

    public void setCuenta(Rubro cuenta) {
        this.cuenta = cuenta;
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
        hash += (idRubro != null ? idRubro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rubro)) {
            return false;
        }
        Rubro other = (Rubro) object;
        if ((this.idRubro == null && other.idRubro != null) || (this.idRubro != null && !this.idRubro.equals(other.idRubro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Rubro[ idRubro=" + idRubro + " ]";
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
}
