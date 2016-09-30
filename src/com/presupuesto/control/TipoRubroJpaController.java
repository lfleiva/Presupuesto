/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.control.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.Rubro;
import com.presupuesto.modelo.TipoRubro;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luis Fernando Leiva
 */
public class TipoRubroJpaController implements Serializable {

    public TipoRubroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoRubro tipoRubro) throws PreexistingEntityException, Exception {
        if (tipoRubro.getRubroList() == null) {
            tipoRubro.setRubroList(new ArrayList<Rubro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rubro> attachedRubroList = new ArrayList<Rubro>();
            for (Rubro rubroListRubroToAttach : tipoRubro.getRubroList()) {
                rubroListRubroToAttach = em.getReference(rubroListRubroToAttach.getClass(), rubroListRubroToAttach.getIdRubro());
                attachedRubroList.add(rubroListRubroToAttach);
            }
            tipoRubro.setRubroList(attachedRubroList);
            em.persist(tipoRubro);
            for (Rubro rubroListRubro : tipoRubro.getRubroList()) {
                TipoRubro oldTipoRubroOfRubroListRubro = rubroListRubro.getTipoRubro();
                rubroListRubro.setTipoRubro(tipoRubro);
                rubroListRubro = em.merge(rubroListRubro);
                if (oldTipoRubroOfRubroListRubro != null) {
                    oldTipoRubroOfRubroListRubro.getRubroList().remove(rubroListRubro);
                    oldTipoRubroOfRubroListRubro = em.merge(oldTipoRubroOfRubroListRubro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoRubro(tipoRubro.getIdTipoRubro()) != null) {
                throw new PreexistingEntityException("TipoRubro " + tipoRubro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoRubro tipoRubro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoRubro persistentTipoRubro = em.find(TipoRubro.class, tipoRubro.getIdTipoRubro());
            List<Rubro> rubroListOld = persistentTipoRubro.getRubroList();
            List<Rubro> rubroListNew = tipoRubro.getRubroList();
            List<Rubro> attachedRubroListNew = new ArrayList<Rubro>();
            for (Rubro rubroListNewRubroToAttach : rubroListNew) {
                rubroListNewRubroToAttach = em.getReference(rubroListNewRubroToAttach.getClass(), rubroListNewRubroToAttach.getIdRubro());
                attachedRubroListNew.add(rubroListNewRubroToAttach);
            }
            rubroListNew = attachedRubroListNew;
            tipoRubro.setRubroList(rubroListNew);
            tipoRubro = em.merge(tipoRubro);
            for (Rubro rubroListOldRubro : rubroListOld) {
                if (!rubroListNew.contains(rubroListOldRubro)) {
                    rubroListOldRubro.setTipoRubro(null);
                    rubroListOldRubro = em.merge(rubroListOldRubro);
                }
            }
            for (Rubro rubroListNewRubro : rubroListNew) {
                if (!rubroListOld.contains(rubroListNewRubro)) {
                    TipoRubro oldTipoRubroOfRubroListNewRubro = rubroListNewRubro.getTipoRubro();
                    rubroListNewRubro.setTipoRubro(tipoRubro);
                    rubroListNewRubro = em.merge(rubroListNewRubro);
                    if (oldTipoRubroOfRubroListNewRubro != null && !oldTipoRubroOfRubroListNewRubro.equals(tipoRubro)) {
                        oldTipoRubroOfRubroListNewRubro.getRubroList().remove(rubroListNewRubro);
                        oldTipoRubroOfRubroListNewRubro = em.merge(oldTipoRubroOfRubroListNewRubro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = tipoRubro.getIdTipoRubro();
                if (findTipoRubro(id) == null) {
                    throw new NonexistentEntityException("The tipoRubro with id " + id + " no longer exists.");
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
            TipoRubro tipoRubro;
            try {
                tipoRubro = em.getReference(TipoRubro.class, id);
                tipoRubro.getIdTipoRubro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoRubro with id " + id + " no longer exists.", enfe);
            }
            List<Rubro> rubroList = tipoRubro.getRubroList();
            for (Rubro rubroListRubro : rubroList) {
                rubroListRubro.setTipoRubro(null);
                rubroListRubro = em.merge(rubroListRubro);
            }
            em.remove(tipoRubro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoRubro> findTipoRubroEntities() {
        return findTipoRubroEntities(true, -1, -1);
    }

    public List<TipoRubro> findTipoRubroEntities(int maxResults, int firstResult) {
        return findTipoRubroEntities(false, maxResults, firstResult);
    }

    private List<TipoRubro> findTipoRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoRubro.class));
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

    public TipoRubro findTipoRubro(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoRubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoRubro> rt = cq.from(TipoRubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
