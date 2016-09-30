/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Presupuesto;
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
public class PresupuestoJpaController implements Serializable {

    public PresupuestoJpaController() {}
    
    public PresupuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Presupuesto presupuesto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro rubro = presupuesto.getRubro();
            if (rubro != null) {
                rubro = em.getReference(rubro.getClass(), rubro.getIdRubro());
                presupuesto.setRubro(rubro);
            }
            Vigencia vigencia = presupuesto.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                presupuesto.setVigencia(vigencia);
            }
            em.persist(presupuesto);
            if (rubro != null) {
                rubro.getPresupuestoList().add(presupuesto);
                rubro = em.merge(rubro);
            }
            if (vigencia != null) {
                vigencia.getPresupuestoList().add(presupuesto);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Presupuesto presupuesto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Presupuesto persistentPresupuesto = em.find(Presupuesto.class, presupuesto.getIdPresupuesto());
            Rubro rubroOld = persistentPresupuesto.getRubro();
            Rubro rubroNew = presupuesto.getRubro();
            Vigencia vigenciaOld = persistentPresupuesto.getVigencia();
            Vigencia vigenciaNew = presupuesto.getVigencia();
            if (rubroNew != null) {
                rubroNew = em.getReference(rubroNew.getClass(), rubroNew.getIdRubro());
                presupuesto.setRubro(rubroNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                presupuesto.setVigencia(vigenciaNew);
            }
            presupuesto = em.merge(presupuesto);
            if (rubroOld != null && !rubroOld.equals(rubroNew)) {
                rubroOld.getPresupuestoList().remove(presupuesto);
                rubroOld = em.merge(rubroOld);
            }
            if (rubroNew != null && !rubroNew.equals(rubroOld)) {
                rubroNew.getPresupuestoList().add(presupuesto);
                rubroNew = em.merge(rubroNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getPresupuestoList().remove(presupuesto);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getPresupuestoList().add(presupuesto);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = presupuesto.getIdPresupuesto();
                if (findPresupuesto(id) == null) {
                    throw new NonexistentEntityException("The presupuesto with id " + id + " no longer exists.");
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
            Presupuesto presupuesto;
            try {
                presupuesto = em.getReference(Presupuesto.class, id);
                presupuesto.getIdPresupuesto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The presupuesto with id " + id + " no longer exists.", enfe);
            }
            Rubro rubro = presupuesto.getRubro();
            if (rubro != null) {
                rubro.getPresupuestoList().remove(presupuesto);
                rubro = em.merge(rubro);
            }
            Vigencia vigencia = presupuesto.getVigencia();
            if (vigencia != null) {
                vigencia.getPresupuestoList().remove(presupuesto);
                vigencia = em.merge(vigencia);
            }
            em.remove(presupuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Presupuesto> findPresupuestoEntities() {
        return findPresupuestoEntities(true, -1, -1);
    }

    public List<Presupuesto> findPresupuestoEntities(int maxResults, int firstResult) {
        return findPresupuestoEntities(false, maxResults, firstResult);
    }

    private List<Presupuesto> findPresupuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Presupuesto.class));
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

    public Presupuesto findPresupuesto(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Presupuesto.class, id);
        } finally {
            em.close();
        }
    }

    public int getPresupuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Presupuesto> rt = cq.from(Presupuesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
