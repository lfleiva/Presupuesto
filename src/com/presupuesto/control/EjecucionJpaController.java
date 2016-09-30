/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Ejecucion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.Vigencia;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis Fernando Leiva
 */
public class EjecucionJpaController implements Serializable {

    public EjecucionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ejecucion ejecucion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro rubro = ejecucion.getRubro();
            if (rubro != null) {
                rubro = em.getReference(rubro.getClass(), rubro.getIdRubro());
                ejecucion.setRubro(rubro);
            }
            Vigencia vigencia = ejecucion.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                ejecucion.setVigencia(vigencia);
            }
            em.persist(ejecucion);
            if (rubro != null) {
                rubro.getEjecucionList().add(ejecucion);
                rubro = em.merge(rubro);
            }
            if (vigencia != null) {
                vigencia.getEjecucionList().add(ejecucion);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ejecucion ejecucion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ejecucion persistentEjecucion = em.find(Ejecucion.class, ejecucion.getIdEjecucion());
            Rubro rubroOld = persistentEjecucion.getRubro();
            Rubro rubroNew = ejecucion.getRubro();
            Vigencia vigenciaOld = persistentEjecucion.getVigencia();
            Vigencia vigenciaNew = ejecucion.getVigencia();
            if (rubroNew != null) {
                rubroNew = em.getReference(rubroNew.getClass(), rubroNew.getIdRubro());
                ejecucion.setRubro(rubroNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                ejecucion.setVigencia(vigenciaNew);
            }
            ejecucion = em.merge(ejecucion);
            if (rubroOld != null && !rubroOld.equals(rubroNew)) {
                rubroOld.getEjecucionList().remove(ejecucion);
                rubroOld = em.merge(rubroOld);
            }
            if (rubroNew != null && !rubroNew.equals(rubroOld)) {
                rubroNew.getEjecucionList().add(ejecucion);
                rubroNew = em.merge(rubroNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getEjecucionList().remove(ejecucion);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getEjecucionList().add(ejecucion);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = ejecucion.getIdEjecucion();
                if (findEjecucion(id) == null) {
                    throw new NonexistentEntityException("The ejecucion with id " + id + " no longer exists.");
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
            Ejecucion ejecucion;
            try {
                ejecucion = em.getReference(Ejecucion.class, id);
                ejecucion.getIdEjecucion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ejecucion with id " + id + " no longer exists.", enfe);
            }
            Rubro rubro = ejecucion.getRubro();
            if (rubro != null) {
                rubro.getEjecucionList().remove(ejecucion);
                rubro = em.merge(rubro);
            }
            Vigencia vigencia = ejecucion.getVigencia();
            if (vigencia != null) {
                vigencia.getEjecucionList().remove(ejecucion);
                vigencia = em.merge(vigencia);
            }
            em.remove(ejecucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ejecucion> findEjecucionEntities() {
        return findEjecucionEntities(true, -1, -1);
    }

    public List<Ejecucion> findEjecucionEntities(int maxResults, int firstResult) {
        return findEjecucionEntities(false, maxResults, firstResult);
    }

    private List<Ejecucion> findEjecucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ejecucion.class));
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

    public Ejecucion findEjecucion(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ejecucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getEjecucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ejecucion> rt = cq.from(Ejecucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
