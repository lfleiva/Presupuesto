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
@Table(name = "comprobante_egreso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComprobanteEgreso.findAll", query = "SELECT c FROM ComprobanteEgreso c"),
    @NamedQuery(name = "ComprobanteEgreso.findByIdComprobanteEgreso", query = "SELECT c FROM ComprobanteEgreso c WHERE c.idComprobanteEgreso = :idComprobanteEgreso"),
    @NamedQuery(name = "ComprobanteEgreso.findByConsecutivo", query = "SELECT c FROM ComprobanteEgreso c WHERE c.consecutivo = :consecutivo"),
    @NamedQuery(name = "ComprobanteEgreso.findByFecha", query = "SELECT c FROM ComprobanteEgreso c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ComprobanteEgreso.findByValorLetras", query = "SELECT c FROM ComprobanteEgreso c WHERE c.valorLetras = :valorLetras"),
    @NamedQuery(name = "ComprobanteEgreso.findByValorCuenta", query = "SELECT c FROM ComprobanteEgreso c WHERE c.valorCuenta = :valorCuenta"),
    @NamedQuery(name = "ComprobanteEgreso.findByValorDescuentos", query = "SELECT c FROM ComprobanteEgreso c WHERE c.valorDescuentos = :valorDescuentos"),
    @NamedQuery(name = "ComprobanteEgreso.findByValorPagar", query = "SELECT c FROM ComprobanteEgreso c WHERE c.valorPagar = :valorPagar"),
    @NamedQuery(name = "ComprobanteEgreso.findByCuentaBanco", query = "SELECT c FROM ComprobanteEgreso c WHERE c.cuentaBanco = :cuentaBanco"),
    @NamedQuery(name = "ComprobanteEgreso.findByCheque", query = "SELECT c FROM ComprobanteEgreso c WHERE c.cheque = :cheque"),
    @NamedQuery(name = "ComprobanteEgreso.findByDescripcion", query = "SELECT c FROM ComprobanteEgreso c WHERE c.descripcion = :descripcion")})
public class ComprobanteEgreso implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_comprobante_egreso")
    private BigDecimal idComprobanteEgreso;
    @Column(name = "consecutivo")
    private BigDecimal consecutivo;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "valor_letras")
    private String valorLetras;
    @Column(name = "valor_cuenta")
    private BigDecimal valorCuenta;
    @Column(name = "valor_descuentos")
    private BigDecimal valorDescuentos;
    @Column(name = "valor_pagar")
    private BigDecimal valorPagar;
    @Column(name = "cuenta_banco")
    private String cuentaBanco;
    @Column(name = "cheque")
    private String cheque;
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "disponibilidad", referencedColumnName = "id_disponibilidad")
    @ManyToOne
    private Disponibilidad disponibilidad;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;
    @OneToMany(mappedBy = "comprobanteEgreso")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<EgresoDescuento> egresoDescuentoList;

    public ComprobanteEgreso() {
    }

    public ComprobanteEgreso(BigDecimal idComprobanteEgreso) {
        this.idComprobanteEgreso = idComprobanteEgreso;
    }

    public BigDecimal getIdComprobanteEgreso() {
        return idComprobanteEgreso;
    }

    public void setIdComprobanteEgreso(BigDecimal idComprobanteEgreso) {
        this.idComprobanteEgreso = idComprobanteEgreso;
    }

    public BigDecimal getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(BigDecimal consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getValorLetras() {
        return valorLetras;
    }

    public void setValorLetras(String valorLetras) {
        this.valorLetras = valorLetras;
    }

    public BigDecimal getValorCuenta() {
        return valorCuenta;
    }

    public void setValorCuenta(BigDecimal valorCuenta) {
        this.valorCuenta = valorCuenta;
    }

    public BigDecimal getValorDescuentos() {
        return valorDescuentos;
    }

    public void setValorDescuentos(BigDecimal valorDescuentos) {
        this.valorDescuentos = valorDescuentos;
    }

    public BigDecimal getValorPagar() {
        return valorPagar;
    }

    public void setValorPagar(BigDecimal valorPagar) {
        this.valorPagar = valorPagar;
    }

    public String getCuentaBanco() {
        return cuentaBanco;
    }

    public void setCuentaBanco(String cuentaBanco) {
        this.cuentaBanco = cuentaBanco;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Vigencia getVigencia() {
        return vigencia;
    }

    public void setVigencia(Vigencia vigencia) {
        this.vigencia = vigencia;
    }

    @XmlTransient
    public List<EgresoDescuento> getEgresoDescuentoList() {
        return egresoDescuentoList;
    }

    public void setEgresoDescuentoList(List<EgresoDescuento> egresoDescuentoList) {
        this.egresoDescuentoList = egresoDescuentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComprobanteEgreso != null ? idComprobanteEgreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComprobanteEgreso)) {
            return false;
        }
        ComprobanteEgreso other = (ComprobanteEgreso) object;
        if ((this.idComprobanteEgreso == null && other.idComprobanteEgreso != null) || (this.idComprobanteEgreso != null && !this.idComprobanteEgreso.equals(other.idComprobanteEgreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.ComprobanteEgreso[ idComprobanteEgreso=" + idComprobanteEgreso + " ]";
    }
    
}
