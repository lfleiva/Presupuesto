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
@Table(name = "ejecucion", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ejecucion.findAll", query = "SELECT e FROM Ejecucion e"),
    @NamedQuery(name = "Ejecucion.findByIdEjecucion", query = "SELECT e FROM Ejecucion e WHERE e.idEjecucion = :idEjecucion"),
    @NamedQuery(name = "Ejecucion.findByPresupuestoInicial", query = "SELECT e FROM Ejecucion e WHERE e.presupuestoInicial = :presupuestoInicial"),
    @NamedQuery(name = "Ejecucion.findByAdiciones", query = "SELECT e FROM Ejecucion e WHERE e.adiciones = :adiciones"),
    @NamedQuery(name = "Ejecucion.findByCreditos", query = "SELECT e FROM Ejecucion e WHERE e.creditos = :creditos"),
    @NamedQuery(name = "Ejecucion.findByContracreditos", query = "SELECT e FROM Ejecucion e WHERE e.contracreditos = :contracreditos"),
    @NamedQuery(name = "Ejecucion.findByPresupuestoFinal", query = "SELECT e FROM Ejecucion e WHERE e.presupuestoFinal = :presupuestoFinal"),
    @NamedQuery(name = "Ejecucion.findByGastoTotal", query = "SELECT e FROM Ejecucion e WHERE e.gastoTotal = :gastoTotal"),
    @NamedQuery(name = "Ejecucion.findBySaldo", query = "SELECT e FROM Ejecucion e WHERE e.saldo = :saldo")})
public class Ejecucion implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "presupuesto_inicial")
    private BigDecimal presupuestoInicial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "adiciones")
    private BigDecimal adiciones;
    @Column(name = "creditos")
    private BigDecimal creditos;
    @Column(name = "contracreditos")
    private BigDecimal contracreditos;
    @Column(name = "presupuesto_final")
    private BigDecimal presupuestoFinal;
    @Column(name = "gasto_total")
    private BigDecimal gastoTotal;
    @Column(name = "saldo")
    private BigDecimal saldo;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ejecucion", nullable = false, precision = 22)
    private BigDecimal idEjecucion;
    @JoinColumn(name = "rubro", referencedColumnName = "id_rubro")
    @ManyToOne
    private Rubro rubro;
    @JoinColumn(name = "vigencia", referencedColumnName = "id_vigencia")
    @ManyToOne
    private Vigencia vigencia;

    public Ejecucion() {
    }

    public Ejecucion(BigDecimal idEjecucion) {
        this.idEjecucion = idEjecucion;
    }

    public BigDecimal getIdEjecucion() {
        return idEjecucion;
    }

    public void setIdEjecucion(BigDecimal idEjecucion) {
        this.idEjecucion = idEjecucion;
    }

    public BigDecimal getPresupuestoInicial() {
        return presupuestoInicial;
    }

    public void setPresupuestoInicial(BigDecimal presupuestoInicial) {
        this.presupuestoInicial = presupuestoInicial;
    }


    public BigDecimal getPresupuestoFinal() {
        return presupuestoFinal;
    }

    public void setPresupuestoFinal(BigDecimal presupuestoFinal) {
        this.presupuestoFinal = presupuestoFinal;
    }

    public BigDecimal getGastoTotal() {
        return gastoTotal;
    }

    public void setGastoTotal(BigDecimal gastoTotal) {
        this.gastoTotal = gastoTotal;
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
        hash += (idEjecucion != null ? idEjecucion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ejecucion)) {
            return false;
        }
        Ejecucion other = (Ejecucion) object;
        if ((this.idEjecucion == null && other.idEjecucion != null) || (this.idEjecucion != null && !this.idEjecucion.equals(other.idEjecucion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Ejecucion[ idEjecucion=" + idEjecucion + " ]";
    }

    public BigDecimal getAdiciones() {
        return adiciones;
    }

    public void setAdiciones(BigDecimal adiciones) {
        this.adiciones = adiciones;
    }

    public BigDecimal getCreditos() {
        return creditos;
    }

    public void setCreditos(BigDecimal creditos) {
        this.creditos = creditos;
    }

    public BigDecimal getContracreditos() {
        return contracreditos;
    }

    public void setContracreditos(BigDecimal contracreditos) {
        this.contracreditos = contracreditos;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
}
