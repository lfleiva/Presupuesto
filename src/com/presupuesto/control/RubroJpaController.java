/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.TipoRubro;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.modelo.Presupuesto;
import java.util.ArrayList;
import java.util.List;
import com.presupuesto.modelo.AdicionRubro;
import com.presupuesto.modelo.Ejecucion;
import com.presupuesto.modelo.DisponibilidadRubro;
import com.presupuesto.modelo.TrasladoRubro;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Fernando Leiva
 */
public class RubroJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;

    public RubroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public RubroJpaController(){}

    public void create(Rubro rubro) {
//        if (rubro.getPresupuestoList() == null) {
//            rubro.setPresupuestoList(new ArrayList<Presupuesto>());
//        }
//        if (rubro.getAdicionRubroList() == null) {
//            rubro.setAdicionRubroList(new ArrayList<AdicionRubro>());
//        }
//        if (rubro.getEjecucionList() == null) {
//            rubro.setEjecucionList(new ArrayList<Ejecucion>());
//        }
//        if (rubro.getDisponibilidadRubroList() == null) {
//            rubro.setDisponibilidadRubroList(new ArrayList<DisponibilidadRubro>());
//        }
//        if (rubro.getRubroList() == null) {
//            rubro.setRubroList(new ArrayList<Rubro>());
//        }
//        if (rubro.getRubroListSubcuenta() == null) {
//            rubro.setRubroListSubcuenta(new ArrayList<Rubro>());
//        }
//        if (rubro.getTrasladoRubroList() == null) {
//            rubro.setTrasladoRubroList(new ArrayList<TrasladoRubro>());
//        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            TipoRubro tipoRubro = rubro.getTipoRubro();
//            if (tipoRubro != null) {
//                tipoRubro = em.getReference(tipoRubro.getClass(), tipoRubro.getIdTipoRubro());
//                rubro.setTipoRubro(tipoRubro);
//            }
//            Rubro subcuenta = rubro.getSubcuenta();
//            if (subcuenta != null) {
//                subcuenta = em.getReference(subcuenta.getClass(), subcuenta.getIdRubro());
//                rubro.setSubcuenta(subcuenta);
//            }
//            Rubro cuenta = rubro.getCuenta();
//            if (cuenta != null) {
//                cuenta = em.getReference(cuenta.getClass(), cuenta.getIdRubro());
//                rubro.setCuenta(cuenta);
//            }
//            Vigencia vigencia = rubro.getVigencia();
//            if (vigencia != null) {
//                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
//                rubro.setVigencia(vigencia);
//            }
//            List<Presupuesto> attachedPresupuestoList = new ArrayList<Presupuesto>();
//            for (Presupuesto presupuestoListPresupuestoToAttach : rubro.getPresupuestoList()) {
//                presupuestoListPresupuestoToAttach = em.getReference(presupuestoListPresupuestoToAttach.getClass(), presupuestoListPresupuestoToAttach.getIdPresupuesto());
//                attachedPresupuestoList.add(presupuestoListPresupuestoToAttach);
//            }
//            rubro.setPresupuestoList(attachedPresupuestoList);
//            List<AdicionRubro> attachedAdicionRubroList = new ArrayList<AdicionRubro>();
//            for (AdicionRubro adicionRubroListAdicionRubroToAttach : rubro.getAdicionRubroList()) {
//                adicionRubroListAdicionRubroToAttach = em.getReference(adicionRubroListAdicionRubroToAttach.getClass(), adicionRubroListAdicionRubroToAttach.getIdAdicionRubro());
//                attachedAdicionRubroList.add(adicionRubroListAdicionRubroToAttach);
//            }
//            rubro.setAdicionRubroList(attachedAdicionRubroList);
//            List<Ejecucion> attachedEjecucionList = new ArrayList<Ejecucion>();
//            for (Ejecucion ejecucionListEjecucionToAttach : rubro.getEjecucionList()) {
//                ejecucionListEjecucionToAttach = em.getReference(ejecucionListEjecucionToAttach.getClass(), ejecucionListEjecucionToAttach.getIdEjecucion());
//                attachedEjecucionList.add(ejecucionListEjecucionToAttach);
//            }
//            rubro.setEjecucionList(attachedEjecucionList);
//            List<DisponibilidadRubro> attachedDisponibilidadRubroList = new ArrayList<DisponibilidadRubro>();
//            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubroToAttach : rubro.getDisponibilidadRubroList()) {
//                disponibilidadRubroListDisponibilidadRubroToAttach = em.getReference(disponibilidadRubroListDisponibilidadRubroToAttach.getClass(), disponibilidadRubroListDisponibilidadRubroToAttach.getIdDisponibilidadRubro());
//                attachedDisponibilidadRubroList.add(disponibilidadRubroListDisponibilidadRubroToAttach);
//            }
//            rubro.setDisponibilidadRubroList(attachedDisponibilidadRubroList);
//            List<Rubro> attachedRubroListCuenta = new ArrayList<Rubro>();
//            for (Rubro rubroListCuentaRubroToAttach : rubro.getRubroListCuenta()) {
//                rubroListCuentaRubroToAttach = em.getReference(rubroListCuentaRubroToAttach.getClass(), rubroListCuentaRubroToAttach.getIdRubro());
//                attachedRubroListCuenta.add(rubroListCuentaRubroToAttach);
//            }
//            rubro.setRubroListCuenta(attachedRubroListCuenta);
//            List<Rubro> attachedRubroListSubcuenta = new ArrayList<Rubro>();
//            for (Rubro rubroListSubcuentaRubroToAttach : rubro.getRubroListSubcuenta()) {
//                rubroListSubcuentaRubroToAttach = em.getReference(rubroListSubcuentaRubroToAttach.getClass(), rubroListSubcuentaRubroToAttach.getIdRubro());
//                attachedRubroListSubcuenta.add(rubroListSubcuentaRubroToAttach);
//            }
//            rubro.setRubroListSubcuenta(attachedRubroListSubcuenta);
//            List<TrasladoRubro> attachedTrasladoRubroList = new ArrayList<TrasladoRubro>();
//            for (TrasladoRubro trasladoRubroListTrasladoRubroToAttach : rubro.getTrasladoRubroList()) {
//                trasladoRubroListTrasladoRubroToAttach = em.getReference(trasladoRubroListTrasladoRubroToAttach.getClass(), trasladoRubroListTrasladoRubroToAttach.getIdTrasladoRubro());
//                attachedTrasladoRubroList.add(trasladoRubroListTrasladoRubroToAttach);
//            }
//            rubro.setTrasladoRubroList(attachedTrasladoRubroList);
//            em.persist(rubro);
//            if (tipoRubro != null) {
//                tipoRubro.getRubroList().add(rubro);
//                tipoRubro = em.merge(tipoRubro);
//            }
//            if (subcuenta != null) {
//                subcuenta.getRubroListCuenta().add(rubro);
//                subcuenta = em.merge(subcuenta);
//            }
//            if (cuenta != null) {
//                cuenta.getRubroListCuenta().add(rubro);
//                cuenta = em.merge(cuenta);
//            }
//            if (vigencia != null) {
//                vigencia.getRubroList().add(rubro);
//                vigencia = em.merge(vigencia);
//            }
//            for (Presupuesto presupuestoListPresupuesto : rubro.getPresupuestoList()) {
//                Rubro oldRubroOfPresupuestoListPresupuesto = presupuestoListPresupuesto.getRubro();
//                presupuestoListPresupuesto.setRubro(rubro);
//                presupuestoListPresupuesto = em.merge(presupuestoListPresupuesto);
//                if (oldRubroOfPresupuestoListPresupuesto != null) {
//                    oldRubroOfPresupuestoListPresupuesto.getPresupuestoList().remove(presupuestoListPresupuesto);
//                    oldRubroOfPresupuestoListPresupuesto = em.merge(oldRubroOfPresupuestoListPresupuesto);
//                }
//            }
//            for (AdicionRubro adicionRubroListAdicionRubro : rubro.getAdicionRubroList()) {
//                Rubro oldRubroOfAdicionRubroListAdicionRubro = adicionRubroListAdicionRubro.getRubro();
//                adicionRubroListAdicionRubro.setRubro(rubro);
//                adicionRubroListAdicionRubro = em.merge(adicionRubroListAdicionRubro);
//                if (oldRubroOfAdicionRubroListAdicionRubro != null) {
//                    oldRubroOfAdicionRubroListAdicionRubro.getAdicionRubroList().remove(adicionRubroListAdicionRubro);
//                    oldRubroOfAdicionRubroListAdicionRubro = em.merge(oldRubroOfAdicionRubroListAdicionRubro);
//                }
//            }
//            for (Ejecucion ejecucionListEjecucion : rubro.getEjecucionList()) {
//                Rubro oldRubroOfEjecucionListEjecucion = ejecucionListEjecucion.getRubro();
//                ejecucionListEjecucion.setRubro(rubro);
//                ejecucionListEjecucion = em.merge(ejecucionListEjecucion);
//                if (oldRubroOfEjecucionListEjecucion != null) {
//                    oldRubroOfEjecucionListEjecucion.getEjecucionList().remove(ejecucionListEjecucion);
//                    oldRubroOfEjecucionListEjecucion = em.merge(oldRubroOfEjecucionListEjecucion);
//                }
//            }
//            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubro : rubro.getDisponibilidadRubroList()) {
//                Rubro oldRubroOfDisponibilidadRubroListDisponibilidadRubro = disponibilidadRubroListDisponibilidadRubro.getRubro();
//                disponibilidadRubroListDisponibilidadRubro.setRubro(rubro);
//                disponibilidadRubroListDisponibilidadRubro = em.merge(disponibilidadRubroListDisponibilidadRubro);
//                if (oldRubroOfDisponibilidadRubroListDisponibilidadRubro != null) {
//                    oldRubroOfDisponibilidadRubroListDisponibilidadRubro.getDisponibilidadRubroList().remove(disponibilidadRubroListDisponibilidadRubro);
//                    oldRubroOfDisponibilidadRubroListDisponibilidadRubro = em.merge(oldRubroOfDisponibilidadRubroListDisponibilidadRubro);
//                }
//            }
//            for (Rubro rubroListCuentaRubro : rubro.getRubroListCuenta()) {
//                Rubro oldSubcuentaOfRubroListCuentaRubro = rubroListCuentaRubro.getSubcuenta();
//                rubroListCuentaRubro.setSubcuenta(rubro);
//                rubroListCuentaRubro = em.merge(rubroListCuentaRubro);
//                if (oldSubcuentaOfRubroListCuentaRubro != null) {
//                    oldSubcuentaOfRubroListCuentaRubro.getRubroListCuenta().remove(rubroListCuentaRubro);
//                    oldSubcuentaOfRubroListCuentaRubro = em.merge(oldSubcuentaOfRubroListCuentaRubro);
//                }
//            }
//            for (Rubro rubroListSubcuentaRubro : rubro.getRubroListSubcuenta()) {
//                Rubro oldCuentaOfRubroListSubcuentaRubro = rubroListSubcuentaRubro.getCuenta();
//                rubroListSubcuentaRubro.setCuenta(rubro);
//                rubroListSubcuentaRubro = em.merge(rubroListSubcuentaRubro);
//                if (oldCuentaOfRubroListSubcuentaRubro != null) {
//                    oldCuentaOfRubroListSubcuentaRubro.getRubroListSubcuenta().remove(rubroListSubcuentaRubro);
//                    oldCuentaOfRubroListSubcuentaRubro = em.merge(oldCuentaOfRubroListSubcuentaRubro);
//                }
//            }
//            for (TrasladoRubro trasladoRubroListTrasladoRubro : rubro.getTrasladoRubroList()) {
//                Rubro oldRubroOfTrasladoRubroListTrasladoRubro = trasladoRubroListTrasladoRubro.getRubro();
//                trasladoRubroListTrasladoRubro.setRubro(rubro);
//                trasladoRubroListTrasladoRubro = em.merge(trasladoRubroListTrasladoRubro);
//                if (oldRubroOfTrasladoRubroListTrasladoRubro != null) {
//                    oldRubroOfTrasladoRubroListTrasladoRubro.getTrasladoRubroList().remove(trasladoRubroListTrasladoRubro);
//                    oldRubroOfTrasladoRubroListTrasladoRubro = em.merge(oldRubroOfTrasladoRubroListTrasladoRubro);
//                }
//            }
//            em.getTransaction().commit();
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    public void edit(Rubro rubro) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            Rubro persistentRubro = em.find(Rubro.class, rubro.getIdRubro());
//            TipoRubro tipoRubroOld = persistentRubro.getTipoRubro();
//            TipoRubro tipoRubroNew = rubro.getTipoRubro();
//            Rubro subcuentaOld = persistentRubro.getSubcuenta();
//            Rubro subcuentaNew = rubro.getSubcuenta();
//            Rubro cuentaOld = persistentRubro.getCuenta();
//            Rubro cuentaNew = rubro.getCuenta();
//            Vigencia vigenciaOld = persistentRubro.getVigencia();
//            Vigencia vigenciaNew = rubro.getVigencia();
//            List<Presupuesto> presupuestoListOld = persistentRubro.getPresupuestoList();
//            List<Presupuesto> presupuestoListNew = rubro.getPresupuestoList();
//            List<AdicionRubro> adicionRubroListOld = persistentRubro.getAdicionRubroList();
//            List<AdicionRubro> adicionRubroListNew = rubro.getAdicionRubroList();
//            List<Ejecucion> ejecucionListOld = persistentRubro.getEjecucionList();
//            List<Ejecucion> ejecucionListNew = rubro.getEjecucionList();
//            List<DisponibilidadRubro> disponibilidadRubroListOld = persistentRubro.getDisponibilidadRubroList();
//            List<DisponibilidadRubro> disponibilidadRubroListNew = rubro.getDisponibilidadRubroList();
//            List<Rubro> rubroListCuentaOld = persistentRubro.getRubroListCuenta();
//            List<Rubro> rubroListCuentaNew = rubro.getRubroListCuenta();
//            List<Rubro> rubroListSubcuentaOld = persistentRubro.getRubroListSubcuenta();
//            List<Rubro> rubroListSubcuentaNew = rubro.getRubroListSubcuenta();
//            List<TrasladoRubro> trasladoRubroListOld = persistentRubro.getTrasladoRubroList();
//            List<TrasladoRubro> trasladoRubroListNew = rubro.getTrasladoRubroList();
//            if (tipoRubroNew != null) {
//                tipoRubroNew = em.getReference(tipoRubroNew.getClass(), tipoRubroNew.getIdTipoRubro());
//                rubro.setTipoRubro(tipoRubroNew);
//            }
//            if (subcuentaNew != null) {
//                subcuentaNew = em.getReference(subcuentaNew.getClass(), subcuentaNew.getIdRubro());
//                rubro.setSubcuenta(subcuentaNew);
//            }
//            if (cuentaNew != null) {
//                cuentaNew = em.getReference(cuentaNew.getClass(), cuentaNew.getIdRubro());
//                rubro.setCuenta(cuentaNew);
//            }
//            if (vigenciaNew != null) {
//                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
//                rubro.setVigencia(vigenciaNew);
//            }
//            List<Presupuesto> attachedPresupuestoListNew = new ArrayList<Presupuesto>();
//            for (Presupuesto presupuestoListNewPresupuestoToAttach : presupuestoListNew) {
//                presupuestoListNewPresupuestoToAttach = em.getReference(presupuestoListNewPresupuestoToAttach.getClass(), presupuestoListNewPresupuestoToAttach.getIdPresupuesto());
//                attachedPresupuestoListNew.add(presupuestoListNewPresupuestoToAttach);
//            }
//            presupuestoListNew = attachedPresupuestoListNew;
//            rubro.setPresupuestoList(presupuestoListNew);
//            List<AdicionRubro> attachedAdicionRubroListNew = new ArrayList<AdicionRubro>();
//            for (AdicionRubro adicionRubroListNewAdicionRubroToAttach : adicionRubroListNew) {
//                adicionRubroListNewAdicionRubroToAttach = em.getReference(adicionRubroListNewAdicionRubroToAttach.getClass(), adicionRubroListNewAdicionRubroToAttach.getIdAdicionRubro());
//                attachedAdicionRubroListNew.add(adicionRubroListNewAdicionRubroToAttach);
//            }
//            adicionRubroListNew = attachedAdicionRubroListNew;
//            rubro.setAdicionRubroList(adicionRubroListNew);
//            List<Ejecucion> attachedEjecucionListNew = new ArrayList<Ejecucion>();
//            for (Ejecucion ejecucionListNewEjecucionToAttach : ejecucionListNew) {
//                ejecucionListNewEjecucionToAttach = em.getReference(ejecucionListNewEjecucionToAttach.getClass(), ejecucionListNewEjecucionToAttach.getIdEjecucion());
//                attachedEjecucionListNew.add(ejecucionListNewEjecucionToAttach);
//            }
//            ejecucionListNew = attachedEjecucionListNew;
//            rubro.setEjecucionList(ejecucionListNew);
//            List<DisponibilidadRubro> attachedDisponibilidadRubroListNew = new ArrayList<DisponibilidadRubro>();
//            for (DisponibilidadRubro disponibilidadRubroListNewDisponibilidadRubroToAttach : disponibilidadRubroListNew) {
//                disponibilidadRubroListNewDisponibilidadRubroToAttach = em.getReference(disponibilidadRubroListNewDisponibilidadRubroToAttach.getClass(), disponibilidadRubroListNewDisponibilidadRubroToAttach.getIdDisponibilidadRubro());
//                attachedDisponibilidadRubroListNew.add(disponibilidadRubroListNewDisponibilidadRubroToAttach);
//            }
//            disponibilidadRubroListNew = attachedDisponibilidadRubroListNew;
//            rubro.setDisponibilidadRubroList(disponibilidadRubroListNew);
//            List<Rubro> attachedRubroListCuentaNew = new ArrayList<Rubro>();
//            for (Rubro rubroListCuentaNewRubroToAttach : rubroListCuentaNew) {
//                rubroListCuentaNewRubroToAttach = em.getReference(rubroListCuentaNewRubroToAttach.getClass(), rubroListCuentaNewRubroToAttach.getIdRubro());
//                attachedRubroListCuentaNew.add(rubroListCuentaNewRubroToAttach);
//            }
//            rubroListCuentaNew = attachedRubroListCuentaNew;
//            rubro.setRubroListCuenta(rubroListCuentaNew);
//            List<Rubro> attachedRubroListSubcuentaNew = new ArrayList<Rubro>();
//            for (Rubro rubroListSubcuentaNewRubroToAttach : rubroListSubcuentaNew) {
//                rubroListSubcuentaNewRubroToAttach = em.getReference(rubroListSubcuentaNewRubroToAttach.getClass(), rubroListSubcuentaNewRubroToAttach.getIdRubro());
//                attachedRubroListSubcuentaNew.add(rubroListSubcuentaNewRubroToAttach);
//            }
//            rubroListSubcuentaNew = attachedRubroListSubcuentaNew;
//            rubro.setRubroListSubcuenta(rubroListSubcuentaNew);
//            List<TrasladoRubro> attachedTrasladoRubroListNew = new ArrayList<TrasladoRubro>();
//            for (TrasladoRubro trasladoRubroListNewTrasladoRubroToAttach : trasladoRubroListNew) {
//                trasladoRubroListNewTrasladoRubroToAttach = em.getReference(trasladoRubroListNewTrasladoRubroToAttach.getClass(), trasladoRubroListNewTrasladoRubroToAttach.getIdTrasladoRubro());
//                attachedTrasladoRubroListNew.add(trasladoRubroListNewTrasladoRubroToAttach);
//            }
//            trasladoRubroListNew = attachedTrasladoRubroListNew;
//            rubro.setTrasladoRubroList(trasladoRubroListNew);
//            rubro = em.merge(rubro);
//            if (tipoRubroOld != null && !tipoRubroOld.equals(tipoRubroNew)) {
//                tipoRubroOld.getRubroList().remove(rubro);
//                tipoRubroOld = em.merge(tipoRubroOld);
//            }
//            if (tipoRubroNew != null && !tipoRubroNew.equals(tipoRubroOld)) {
//                tipoRubroNew.getRubroList().add(rubro);
//                tipoRubroNew = em.merge(tipoRubroNew);
//            }
//            if (subcuentaOld != null && !subcuentaOld.equals(subcuentaNew)) {
//                subcuentaOld.getRubroListCuenta().remove(rubro);
//                subcuentaOld = em.merge(subcuentaOld);
//            }
//            if (subcuentaNew != null && !subcuentaNew.equals(subcuentaOld)) {
//                subcuentaNew.getRubroListCuenta().add(rubro);
//                subcuentaNew = em.merge(subcuentaNew);
//            }
//            if (cuentaOld != null && !cuentaOld.equals(cuentaNew)) {
//                cuentaOld.getRubroListCuenta().remove(rubro);
//                cuentaOld = em.merge(cuentaOld);
//            }
//            if (cuentaNew != null && !cuentaNew.equals(cuentaOld)) {
//                cuentaNew.getRubroListCuenta().add(rubro);
//                cuentaNew = em.merge(cuentaNew);
//            }
//            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
//                vigenciaOld.getRubroList().remove(rubro);
//                vigenciaOld = em.merge(vigenciaOld);
//            }
//            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
//                vigenciaNew.getRubroList().add(rubro);
//                vigenciaNew = em.merge(vigenciaNew);
//            }
//            for (Presupuesto presupuestoListOldPresupuesto : presupuestoListOld) {
//                if (!presupuestoListNew.contains(presupuestoListOldPresupuesto)) {
//                    presupuestoListOldPresupuesto.setRubro(null);
//                    presupuestoListOldPresupuesto = em.merge(presupuestoListOldPresupuesto);
//                }
//            }
//            for (Presupuesto presupuestoListNewPresupuesto : presupuestoListNew) {
//                if (!presupuestoListOld.contains(presupuestoListNewPresupuesto)) {
//                    Rubro oldRubroOfPresupuestoListNewPresupuesto = presupuestoListNewPresupuesto.getRubro();
//                    presupuestoListNewPresupuesto.setRubro(rubro);
//                    presupuestoListNewPresupuesto = em.merge(presupuestoListNewPresupuesto);
//                    if (oldRubroOfPresupuestoListNewPresupuesto != null && !oldRubroOfPresupuestoListNewPresupuesto.equals(rubro)) {
//                        oldRubroOfPresupuestoListNewPresupuesto.getPresupuestoList().remove(presupuestoListNewPresupuesto);
//                        oldRubroOfPresupuestoListNewPresupuesto = em.merge(oldRubroOfPresupuestoListNewPresupuesto);
//                    }
//                }
//            }
//            for (AdicionRubro adicionRubroListOldAdicionRubro : adicionRubroListOld) {
//                if (!adicionRubroListNew.contains(adicionRubroListOldAdicionRubro)) {
//                    adicionRubroListOldAdicionRubro.setRubro(null);
//                    adicionRubroListOldAdicionRubro = em.merge(adicionRubroListOldAdicionRubro);
//                }
//            }
//            for (AdicionRubro adicionRubroListNewAdicionRubro : adicionRubroListNew) {
//                if (!adicionRubroListOld.contains(adicionRubroListNewAdicionRubro)) {
//                    Rubro oldRubroOfAdicionRubroListNewAdicionRubro = adicionRubroListNewAdicionRubro.getRubro();
//                    adicionRubroListNewAdicionRubro.setRubro(rubro);
//                    adicionRubroListNewAdicionRubro = em.merge(adicionRubroListNewAdicionRubro);
//                    if (oldRubroOfAdicionRubroListNewAdicionRubro != null && !oldRubroOfAdicionRubroListNewAdicionRubro.equals(rubro)) {
//                        oldRubroOfAdicionRubroListNewAdicionRubro.getAdicionRubroList().remove(adicionRubroListNewAdicionRubro);
//                        oldRubroOfAdicionRubroListNewAdicionRubro = em.merge(oldRubroOfAdicionRubroListNewAdicionRubro);
//                    }
//                }
//            }
//            for (Ejecucion ejecucionListOldEjecucion : ejecucionListOld) {
//                if (!ejecucionListNew.contains(ejecucionListOldEjecucion)) {
//                    ejecucionListOldEjecucion.setRubro(null);
//                    ejecucionListOldEjecucion = em.merge(ejecucionListOldEjecucion);
//                }
//            }
//            for (Ejecucion ejecucionListNewEjecucion : ejecucionListNew) {
//                if (!ejecucionListOld.contains(ejecucionListNewEjecucion)) {
//                    Rubro oldRubroOfEjecucionListNewEjecucion = ejecucionListNewEjecucion.getRubro();
//                    ejecucionListNewEjecucion.setRubro(rubro);
//                    ejecucionListNewEjecucion = em.merge(ejecucionListNewEjecucion);
//                    if (oldRubroOfEjecucionListNewEjecucion != null && !oldRubroOfEjecucionListNewEjecucion.equals(rubro)) {
//                        oldRubroOfEjecucionListNewEjecucion.getEjecucionList().remove(ejecucionListNewEjecucion);
//                        oldRubroOfEjecucionListNewEjecucion = em.merge(oldRubroOfEjecucionListNewEjecucion);
//                    }
//                }
//            }
//            for (DisponibilidadRubro disponibilidadRubroListOldDisponibilidadRubro : disponibilidadRubroListOld) {
//                if (!disponibilidadRubroListNew.contains(disponibilidadRubroListOldDisponibilidadRubro)) {
//                    disponibilidadRubroListOldDisponibilidadRubro.setRubro(null);
//                    disponibilidadRubroListOldDisponibilidadRubro = em.merge(disponibilidadRubroListOldDisponibilidadRubro);
//                }
//            }
//            for (DisponibilidadRubro disponibilidadRubroListNewDisponibilidadRubro : disponibilidadRubroListNew) {
//                if (!disponibilidadRubroListOld.contains(disponibilidadRubroListNewDisponibilidadRubro)) {
//                    Rubro oldRubroOfDisponibilidadRubroListNewDisponibilidadRubro = disponibilidadRubroListNewDisponibilidadRubro.getRubro();
//                    disponibilidadRubroListNewDisponibilidadRubro.setRubro(rubro);
//                    disponibilidadRubroListNewDisponibilidadRubro = em.merge(disponibilidadRubroListNewDisponibilidadRubro);
//                    if (oldRubroOfDisponibilidadRubroListNewDisponibilidadRubro != null && !oldRubroOfDisponibilidadRubroListNewDisponibilidadRubro.equals(rubro)) {
//                        oldRubroOfDisponibilidadRubroListNewDisponibilidadRubro.getDisponibilidadRubroList().remove(disponibilidadRubroListNewDisponibilidadRubro);
//                        oldRubroOfDisponibilidadRubroListNewDisponibilidadRubro = em.merge(oldRubroOfDisponibilidadRubroListNewDisponibilidadRubro);
//                    }
//                }
//            }
//            for (Rubro rubroListCuentaOldRubro : rubroListCuentaOld) {
//                if (!rubroListCuentaNew.contains(rubroListCuentaOldRubro)) {
//                    rubroListCuentaOldRubro.setSubcuenta(null);
//                    rubroListCuentaOldRubro = em.merge(rubroListCuentaOldRubro);
//                }
//            }
//            for (Rubro rubroListCuentaNewRubro : rubroListCuentaNew) {
//                if (!rubroListCuentaOld.contains(rubroListCuentaNewRubro)) {
//                    Rubro oldSubcuentaOfRubroListCuentaNewRubro = rubroListCuentaNewRubro.getSubcuenta();
//                    rubroListCuentaNewRubro.setSubcuenta(rubro);
//                    rubroListCuentaNewRubro = em.merge(rubroListCuentaNewRubro);
//                    if (oldSubcuentaOfRubroListCuentaNewRubro != null && !oldSubcuentaOfRubroListCuentaNewRubro.equals(rubro)) {
//                        oldSubcuentaOfRubroListCuentaNewRubro.getRubroListCuenta().remove(rubroListCuentaNewRubro);
//                        oldSubcuentaOfRubroListCuentaNewRubro = em.merge(oldSubcuentaOfRubroListCuentaNewRubro);
//                    }
//                }
//            }
//            for (Rubro rubroListSubcuentaOldRubro : rubroListSubcuentaOld) {
//                if (!rubroListSubcuentaNew.contains(rubroListSubcuentaOldRubro)) {
//                    rubroListSubcuentaOldRubro.setCuenta(null);
//                    rubroListSubcuentaOldRubro = em.merge(rubroListSubcuentaOldRubro);
//                }
//            }
//            for (Rubro rubroListSubcuentaNewRubro : rubroListSubcuentaNew) {
//                if (!rubroListSubcuentaOld.contains(rubroListSubcuentaNewRubro)) {
//                    Rubro oldCuentaOfRubroListSubcuentaNewRubro = rubroListSubcuentaNewRubro.getCuenta();
//                    rubroListSubcuentaNewRubro.setCuenta(rubro);
//                    rubroListSubcuentaNewRubro = em.merge(rubroListSubcuentaNewRubro);
//                    if (oldCuentaOfRubroListSubcuentaNewRubro != null && !oldCuentaOfRubroListSubcuentaNewRubro.equals(rubro)) {
//                        oldCuentaOfRubroListSubcuentaNewRubro.getRubroListSubcuenta().remove(rubroListSubcuentaNewRubro);
//                        oldCuentaOfRubroListSubcuentaNewRubro = em.merge(oldCuentaOfRubroListSubcuentaNewRubro);
//                    }
//                }
//            }
//            for (TrasladoRubro trasladoRubroListOldTrasladoRubro : trasladoRubroListOld) {
//                if (!trasladoRubroListNew.contains(trasladoRubroListOldTrasladoRubro)) {
//                    trasladoRubroListOldTrasladoRubro.setRubro(null);
//                    trasladoRubroListOldTrasladoRubro = em.merge(trasladoRubroListOldTrasladoRubro);
//                }
//            }
//            for (TrasladoRubro trasladoRubroListNewTrasladoRubro : trasladoRubroListNew) {
//                if (!trasladoRubroListOld.contains(trasladoRubroListNewTrasladoRubro)) {
//                    Rubro oldRubroOfTrasladoRubroListNewTrasladoRubro = trasladoRubroListNewTrasladoRubro.getRubro();
//                    trasladoRubroListNewTrasladoRubro.setRubro(rubro);
//                    trasladoRubroListNewTrasladoRubro = em.merge(trasladoRubroListNewTrasladoRubro);
//                    if (oldRubroOfTrasladoRubroListNewTrasladoRubro != null && !oldRubroOfTrasladoRubroListNewTrasladoRubro.equals(rubro)) {
//                        oldRubroOfTrasladoRubroListNewTrasladoRubro.getTrasladoRubroList().remove(trasladoRubroListNewTrasladoRubro);
//                        oldRubroOfTrasladoRubroListNewTrasladoRubro = em.merge(oldRubroOfTrasladoRubroListNewTrasladoRubro);
//                    }
//                }
//            }
//            em.getTransaction().commit();
//        } catch (Exception ex) {
//            String msg = ex.getLocalizedMessage();
//            if (msg == null || msg.length() == 0) {
//                BigDecimal id = rubro.getIdRubro();
//                if (findRubro(id) == null) {
//                    throw new NonexistentEntityException("The rubro with id " + id + " no longer exists.");
//                }
//            }
//            throw ex;
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    public void destroy(Rubro rubro) throws NonexistentEntityException {
        BigDecimal id = rubro.getIdRubro();
        
        if (validarEliminarRubro(rubro)) {
            entityManager = null;
            try {
                entityManager = getEntityManager();
                entityManager.getTransaction().begin();
               
                try {
                    //rubro = em.getReference(Rubro.class, id);
                    rubro.getIdRubro();
                } catch (EntityNotFoundException enfe) {
                    throw new NonexistentEntityException("The rubro with id " + id + " no longer exists.", enfe);
                }
                TipoRubro tipoRubro = rubro.getTipoRubro();
                if (tipoRubro != null) {
                    tipoRubro.getRubroList().remove(rubro);
                    tipoRubro = entityManager.merge(tipoRubro);
                }
                Rubro subcuenta = rubro.getSubcuenta();
                if (subcuenta != null) {
                    subcuenta.getRubroList().remove(rubro);
                    subcuenta = entityManager.merge(subcuenta);
                }
                Rubro cuenta = rubro.getCuenta();
                if (cuenta != null) {
                    cuenta.getRubroList().remove(rubro);
                    cuenta = entityManager.merge(cuenta);
                }
                Vigencia vigencia = rubro.getVigencia();
                if (vigencia != null) {
                    vigencia.getRubroList().remove(rubro);
                    vigencia = entityManager.merge(vigencia);
                }
                /*
                List<Presupuesto> presupuestoList = rubro.getPresupuestoList();
                for (Presupuesto presupuestoListPresupuesto : presupuestoList) {
                    rubro.getPresupuestoList().remove(presupuestoListPresupuesto);
                    rubro = em.merge(rubro);
                }
                List<AdicionRubro> adicionRubroList = rubro.getAdicionRubroList();
                for (AdicionRubro adicionRubroListAdicionRubro : adicionRubroList) {
                    adicionRubroListAdicionRubro.setRubro(null);
                    adicionRubroListAdicionRubro = em.merge(adicionRubroListAdicionRubro);
                }
                List<Ejecucion> ejecucionList = rubro.getEjecucionList();
                for (Ejecucion ejecucionListEjecucion : ejecucionList) {
                    ejecucionListEjecucion.setRubro(null);
                    ejecucionListEjecucion = em.merge(ejecucionListEjecucion);
                }
                List<DisponibilidadRubro> disponibilidadRubroList = rubro.getDisponibilidadRubroList();
                for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubro : disponibilidadRubroList) {
                    disponibilidadRubroListDisponibilidadRubro.setRubro(null);
                    disponibilidadRubroListDisponibilidadRubro = em.merge(disponibilidadRubroListDisponibilidadRubro);
                }
                List<Rubro> rubroListCuenta = rubro.getRubroListCuenta();
                for (Rubro rubroListCuentaRubro : rubroListCuenta) {
                    rubroListCuentaRubro.setSubcuenta(null);
                    rubroListCuentaRubro = em.merge(rubroListCuentaRubro);
                }
                List<Rubro> rubroListSubcuenta = rubro.getRubroListSubcuenta();
                for (Rubro rubroListSubcuentaRubro : rubroListSubcuenta) {
                    rubroListSubcuentaRubro.setCuenta(null);
                    rubroListSubcuentaRubro = em.merge(rubroListSubcuentaRubro);
                }
                List<TrasladoRubro> trasladoRubroList = rubro.getTrasladoRubroList();
                for (TrasladoRubro trasladoRubroListTrasladoRubro : trasladoRubroList) {
                    trasladoRubroListTrasladoRubro.setRubro(null);
                    trasladoRubroListTrasladoRubro = em.merge(trasladoRubroListTrasladoRubro);
                }*/
                entityManager.remove(entityManager.contains(rubro) ? rubro : entityManager.merge(rubro));
                entityManager.getTransaction().commit();
            } finally {
                if (entityManager != null) {
                    entityManager.close();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "El rubro a eliminar tiene relacion dentro del presupuesto. Imposible eliminar", "Eliminar Rubro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Boolean validarEliminarRubro(Rubro rubro) throws NonexistentEntityException {
        Boolean validacionExitosa = true;

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            try {
                //rubro = em.getReference(Rubro.class, id);
                rubro.getIdRubro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rubro with id " + rubro.getIdRubro() + " no longer exists.", enfe);
            }

            if (!rubro.getRubroList().isEmpty() || !rubro.getRubroList1().isEmpty() || !rubro.getPresupuestoList().isEmpty() || !rubro.getDisponibilidadRubroList().isEmpty() || !rubro.getAdicionRubroList().isEmpty() || !rubro.getEjecucionList().isEmpty() || !rubro.getTrasladoRubroList().isEmpty()) {
                validacionExitosa = false;
            }

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return validacionExitosa;
    }

    public List<Rubro> findRubroEntities() {
        return findRubroEntities(true, -1, -1);
    }

    public List<Rubro> findRubroEntities(int maxResults, int firstResult) {
        return findRubroEntities(false, maxResults, firstResult);
    }

    private List<Rubro> findRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rubro.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Rubro findRubro(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rubro> rt = cq.from(Rubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
