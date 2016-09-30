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
import com.presupuesto.modelo.Adicion;
import com.presupuesto.modelo.AdicionRubro;
import com.presupuesto.modelo.Vigencia;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis Fernando Leiva
 */
public class AdicionRubroJpaController1 implements Serializable {

    public AdicionRubroJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AdicionRubro adicionRubro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro rubro = adicionRubro.getRubro();
            if (rubro != null) {
                rubro = em.getReference(rubro.getClass(), rubro.getIdRubro());
                adicionRubro.setRubro(rubro);
            }
            Adicion adicion = adicionRubro.getAdicion();
            if (adicion != null) {
                adicion = em.getReference(adicion.getClass(), adicion.getIdAdicion());
                adicionRubro.setAdicion(adicion);
            }
            Vigencia vigencia = adicionRubro.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                adicionRubro.setVigencia(vigencia);
            }
            em.persist(adicionRubro);
            if (rubro != null) {
                rubro.getAdicionRubroList().add(adicionRubro);
                rubro = em.merge(rubro);
            }
            if (adicion != null) {
                adicion.getAdicionRubroList().add(adicionRubro);
                adicion = em.merge(adicion);
            }
            if (vigencia != null) {
                vigencia.getAdicionRubroList().add(adicionRubro);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AdicionRubro adicionRubro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AdicionRubro persistentAdicionRubro = em.find(AdicionRubro.class, adicionRubro.getIdAdicionRubro());
            Rubro rubroOld = persistentAdicionRubro.getRubro();
            Rubro rubroNew = adicionRubro.getRubro();
            Adicion adicionOld = persistentAdicionRubro.getAdicion();
            Adicion adicionNew = adicionRubro.getAdicion();
            Vigencia vigenciaOld = persistentAdicionRubro.getVigencia();
            Vigencia vigenciaNew = adicionRubro.getVigencia();
            if (rubroNew != null) {
                rubroNew = em.getReference(rubroNew.getClass(), rubroNew.getIdRubro());
                adicionRubro.setRubro(rubroNew);
            }
            if (adicionNew != null) {
                adicionNew = em.getReference(adicionNew.getClass(), adicionNew.getIdAdicion());
                adicionRubro.setAdicion(adicionNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                adicionRubro.setVigencia(vigenciaNew);
            }
            adicionRubro = em.merge(adicionRubro);
            if (rubroOld != null && !rubroOld.equals(rubroNew)) {
                rubroOld.getAdicionRubroList().remove(adicionRubro);
                rubroOld = em.merge(rubroOld);
            }
            if (rubroNew != null && !rubroNew.equals(rubroOld)) {
                rubroNew.getAdicionRubroList().add(adicionRubro);
                rubroNew = em.merge(rubroNew);
            }
            if (adicionOld != null && !adicionOld.equals(adicionNew)) {
                adicionOld.getAdicionRubroList().remove(adicionRubro);
                adicionOld = em.merge(adicionOld);
            }
            if (adicionNew != null && !adicionNew.equals(adicionOld)) {
                adicionNew.getAdicionRubroList().add(adicionRubro);
                adicionNew = em.merge(adicionNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getAdicionRubroList().remove(adicionRubro);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getAdicionRubroList().add(adicionRubro);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = adicionRubro.getIdAdicionRubro();
                if (findAdicionRubro(id) == null) {
                    throw new NonexistentEntityException("The adicionRubro with id " + id + " no longer exists.");
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
            AdicionRubro adicionRubro;
            try {
                adicionRubro = em.getReference(AdicionRubro.class, id);
                adicionRubro.getIdAdicionRubro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adicionRubro with id " + id + " no longer exists.", enfe);
            }
            Rubro rubro = adicionRubro.getRubro();
            if (rubro != null) {
                rubro.getAdicionRubroList().remove(adicionRubro);
                rubro = em.merge(rubro);
            }
            Adicion adicion = adicionRubro.getAdicion();
            if (adicion != null) {
                adicion.getAdicionRubroList().remove(adicionRubro);
                adicion = em.merge(adicion);
            }
            Vigencia vigencia = adicionRubro.getVigencia();
            if (vigencia != null) {
                vigencia.getAdicionRubroList().remove(adicionRubro);
                vigencia = em.merge(vigencia);
            }
            em.remove(adicionRubro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AdicionRubro> findAdicionRubroEntities() {
        return findAdicionRubroEntities(true, -1, -1);
    }

    public List<AdicionRubro> findAdicionRubroEntities(int maxResults, int firstResult) {
        return findAdicionRubroEntities(false, maxResults, firstResult);
    }

    private List<AdicionRubro> findAdicionRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AdicionRubro.class));
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

    public AdicionRubro findAdicionRubro(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AdicionRubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdicionRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AdicionRubro> rt = cq.from(AdicionRubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
