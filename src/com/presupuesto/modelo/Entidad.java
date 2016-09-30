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
@Table(name = "entidad", catalog = "presupuesto", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entidad.findAll", query = "SELECT e FROM Entidad e"),
    @NamedQuery(name = "Entidad.findByIdEntidad", query = "SELECT e FROM Entidad e WHERE e.idEntidad = :idEntidad"),
    @NamedQuery(name = "Entidad.findByNombre", query = "SELECT e FROM Entidad e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Entidad.findByNombreCorto", query = "SELECT e FROM Entidad e WHERE e.nombreCorto = :nombreCorto"),
    @NamedQuery(name = "Entidad.findByNit", query = "SELECT e FROM Entidad e WHERE e.nit = :nit"),
    @NamedQuery(name = "Entidad.findByDepartamento", query = "SELECT e FROM Entidad e WHERE e.departamento = :departamento"),
    @NamedQuery(name = "Entidad.findByCiudad", query = "SELECT e FROM Entidad e WHERE e.ciudad = :ciudad"),
    @NamedQuery(name = "Entidad.findByDireccion", query = "SELECT e FROM Entidad e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "Entidad.findByTelefono", query = "SELECT e FROM Entidad e WHERE e.telefono = :telefono"),
    @NamedQuery(name = "Entidad.findByCelular", query = "SELECT e FROM Entidad e WHERE e.celular = :celular")})
public class Entidad implements Serializable {

    @Column(name = "gerente")
    private String gerente;
    @Column(name = "cargo_gerente")
    private String cargoGerente;
    @Column(name = "tesorero")
    private String tesorero;
    @Column(name = "cargo_tesorero")
    private String cargoTesorero;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "id_entidad", nullable = false, precision = 22)
    private BigDecimal idEntidad;
    @Column(name = "nombre", length = 1000)
    private String nombre;
    @Column(name = "nombre_corto", length = 1000)
    private String nombreCorto;
    @Column(name = "nit", length = 100)
    private String nit;
    @Column(name = "departamento", length = 500)
    private String departamento;
    @Column(name = "ciudad", length = 500)
    private String ciudad;
    @Column(name = "direccion", length = 1000)
    private String direccion;
    @Column(name = "telefono", length = 500)
    private String telefono;
    @Column(name = "celular", length = 500)
    private String celular;

    public Entidad() {
    }

    public Entidad(BigDecimal idEntidad) {
        this.idEntidad = idEntidad;
    }

    public BigDecimal getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(BigDecimal idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntidad != null ? idEntidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entidad)) {
            return false;
        }
        Entidad other = (Entidad) object;
        if ((this.idEntidad == null && other.idEntidad != null) || (this.idEntidad != null && !this.idEntidad.equals(other.idEntidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.presupuesto.modelo.Entidad[ idEntidad=" + idEntidad + " ]";
    }

    public String getGerente() {
        return gerente;
    }

    public void setGerente(String gerente) {
        this.gerente = gerente;
    }

    public String getCargoGerente() {
        return cargoGerente;
    }

    public void setCargoGerente(String cargoGerente) {
        this.cargoGerente = cargoGerente;
    }

    public String getTesorero() {
        return tesorero;
    }

    public void setTesorero(String tesorero) {
        this.tesorero = tesorero;
    }

    public String getCargoTesorero() {
        return cargoTesorero;
    }

    public void setCargoTesorero(String cargoTesorero) {
        this.cargoTesorero = cargoTesorero;
    }
    
}
