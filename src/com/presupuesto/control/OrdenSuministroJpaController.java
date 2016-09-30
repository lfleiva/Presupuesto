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
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.OrdenSuministro;
import com.presupuesto.modelo.Suministro;
import com.presupuesto.vista.Orden_Suministro_Presupuestal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Luis Fernando Leiva
 */
public class OrdenSuministroJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;

    public OrdenSuministroJpaController() {
    }

    public OrdenSuministroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public BigDecimal consultarMaximoOrdenSuministro(Vigencia vigencia) {
        BigDecimal maximo = new BigDecimal(0);

        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT MAX(O.consecutivo) FROM OrdenSuministro O ");
            sql.append("WHERE O.vigencia = :vigencia");

            Query consulta = entityManager.createQuery(sql.toString());
            consulta.setParameter("vigencia", vigencia);

            if (consulta.getSingleResult() != null) {
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

    public void create(OrdenSuministro ordenSuministro) {
        if (ordenSuministro.getSuministroList() == null) {
            ordenSuministro.setSuministroList(new ArrayList<Suministro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vigencia vigencia = ordenSuministro.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                ordenSuministro.setVigencia(vigencia);
            }
            Disponibilidad disponibilidad = ordenSuministro.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad = em.getReference(disponibilidad.getClass(), disponibilidad.getIdDisponibilidad());
                ordenSuministro.setDisponibilidad(disponibilidad);
            }
            List<Suministro> attachedSuministroList = new ArrayList<Suministro>();
            for (Suministro suministroListSuministroToAttach : ordenSuministro.getSuministroList()) {
                suministroListSuministroToAttach = em.getReference(suministroListSuministroToAttach.getClass(), suministroListSuministroToAttach.getIdSuministro());
                attachedSuministroList.add(suministroListSuministroToAttach);
            }
            ordenSuministro.setSuministroList(attachedSuministroList);
            em.persist(ordenSuministro);
            if (vigencia != null) {
                vigencia.getOrdenSuministroList().add(ordenSuministro);
                vigencia = em.merge(vigencia);
            }
            if (disponibilidad != null) {
                disponibilidad.getOrdenSuministroList().add(ordenSuministro);
                disponibilidad = em.merge(disponibilidad);
            }
            for (Suministro suministroListSuministro : ordenSuministro.getSuministroList()) {
                OrdenSuministro oldOrdenSuministroOfSuministroListSuministro = suministroListSuministro.getOrdenSuministro();
                suministroListSuministro.setOrdenSuministro(ordenSuministro);
                suministroListSuministro = em.merge(suministroListSuministro);
                if (oldOrdenSuministroOfSuministroListSuministro != null) {
                    oldOrdenSuministroOfSuministroListSuministro.getSuministroList().remove(suministroListSuministro);
                    oldOrdenSuministroOfSuministroListSuministro = em.merge(oldOrdenSuministroOfSuministroListSuministro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrdenSuministro ordenSuministro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrdenSuministro persistentOrdenSuministro = em.find(OrdenSuministro.class, ordenSuministro.getIdOrdenSuministro());
            Vigencia vigenciaOld = persistentOrdenSuministro.getVigencia();
            Vigencia vigenciaNew = ordenSuministro.getVigencia();
            Disponibilidad disponibilidadOld = persistentOrdenSuministro.getDisponibilidad();
            Disponibilidad disponibilidadNew = ordenSuministro.getDisponibilidad();
            List<Suministro> suministroListOld = persistentOrdenSuministro.getSuministroList();
            List<Suministro> suministroListNew = ordenSuministro.getSuministroList();
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                ordenSuministro.setVigencia(vigenciaNew);
            }
            if (disponibilidadNew != null) {
                disponibilidadNew = em.getReference(disponibilidadNew.getClass(), disponibilidadNew.getIdDisponibilidad());
                ordenSuministro.setDisponibilidad(disponibilidadNew);
            }
            List<Suministro> attachedSuministroListNew = new ArrayList<Suministro>();
            for (Suministro suministroListNewSuministroToAttach : suministroListNew) {
                suministroListNewSuministroToAttach = em.getReference(suministroListNewSuministroToAttach.getClass(), suministroListNewSuministroToAttach.getIdSuministro());
                attachedSuministroListNew.add(suministroListNewSuministroToAttach);
            }
            suministroListNew = attachedSuministroListNew;
            ordenSuministro.setSuministroList(suministroListNew);
            ordenSuministro = em.merge(ordenSuministro);
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getOrdenSuministroList().remove(ordenSuministro);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getOrdenSuministroList().add(ordenSuministro);
                vigenciaNew = em.merge(vigenciaNew);
            }
            if (disponibilidadOld != null && !disponibilidadOld.equals(disponibilidadNew)) {
                disponibilidadOld.getOrdenSuministroList().remove(ordenSuministro);
                disponibilidadOld = em.merge(disponibilidadOld);
            }
            if (disponibilidadNew != null && !disponibilidadNew.equals(disponibilidadOld)) {
                disponibilidadNew.getOrdenSuministroList().add(ordenSuministro);
                disponibilidadNew = em.merge(disponibilidadNew);
            }
            for (Suministro suministroListOldSuministro : suministroListOld) {
                if (!suministroListNew.contains(suministroListOldSuministro)) {
                    suministroListOldSuministro.setOrdenSuministro(null);
                    suministroListOldSuministro = em.merge(suministroListOldSuministro);
                }
            }
            for (Suministro suministroListNewSuministro : suministroListNew) {
                if (!suministroListOld.contains(suministroListNewSuministro)) {
                    OrdenSuministro oldOrdenSuministroOfSuministroListNewSuministro = suministroListNewSuministro.getOrdenSuministro();
                    suministroListNewSuministro.setOrdenSuministro(ordenSuministro);
                    suministroListNewSuministro = em.merge(suministroListNewSuministro);
                    if (oldOrdenSuministroOfSuministroListNewSuministro != null && !oldOrdenSuministroOfSuministroListNewSuministro.equals(ordenSuministro)) {
                        oldOrdenSuministroOfSuministroListNewSuministro.getSuministroList().remove(suministroListNewSuministro);
                        oldOrdenSuministroOfSuministroListNewSuministro = em.merge(oldOrdenSuministroOfSuministroListNewSuministro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = ordenSuministro.getIdOrdenSuministro();
                if (findOrdenSuministro(id) == null) {
                    throw new NonexistentEntityException("The ordenSuministro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(OrdenSuministro ordenSuministro) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            try {
                ordenSuministro.getIdOrdenSuministro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordenSuministro with id " + ordenSuministro.getIdOrdenSuministro() + " no longer exists.", enfe);
            }
            Vigencia vigencia = ordenSuministro.getVigencia();
            if (vigencia != null) {
                vigencia.getOrdenSuministroList().remove(ordenSuministro);
                vigencia = entityManager.merge(vigencia);
            }
            Disponibilidad disponibilidad = ordenSuministro.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad.getOrdenSuministroList().remove(ordenSuministro);
                disponibilidad = entityManager.merge(disponibilidad);
            }       

            SuministroJpaController suministroController = new SuministroJpaController();
            while (!ordenSuministro.getSuministroList().isEmpty()) {
                try {
                    Suministro suministro = new Suministro();
                    suministro = ordenSuministro.getSuministroList().get(0);
                    suministroController.destroy(suministro);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Orden_Suministro_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            entityManager.remove(entityManager.contains(ordenSuministro) ? ordenSuministro : entityManager.merge(ordenSuministro));
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<OrdenSuministro> findOrdenSuministroEntities() {
        return findOrdenSuministroEntities(true, -1, -1);
    }

    public List<OrdenSuministro> findOrdenSuministroEntities(int maxResults, int firstResult) {
        return findOrdenSuministroEntities(false, maxResults, firstResult);
    }

    private List<OrdenSuministro> findOrdenSuministroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrdenSuministro.class));
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

    public OrdenSuministro findOrdenSuministro(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrdenSuministro.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenSuministroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrdenSuministro> rt = cq.from(OrdenSuministro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
