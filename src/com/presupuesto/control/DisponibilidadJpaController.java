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
import com.presupuesto.modelo.Beneficiario;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.modelo.DisponibilidadRubro;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Luis Fernando Leiva
 */
public class DisponibilidadJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;
    private DisponibilidadRubroJpaController disponibilidadRubroJpaController;    
    
    public DisponibilidadJpaController() {}
    
    public DisponibilidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public BigDecimal consultarMaximoDisponibilidad(Vigencia vigencia){
        BigDecimal maximo = new BigDecimal(0);
        
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT MAX(D.consecutivo) FROM Disponibilidad D ");
            sql.append("WHERE D.vigencia = :vigencia");
            
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
    
    public void create(Disponibilidad disponibilidad) {
        if (disponibilidad.getDisponibilidadRubroList() == null) {
            disponibilidad.setDisponibilidadRubroList(new ArrayList<DisponibilidadRubro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Beneficiario beneficiario = disponibilidad.getBeneficiario();
            if (beneficiario != null) {
                beneficiario = em.getReference(beneficiario.getClass(), beneficiario.getIdBeneficiario());
                disponibilidad.setBeneficiario(beneficiario);
            }
            Vigencia vigencia = disponibilidad.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                disponibilidad.setVigencia(vigencia);
            }
            List<DisponibilidadRubro> attachedDisponibilidadRubroList = new ArrayList<DisponibilidadRubro>();
            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubroToAttach : disponibilidad.getDisponibilidadRubroList()) {
                disponibilidadRubroListDisponibilidadRubroToAttach = em.getReference(disponibilidadRubroListDisponibilidadRubroToAttach.getClass(), disponibilidadRubroListDisponibilidadRubroToAttach.getIdDisponibilidadRubro());
                attachedDisponibilidadRubroList.add(disponibilidadRubroListDisponibilidadRubroToAttach);
            }
            disponibilidad.setDisponibilidadRubroList(attachedDisponibilidadRubroList);
            em.persist(disponibilidad);
            if (beneficiario != null) {
                beneficiario.getDisponibilidadList().add(disponibilidad);
                beneficiario = em.merge(beneficiario);
            }
            if (vigencia != null) {
                vigencia.getDisponibilidadList().add(disponibilidad);
                vigencia = em.merge(vigencia);
            }
            for (DisponibilidadRubro disponibilidadRubroListDisponibilidadRubro : disponibilidad.getDisponibilidadRubroList()) {
                Disponibilidad oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro = disponibilidadRubroListDisponibilidadRubro.getDisponibilidad();
                disponibilidadRubroListDisponibilidadRubro.setDisponibilidad(disponibilidad);
                disponibilidadRubroListDisponibilidadRubro = em.merge(disponibilidadRubroListDisponibilidadRubro);
                if (oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro != null) {
                    oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro.getDisponibilidadRubroList().remove(disponibilidadRubroListDisponibilidadRubro);
                    oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro = em.merge(oldDisponibilidadOfDisponibilidadRubroListDisponibilidadRubro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Disponibilidad disponibilidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disponibilidad persistentDisponibilidad = em.find(Disponibilidad.class, disponibilidad.getIdDisponibilidad());
            Beneficiario beneficiarioOld = persistentDisponibilidad.getBeneficiario();
            Beneficiario beneficiarioNew = disponibilidad.getBeneficiario();
            Vigencia vigenciaOld = persistentDisponibilidad.getVigencia();
            Vigencia vigenciaNew = disponibilidad.getVigencia();
            List<DisponibilidadRubro> disponibilidadRubroListOld = persistentDisponibilidad.getDisponibilidadRubroList();
            List<DisponibilidadRubro> disponibilidadRubroListNew = disponibilidad.getDisponibilidadRubroList();
            if (beneficiarioNew != null) {
                beneficiarioNew = em.getReference(beneficiarioNew.getClass(), beneficiarioNew.getIdBeneficiario());
                disponibilidad.setBeneficiario(beneficiarioNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                disponibilidad.setVigencia(vigenciaNew);
            }
            List<DisponibilidadRubro> attachedDisponibilidadRubroListNew = new ArrayList<DisponibilidadRubro>();
            for (DisponibilidadRubro disponibilidadRubroListNewDisponibilidadRubroToAttach : disponibilidadRubroListNew) {
                disponibilidadRubroListNewDisponibilidadRubroToAttach = em.getReference(disponibilidadRubroListNewDisponibilidadRubroToAttach.getClass(), disponibilidadRubroListNewDisponibilidadRubroToAttach.getIdDisponibilidadRubro());
                attachedDisponibilidadRubroListNew.add(disponibilidadRubroListNewDisponibilidadRubroToAttach);
            }
            disponibilidadRubroListNew = attachedDisponibilidadRubroListNew;
            disponibilidad.setDisponibilidadRubroList(disponibilidadRubroListNew);
            disponibilidad = em.merge(disponibilidad);
            if (beneficiarioOld != null && !beneficiarioOld.equals(beneficiarioNew)) {
                beneficiarioOld.getDisponibilidadList().remove(disponibilidad);
                beneficiarioOld = em.merge(beneficiarioOld);
            }
            if (beneficiarioNew != null && !beneficiarioNew.equals(beneficiarioOld)) {
                beneficiarioNew.getDisponibilidadList().add(disponibilidad);
                beneficiarioNew = em.merge(beneficiarioNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getDisponibilidadList().remove(disponibilidad);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getDisponibilidadList().add(disponibilidad);
                vigenciaNew = em.merge(vigenciaNew);
            }
            for (DisponibilidadRubro disponibilidadRubroListOldDisponibilidadRubro : disponibilidadRubroListOld) {
                if (!disponibilidadRubroListNew.contains(disponibilidadRubroListOldDisponibilidadRubro)) {
                    disponibilidadRubroListOldDisponibilidadRubro.setDisponibilidad(null);
                    disponibilidadRubroListOldDisponibilidadRubro = em.merge(disponibilidadRubroListOldDisponibilidadRubro);
                }
            }
            for (DisponibilidadRubro disponibilidadRubroListNewDisponibilidadRubro : disponibilidadRubroListNew) {
                if (!disponibilidadRubroListOld.contains(disponibilidadRubroListNewDisponibilidadRubro)) {
                    Disponibilidad oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro = disponibilidadRubroListNewDisponibilidadRubro.getDisponibilidad();
                    disponibilidadRubroListNewDisponibilidadRubro.setDisponibilidad(disponibilidad);
                    disponibilidadRubroListNewDisponibilidadRubro = em.merge(disponibilidadRubroListNewDisponibilidadRubro);
                    if (oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro != null && !oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro.equals(disponibilidad)) {
                        oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro.getDisponibilidadRubroList().remove(disponibilidadRubroListNewDisponibilidadRubro);
                        oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro = em.merge(oldDisponibilidadOfDisponibilidadRubroListNewDisponibilidadRubro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = disponibilidad.getIdDisponibilidad();
                if (findDisponibilidad(id) == null) {
                    throw new NonexistentEntityException("The disponibilidad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Disponibilidad disponibilidad) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            
            try {                
                disponibilidad.getIdDisponibilidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The disponibilidad with id " + disponibilidad.getIdDisponibilidad() + " no longer exists.", enfe);
            }
            Beneficiario beneficiario = disponibilidad.getBeneficiario();
            if (beneficiario != null) {
                beneficiario.getDisponibilidadList().remove(disponibilidad);
                beneficiario = entityManager.merge(beneficiario);
            }
            Vigencia vigencia = disponibilidad.getVigencia();
            if (vigencia != null) {
                vigencia.getDisponibilidadList().remove(disponibilidad);
                vigencia = entityManager.merge(vigencia);
            }
            List<DisponibilidadRubro> disponibilidadRubroList = disponibilidad.getDisponibilidadRubroList();
            while(!disponibilidadRubroList.isEmpty()) {
                disponibilidadRubroJpaController = new DisponibilidadRubroJpaController();
                DisponibilidadRubro disponibilidadRubroListDisponibilidadRubro = disponibilidadRubroList.get(0);
                disponibilidadRubroJpaController.destroy(disponibilidadRubroListDisponibilidadRubro);
            }
            entityManager.remove(entityManager.contains(disponibilidad) ? disponibilidad : entityManager.merge(disponibilidad));
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<Disponibilidad> findDisponibilidadEntities() {
        return findDisponibilidadEntities(true, -1, -1);
    }

    public List<Disponibilidad> findDisponibilidadEntities(int maxResults, int firstResult) {
        return findDisponibilidadEntities(false, maxResults, firstResult);
    }

    private List<Disponibilidad> findDisponibilidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Disponibilidad.class));
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

    public Disponibilidad findDisponibilidad(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Disponibilidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getDisponibilidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Disponibilidad> rt = cq.from(Disponibilidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
