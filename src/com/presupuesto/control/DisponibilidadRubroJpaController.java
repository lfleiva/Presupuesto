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
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.DisponibilidadRubro;
import com.presupuesto.modelo.Vigencia;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Luis Fernando Leiva
 */
public class DisponibilidadRubroJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;
    
    public DisponibilidadRubroJpaController() {}
    
    public DisponibilidadRubroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void create(DisponibilidadRubro disponibilidadRubro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro rubro = disponibilidadRubro.getRubro();
            if (rubro != null) {
                rubro = em.getReference(rubro.getClass(), rubro.getIdRubro());
                disponibilidadRubro.setRubro(rubro);
            }
            Disponibilidad disponibilidad = disponibilidadRubro.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad = em.getReference(disponibilidad.getClass(), disponibilidad.getIdDisponibilidad());
                disponibilidadRubro.setDisponibilidad(disponibilidad);
            }
            Vigencia vigencia = disponibilidadRubro.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                disponibilidadRubro.setVigencia(vigencia);
            }
            em.persist(disponibilidadRubro);
            if (rubro != null) {
                rubro.getDisponibilidadRubroList().add(disponibilidadRubro);
                rubro = em.merge(rubro);
            }
            if (disponibilidad != null) {
                disponibilidad.getDisponibilidadRubroList().add(disponibilidadRubro);
                disponibilidad = em.merge(disponibilidad);
            }
            if (vigencia != null) {
                vigencia.getDisponibilidadRubroList().add(disponibilidadRubro);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DisponibilidadRubro disponibilidadRubro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DisponibilidadRubro persistentDisponibilidadRubro = em.find(DisponibilidadRubro.class, disponibilidadRubro.getIdDisponibilidadRubro());
            Rubro rubroOld = persistentDisponibilidadRubro.getRubro();
            Rubro rubroNew = disponibilidadRubro.getRubro();
            Disponibilidad disponibilidadOld = persistentDisponibilidadRubro.getDisponibilidad();
            Disponibilidad disponibilidadNew = disponibilidadRubro.getDisponibilidad();
            Vigencia vigenciaOld = persistentDisponibilidadRubro.getVigencia();
            Vigencia vigenciaNew = disponibilidadRubro.getVigencia();
            if (rubroNew != null) {
                rubroNew = em.getReference(rubroNew.getClass(), rubroNew.getIdRubro());
                disponibilidadRubro.setRubro(rubroNew);
            }
            if (disponibilidadNew != null) {
                disponibilidadNew = em.getReference(disponibilidadNew.getClass(), disponibilidadNew.getIdDisponibilidad());
                disponibilidadRubro.setDisponibilidad(disponibilidadNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                disponibilidadRubro.setVigencia(vigenciaNew);
            }
            disponibilidadRubro = em.merge(disponibilidadRubro);
            if (rubroOld != null && !rubroOld.equals(rubroNew)) {
                rubroOld.getDisponibilidadRubroList().remove(disponibilidadRubro);
                rubroOld = em.merge(rubroOld);
            }
            if (rubroNew != null && !rubroNew.equals(rubroOld)) {
                rubroNew.getDisponibilidadRubroList().add(disponibilidadRubro);
                rubroNew = em.merge(rubroNew);
            }
            if (disponibilidadOld != null && !disponibilidadOld.equals(disponibilidadNew)) {
                disponibilidadOld.getDisponibilidadRubroList().remove(disponibilidadRubro);
                disponibilidadOld = em.merge(disponibilidadOld);
            }
            if (disponibilidadNew != null && !disponibilidadNew.equals(disponibilidadOld)) {
                disponibilidadNew.getDisponibilidadRubroList().add(disponibilidadRubro);
                disponibilidadNew = em.merge(disponibilidadNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getDisponibilidadRubroList().remove(disponibilidadRubro);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getDisponibilidadRubroList().add(disponibilidadRubro);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = disponibilidadRubro.getIdDisponibilidadRubro();
                if (findDisponibilidadRubro(id) == null) {
                    throw new NonexistentEntityException("The disponibilidadRubro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DisponibilidadRubro disponibilidadRubro) throws NonexistentEntityException {
       entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            try {
                //disponibilidadRubro = em.getReference(DisponibilidadRubro.class, id);
                disponibilidadRubro.getIdDisponibilidadRubro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The disponibilidadRubro with id " + disponibilidadRubro.getIdDisponibilidadRubro() + " no longer exists.", enfe);
            }
            Rubro rubro = disponibilidadRubro.getRubro();
            if (rubro != null) {
                rubro.getDisponibilidadRubroList().remove(disponibilidadRubro);
                rubro = entityManager.merge(rubro);
            }
            Disponibilidad disponibilidad = disponibilidadRubro.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad.getDisponibilidadRubroList().remove(disponibilidadRubro);
                disponibilidad = entityManager.merge(disponibilidad);
            }
            Vigencia vigencia = disponibilidadRubro.getVigencia();
            if (vigencia != null) {
                vigencia.getDisponibilidadRubroList().remove(disponibilidadRubro);
                vigencia = entityManager.merge(vigencia);
            }
            entityManager.remove(entityManager.contains(disponibilidadRubro) ? disponibilidadRubro : entityManager.merge(disponibilidadRubro));
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<DisponibilidadRubro> findDisponibilidadRubroEntities() {
        return findDisponibilidadRubroEntities(true, -1, -1);
    }

    public List<DisponibilidadRubro> findDisponibilidadRubroEntities(int maxResults, int firstResult) {
        return findDisponibilidadRubroEntities(false, maxResults, firstResult);
    }

    private List<DisponibilidadRubro> findDisponibilidadRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DisponibilidadRubro.class));
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

    public DisponibilidadRubro findDisponibilidadRubro(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DisponibilidadRubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getDisponibilidadRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DisponibilidadRubro> rt = cq.from(DisponibilidadRubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
