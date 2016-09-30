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
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.Ops;
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
public class OpsJpaController implements Serializable {
    
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;
    
    public OpsJpaController() {}
    
    public OpsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public BigDecimal consultarMaximoRegistro(Vigencia vigencia){
        BigDecimal maximo = new BigDecimal(0);
        
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT MAX(O.consecutivo) FROM Ops O ");
            sql.append("WHERE O.vigencia = :vigencia");
            
            Query consulta = entityManager.createQuery(sql.toString());
            consulta.setParameter("vigencia", vigencia);
            
            if(consulta.getSingleResult() != null) {
                maximo = new BigDecimal(consulta.getSingleResult().toString());
            }
                        
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        
        return maximo;
    }

    public void create(Ops ops) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disponibilidad disponibilidad = ops.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad = em.getReference(disponibilidad.getClass(), disponibilidad.getIdDisponibilidad());
                ops.setDisponibilidad(disponibilidad);
            }
            Vigencia vigencia = ops.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                ops.setVigencia(vigencia);
            }
            em.persist(ops);
            if (disponibilidad != null) {
                disponibilidad.getOpsList().add(ops);
                disponibilidad = em.merge(disponibilidad);
            }
            if (vigencia != null) {
                vigencia.getOpsList().add(ops);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ops ops) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ops persistentOps = em.find(Ops.class, ops.getIdOps());
            Disponibilidad disponibilidadOld = persistentOps.getDisponibilidad();
            Disponibilidad disponibilidadNew = ops.getDisponibilidad();
            Vigencia vigenciaOld = persistentOps.getVigencia();
            Vigencia vigenciaNew = ops.getVigencia();
            if (disponibilidadNew != null) {
                disponibilidadNew = em.getReference(disponibilidadNew.getClass(), disponibilidadNew.getIdDisponibilidad());
                ops.setDisponibilidad(disponibilidadNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                ops.setVigencia(vigenciaNew);
            }
            ops = em.merge(ops);
            if (disponibilidadOld != null && !disponibilidadOld.equals(disponibilidadNew)) {
                disponibilidadOld.getOpsList().remove(ops);
                disponibilidadOld = em.merge(disponibilidadOld);
            }
            if (disponibilidadNew != null && !disponibilidadNew.equals(disponibilidadOld)) {
                disponibilidadNew.getOpsList().add(ops);
                disponibilidadNew = em.merge(disponibilidadNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getOpsList().remove(ops);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getOpsList().add(ops);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = ops.getIdOps();
                if (findOps(id) == null) {
                    throw new NonexistentEntityException("The ops with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Ops ops) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            
            try {
                ops = entityManager.getReference(Ops.class, ops.getIdOps());
                ops.getIdOps();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ops with id " + ops.getIdOps() + " no longer exists.", enfe);
            }
            Disponibilidad disponibilidad = ops.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad.getOpsList().remove(ops);
                disponibilidad = entityManager.merge(disponibilidad);
            }
            Vigencia vigencia = ops.getVigencia();
            if (vigencia != null) {
                vigencia.getOpsList().remove(ops);
                vigencia = entityManager.merge(vigencia);
            }
            entityManager.remove(ops);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<Ops> findOpsEntities() {
        return findOpsEntities(true, -1, -1);
    }

    public List<Ops> findOpsEntities(int maxResults, int firstResult) {
        return findOpsEntities(false, maxResults, firstResult);
    }

    private List<Ops> findOpsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ops.class));
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

    public Ops findOps(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ops.class, id);
        } finally {
            em.close();
        }
    }

    public int getOpsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ops> rt = cq.from(Ops.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
