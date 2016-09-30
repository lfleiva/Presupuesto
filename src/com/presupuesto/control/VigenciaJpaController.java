/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.control.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.Presupuesto;
import java.util.ArrayList;
import java.util.List;
import com.presupuesto.modelo.AdicionRubro;
import com.presupuesto.modelo.Traslado;
import com.presupuesto.modelo.Ejecucion;
import com.presupuesto.modelo.DisponibilidadRubro;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.Adicion;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.TrasladoRubro;
import com.presupuesto.modelo.Vigencia;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis Fernando Leiva
 */
public class VigenciaJpaController implements Serializable {

    public VigenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vigencia vigencia) throws PreexistingEntityException, Exception {
        if (vigencia.getPresupuestoList() == null) {
            vigencia.setPresupuestoList(new ArrayList<Presupuesto>());
        }
        if (vigencia.getAdicionRubroList() == null) {
            vigencia.setAdicionRubroList(new ArrayList<AdicionRubro>());
        }
        if (vigencia.getTrasladoList() == null) {
            vigencia.setTrasladoList(new ArrayList<Traslado>());
        }
        if (vigencia.getEjecucionList() == null) {
            vigencia.setEjecucionList(new ArrayList<Ejecucion>());
        }
        if (vigencia.getDisponibilidadRubroList() == null) {
            vigencia.setDisponibilidadRubroList(new ArrayList<DisponibilidadRubro>());
        }
        if (vigencia.getDisponibilidadList() == null) {
            vigencia.setDisponibilidadList(new ArrayList<Disponibilidad>());
        }
        if (vigencia.getAdicionList() == null) {
            vigencia.setAdicionList(new ArrayList<Adicion>());
        }
        if (vigencia.getRubroList() == null) {
            vigencia.setRubroList(new ArrayList<Rubro>());
        }
        if (vigencia.getTrasladoRubroList() == null) {
            vigencia.setTrasladoRubroList(new ArrayList<TrasladoRubro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Presupuesto> attachedPresupuestoList = new ArrayList<Presupuesto>();
            for (Presupuesto presupuestoListPresupuestoToAttach : vigencia.getPresupuestoList()) {
                presupuestoListPresupuestoToAttach = em.getReference(presupuestoListPresupuestoToAttach.getClass(), presupuestoListPresupuestoToAttach.getIdPresupuesto());
                attachedPresupuestoList.add(presupuestoListPresupuestoToAttach);
            }
            vigencia.setPresupuestoList(attachedPresupuestoList);
            List<AdicionRubro> attachedAdicionRubroList = new ArrayList<AdicionRubro>();
            for (AdicionRubro adicionRubroListAdicionRubroToAttach : vigencia.getAdicionRubroList()) {
                adicionRubroListAdicionRubroToAttach = em.getReference(adicionRubroListAdicionRubroToAttach.getClass(), adicionRubroListAdicionRubroToAttach.getIdAdicionRubro());
                attachedAdicionRubroList.add(adicionRubroListAdicionRubroToAttach);
            }
            vigencia.setAdicionRubroList(attachedAdicionRubroList);
            List<Traslado> attachedTrasladoList = new ArrayList<Traslado>();
            for (Traslado trasladoListTrasladoToAttach : vigencia.getTrasladoList()) {
                trasladoListTrasladoToAttach = em.getReference(trasladoListTrasladoToAttach.getClass(), trasladoListTrasladoToAttach.getIdTraslado());
                attachedTrasladoList.add(trasladoListTrasladoToAttach);
            }
            vigencia.setTrasladoList(attachedTrasladoList);
            List<Ejecucion> attachedEjecucionList = new ArrayList<Ejecucion>();
            for (Ejecucion ejecucionListEjecucionToAttach : vigencia.getEjecucionList()) {
                ejecucionListEjecucionToAttach = em.getReference(ejecucionListEjecucionToAttach.getClass(), ejecucionListEjecucionToAttach.getIdEjecucion());
                attachedEjecucionList.add(ejecucionListEjecucionToAttach);
            }
            vigencia.setEjecucionList(attachedEjecucionList);
            List<DisponibilidadRubro> attachedDisponibilidadRubroList = new ArrayList<DisponibilidadRubro>();
            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubroToAttach : vigencia.getDisponibilidadRubroList()) {
                disponibilidadRubroListDisponibilidadRubroToAttach = em.getReference(disponibilidadRubroListDisponibilidadRubroToAttach.getClass(), disponibilidadRubroListDisponibilidadRubroToAttach.getIdDisponibilidadRubro());
                attachedDisponibilidadRubroList.add(disponibilidadRubroListDisponibilidadRubroToAttach);
            }
            vigencia.setDisponibilidadRubroList(attachedDisponibilidadRubroList);
            List<Disponibilidad> attachedDisponibilidadList = new ArrayList<Disponibilidad>();
            for (Disponibilidad disponibilidadListDisponibilidadToAttach : vigencia.getDisponibilidadList()) {
                disponibilidadListDisponibilidadToAttach = em.getReference(disponibilidadListDisponibilidadToAttach.getClass(), disponibilidadListDisponibilidadToAttach.getIdDisponibilidad());
                attachedDisponibilidadList.add(disponibilidadListDisponibilidadToAttach);
            }
            vigencia.setDisponibilidadList(attachedDisponibilidadList);
            List<Adicion> attachedAdicionList = new ArrayList<Adicion>();
            for (Adicion adicionListAdicionToAttach : vigencia.getAdicionList()) {
                adicionListAdicionToAttach = em.getReference(adicionListAdicionToAttach.getClass(), adicionListAdicionToAttach.getIdAdicion());
                attachedAdicionList.add(adicionListAdicionToAttach);
            }
            vigencia.setAdicionList(attachedAdicionList);
            List<Rubro> attachedRubroList = new ArrayList<Rubro>();
            for (Rubro rubroListRubroToAttach : vigencia.getRubroList()) {
                rubroListRubroToAttach = em.getReference(rubroListRubroToAttach.getClass(), rubroListRubroToAttach.getIdRubro());
                attachedRubroList.add(rubroListRubroToAttach);
            }
            vigencia.setRubroList(attachedRubroList);
            List<TrasladoRubro> attachedTrasladoRubroList = new ArrayList<TrasladoRubro>();
            for (TrasladoRubro trasladoRubroListTrasladoRubroToAttach : vigencia.getTrasladoRubroList()) {
                trasladoRubroListTrasladoRubroToAttach = em.getReference(trasladoRubroListTrasladoRubroToAttach.getClass(), trasladoRubroListTrasladoRubroToAttach.getIdTrasladoRubro());
                attachedTrasladoRubroList.add(trasladoRubroListTrasladoRubroToAttach);
            }
            vigencia.setTrasladoRubroList(attachedTrasladoRubroList);
            em.persist(vigencia);
            for (Presupuesto presupuestoListPresupuesto : vigencia.getPresupuestoList()) {
                Vigencia oldVigenciaOfPresupuestoListPresupuesto = presupuestoListPresupuesto.getVigencia();
                presupuestoListPresupuesto.setVigencia(vigencia);
                presupuestoListPresupuesto = em.merge(presupuestoListPresupuesto);
                if (oldVigenciaOfPresupuestoListPresupuesto != null) {
                    oldVigenciaOfPresupuestoListPresupuesto.getPresupuestoList().remove(presupuestoListPresupuesto);
                    oldVigenciaOfPresupuestoListPresupuesto = em.merge(oldVigenciaOfPresupuestoListPresupuesto);
                }
            }
            for (AdicionRubro adicionRubroListAdicionRubro : vigencia.getAdicionRubroList()) {
                Vigencia oldVigenciaOfAdicionRubroListAdicionRubro = adicionRubroListAdicionRubro.getVigencia();
                adicionRubroListAdicionRubro.setVigencia(vigencia);
                adicionRubroListAdicionRubro = em.merge(adicionRubroListAdicionRubro);
                if (oldVigenciaOfAdicionRubroListAdicionRubro != null) {
                    oldVigenciaOfAdicionRubroListAdicionRubro.getAdicionRubroList().remove(adicionRubroListAdicionRubro);
                    oldVigenciaOfAdicionRubroListAdicionRubro = em.merge(oldVigenciaOfAdicionRubroListAdicionRubro);
                }
            }
            for (Traslado trasladoListTraslado : vigencia.getTrasladoList()) {
                Vigencia oldVigenciaOfTrasladoListTraslado = trasladoListTraslado.getVigencia();
                trasladoListTraslado.setVigencia(vigencia);
                trasladoListTraslado = em.merge(trasladoListTraslado);
                if (oldVigenciaOfTrasladoListTraslado != null) {
                    oldVigenciaOfTrasladoListTraslado.getTrasladoList().remove(trasladoListTraslado);
                    oldVigenciaOfTrasladoListTraslado = em.merge(oldVigenciaOfTrasladoListTraslado);
                }
            }
            for (Ejecucion ejecucionListEjecucion : vigencia.getEjecucionList()) {
                Vigencia oldVigenciaOfEjecucionListEjecucion = ejecucionListEjecucion.getVigencia();
                ejecucionListEjecucion.setVigencia(vigencia);
                ejecucionListEjecucion = em.merge(ejecucionListEjecucion);
                if (oldVigenciaOfEjecucionListEjecucion != null) {
                    oldVigenciaOfEjecucionListEjecucion.getEjecucionList().remove(ejecucionListEjecucion);
                    oldVigenciaOfEjecucionListEjecucion = em.merge(oldVigenciaOfEjecucionListEjecucion);
                }
            }
            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubro : vigencia.getDisponibilidadRubroList()) {
                Vigencia oldVigenciaOfDisponibilidadRubroListDisponibilidadRubro = disponibilidadRubroListDisponibilidadRubro.getVigencia();
                disponibilidadRubroListDisponibilidadRubro.setVigencia(vigencia);
                disponibilidadRubroListDisponibilidadRubro = em.merge(disponibilidadRubroListDisponibilidadRubro);
                if (oldVigenciaOfDisponibilidadRubroListDisponibilidadRubro != null) {
                    oldVigenciaOfDisponibilidadRubroListDisponibilidadRubro.getDisponibilidadRubroList().remove(disponibilidadRubroListDisponibilidadRubro);
                    oldVigenciaOfDisponibilidadRubroListDisponibilidadRubro = em.merge(oldVigenciaOfDisponibilidadRubroListDisponibilidadRubro);
                }
            }
            for (Disponibilidad disponibilidadListDisponibilidad : vigencia.getDisponibilidadList()) {
                Vigencia oldVigenciaOfDisponibilidadListDisponibilidad = disponibilidadListDisponibilidad.getVigencia();
                disponibilidadListDisponibilidad.setVigencia(vigencia);
                disponibilidadListDisponibilidad = em.merge(disponibilidadListDisponibilidad);
                if (oldVigenciaOfDisponibilidadListDisponibilidad != null) {
                    oldVigenciaOfDisponibilidadListDisponibilidad.getDisponibilidadList().remove(disponibilidadListDisponibilidad);
                    oldVigenciaOfDisponibilidadListDisponibilidad = em.merge(oldVigenciaOfDisponibilidadListDisponibilidad);
                }
            }
            for (Adicion adicionListAdicion : vigencia.getAdicionList()) {
                Vigencia oldVigenciaOfAdicionListAdicion = adicionListAdicion.getVigencia();
                adicionListAdicion.setVigencia(vigencia);
                adicionListAdicion = em.merge(adicionListAdicion);
                if (oldVigenciaOfAdicionListAdicion != null) {
                    oldVigenciaOfAdicionListAdicion.getAdicionList().remove(adicionListAdicion);
                    oldVigenciaOfAdicionListAdicion = em.merge(oldVigenciaOfAdicionListAdicion);
                }
            }
            for (Rubro rubroListRubro : vigencia.getRubroList()) {
                Vigencia oldVigenciaOfRubroListRubro = rubroListRubro.getVigencia();
                rubroListRubro.setVigencia(vigencia);
                rubroListRubro = em.merge(rubroListRubro);
                if (oldVigenciaOfRubroListRubro != null) {
                    oldVigenciaOfRubroListRubro.getRubroList().remove(rubroListRubro);
                    oldVigenciaOfRubroListRubro = em.merge(oldVigenciaOfRubroListRubro);
                }
            }
            for (TrasladoRubro trasladoRubroListTrasladoRubro : vigencia.getTrasladoRubroList()) {
                Vigencia oldVigenciaOfTrasladoRubroListTrasladoRubro = trasladoRubroListTrasladoRubro.getVigencia();
                trasladoRubroListTrasladoRubro.setVigencia(vigencia);
                trasladoRubroListTrasladoRubro = em.merge(trasladoRubroListTrasladoRubro);
                if (oldVigenciaOfTrasladoRubroListTrasladoRubro != null) {
                    oldVigenciaOfTrasladoRubroListTrasladoRubro.getTrasladoRubroList().remove(trasladoRubroListTrasladoRubro);
                    oldVigenciaOfTrasladoRubroListTrasladoRubro = em.merge(oldVigenciaOfTrasladoRubroListTrasladoRubro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVigencia(vigencia.getIdVigencia()) != null) {
                throw new PreexistingEntityException("Vigencia " + vigencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vigencia vigencia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vigencia persistentVigencia = em.find(Vigencia.class, vigencia.getIdVigencia());
            List<Presupuesto> presupuestoListOld = persistentVigencia.getPresupuestoList();
            List<Presupuesto> presupuestoListNew = vigencia.getPresupuestoList();
            List<AdicionRubro> adicionRubroListOld = persistentVigencia.getAdicionRubroList();
            List<AdicionRubro> adicionRubroListNew = vigencia.getAdicionRubroList();
            List<Traslado> trasladoListOld = persistentVigencia.getTrasladoList();
            List<Traslado> trasladoListNew = vigencia.getTrasladoList();
            List<Ejecucion> ejecucionListOld = persistentVigencia.getEjecucionList();
            List<Ejecucion> ejecucionListNew = vigencia.getEjecucionList();
            List<DisponibilidadRubro> disponibilidadRubroListOld = persistentVigencia.getDisponibilidadRubroList();
            List<DisponibilidadRubro> disponibilidadRubroListNew = vigencia.getDisponibilidadRubroList();
            List<Disponibilidad> disponibilidadListOld = persistentVigencia.getDisponibilidadList();
            List<Disponibilidad> disponibilidadListNew = vigencia.getDisponibilidadList();
            List<Adicion> adicionListOld = persistentVigencia.getAdicionList();
            List<Adicion> adicionListNew = vigencia.getAdicionList();
            List<Rubro> rubroListOld = persistentVigencia.getRubroList();
            List<Rubro> rubroListNew = vigencia.getRubroList();
            List<TrasladoRubro> trasladoRubroListOld = persistentVigencia.getTrasladoRubroList();
            List<TrasladoRubro> trasladoRubroListNew = vigencia.getTrasladoRubroList();
            List<Presupuesto> attachedPresupuestoListNew = new ArrayList<Presupuesto>();
            for (Presupuesto presupuestoListNewPresupuestoToAttach : presupuestoListNew) {
                presupuestoListNewPresupuestoToAttach = em.getReference(presupuestoListNewPresupuestoToAttach.getClass(), presupuestoListNewPresupuestoToAttach.getIdPresupuesto());
                attachedPresupuestoListNew.add(presupuestoListNewPresupuestoToAttach);
            }
            presupuestoListNew = attachedPresupuestoListNew;
            vigencia.setPresupuestoList(presupuestoListNew);
            List<AdicionRubro> attachedAdicionRubroListNew = new ArrayList<AdicionRubro>();
            for (AdicionRubro adicionRubroListNewAdicionRubroToAttach : adicionRubroListNew) {
                adicionRubroListNewAdicionRubroToAttach = em.getReference(adicionRubroListNewAdicionRubroToAttach.getClass(), adicionRubroListNewAdicionRubroToAttach.getIdAdicionRubro());
                attachedAdicionRubroListNew.add(adicionRubroListNewAdicionRubroToAttach);
            }
            adicionRubroListNew = attachedAdicionRubroListNew;
            vigencia.setAdicionRubroList(adicionRubroListNew);
            List<Traslado> attachedTrasladoListNew = new ArrayList<Traslado>();
            for (Traslado trasladoListNewTrasladoToAttach : trasladoListNew) {
                trasladoListNewTrasladoToAttach = em.getReference(trasladoListNewTrasladoToAttach.getClass(), trasladoListNewTrasladoToAttach.getIdTraslado());
                attachedTrasladoListNew.add(trasladoListNewTrasladoToAttach);
            }
            trasladoListNew = attachedTrasladoListNew;
            vigencia.setTrasladoList(trasladoListNew);
            List<Ejecucion> attachedEjecucionListNew = new ArrayList<Ejecucion>();
            for (Ejecucion ejecucionListNewEjecucionToAttach : ejecucionListNew) {
                ejecucionListNewEjecucionToAttach = em.getReference(ejecucionListNewEjecucionToAttach.getClass(), ejecucionListNewEjecucionToAttach.getIdEjecucion());
                attachedEjecucionListNew.add(ejecucionListNewEjecucionToAttach);
            }
            ejecucionListNew = attachedEjecucionListNew;
            vigencia.setEjecucionList(ejecucionListNew);
            List<DisponibilidadRubro> attachedDisponibilidadRubroListNew = new ArrayList<DisponibilidadRubro>();
            for (DisponibilidadRubro disponibilidadRubroListNewDisponibilidadRubroToAttach : disponibilidadRubroListNew) {
                disponibilidadRubroListNewDisponibilidadRubroToAttach = em.getReference(disponibilidadRubroListNewDisponibilidadRubroToAttach.getClass(), disponibilidadRubroListNewDisponibilidadRubroToAttach.getIdDisponibilidadRubro());
                attachedDisponibilidadRubroListNew.add(disponibilidadRubroListNewDisponibilidadRubroToAttach);
            }
            disponibilidadRubroListNew = attachedDisponibilidadRubroListNew;
            vigencia.setDisponibilidadRubroList(disponibilidadRubroListNew);
            List<Disponibilidad> attachedDisponibilidadListNew = new ArrayList<Disponibilidad>();
            for (Disponibilidad disponibilidadListNewDisponibilidadToAttach : disponibilidadListNew) {
                disponibilidadListNewDisponibilidadToAttach = em.getReference(disponibilidadListNewDisponibilidadToAttach.getClass(), disponibilidadListNewDisponibilidadToAttach.getIdDisponibilidad());
                attachedDisponibilidadListNew.add(disponibilidadListNewDisponibilidadToAttach);
            }
            disponibilidadListNew = attachedDisponibilidadListNew;
            vigencia.setDisponibilidadList(disponibilidadListNew);
            List<Adicion> attachedAdicionListNew = new ArrayList<Adicion>();
            for (Adicion adicionListNewAdicionToAttach : adicionListNew) {
                adicionListNewAdicionToAttach = em.getReference(adicionListNewAdicionToAttach.getClass(), adicionListNewAdicionToAttach.getIdAdicion());
                attachedAdicionListNew.add(adicionListNewAdicionToAttach);
            }
            adicionListNew = attachedAdicionListNew;
            vigencia.setAdicionList(adicionListNew);
            List<Rubro> attachedRubroListNew = new ArrayList<Rubro>();
            for (Rubro rubroListNewRubroToAttach : rubroListNew) {
                rubroListNewRubroToAttach = em.getReference(rubroListNewRubroToAttach.getClass(), rubroListNewRubroToAttach.getIdRubro());
                attachedRubroListNew.add(rubroListNewRubroToAttach);
            }
            rubroListNew = attachedRubroListNew;
            vigencia.setRubroList(rubroListNew);
            List<TrasladoRubro> attachedTrasladoRubroListNew = new ArrayList<TrasladoRubro>();
            for (TrasladoRubro trasladoRubroListNewTrasladoRubroToAttach : trasladoRubroListNew) {
                trasladoRubroListNewTrasladoRubroToAttach = em.getReference(trasladoRubroListNewTrasladoRubroToAttach.getClass(), trasladoRubroListNewTrasladoRubroToAttach.getIdTrasladoRubro());
                attachedTrasladoRubroListNew.add(trasladoRubroListNewTrasladoRubroToAttach);
            }
            trasladoRubroListNew = attachedTrasladoRubroListNew;
            vigencia.setTrasladoRubroList(trasladoRubroListNew);
            vigencia = em.merge(vigencia);
            for (Presupuesto presupuestoListOldPresupuesto : presupuestoListOld) {
                if (!presupuestoListNew.contains(presupuestoListOldPresupuesto)) {
                    presupuestoListOldPresupuesto.setVigencia(null);
                    presupuestoListOldPresupuesto = em.merge(presupuestoListOldPresupuesto);
                }
            }
            for (Presupuesto presupuestoListNewPresupuesto : presupuestoListNew) {
                if (!presupuestoListOld.contains(presupuestoListNewPresupuesto)) {
                    Vigencia oldVigenciaOfPresupuestoListNewPresupuesto = presupuestoListNewPresupuesto.getVigencia();
                    presupuestoListNewPresupuesto.setVigencia(vigencia);
                    presupuestoListNewPresupuesto = em.merge(presupuestoListNewPresupuesto);
                    if (oldVigenciaOfPresupuestoListNewPresupuesto != null && !oldVigenciaOfPresupuestoListNewPresupuesto.equals(vigencia)) {
                        oldVigenciaOfPresupuestoListNewPresupuesto.getPresupuestoList().remove(presupuestoListNewPresupuesto);
                        oldVigenciaOfPresupuestoListNewPresupuesto = em.merge(oldVigenciaOfPresupuestoListNewPresupuesto);
                    }
                }
            }
            for (AdicionRubro adicionRubroListOldAdicionRubro : adicionRubroListOld) {
                if (!adicionRubroListNew.contains(adicionRubroListOldAdicionRubro)) {
                    adicionRubroListOldAdicionRubro.setVigencia(null);
                    adicionRubroListOldAdicionRubro = em.merge(adicionRubroListOldAdicionRubro);
                }
            }
            for (AdicionRubro adicionRubroListNewAdicionRubro : adicionRubroListNew) {
                if (!adicionRubroListOld.contains(adicionRubroListNewAdicionRubro)) {
                    Vigencia oldVigenciaOfAdicionRubroListNewAdicionRubro = adicionRubroListNewAdicionRubro.getVigencia();
                    adicionRubroListNewAdicionRubro.setVigencia(vigencia);
                    adicionRubroListNewAdicionRubro = em.merge(adicionRubroListNewAdicionRubro);
                    if (oldVigenciaOfAdicionRubroListNewAdicionRubro != null && !oldVigenciaOfAdicionRubroListNewAdicionRubro.equals(vigencia)) {
                        oldVigenciaOfAdicionRubroListNewAdicionRubro.getAdicionRubroList().remove(adicionRubroListNewAdicionRubro);
                        oldVigenciaOfAdicionRubroListNewAdicionRubro = em.merge(oldVigenciaOfAdicionRubroListNewAdicionRubro);
                    }
                }
            }
            for (Traslado trasladoListOldTraslado : trasladoListOld) {
                if (!trasladoListNew.contains(trasladoListOldTraslado)) {
                    trasladoListOldTraslado.setVigencia(null);
                    trasladoListOldTraslado = em.merge(trasladoListOldTraslado);
                }
            }
            for (Traslado trasladoListNewTraslado : trasladoListNew) {
                if (!trasladoListOld.contains(trasladoListNewTraslado)) {
                    Vigencia oldVigenciaOfTrasladoListNewTraslado = trasladoListNewTraslado.getVigencia();
                    trasladoListNewTraslado.setVigencia(vigencia);
                    trasladoListNewTraslado = em.merge(trasladoListNewTraslado);
                    if (oldVigenciaOfTrasladoListNewTraslado != null && !oldVigenciaOfTrasladoListNewTraslado.equals(vigencia)) {
                        oldVigenciaOfTrasladoListNewTraslado.getTrasladoList().remove(trasladoListNewTraslado);
                        oldVigenciaOfTrasladoListNewTraslado = em.merge(oldVigenciaOfTrasladoListNewTraslado);
                    }
                }
            }
            for (Ejecucion ejecucionListOldEjecucion : ejecucionListOld) {
                if (!ejecucionListNew.contains(ejecucionListOldEjecucion)) {
                    ejecucionListOldEjecucion.setVigencia(null);
                    ejecucionListOldEjecucion = em.merge(ejecucionListOldEjecucion);
                }
            }
            for (Ejecucion ejecucionListNewEjecucion : ejecucionListNew) {
                if (!ejecucionListOld.contains(ejecucionListNewEjecucion)) {
                    Vigencia oldVigenciaOfEjecucionListNewEjecucion = ejecucionListNewEjecucion.getVigencia();
                    ejecucionListNewEjecucion.setVigencia(vigencia);
                    ejecucionListNewEjecucion = em.merge(ejecucionListNewEjecucion);
                    if (oldVigenciaOfEjecucionListNewEjecucion != null && !oldVigenciaOfEjecucionListNewEjecucion.equals(vigencia)) {
                        oldVigenciaOfEjecucionListNewEjecucion.getEjecucionList().remove(ejecucionListNewEjecucion);
                        oldVigenciaOfEjecucionListNewEjecucion = em.merge(oldVigenciaOfEjecucionListNewEjecucion);
                    }
                }
            }
            for (DisponibilidadRubro disponibilidadRubroListOldDisponibilidadRubro : disponibilidadRubroListOld) {
                if (!disponibilidadRubroListNew.contains(disponibilidadRubroListOldDisponibilidadRubro)) {
                    disponibilidadRubroListOldDisponibilidadRubro.setVigencia(null);
                    disponibilidadRubroListOldDisponibilidadRubro = em.merge(disponibilidadRubroListOldDisponibilidadRubro);
                }
            }
            for (DisponibilidadRubro disponibilidadRubroListNewDisponibilidadRubro : disponibilidadRubroListNew) {
                if (!disponibilidadRubroListOld.contains(disponibilidadRubroListNewDisponibilidadRubro)) {
                    Vigencia oldVigenciaOfDisponibilidadRubroListNewDisponibilidadRubro = disponibilidadRubroListNewDisponibilidadRubro.getVigencia();
                    disponibilidadRubroListNewDisponibilidadRubro.setVigencia(vigencia);
                    disponibilidadRubroListNewDisponibilidadRubro = em.merge(disponibilidadRubroListNewDisponibilidadRubro);
                    if (oldVigenciaOfDisponibilidadRubroListNewDisponibilidadRubro != null && !oldVigenciaOfDisponibilidadRubroListNewDisponibilidadRubro.equals(vigencia)) {
                        oldVigenciaOfDisponibilidadRubroListNewDisponibilidadRubro.getDisponibilidadRubroList().remove(disponibilidadRubroListNewDisponibilidadRubro);
                        oldVigenciaOfDisponibilidadRubroListNewDisponibilidadRubro = em.merge(oldVigenciaOfDisponibilidadRubroListNewDisponibilidadRubro);
                    }
                }
            }
            for (Disponibilidad disponibilidadListOldDisponibilidad : disponibilidadListOld) {
                if (!disponibilidadListNew.contains(disponibilidadListOldDisponibilidad)) {
                    disponibilidadListOldDisponibilidad.setVigencia(null);
                    disponibilidadListOldDisponibilidad = em.merge(disponibilidadListOldDisponibilidad);
                }
            }
            for (Disponibilidad disponibilidadListNewDisponibilidad : disponibilidadListNew) {
                if (!disponibilidadListOld.contains(disponibilidadListNewDisponibilidad)) {
                    Vigencia oldVigenciaOfDisponibilidadListNewDisponibilidad = disponibilidadListNewDisponibilidad.getVigencia();
                    disponibilidadListNewDisponibilidad.setVigencia(vigencia);
                    disponibilidadListNewDisponibilidad = em.merge(disponibilidadListNewDisponibilidad);
                    if (oldVigenciaOfDisponibilidadListNewDisponibilidad != null && !oldVigenciaOfDisponibilidadListNewDisponibilidad.equals(vigencia)) {
                        oldVigenciaOfDisponibilidadListNewDisponibilidad.getDisponibilidadList().remove(disponibilidadListNewDisponibilidad);
                        oldVigenciaOfDisponibilidadListNewDisponibilidad = em.merge(oldVigenciaOfDisponibilidadListNewDisponibilidad);
                    }
                }
            }
            for (Adicion adicionListOldAdicion : adicionListOld) {
                if (!adicionListNew.contains(adicionListOldAdicion)) {
                    adicionListOldAdicion.setVigencia(null);
                    adicionListOldAdicion = em.merge(adicionListOldAdicion);
                }
            }
            for (Adicion adicionListNewAdicion : adicionListNew) {
                if (!adicionListOld.contains(adicionListNewAdicion)) {
                    Vigencia oldVigenciaOfAdicionListNewAdicion = adicionListNewAdicion.getVigencia();
                    adicionListNewAdicion.setVigencia(vigencia);
                    adicionListNewAdicion = em.merge(adicionListNewAdicion);
                    if (oldVigenciaOfAdicionListNewAdicion != null && !oldVigenciaOfAdicionListNewAdicion.equals(vigencia)) {
                        oldVigenciaOfAdicionListNewAdicion.getAdicionList().remove(adicionListNewAdicion);
                        oldVigenciaOfAdicionListNewAdicion = em.merge(oldVigenciaOfAdicionListNewAdicion);
                    }
                }
            }
            for (Rubro rubroListOldRubro : rubroListOld) {
                if (!rubroListNew.contains(rubroListOldRubro)) {
                    rubroListOldRubro.setVigencia(null);
                    rubroListOldRubro = em.merge(rubroListOldRubro);
                }
            }
            for (Rubro rubroListNewRubro : rubroListNew) {
                if (!rubroListOld.contains(rubroListNewRubro)) {
                    Vigencia oldVigenciaOfRubroListNewRubro = rubroListNewRubro.getVigencia();
                    rubroListNewRubro.setVigencia(vigencia);
                    rubroListNewRubro = em.merge(rubroListNewRubro);
                    if (oldVigenciaOfRubroListNewRubro != null && !oldVigenciaOfRubroListNewRubro.equals(vigencia)) {
                        oldVigenciaOfRubroListNewRubro.getRubroList().remove(rubroListNewRubro);
                        oldVigenciaOfRubroListNewRubro = em.merge(oldVigenciaOfRubroListNewRubro);
                    }
                }
            }
            for (TrasladoRubro trasladoRubroListOldTrasladoRubro : trasladoRubroListOld) {
                if (!trasladoRubroListNew.contains(trasladoRubroListOldTrasladoRubro)) {
                    trasladoRubroListOldTrasladoRubro.setVigencia(null);
                    trasladoRubroListOldTrasladoRubro = em.merge(trasladoRubroListOldTrasladoRubro);
                }
            }
            for (TrasladoRubro trasladoRubroListNewTrasladoRubro : trasladoRubroListNew) {
                if (!trasladoRubroListOld.contains(trasladoRubroListNewTrasladoRubro)) {
                    Vigencia oldVigenciaOfTrasladoRubroListNewTrasladoRubro = trasladoRubroListNewTrasladoRubro.getVigencia();
                    trasladoRubroListNewTrasladoRubro.setVigencia(vigencia);
                    trasladoRubroListNewTrasladoRubro = em.merge(trasladoRubroListNewTrasladoRubro);
                    if (oldVigenciaOfTrasladoRubroListNewTrasladoRubro != null && !oldVigenciaOfTrasladoRubroListNewTrasladoRubro.equals(vigencia)) {
                        oldVigenciaOfTrasladoRubroListNewTrasladoRubro.getTrasladoRubroList().remove(trasladoRubroListNewTrasladoRubro);
                        oldVigenciaOfTrasladoRubroListNewTrasladoRubro = em.merge(oldVigenciaOfTrasladoRubroListNewTrasladoRubro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = vigencia.getIdVigencia();
                if (findVigencia(id) == null) {
                    throw new NonexistentEntityException("The vigencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vigencia vigencia;
            try {
                vigencia = em.getReference(Vigencia.class, id);
                vigencia.getIdVigencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vigencia with id " + id + " no longer exists.", enfe);
            }
            List<Presupuesto> presupuestoList = vigencia.getPresupuestoList();
            for (Presupuesto presupuestoListPresupuesto : presupuestoList) {
                presupuestoListPresupuesto.setVigencia(null);
                presupuestoListPresupuesto = em.merge(presupuestoListPresupuesto);
            }
            List<AdicionRubro> adicionRubroList = vigencia.getAdicionRubroList();
            for (AdicionRubro adicionRubroListAdicionRubro : adicionRubroList) {
                adicionRubroListAdicionRubro.setVigencia(null);
                adicionRubroListAdicionRubro = em.merge(adicionRubroListAdicionRubro);
            }
            List<Traslado> trasladoList = vigencia.getTrasladoList();
            for (Traslado trasladoListTraslado : trasladoList) {
                trasladoListTraslado.setVigencia(null);
                trasladoListTraslado = em.merge(trasladoListTraslado);
            }
            List<Ejecucion> ejecucionList = vigencia.getEjecucionList();
            for (Ejecucion ejecucionListEjecucion : ejecucionList) {
                ejecucionListEjecucion.setVigencia(null);
                ejecucionListEjecucion = em.merge(ejecucionListEjecucion);
            }
            List<DisponibilidadRubro> disponibilidadRubroList = vigencia.getDisponibilidadRubroList();
            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubro : disponibilidadRubroList) {
                disponibilidadRubroListDisponibilidadRubro.setVigencia(null);
                disponibilidadRubroListDisponibilidadRubro = em.merge(disponibilidadRubroListDisponibilidadRubro);
            }
            List<Disponibilidad> disponibilidadList = vigencia.getDisponibilidadList();
            for (Disponibilidad disponibilidadListDisponibilidad : disponibilidadList) {
                disponibilidadListDisponibilidad.setVigencia(null);
                disponibilidadListDisponibilidad = em.merge(disponibilidadListDisponibilidad);
            }
            List<Adicion> adicionList = vigencia.getAdicionList();
            for (Adicion adicionListAdicion : adicionList) {
                adicionListAdicion.setVigencia(null);
                adicionListAdicion = em.merge(adicionListAdicion);
            }
            List<Rubro> rubroList = vigencia.getRubroList();
            for (Rubro rubroListRubro : rubroList) {
                rubroListRubro.setVigencia(null);
                rubroListRubro = em.merge(rubroListRubro);
            }
            List<TrasladoRubro> trasladoRubroList = vigencia.getTrasladoRubroList();
            for (TrasladoRubro trasladoRubroListTrasladoRubro : trasladoRubroList) {
                trasladoRubroListTrasladoRubro.setVigencia(null);
                trasladoRubroListTrasladoRubro = em.merge(trasladoRubroListTrasladoRubro);
            }
            em.remove(vigencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vigencia> findVigenciaEntities() {
        return findVigenciaEntities(true, -1, -1);
    }

    public List<Vigencia> findVigenciaEntities(int maxResults, int firstResult) {
        return findVigenciaEntities(false, maxResults, firstResult);
    }

    private List<Vigencia> findVigenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vigencia.class));
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

    public Vigencia findVigencia(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vigencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getVigenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vigencia> rt = cq.from(Vigencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
