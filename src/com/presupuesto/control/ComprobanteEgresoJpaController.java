/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presupuesto.control;

import com.presupuesto.control.exceptions.NonexistentEntityException;
import com.presupuesto.modelo.ComprobanteEgreso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.presupuesto.modelo.Disponibilidad;
import com.presupuesto.modelo.Vigencia;
import com.presupuesto.modelo.EgresoDescuento;
import com.presupuesto.vista.Disponibilidad_Presupuestal;
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
public class ComprobanteEgresoJpaController implements Serializable {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PresupuestoPU");
    private EntityManager entityManager;

    public ComprobanteEgresoJpaController() {
    }

    public ComprobanteEgresoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public BigDecimal consultarMaximoRegistro(Vigencia vigencia) {
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

    public void create(ComprobanteEgreso comprobanteEgreso) {
        if (comprobanteEgreso.getEgresoDescuentoList() == null) {
            comprobanteEgreso.setEgresoDescuentoList(new ArrayList<EgresoDescuento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disponibilidad disponibilidad = comprobanteEgreso.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad = em.getReference(disponibilidad.getClass(), disponibilidad.getIdDisponibilidad());
                comprobanteEgreso.setDisponibilidad(disponibilidad);
            }
            Vigencia vigencia = comprobanteEgreso.getVigencia();
            if (vigencia != null) {
                vigencia = em.getReference(vigencia.getClass(), vigencia.getIdVigencia());
                comprobanteEgreso.setVigencia(vigencia);
            }
            List<EgresoDescuento> attachedEgresoDescuentoList = new ArrayList<EgresoDescuento>();
            for (EgresoDescuento egresoDescuentoListEgresoDescuentoToAttach : comprobanteEgreso.getEgresoDescuentoList()) {
                egresoDescuentoListEgresoDescuentoToAttach = em.getReference(egresoDescuentoListEgresoDescuentoToAttach.getClass(), egresoDescuentoListEgresoDescuentoToAttach.getIdEgresoDescuento());
                attachedEgresoDescuentoList.add(egresoDescuentoListEgresoDescuentoToAttach);
            }
            comprobanteEgreso.setEgresoDescuentoList(attachedEgresoDescuentoList);
            em.persist(comprobanteEgreso);
            if (disponibilidad != null) {
                disponibilidad.getComprobanteEgresoList().add(comprobanteEgreso);
                disponibilidad = em.merge(disponibilidad);
            }
            if (vigencia != null) {
                vigencia.getComprobanteEgresoList().add(comprobanteEgreso);
                vigencia = em.merge(vigencia);
            }
            for (EgresoDescuento egresoDescuentoListEgresoDescuento : comprobanteEgreso.getEgresoDescuentoList()) {
                ComprobanteEgreso oldComprobanteEgresoOfEgresoDescuentoListEgresoDescuento = egresoDescuentoListEgresoDescuento.getComprobanteEgreso();
                egresoDescuentoListEgresoDescuento.setComprobanteEgreso(comprobanteEgreso);
                egresoDescuentoListEgresoDescuento = em.merge(egresoDescuentoListEgresoDescuento);
                if (oldComprobanteEgresoOfEgresoDescuentoListEgresoDescuento != null) {
                    oldComprobanteEgresoOfEgresoDescuentoListEgresoDescuento.getEgresoDescuentoList().remove(egresoDescuentoListEgresoDescuento);
                    oldComprobanteEgresoOfEgresoDescuentoListEgresoDescuento = em.merge(oldComprobanteEgresoOfEgresoDescuentoListEgresoDescuento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComprobanteEgreso comprobanteEgreso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComprobanteEgreso persistentComprobanteEgreso = em.find(ComprobanteEgreso.class, comprobanteEgreso.getIdComprobanteEgreso());
            Disponibilidad disponibilidadOld = persistentComprobanteEgreso.getDisponibilidad();
            Disponibilidad disponibilidadNew = comprobanteEgreso.getDisponibilidad();
            Vigencia vigenciaOld = persistentComprobanteEgreso.getVigencia();
            Vigencia vigenciaNew = comprobanteEgreso.getVigencia();
            List<EgresoDescuento> egresoDescuentoListOld = persistentComprobanteEgreso.getEgresoDescuentoList();
            List<EgresoDescuento> egresoDescuentoListNew = comprobanteEgreso.getEgresoDescuentoList();
            if (disponibilidadNew != null) {
                disponibilidadNew = em.getReference(disponibilidadNew.getClass(), disponibilidadNew.getIdDisponibilidad());
                comprobanteEgreso.setDisponibilidad(disponibilidadNew);
            }
            if (vigenciaNew != null) {
                vigenciaNew = em.getReference(vigenciaNew.getClass(), vigenciaNew.getIdVigencia());
                comprobanteEgreso.setVigencia(vigenciaNew);
            }
            List<EgresoDescuento> attachedEgresoDescuentoListNew = new ArrayList<EgresoDescuento>();
            for (EgresoDescuento egresoDescuentoListNewEgresoDescuentoToAttach : egresoDescuentoListNew) {
                egresoDescuentoListNewEgresoDescuentoToAttach = em.getReference(egresoDescuentoListNewEgresoDescuentoToAttach.getClass(), egresoDescuentoListNewEgresoDescuentoToAttach.getIdEgresoDescuento());
                attachedEgresoDescuentoListNew.add(egresoDescuentoListNewEgresoDescuentoToAttach);
            }
            egresoDescuentoListNew = attachedEgresoDescuentoListNew;
            comprobanteEgreso.setEgresoDescuentoList(egresoDescuentoListNew);
            comprobanteEgreso = em.merge(comprobanteEgreso);
            if (disponibilidadOld != null && !disponibilidadOld.equals(disponibilidadNew)) {
                disponibilidadOld.getComprobanteEgresoList().remove(comprobanteEgreso);
                disponibilidadOld = em.merge(disponibilidadOld);
            }
            if (disponibilidadNew != null && !disponibilidadNew.equals(disponibilidadOld)) {
                disponibilidadNew.getComprobanteEgresoList().add(comprobanteEgreso);
                disponibilidadNew = em.merge(disponibilidadNew);
            }
            if (vigenciaOld != null && !vigenciaOld.equals(vigenciaNew)) {
                vigenciaOld.getComprobanteEgresoList().remove(comprobanteEgreso);
                vigenciaOld = em.merge(vigenciaOld);
            }
            if (vigenciaNew != null && !vigenciaNew.equals(vigenciaOld)) {
                vigenciaNew.getComprobanteEgresoList().add(comprobanteEgreso);
                vigenciaNew = em.merge(vigenciaNew);
            }
            for (EgresoDescuento egresoDescuentoListOldEgresoDescuento : egresoDescuentoListOld) {
                if (!egresoDescuentoListNew.contains(egresoDescuentoListOldEgresoDescuento)) {
                    egresoDescuentoListOldEgresoDescuento.setComprobanteEgreso(null);
                    egresoDescuentoListOldEgresoDescuento = em.merge(egresoDescuentoListOldEgresoDescuento);
                }
            }
            for (EgresoDescuento egresoDescuentoListNewEgresoDescuento : egresoDescuentoListNew) {
                if (!egresoDescuentoListOld.contains(egresoDescuentoListNewEgresoDescuento)) {
                    ComprobanteEgreso oldComprobanteEgresoOfEgresoDescuentoListNewEgresoDescuento = egresoDescuentoListNewEgresoDescuento.getComprobanteEgreso();
                    egresoDescuentoListNewEgresoDescuento.setComprobanteEgreso(comprobanteEgreso);
                    egresoDescuentoListNewEgresoDescuento = em.merge(egresoDescuentoListNewEgresoDescuento);
                    if (oldComprobanteEgresoOfEgresoDescuentoListNewEgresoDescuento != null && !oldComprobanteEgresoOfEgresoDescuentoListNewEgresoDescuento.equals(comprobanteEgreso)) {
                        oldComprobanteEgresoOfEgresoDescuentoListNewEgresoDescuento.getEgresoDescuentoList().remove(egresoDescuentoListNewEgresoDescuento);
                        oldComprobanteEgresoOfEgresoDescuentoListNewEgresoDescuento = em.merge(oldComprobanteEgresoOfEgresoDescuentoListNewEgresoDescuento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = comprobanteEgreso.getIdComprobanteEgreso();
                if (findComprobanteEgreso(id) == null) {
                    throw new NonexistentEntityException("The comprobanteEgreso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ComprobanteEgreso comprobante) throws NonexistentEntityException {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            try {
                comprobante.getIdComprobanteEgreso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comprobanteEgreso with id " + comprobante.getIdComprobanteEgreso() + " no longer exists.", enfe);
            }
            Disponibilidad disponibilidad = comprobante.getDisponibilidad();
            if (disponibilidad != null) {
                disponibilidad.getComprobanteEgresoList().remove(comprobante);
                disponibilidad = entityManager.merge(disponibilidad);
            }
            Vigencia vigencia = comprobante.getVigencia();
            if (vigencia != null) {
                vigencia.getComprobanteEgresoList().remove(comprobante);
                vigencia = entityManager.merge(vigencia);
            }
            //List<EgresoDescuento> egresoDescuentoList = comprobante.getEgresoDescuentoList();
            EgresoDescuentoJpaController egresoDescuentoController = new EgresoDescuentoJpaController();

            while (!comprobante.getEgresoDescuentoList().isEmpty()) {
                try {
                    egresoDescuentoController = new EgresoDescuentoJpaController();
                    EgresoDescuento egresoDescuento = new EgresoDescuento();
                    egresoDescuento = comprobante.getEgresoDescuentoList().get(0);

                    egresoDescuentoController.destroy(egresoDescuento);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Disponibilidad_Presupuestal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

//            for (EgresoDescuento egresoDescuentoListEgresoDescuento : egresoDescuentoList) {
//                egresoDescuentoController.destroy(egresoDescuentoListEgresoDescuento);
////                egresoDescuentoListEgresoDescuento.setComprobanteEgreso(null);
////                egresoDescuentoListEgresoDescuento = entityManager.merge(egresoDescuentoListEgresoDescuento);
//            }

            entityManager.remove(entityManager.contains(comprobante) ? comprobante : entityManager.merge(comprobante));            
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public List<ComprobanteEgreso> findComprobanteEgresoEntities() {
        return findComprobanteEgresoEntities(true, -1, -1);
    }

    public List<ComprobanteEgreso> findComprobanteEgresoEntities(int maxResults, int firstResult) {
        return findComprobanteEgresoEntities(false, maxResults, firstResult);
    }

    private List<ComprobanteEgreso> findComprobanteEgresoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComprobanteEgreso.class));
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

    public ComprobanteEgreso findComprobanteEgreso(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComprobanteEgreso.class, id);
        } finally {
            em.close();
        }
    }

    public int getComprobanteEgresoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComprobanteEgreso> rt = cq.from(ComprobanteEgreso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
