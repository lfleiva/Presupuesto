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
@Table(name = "beneficiario", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Beneficiario.findAll", query = "SELECT b FROM Beneficiario b"),
    @NamedQuery(name = "Beneficiario.findByIdBeneficiario", query = "SELECT b FROM Beneficiario b WHERE b.idBeneficiario = :idBeneficiario"),
    @NamedQuery(name = "Beneficiario.findByIdentificacion", query = "SELECT b FROM Beneficiario b WHERE b.identificacion = :identificacion"),
    @NamedQuery(name = "Beneficiario.findByNombre", query = "SELECT b FROM Beneficiario b WHERE b.nombre = :nombre"),
    @NamedQuery(name = "Beneficiario.findByDireccion", query = "SELECT b FROM Beneficiario b WHERE b.direccion = :direccion"),
    @NamedQuery(name = "Beneficiario.findByTelefono", query = "SELECT b FROM Beneficiario b WHERE b.telefono = :telefono"),
    @NamedQuery(name = "Beneficiario.findByEmail", query = "SELECT b FROM Beneficiario b WHERE b.email = :email")})
public class Beneficiario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_beneficiario", nullable = false, precision = 22)
    private BigDecimal idBeneficiario;
    @Column(name = "identificacion", length = 500)
    private String identificacion;
    @Column(name = "nombre", length = 1000)
    private String nombre;
    @Column(name = "direccion", length = 1000)
    private String direccion;
    @Column(name = "telefono", length = 500)
    private String telefono;
    @Column(name = "email", length = 1000)
    private String email;
    @OneToMany(mappedBy = "beneficiario")
    private List<Disponibilidad> disponibilidadList;

    public Beneficiario() {
    }

    public Beneficiario(BigDecimal idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public BigDecimal getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(BigDecimal idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public List<Disponibilidad> getDisponibilidadList() {
        return disponibilidadList;
    }

    public void setDisponibilidadList(List<Disponibilidad> disponibilidadList) {
        this.disponibilidadList = disponibilidadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBeneficiario != null ? idBeneficiario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Beneficiario)) {
            return false;
        }
        Beneficiario other = (Beneficiario) object;
        if ((this.idBeneficiario == null && other.idBeneficiario != null) || (this.idBeneficiario != null && !this.idBeneficiario.equals(other.idBeneficiario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Beneficiario[ idBeneficiario=" + idBeneficiario + " ]";
    }
    
}
