/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Descuento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.modelo.EgresoDescuento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis Fernando Leiva
 */
public class DescuentoJpaController implements Serializable {

    public DescuentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Descuento descuento) {
        if (descuento.getEgresoDescuentoList() == null) {
            descuento.setEgresoDescuentoList(new ArrayList<EgresoDescuento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vigencia vigencia = descuento.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                descuento.setVigencia(vigencia);
            }
            List<EgresoDescuento> attachedEgresoDescuentoList = new ArrayList<EgresoDescuento>();
            for (EgresoDescuento egresoDescuentoListEgresoDescuentoToAttach : descuento.getEgresoDescuentoList()) {
                egresoDescuentoListEgresoDescuentoToAttach = em.getReference(egresoDescuentoListEgresoDescuentoToAttach.getClass(), egresoDescuentoListEgresoDescuentoToAttach.getIdEgresoDescuento());
                attachedEgresoDescuentoList.add(egresoDescuentoListEgresoDescuentoToAttach);
            }
            descuento.setEgresoDescuentoList(attachedEgresoDescuentoList);
            em.persist(descuento);
            if (vigencia != null) {
                vigencia.getDescuentoList().add(descuento);
                vigencia = em.merge(vigencia);
            }
            for (EgresoDescuento egresoDescuentoListEgresoDescuento : descuento.getEgresoDescuentoList()) {
                Descuento oldDescuentoOfEgresoDescuentoListEgresoDescuento = egresoDescuentoListEgresoDescuento.getDescuento();
                egresoDescuentoListEgresoDescuento.setDescuento(descuento);
                egresoDescuentoListEgresoDescuento = em.merge(egresoDescuentoListEgresoDescuento);
                if (oldDescuentoOfEgresoDescuentoListEgresoDescuento != null) {
                    oldDescuentoOfEgresoDescuentoListEgresoDescuento.getEgresoDescuentoList().remove(egresoDescuentoListEgresoDescuento);
                    oldDescuentoOfEgresoDescuentoListEgresoDescuento = em.merge(oldDescuentoOfEgresoDescuentoListEgresoDescuento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Descuento descuento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Descuento persistentDescuento = em.find(Descuento.class, descuento.getIdDescuento());
            Vigencia vigenciaOld = persistentDescuento.getVigencia();
            Vigencia vigenciaNew = descuento.getVigencia();
            List<EgresoDescuento> egresoDescuentoListOld = persistentDescuento.getEgresoDescuentoList();
            List<EgresoDescuento> egresoDescuentoListNew = descuento.getEgresoDescuentoList();
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                descuento.setVigencia(vigenciaNew);
            }
            List<EgresoDescuento> attachedEgresoDescuentoListNew = new ArrayList<EgresoDescuento>();
            for (EgresoDescuento egresoDescuentoListNewEgresoDescuentoToAttach : egresoDescuentoListNew) {
                egresoDescuentoListNewEgresoDescuentoToAttach = em.getReference(egresoDescuentoListNewEgresoDescuentoToAttach.getClass(), egresoDescuentoListNewEgresoDescuentoToAttach.getIdEgresoDescuento());
                attachedEgresoDescuentoListNew.add(egresoDescuentoListNewEgresoDescuentoToAttach);
            }
            egresoDescuentoListNew = attachedEgresoDescuentoListNew;
            descuento.setEgresoDescuentoList(egresoDescuentoListNew);
            descuento = em.merge(descuento);
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getDescuentoList().remove(descuento);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getDescuentoList().add(descuento);
                vigenciaNew = em.merge(vigenciaNew);
            }
            for (EgresoDescuento egresoDescuentoListOldEgresoDescuento : egresoDescuentoListOld) {
                if (!egresoDescuentoListNew.contains(egresoDescuentoListOldEgresoDescuento)) {
                    egresoDescuentoListOldEgresoDescuento.setDescuento(null);
                    egresoDescuentoListOldEgresoDescuento = em.merge(egresoDescuentoListOldEgresoDescuento);
                }
            }
            for (EgresoDescuento egresoDescuentoListNewEgresoDescuento : egresoDescuentoListNew) {
                if (!egresoDescuentoListOld.contains(egresoDescuentoListNewEgresoDescuento)) {
                    Descuento oldDescuentoOfEgresoDescuentoListNewEgresoDescuento = egresoDescuentoListNewEgresoDescuento.getDescuento();
                    egresoDescuentoListNewEgresoDescuento.setDescuento(descuento);
                    egresoDescuentoListNewEgresoDescuento = em.merge(egresoDescuentoListNewEgresoDescuento);
                    if (oldDescuentoOfEgresoDescuentoListNewEgresoDescuento != null && !oldDescuentoOfEgresoDescuentoListNewEgresoDescuento.equals(descuento)) {
                        oldDescuentoOfEgresoDescuentoListNewEgresoDescuento.getEgresoDescuentoList().remove(egresoDescuentoListNewEgresoDescuento);
                        oldDescuentoOfEgresoDescuentoListNewEgresoDescuento = em.merge(oldDescuentoOfEgresoDescuentoListNewEgresoDescuento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = descuento.getIdDescuento();
                if (findDescuento(id) == null) {
                    throw new NonexistentEntityException("The descuento with id " + id + " no longer exists.");
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
            Descuento descuento;
            try {
                descuento = em.getReference(Descuento.class, id);
                descuento.getIdDescuento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The descuento with id " + id + " no longer exists.", enfe);
            }
            Vigencia vigencia = descuento.getVigencia();
            if (vigencia != null) {
                vigencia.getDescuentoList().remove(descuento);
                vigencia = em.merge(vigencia);
            }
            List<EgresoDescuento> egresoDescuentoList = descuento.getEgresoDescuentoList();
            for (EgresoDescuento egresoDescuentoListEgresoDescuento : egresoDescuentoList) {
                egresoDescuentoListEgresoDescuento.setDescuento(null);
                egresoDescuentoListEgresoDescuento = em.merge(egresoDescuentoListEgresoDescuento);
            }
            em.remove(descuento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Descuento> findDescuentoEntities() {
        return findDescuentoEntities(true, -1, -1);
    }

    public List<Descuento> findDescuentoEntities(int maxResults, int firstResult) {
        return findDescuentoEntities(false, maxResults, firstResult);
    }

    private List<Descuento> findDescuentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Descuento.class));
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

    public Descuento findDescuento(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Descuento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDescuentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Descuento> rt = cq.from(Descuento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
