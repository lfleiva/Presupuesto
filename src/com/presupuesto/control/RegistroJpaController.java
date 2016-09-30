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
import com.presupuesto.modelo.Registro;
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
public class RegistroJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;
    
    public RegistroJpaController() {}
    
    public RegistroJpaController(EntityManagerFactory emf) {
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
            sql.append("SELECT MAX(R.consecutivo) FROM Registro R ");
            sql.append("WHERE R.vigencia = :vigencia");
            
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
    
    public void create(Registro registro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disponibilidad disponibilidad = registro.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad = em.getReference(disponibilidad.getClass(), disponibilidad.getIdDisponibilidad());
                registro.setDisponibilidad(disponibilidad);
            }
            Vigencia vigencia = registro.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                registro.setVigencia(vigencia);
            }
            em.persist(registro);
            if (disponibilidad != null) {
                disponibilidad.getRegistroList().add(registro);
                disponibilidad = em.merge(disponibilidad);
            }
            if (vigencia != null) {
                vigencia.getRegistroList().add(registro);
                vigencia = em.merge(vigencia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Registro registro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Registro persistentRegistro = em.find(Registro.class, registro.getIdRegistro());
            Disponibilidad disponibilidadOld = persistentRegistro.getDisponibilidad();
            Disponibilidad disponibilidadNew = registro.getDisponibilidad();
            Vigencia vigenciaOld = persistentRegistro.getVigencia();
            Vigencia vigenciaNew = registro.getVigencia();
            if (disponibilidadNew != null) {
                disponibilidadNew = em.getReference(disponibilidadNew.getClass(), disponibilidadNew.getIdDisponibilidad());
                registro.setDisponibilidad(disponibilidadNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                registro.setVigencia(vigenciaNew);
            }
            registro = em.merge(registro);
            if (disponibilidadOld != null && !disponibilidadOld.equals(disponibilidadNew)) {
                disponibilidadOld.getRegistroList().remove(registro);
                disponibilidadOld = em.merge(disponibilidadOld);
            }
            if (disponibilidadNew != null && !disponibilidadNew.equals(disponibilidadOld)) {
                disponibilidadNew.getRegistroList().add(registro);
                disponibilidadNew = em.merge(disponibilidadNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getRegistroList().remove(registro);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getRegistroList().add(registro);
                vigenciaNew = em.merge(vigenciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = registro.getIdRegistro();
                if (findRegistro(id) == null) {
                    throw new NonexistentEntityException("The registro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Registro registro) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            
            try {
                registro = entityManager.getReference(Registro.class, registro.getIdRegistro());
                registro.getIdRegistro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The registro with id " + registro.getIdRegistro() + " no longer exists.", enfe);
            }
            Disponibilidad disponibilidad = registro.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad.getRegistroList().remove(registro);
                disponibilidad = entityManager.merge(disponibilidad);
            }
            Vigencia vigencia = registro.getVigencia();
            if (vigencia != null) {
                vigencia.getRegistroList().remove(registro);
                vigencia = entityManager.merge(vigencia);
            }
            entityManager.remove(registro);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<Registro> findRegistroEntities() {
        return findRegistroEntities(true, -1, -1);
    }

    public List<Registro> findRegistroEntities(int maxResults, int firstResult) {
        return findRegistroEntities(false, maxResults, firstResult);
    }

    private List<Registro> findRegistroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Registro.class));
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

    public Registro findRegistro(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Registro.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegistroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Registro> rt = cq.from(Registro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
