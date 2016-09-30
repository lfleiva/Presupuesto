/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Traslado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.modelo.TrasladoRubro;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis Fernando Leiva
 */
public class TrasladoJpaController implements Serializable {

    public TrasladoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Traslado traslado) {
        if (traslado.getTrasladoRubroList() == null) {
            traslado.setTrasladoRubroList(new ArrayList<TrasladoRubro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vigencia vigencia = traslado.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                traslado.setVigencia(vigencia);
            }
            List<TrasladoRubro> attachedTrasladoRubroList = new ArrayList<TrasladoRubro>();
            for (TrasladoRubro trasladoRubroListTrasladoRubroToAttach : traslado.getTrasladoRubroList()) {
                trasladoRubroListTrasladoRubroToAttach = em.getReference(trasladoRubroListTrasladoRubroToAttach.getClass(), trasladoRubroListTrasladoRubroToAttach.getIdTrasladoRubro());
                attachedTrasladoRubroList.add(trasladoRubroListTrasladoRubroToAttach);
            }
            traslado.setTrasladoRubroList(attachedTrasladoRubroList);
            em.persist(traslado);
            if (vigencia != null) {
                vigencia.getTrasladoList().add(traslado);
                vigencia = em.merge(vigencia);
            }
            for (TrasladoRubro trasladoRubroListTrasladoRubro : traslado.getTrasladoRubroList()) {
                Traslado oldTrasladoOfTrasladoRubroListTrasladoRubro = trasladoRubroListTrasladoRubro.getTraslado();
                trasladoRubroListTrasladoRubro.setTraslado(traslado);
                trasladoRubroListTrasladoRubro = em.merge(trasladoRubroListTrasladoRubro);
                if (oldTrasladoOfTrasladoRubroListTrasladoRubro != null) {
                    oldTrasladoOfTrasladoRubroListTrasladoRubro.getTrasladoRubroList().remove(trasladoRubroListTrasladoRubro);
                    oldTrasladoOfTrasladoRubroListTrasladoRubro = em.merge(oldTrasladoOfTrasladoRubroListTrasladoRubro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Traslado traslado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Traslado persistentTraslado = em.find(Traslado.class, traslado.getIdTraslado());
            Vigencia vigenciaOld = persistentTraslado.getVigencia();
            Vigencia vigenciaNew = traslado.getVigencia();
            List<TrasladoRubro> trasladoRubroListOld = persistentTraslado.getTrasladoRubroList();
            List<TrasladoRubro> trasladoRubroListNew = traslado.getTrasladoRubroList();
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                traslado.setVigencia(vigenciaNew);
            }
            List<TrasladoRubro> attachedTrasladoRubroListNew = new ArrayList<TrasladoRubro>();
            for (TrasladoRubro trasladoRubroListNewTrasladoRubroToAttach : trasladoRubroListNew) {
                trasladoRubroListNewTrasladoRubroToAttach = em.getReference(trasladoRubroListNewTrasladoRubroToAttach.getClass(), trasladoRubroListNewTrasladoRubroToAttach.getIdTrasladoRubro());
                attachedTrasladoRubroListNew.add(trasladoRubroListNewTrasladoRubroToAttach);
            }
            trasladoRubroListNew = attachedTrasladoRubroListNew;
            traslado.setTrasladoRubroList(trasladoRubroListNew);
            traslado = em.merge(traslado);
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getTrasladoList().remove(traslado);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getTrasladoList().add(traslado);
                vigenciaNew = em.merge(vigenciaNew);
            }
            for (TrasladoRubro trasladoRubroListOldTrasladoRubro : trasladoRubroListOld) {
                if (!trasladoRubroListNew.contains(trasladoRubroListOldTrasladoRubro)) {
                    trasladoRubroListOldTrasladoRubro.setTraslado(null);
                    trasladoRubroListOldTrasladoRubro = em.merge(trasladoRubroListOldTrasladoRubro);
                }
            }
            for (TrasladoRubro trasladoRubroListNewTrasladoRubro : trasladoRubroListNew) {
                if (!trasladoRubroListOld.contains(trasladoRubroListNewTrasladoRubro)) {
                    Traslado oldTrasladoOfTrasladoRubroListNewTrasladoRubro = trasladoRubroListNewTrasladoRubro.getTraslado();
                    trasladoRubroListNewTrasladoRubro.setTraslado(traslado);
                    trasladoRubroListNewTrasladoRubro = em.merge(trasladoRubroListNewTrasladoRubro);
                    if (oldTrasladoOfTrasladoRubroListNewTrasladoRubro != null && !oldTrasladoOfTrasladoRubroListNewTrasladoRubro.equals(traslado)) {
                        oldTrasladoOfTrasladoRubroListNewTrasladoRubro.getTrasladoRubroList().remove(trasladoRubroListNewTrasladoRubro);
                        oldTrasladoOfTrasladoRubroListNewTrasladoRubro = em.merge(oldTrasladoOfTrasladoRubroListNewTrasladoRubro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = traslado.getIdTraslado();
                if (findTraslado(id) == null) {
                    throw new NonexistentEntityException("The traslado with id " + id + " no longer exists.");
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
            Traslado traslado;
            try {
                traslado = em.getReference(Traslado.class, id);
                traslado.getIdTraslado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The traslado with id " + id + " no longer exists.", enfe);
            }
            Vigencia vigencia = traslado.getVigencia();
            if (vigencia != null) {
                vigencia.getTrasladoList().remove(traslado);
                vigencia = em.merge(vigencia);
            }
            List<TrasladoRubro> trasladoRubroList = traslado.getTrasladoRubroList();
            for (TrasladoRubro trasladoRubroListTrasladoRubro : trasladoRubroList) {
                trasladoRubroListTrasladoRubro.setTraslado(null);
                trasladoRubroListTrasladoRubro = em.merge(trasladoRubroListTrasladoRubro);
            }
            em.remove(traslado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Traslado> findTrasladoEntities() {
        return findTrasladoEntities(true, -1, -1);
    }

    public List<Traslado> findTrasladoEntities(int maxResults, int firstResult) {
        return findTrasladoEntities(false, maxResults, firstResult);
    }

    private List<Traslado> findTrasladoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Traslado.class));
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

    public Traslado findTraslado(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Traslado.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrasladoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Traslado> rt = cq.from(Traslado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
