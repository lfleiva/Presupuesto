package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DisponibilidadRubro.class)
public abstract class DisponibilidadRubro_ {

	public static volatile SingularAttribute<DisponibilidadRubro, Vigencia> vigencia;
	public static volatile SingularAttribute<DisponibilidadRubro, Rubro> rubro;
	public static volatile SingularAttribute<DisponibilidadRubro, Disponibilidad> disponibilidad;
	public static volatile SingularAttribute<DisponibilidadRubro, BigDecimal> idDisponibilidadRubro;
	public static volatile SingularAttribute<DisponibilidadRubro, BigDecimal> valor;

}

