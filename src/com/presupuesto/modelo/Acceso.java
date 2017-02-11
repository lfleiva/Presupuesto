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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "acceso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Acceso.findAll", query = "SELECT a FROM Acceso a")
    , @NamedQuery(name = "Acceso.findByIdAcceso", query = "SELECT a FROM Acceso a WHERE a.idAcceso = :idAcceso")
    , @NamedQuery(name = "Acceso.findByUsuario", query = "SELECT a FROM Acceso a WHERE a.usuario = :usuario")
    , @NamedQuery(name = "Acceso.findByPassword", query = "SELECT a FROM Acceso a WHERE a.password = :password")})
public class Acceso implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_acceso")
    private BigDecimal idAcceso;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "password")
    private String password;

    public Acceso() {
    }

    public Acceso(BigDecimal idAcceso) {
        this.idAcceso = idAcceso;
    }

    public BigDecimal getIdAcceso() {
        return idAcceso;
    }

    public void setIdAcceso(BigDecimal idAcceso) {
        this.idAcceso = idAcceso;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAcceso != null ? idAcceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Acceso)) {
            return false;
        }
        Acceso other = (Acceso) object;
        if ((this.idAcceso == null && other.idAcceso != null) || (this.idAcceso != null && !this.idAcceso.equals(other.idAcceso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Acceso[ idAcceso=" + idAcceso + " ]";
    }
    
}
