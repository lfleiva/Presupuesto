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
import com.presupuesto.modelo.Descuento;
import com.presupuesto.modelo.ComprobanteEgreso;
import com.presupuesto.modelo.EgresoDescuento;
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
public class EgresoDescuentoJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;
    
    public EgresoDescuentoJpaController() {}
    
    public EgresoDescuentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void create(EgresoDescuento egresoDescuento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Descuento descuento = egresoDescuento.getDescuento();
            if (descuento != null) {
                descuento = em.getReference(descuento.getClass(), descuento.getIdDescuento());
                egresoDescuento.setDescuento(descuento);
            }
            ComprobanteEgreso comprobanteEgreso = egresoDescuento.getComprobanteEgreso();
            if (comprobanteEgreso != null) {
                comprobanteEgreso = em.getReference(comprobanteEgreso.getClass(), comprobanteEgreso.getIdComprobanteEgreso());
                egresoDescuento.setComprobanteEgreso(comprobanteEgreso);
            }
            Vigencia vigencia = egresoDescuento.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                egresoDescuento.setVigencia(vigencia);
            }
            em.persist(egresoDescuento);
            if (descuento != null) {
                descuento.getEgresoDescuentoList().add(egresoDescuento);
                descuento = em.merge(descuento);
            }
            if (comprobanteEgreso != null) {
                comprobanteEgreso.getEgresoDescuentoList().add(egresoDescuento);
                comprobanteEgreso = em.merge(comprobanteEgreso);
            }
            if (vigencia != null) {
                vigencia.getEgresoDescuentoList().add(egresoDescuento);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EgresoDescuento egresoDescuento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EgresoDescuento persistentEgresoDescuento = em.find(EgresoDescuento.class, egresoDescuento.getIdEgresoDescuento());
            Descuento descuentoOld = persistentEgresoDescuento.getDescuento();
            Descuento descuentoNew = egresoDescuento.getDescuento();
            ComprobanteEgreso comprobanteEgresoOld = persistentEgresoDescuento.getComprobanteEgreso();
            ComprobanteEgreso comprobanteEgresoNew = egresoDescuento.getComprobanteEgreso();
            Vigencia vigenciaOld = persistentEgresoDescuento.getVigencia();
            Vigencia vigenciaNew = egresoDescuento.getVigencia();
            if (descuentoNew != null) {
                descuentoNew = em.getReference(descuentoNew.getClass(), descuentoNew.getIdDescuento());
                egresoDescuento.setDescuento(descuentoNew);
            }
            if (comprobanteEgresoNew != null) {
                comprobanteEgresoNew = em.getReference(comprobanteEgresoNew.getClass(), comprobanteEgresoNew.getIdComprobanteEgreso());
                egresoDescuento.setComprobanteEgreso(comprobanteEgresoNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                egresoDescuento.setVigencia(vigenciaNew);
            }
            egresoDescuento = em.merge(egresoDescuento);
            if (descuentoOld != null && !descuentoOld.equals(descuentoNew)) {
                descuentoOld.getEgresoDescuentoList().remove(egresoDescuento);
                descuentoOld = em.merge(descuentoOld);
            }
            if (descuentoNew != null && !descuentoNew.equals(descuentoOld)) {
                descuentoNew.getEgresoDescuentoList().add(egresoDescuento);
                descuentoNew = em.merge(descuentoNew);
            }
            if (comprobanteEgresoOld != null && !comprobanteEgresoOld.equals(comprobanteEgresoNew)) {
                comprobanteEgresoOld.getEgresoDescuentoList().remove(egresoDescuento);
                comprobanteEgresoOld = em.merge(comprobanteEgresoOld);
            }
            if (comprobanteEgresoNew != null && !comprobanteEgresoNew.equals(comprobanteEgresoOld)) {
                comprobanteEgresoNew.getEgresoDescuentoList().add(egresoDescuento);
                comprobanteEgresoNew = em.merge(comprobanteEgresoNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getEgresoDescuentoList().remove(egresoDescuento);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getEgresoDescuentoList().add(egresoDescuento);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = egresoDescuento.getIdEgresoDescuento();
                if (findEgresoDescuento(id) == null) {
                    throw new NonexistentEntityException("The egresoDescuento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EgresoDescuento egresoDescuento) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            
            try {
                //egresoDescuento = entityManager.getReference(EgresoDescuento.class, egresoDescuento.getIdEgresoDescuento());
                egresoDescuento.getIdEgresoDescuento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The egresoDescuento with id " + egresoDescuento.getIdEgresoDescuento() + " no longer exists.", enfe);
            }
            Descuento descuento = egresoDescuento.getDescuento();
            if (descuento != null) {
                descuento.getEgresoDescuentoList().remove(egresoDescuento);
                descuento = entityManager.merge(descuento);
            }
            ComprobanteEgreso comprobanteEgreso = egresoDescuento.getComprobanteEgreso();
            if (comprobanteEgreso != null) {
                comprobanteEgreso.getEgresoDescuentoList().remove(egresoDescuento);
                comprobanteEgreso = entityManager.merge(comprobanteEgreso);
            }
            Vigencia vigencia = egresoDescuento.getVigencia();
            if (vigencia != null) {
                vigencia.getEgresoDescuentoList().remove(egresoDescuento);
                vigencia = entityManager.merge(vigencia);
            }
            
            entityManager.remove(entityManager.contains(egresoDescuento) ? egresoDescuento : entityManager.merge(egresoDescuento));            
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<EgresoDescuento> findEgresoDescuentoEntities() {
        return findEgresoDescuentoEntities(true, -1, -1);
    }

    public List<EgresoDescuento> findEgresoDescuentoEntities(int maxResults, int firstResult) {
        return findEgresoDescuentoEntities(false, maxResults, firstResult);
    }

    private List<EgresoDescuento> findEgresoDescuentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EgresoDescuento.class));
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

    public EgresoDescuento findEgresoDescuento(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EgresoDescuento.class, id);
        } finally {
            em.close();
        }
    }

    public int getEgresoDescuentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EgresoDescuento> rt = cq.from(EgresoDescuento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
