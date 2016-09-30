/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Beneficiario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.Disponibilidad;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Luis Fernando Leiva
 */
public class BeneficiarioJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;
    
    public BeneficiarioJpaController() {}
    
    public BeneficiarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void create(Beneficiario beneficiario) {
        if (beneficiario.getDisponibilidadList() == null) {
            beneficiario.setDisponibilidadList(new ArrayList<Disponibilidad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Disponibilidad> attachedDisponibilidadList = new ArrayList<Disponibilidad>();
            for (Disponibilidad disponibilidadListDisponibilidadToAttach : beneficiario.getDisponibilidadList()) {
                disponibilidadListDisponibilidadToAttach = em.getReference(disponibilidadListDisponibilidadToAttach.getClass(), disponibilidadListDisponibilidadToAttach.getIdDisponibilidad());
                attachedDisponibilidadList.add(disponibilidadListDisponibilidadToAttach);
            }
            beneficiario.setDisponibilidadList(attachedDisponibilidadList);
            em.persist(beneficiario);
            for (Disponibilidad disponibilidadListDisponibilidad : beneficiario.getDisponibilidadList()) {
                Beneficiario oldBeneficiarioOfDisponibilidadListDisponibilidad = disponibilidadListDisponibilidad.getBeneficiario();
                disponibilidadListDisponibilidad.setBeneficiario(beneficiario);
                disponibilidadListDisponibilidad = em.merge(disponibilidadListDisponibilidad);
                if (oldBeneficiarioOfDisponibilidadListDisponibilidad != null) {
                    oldBeneficiarioOfDisponibilidadListDisponibilidad.getDisponibilidadList().remove(disponibilidadListDisponibilidad);
                    oldBeneficiarioOfDisponibilidadListDisponibilidad = em.merge(oldBeneficiarioOfDisponibilidadListDisponibilidad);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Beneficiario beneficiario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Beneficiario persistentBeneficiario = em.find(Beneficiario.class, beneficiario.getIdBeneficiario());
            List<Disponibilidad> disponibilidadListOld = persistentBeneficiario.getDisponibilidadList();
            List<Disponibilidad> disponibilidadListNew = beneficiario.getDisponibilidadList();
            List<Disponibilidad> attachedDisponibilidadListNew = new ArrayList<Disponibilidad>();
            for (Disponibilidad disponibilidadListNewDisponibilidadToAttach : disponibilidadListNew) {
                disponibilidadListNewDisponibilidadToAttach = em.getReference(disponibilidadListNewDisponibilidadToAttach.getClass(), disponibilidadListNewDisponibilidadToAttach.getIdDisponibilidad());
                attachedDisponibilidadListNew.add(disponibilidadListNewDisponibilidadToAttach);
            }
            disponibilidadListNew = attachedDisponibilidadListNew;
            beneficiario.setDisponibilidadList(disponibilidadListNew);
            beneficiario = em.merge(beneficiario);
            for (Disponibilidad disponibilidadListOldDisponibilidad : disponibilidadListOld) {
                if (!disponibilidadListNew.contains(disponibilidadListOldDisponibilidad)) {
                    disponibilidadListOldDisponibilidad.setBeneficiario(null);
                    disponibilidadListOldDisponibilidad = em.merge(disponibilidadListOldDisponibilidad);
                }
            }
            for (Disponibilidad disponibilidadListNewDisponibilidad : disponibilidadListNew) {
                if (!disponibilidadListOld.contains(disponibilidadListNewDisponibilidad)) {
                    Beneficiario oldBeneficiarioOfDisponibilidadListNewDisponibilidad = disponibilidadListNewDisponibilidad.getBeneficiario();
                    disponibilidadListNewDisponibilidad.setBeneficiario(beneficiario);
                    disponibilidadListNewDisponibilidad = em.merge(disponibilidadListNewDisponibilidad);
                    if (oldBeneficiarioOfDisponibilidadListNewDisponibilidad != null && !oldBeneficiarioOfDisponibilidadListNewDisponibilidad.equals(beneficiario)) {
                        oldBeneficiarioOfDisponibilidadListNewDisponibilidad.getDisponibilidadList().remove(disponibilidadListNewDisponibilidad);
                        oldBeneficiarioOfDisponibilidadListNewDisponibilidad = em.merge(oldBeneficiarioOfDisponibilidadListNewDisponibilidad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = beneficiario.getIdBeneficiario();
                if (findBeneficiario(id) == null) {
                    throw new NonexistentEntityException("The beneficiario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Beneficiario beneficiario) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();            
            try {                
                beneficiario.getIdBeneficiario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The beneficiario with id " + beneficiario.getIdentificacion() + " no longer exists.", enfe);
            }            
            entityManager.remove(entityManager.contains(beneficiario) ? beneficiario : entityManager.merge(beneficiario));
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<Beneficiario> findBeneficiarioEntities() {
        return findBeneficiarioEntities(true, -1, -1);
    }

    public List<Beneficiario> findBeneficiarioEntities(int maxResults, int firstResult) {
        return findBeneficiarioEntities(false, maxResults, firstResult);
    }

    private List<Beneficiario> findBeneficiarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Beneficiario.class));
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

    public Beneficiario findBeneficiario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Beneficiario.class, id);
        } finally {
            em.close();
        }
    }

    public int getBeneficiarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Beneficiario> rt = cq.from(Beneficiario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
