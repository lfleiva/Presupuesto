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
import com.presupuesto.modelo.OrdenSuministro;
import com.presupuesto.modelo.Suministro;
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
public class SuministroJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;
    
    public SuministroJpaController() {}
    
    public SuministroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void create(Suministro suministro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrdenSuministro ordenSuministro = suministro.getOrdenSuministro();
            if (ordenSuministro != null) {
                ordenSuministro = em.getReference(ordenSuministro.getClass(), ordenSuministro.getIdOrdenSuministro());
                suministro.setOrdenSuministro(ordenSuministro);
            }
            Vigencia vigencia = suministro.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                suministro.setVigencia(vigencia);
            }
            em.persist(suministro);
            if (ordenSuministro != null) {
                ordenSuministro.getSuministroList().add(suministro);
                ordenSuministro = em.merge(ordenSuministro);
            }
            if (vigencia != null) {
                vigencia.getSuministroList().add(suministro);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Suministro suministro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Suministro persistentSuministro = em.find(Suministro.class, suministro.getIdSuministro());
            OrdenSuministro ordenSuministroOld = persistentSuministro.getOrdenSuministro();
            OrdenSuministro ordenSuministroNew = suministro.getOrdenSuministro();
            Vigencia vigenciaOld = persistentSuministro.getVigencia();
            Vigencia vigenciaNew = suministro.getVigencia();
            if (ordenSuministroNew != null) {
                ordenSuministroNew = em.getReference(ordenSuministroNew.getClass(), ordenSuministroNew.getIdOrdenSuministro());
                suministro.setOrdenSuministro(ordenSuministroNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                suministro.setVigencia(vigenciaNew);
            }
            suministro = em.merge(suministro);
            if (ordenSuministroOld != null && !ordenSuministroOld.equals(ordenSuministroNew)) {
                ordenSuministroOld.getSuministroList().remove(suministro);
                ordenSuministroOld = em.merge(ordenSuministroOld);
            }
            if (ordenSuministroNew != null && !ordenSuministroNew.equals(ordenSuministroOld)) {
                ordenSuministroNew.getSuministroList().add(suministro);
                ordenSuministroNew = em.merge(ordenSuministroNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getSuministroList().remove(suministro);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getSuministroList().add(suministro);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = suministro.getIdSuministro();
                if (findSuministro(id) == null) {
                    throw new NonexistentEntityException("The suministro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Suministro suministro) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            
            try {                
                suministro.getIdSuministro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The suministro with id " + suministro.getIdSuministro() + " no longer exists.", enfe);
            }
            OrdenSuministro ordenSuministro = suministro.getOrdenSuministro();
            if (ordenSuministro != null) {
                ordenSuministro.getSuministroList().remove(suministro);
                ordenSuministro = entityManager.merge(ordenSuministro);
            }
            Vigencia vigencia = suministro.getVigencia();
            if (vigencia != null) {
                vigencia.getSuministroList().remove(suministro);
                vigencia = entityManager.merge(vigencia);
            }
            entityManager.remove(entityManager.contains(suministro) ? suministro : entityManager.merge(suministro));
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<Suministro> findSuministroEntities() {
        return findSuministroEntities(true, -1, -1);
    }

    public List<Suministro> findSuministroEntities(int maxResults, int firstResult) {
        return findSuministroEntities(false, maxResults, firstResult);
    }

    private List<Suministro> findSuministroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Suministro.class));
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

    public Suministro findSuministro(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Suministro.class, id);
        } finally {
            em.close();
        }
    }

    public int getSuministroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Suministro> rt = cq.from(Suministro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
