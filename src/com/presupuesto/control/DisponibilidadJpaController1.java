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
import com.presupuesto.modelo.Beneficiario;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.modelo.DisponibilidadRubro;
import java.util.ArrayList;
import java.util.List;
import com.presupuesto.modelo.Registro;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis Fernando Leiva
 */
public class DisponibilidadJpaController1 implements Serializable {

    public DisponibilidadJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Disponibilidad disponibilidad) {
        if (disponibilidad.getDisponibilidadRubroList() == null) {
            disponibilidad.setDisponibilidadRubroList(new ArrayList<DisponibilidadRubro>());
        }
        if (disponibilidad.getRegistroList() == null) {
            disponibilidad.setRegistroList(new ArrayList<Registro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Beneficiario beneficiario = disponibilidad.getBeneficiario();
            if (beneficiario != null) {
                beneficiario = em.getReference(beneficiario.getClass(), beneficiario.getIdBeneficiario());
                disponibilidad.setBeneficiario(beneficiario);
            }
            Vigencia vigencia = disponibilidad.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                disponibilidad.setVigencia(vigencia);
            }
            List<DisponibilidadRubro> attachedDisponibilidadRubroList = new ArrayList<DisponibilidadRubro>();
            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubroToAttach : disponibilidad.getDisponibilidadRubroList()) {
                disponibilidadRubroListDisponibilidadRubroToAttach = em.getReference(disponibilidadRubroListDisponibilidadRubroToAttach.getClass(), disponibilidadRubroListDisponibilidadRubroToAttach.getIdDisponibilidadRubro());
                attachedDisponibilidadRubroList.add(disponibilidadRubroListDisponibilidadRubroToAttach);
            }
            disponibilidad.setDisponibilidadRubroList(attachedDisponibilidadRubroList);
            List<Registro> attachedRegistroList = new ArrayList<Registro>();
            for (Registro registroListRegistroToAttach : disponibilidad.getRegistroList()) {
                registroListRegistroToAttach = em.getReference(registroListRegistroToAttach.getClass(), registroListRegistroToAttach.getIdRegistro());
                attachedRegistroList.add(registroListRegistroToAttach);
            }
            disponibilidad.setRegistroList(attachedRegistroList);
            em.persist(disponibilidad);
            if (beneficiario != null) {
                beneficiario.getDisponibilidadList().add(disponibilidad);
                beneficiario = em.merge(beneficiario);
            }
            if (vigencia != null) {
                vigencia.getDisponibilidadList().add(disponibilidad);
                vigencia = em.merge(vigencia);
            }
            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubro : disponibilidad.getDisponibilidadRubroList()) {
                Disponibilidad oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro = disponibilidadRubroListDisponibilidadRubro.getDisponibilidad();
                disponibilidadRubroListDisponibilidadRubro.setDisponibilidad(disponibilidad);
                disponibilidadRubroListDisponibilidadRubro = em.merge(disponibilidadRubroListDisponibilidadRubro);
                if (oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro != null) {
                    oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro.getDisponibilidadRubroList().remove(disponibilidadRubroListDisponibilidadRubro);
                    oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro = em.merge(oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro);
                }
            }
            for (Registro registroListRegistro : disponibilidad.getRegistroList()) {
                Disponibilidad oldDisponibilidadOfRegistroListRegistro = registroListRegistro.getDisponibilidad();
                registroListRegistro.setDisponibilidad(disponibilidad);
                registroListRegistro = em.merge(registroListRegistro);
                if (oldDisponibilidadOfRegistroListRegistro != null) {
                    oldDisponibilidadOfRegistroListRegistro.getRegistroList().remove(registroListRegistro);
                    oldDisponibilidadOfRegistroListRegistro = em.merge(oldDisponibilidadOfRegistroListRegistro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Disponibilidad disponibilidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disponibilidad persistentDisponibilidad = em.find(Disponibilidad.class, disponibilidad.getIdDisponibilidad());
            Beneficiario beneficiarioOld = persistentDisponibilidad.getBeneficiario();
            Beneficiario beneficiarioNew = disponibilidad.getBeneficiario();
            Vigencia vigenciaOld = persistentDisponibilidad.getVigencia();
            Vigencia vigenciaNew = disponibilidad.getVigencia();
            List<DisponibilidadRubro> disponibilidadRubroListOld = persistentDisponibilidad.getDisponibilidadRubroList();
            List<DisponibilidadRubro> disponibilidadRubroListNew = disponibilidad.getDisponibilidadRubroList();
            List<Registro> registroListOld = persistentDisponibilidad.getRegistroList();
            List<Registro> registroListNew = disponibilidad.getRegistroList();
            if (beneficiarioNew != null) {
                beneficiarioNew = em.getReference(beneficiarioNew.getClass(), beneficiarioNew.getIdBeneficiario());
                disponibilidad.setBeneficiario(beneficiarioNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                disponibilidad.setVigencia(vigenciaNew);
            }
            List<DisponibilidadRubro> attachedDisponibilidadRubroListNew = new ArrayList<DisponibilidadRubro>();
            for (DisponibilidadRubro disponibilidadRubroListNewDisponibilidadRubroToAttach : disponibilidadRubroListNew) {
                disponibilidadRubroListNewDisponibilidadRubroToAttach = em.getReference(disponibilidadRubroListNewDisponibilidadRubroToAttach.getClass(), disponibilidadRubroListNewDisponibilidadRubroToAttach.getIdDisponibilidadRubro());
                attachedDisponibilidadRubroListNew.add(disponibilidadRubroListNewDisponibilidadRubroToAttach);
            }
            disponibilidadRubroListNew = attachedDisponibilidadRubroListNew;
            disponibilidad.setDisponibilidadRubroList(disponibilidadRubroListNew);
            List<Registro> attachedRegistroListNew = new ArrayList<Registro>();
            for (Registro registroListNewRegistroToAttach : registroListNew) {
                registroListNewRegistroToAttach = em.getReference(registroListNewRegistroToAttach.getClass(), registroListNewRegistroToAttach.getIdRegistro());
                attachedRegistroListNew.add(registroListNewRegistroToAttach);
            }
            registroListNew = attachedRegistroListNew;
            disponibilidad.setRegistroList(registroListNew);
            disponibilidad = em.merge(disponibilidad);
            if (beneficiarioOld != null && !beneficiarioOld.equals(beneficiarioNew)) {
                beneficiarioOld.getDisponibilidadList().remove(disponibilidad);
                beneficiarioOld = em.merge(beneficiarioOld);
            }
            if (beneficiarioNew != null && !beneficiarioNew.equals(beneficiarioOld)) {
                beneficiarioNew.getDisponibilidadList().add(disponibilidad);
                beneficiarioNew = em.merge(beneficiarioNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getDisponibilidadList().remove(disponibilidad);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getDisponibilidadList().add(disponibilidad);
                vigenciaNew = em.merge(vigenciaNew);
            }
            for (DisponibilidadRubro disponibilidadRubroListOldDisponibilidadRubro : disponibilidadRubroListOld) {
                if (!disponibilidadRubroListNew.contains(disponibilidadRubroListOldDisponibilidadRubro)) {
                    disponibilidadRubroListOldDisponibilidadRubro.setDisponibilidad(null);
                    disponibilidadRubroListOldDisponibilidadRubro = em.merge(disponibilidadRubroListOldDisponibilidadRubro);
                }
            }
            for (DisponibilidadRubro disponibilidadRubroListNewDisponibilidadRubro : disponibilidadRubroListNew) {
                if (!disponibilidadRubroListOld.contains(disponibilidadRubroListNewDisponibilidadRubro)) {
                    Disponibilidad oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro = disponibilidadRubroListNewDisponibilidadRubro.getDisponibilidad();
                    disponibilidadRubroListNewDisponibilidadRubro.setDisponibilidad(disponibilidad);
                    disponibilidadRubroListNewDisponibilidadRubro = em.merge(disponibilidadRubroListNewDisponibilidadRubro);
                    if (oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro != null && !oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro.equals(disponibilidad)) {
                        oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro.getDisponibilidadRubroList().remove(disponibilidadRubroListNewDisponibilidadRubro);
                        oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro = em.merge(oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro);
                    }
                }
            }
            for (Registro registroListOldRegistro : registroListOld) {
                if (!registroListNew.contains(registroListOldRegistro)) {
                    registroListOldRegistro.setDisponibilidad(null);
                    registroListOldRegistro = em.merge(registroListOldRegistro);
                }
            }
            for (Registro registroListNewRegistro : registroListNew) {
                if (!registroListOld.contains(registroListNewRegistro)) {
                    Disponibilidad oldDisponibilidadOfRegistroListNewRegistro = registroListNewRegistro.getDisponibilidad();
                    registroListNewRegistro.setDisponibilidad(disponibilidad);
                    registroListNewRegistro = em.merge(registroListNewRegistro);
                    if (oldDisponibilidadOfRegistroListNewRegistro != null && !oldDisponibilidadOfRegistroListNewRegistro.equals(disponibilidad)) {
                        oldDisponibilidadOfRegistroListNewRegistro.getRegistroList().remove(registroListNewRegistro);
                        oldDisponibilidadOfRegistroListNewRegistro = em.merge(oldDisponibilidadOfRegistroListNewRegistro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = disponibilidad.getIdDisponibilidad();
                if (findDisponibilidad(id) == null) {
                    throw new NonexistentEntityException("The disponibilidad with id " + id + " no longer exists.");
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
            Disponibilidad disponibilidad;
            try {
                disponibilidad = em.getReference(Disponibilidad.class, id);
                disponibilidad.getIdDisponibilidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The disponibilidad with id " + id + " no longer exists.", enfe);
            }
            Beneficiario beneficiario = disponibilidad.getBeneficiario();
            if (beneficiario != null) {
                beneficiario.getDisponibilidadList().remove(disponibilidad);
                beneficiario = em.merge(beneficiario);
            }
            Vigencia vigencia = disponibilidad.getVigencia();
            if (vigencia != null) {
                vigencia.getDisponibilidadList().remove(disponibilidad);
                vigencia = em.merge(vigencia);
            }
            List<DisponibilidadRubro> disponibilidadRubroList = disponibilidad.getDisponibilidadRubroList();
            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubro : disponibilidadRubroList) {
                disponibilidadRubroListDisponibilidadRubro.setDisponibilidad(null);
                disponibilidadRubroListDisponibilidadRubro = em.merge(disponibilidadRubroListDisponibilidadRubro);
            }
            List<Registro> registroList = disponibilidad.getRegistroList();
            for (Registro registroListRegistro : registroList) {
                registroListRegistro.setDisponibilidad(null);
                registroListRegistro = em.merge(registroListRegistro);
            }
            em.remove(disponibilidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Disponibilidad> findDisponibilidadEntities() {
        return findDisponibilidadEntities(true, -1, -1);
    }

    public List<Disponibilidad> findDisponibilidadEntities(int maxResults, int firstResult) {
        return findDisponibilidadEntities(false, maxResults, firstResult);
    }

    private List<Disponibilidad> findDisponibilidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Disponibilidad.class));
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

    public Disponibilidad findDisponibilidad(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Disponibilidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getDisponibilidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Disponibilidad> rt = cq.from(Disponibilidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
