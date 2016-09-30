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
import com.presupuesto.modelo.Traslado;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.TrasladoRubro;
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
public class TrasladoRubroJpaController implements Serializable {
    
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;
    
    public TrasladoRubroJpaController(){}

    public TrasladoRubroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void create(TrasladoRubro trasladoRubro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Traslado traslado = trasladoRubro.getTraslado();
            if (traslado != null) {
                traslado = em.getReference(traslado.getClass(), traslado.getIdTraslado());
                trasladoRubro.setTraslado(traslado);
            }
            Rubro rubro = trasladoRubro.getRubro();
            if (rubro != null) {
                rubro = em.getReference(rubro.getClass(), rubro.getIdRubro());
                trasladoRubro.setRubro(rubro);
            }
            Vigencia vigencia = trasladoRubro.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                trasladoRubro.setVigencia(vigencia);
            }
            em.persist(trasladoRubro);
            if (traslado != null) {
                traslado.getTrasladoRubroList().add(trasladoRubro);
                traslado = em.merge(traslado);
            }
            if (rubro != null) {
                rubro.getTrasladoRubroList().add(trasladoRubro);
                rubro = em.merge(rubro);
            }
            if (vigencia != null) {
                vigencia.getTrasladoRubroList().add(trasladoRubro);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TrasladoRubro trasladoRubro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TrasladoRubro persistentTrasladoRubro = em.find(TrasladoRubro.class, trasladoRubro.getIdTrasladoRubro());
            Traslado trasladoOld = persistentTrasladoRubro.getTraslado();
            Traslado trasladoNew = trasladoRubro.getTraslado();
            Rubro rubroOld = persistentTrasladoRubro.getRubro();
            Rubro rubroNew = trasladoRubro.getRubro();
            Vigencia vigenciaOld = persistentTrasladoRubro.getVigencia();
            Vigencia vigenciaNew = trasladoRubro.getVigencia();
            if (trasladoNew != null) {
                trasladoNew = em.getReference(trasladoNew.getClass(), trasladoNew.getIdTraslado());
                trasladoRubro.setTraslado(trasladoNew);
            }
            if (rubroNew != null) {
                rubroNew = em.getReference(rubroNew.getClass(), rubroNew.getIdRubro());
                trasladoRubro.setRubro(rubroNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                trasladoRubro.setVigencia(vigenciaNew);
            }
            trasladoRubro = em.merge(trasladoRubro);
            if (trasladoOld != null && !trasladoOld.equals(trasladoNew)) {
                trasladoOld.getTrasladoRubroList().remove(trasladoRubro);
                trasladoOld = em.merge(trasladoOld);
            }
            if (trasladoNew != null && !trasladoNew.equals(trasladoOld)) {
                trasladoNew.getTrasladoRubroList().add(trasladoRubro);
                trasladoNew = em.merge(trasladoNew);
            }
            if (rubroOld != null && !rubroOld.equals(rubroNew)) {
                rubroOld.getTrasladoRubroList().remove(trasladoRubro);
                rubroOld = em.merge(rubroOld);
            }
            if (rubroNew != null && !rubroNew.equals(rubroOld)) {
                rubroNew.getTrasladoRubroList().add(trasladoRubro);
                rubroNew = em.merge(rubroNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getTrasladoRubroList().remove(trasladoRubro);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getTrasladoRubroList().add(trasladoRubro);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = trasladoRubro.getIdTrasladoRubro();
                if (findTrasladoRubro(id) == null) {
                    throw new NonexistentEntityException("The trasladoRubro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TrasladoRubro trasladoRubro) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            
            try {                
                trasladoRubro.getIdTrasladoRubro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trasladoRubro with id " + trasladoRubro.getIdTrasladoRubro() + " no longer exists.", enfe);
            }
            Traslado traslado = trasladoRubro.getTraslado();
            if (traslado != null) {
                traslado.getTrasladoRubroList().remove(trasladoRubro);
                traslado = entityManager.merge(traslado);
            }
            Rubro rubro = trasladoRubro.getRubro();
            if (rubro != null) {
                rubro.getTrasladoRubroList().remove(trasladoRubro);
                rubro = entityManager.merge(rubro);
            }
            Vigencia vigencia = trasladoRubro.getVigencia();
            if (vigencia != null) {
                vigencia.getTrasladoRubroList().remove(trasladoRubro);
                vigencia = entityManager.merge(vigencia);
            }            
            entityManager.remove(entityManager.contains(trasladoRubro) ? trasladoRubro : entityManager.merge(trasladoRubro));
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<TrasladoRubro> findTrasladoRubroEntities() {
        return findTrasladoRubroEntities(true, -1, -1);
    }

    public List<TrasladoRubro> findTrasladoRubroEntities(int maxResults, int firstResult) {
        return findTrasladoRubroEntities(false, maxResults, firstResult);
    }

    private List<TrasladoRubro> findTrasladoRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TrasladoRubro.class));
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

    public TrasladoRubro findTrasladoRubro(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TrasladoRubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrasladoRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TrasladoRubro> rt = cq.from(TrasladoRubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
