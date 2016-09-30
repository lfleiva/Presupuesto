/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.Adicion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.modelo.AdicionRubro;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis Fernando Leiva
 */
public class AdicionJpaController1 implements Serializable {

    public AdicionJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Adicion adicion) {
        if (adicion.getAdicionRubroList() == null) {
            adicion.setAdicionRubroList(new ArrayList<AdicionRubro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vigencia vigencia = adicion.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                adicion.setVigencia(vigencia);
            }
            List<AdicionRubro> attachedAdicionRubroList = new ArrayList<AdicionRubro>();
            for (AdicionRubro adicionRubroListAdicionRubroToAttach : adicion.getAdicionRubroList()) {
                adicionRubroListAdicionRubroToAttach = em.getReference(adicionRubroListAdicionRubroToAttach.getClass(), adicionRubroListAdicionRubroToAttach.getIdAdicionRubro());
                attachedAdicionRubroList.add(adicionRubroListAdicionRubroToAttach);
            }
            adicion.setAdicionRubroList(attachedAdicionRubroList);
            em.persist(adicion);
            if (vigencia != null) {
                vigencia.getAdicionList().add(adicion);
                vigencia = em.merge(vigencia);
            }
            for (AdicionRubro adicionRubroListAdicionRubro : adicion.getAdicionRubroList()) {
                Adicion oldAdicionOfAdicionRubroListAdicionRubro = adicionRubroListAdicionRubro.getAdicion();
                adicionRubroListAdicionRubro.setAdicion(adicion);
                adicionRubroListAdicionRubro = em.merge(adicionRubroListAdicionRubro);
                if (oldAdicionOfAdicionRubroListAdicionRubro != null) {
                    oldAdicionOfAdicionRubroListAdicionRubro.getAdicionRubroList().remove(adicionRubroListAdicionRubro);
                    oldAdicionOfAdicionRubroListAdicionRubro = em.merge(oldAdicionOfAdicionRubroListAdicionRubro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Adicion adicion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Adicion persistentAdicion = em.find(Adicion.class, adicion.getIdAdicion());
            Vigencia vigenciaOld = persistentAdicion.getVigencia();
            Vigencia vigenciaNew = adicion.getVigencia();
            List<AdicionRubro> adicionRubroListOld = persistentAdicion.getAdicionRubroList();
            List<AdicionRubro> adicionRubroListNew = adicion.getAdicionRubroList();
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                adicion.setVigencia(vigenciaNew);
            }
            List<AdicionRubro> attachedAdicionRubroListNew = new ArrayList<AdicionRubro>();
            for (AdicionRubro adicionRubroListNewAdicionRubroToAttach : adicionRubroListNew) {
                adicionRubroListNewAdicionRubroToAttach = em.getReference(adicionRubroListNewAdicionRubroToAttach.getClass(), adicionRubroListNewAdicionRubroToAttach.getIdAdicionRubro());
                attachedAdicionRubroListNew.add(adicionRubroListNewAdicionRubroToAttach);
            }
            adicionRubroListNew = attachedAdicionRubroListNew;
            adicion.setAdicionRubroList(adicionRubroListNew);
            adicion = em.merge(adicion);
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getAdicionList().remove(adicion);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getAdicionList().add(adicion);
                vigenciaNew = em.merge(vigenciaNew);
            }
            for (AdicionRubro adicionRubroListOldAdicionRubro : adicionRubroListOld) {
                if (!adicionRubroListNew.contains(adicionRubroListOldAdicionRubro)) {
                    adicionRubroListOldAdicionRubro.setAdicion(null);
                    adicionRubroListOldAdicionRubro = em.merge(adicionRubroListOldAdicionRubro);
                }
            }
            for (AdicionRubro adicionRubroListNewAdicionRubro : adicionRubroListNew) {
                if (!adicionRubroListOld.contains(adicionRubroListNewAdicionRubro)) {
                    Adicion oldAdicionOfAdicionRubroListNewAdicionRubro = adicionRubroListNewAdicionRubro.getAdicion();
                    adicionRubroListNewAdicionRubro.setAdicion(adicion);
                    adicionRubroListNewAdicionRubro = em.merge(adicionRubroListNewAdicionRubro);
                    if (oldAdicionOfAdicionRubroListNewAdicionRubro != null && !oldAdicionOfAdicionRubroListNewAdicionRubro.equals(adicion)) {
                        oldAdicionOfAdicionRubroListNewAdicionRubro.getAdicionRubroList().remove(adicionRubroListNewAdicionRubro);
                        oldAdicionOfAdicionRubroListNewAdicionRubro = em.merge(oldAdicionOfAdicionRubroListNewAdicionRubro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = adicion.getIdAdicion();
                if (findAdicion(id) == null) {
                    throw new NonexistentEntityException("The adicion with id " + id + " no longer exists.");
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
            Adicion adicion;
            try {
                adicion = em.getReference(Adicion.class, id);
                adicion.getIdAdicion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adicion with id " + id + " no longer exists.", enfe);
            }
            Vigencia vigencia = adicion.getVigencia();
            if (vigencia != null) {
                vigencia.getAdicionList().remove(adicion);
                vigencia = em.merge(vigencia);
            }
            List<AdicionRubro> adicionRubroList = adicion.getAdicionRubroList();
            for (AdicionRubro adicionRubroListAdicionRubro : adicionRubroList) {
                adicionRubroListAdicionRubro.setAdicion(null);
                adicionRubroListAdicionRubro = em.merge(adicionRubroListAdicionRubro);
            }
            em.remove(adicion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Adicion> findAdicionEntities() {
        return findAdicionEntities(true, -1, -1);
    }

    public List<Adicion> findAdicionEntities(int maxResults, int firstResult) {
        return findAdicionEntities(false, maxResults, firstResult);
    }

    private List<Adicion> findAdicionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Adicion.class));
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

    public Adicion findAdicion(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Adicion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdicionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Adicion> rt = cq.from(Adicion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
