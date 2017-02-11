package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Presupuesto.class)
public abstract class Presupuesto_ {

	public static volatile SingularAttribute<Presupuesto, Vigencia> vigencia;
	public static volatile SingularAttribute<Presupuesto, Rubro> rubro;
	public static volatile SingularAttribute<Presupuesto, BigDecimal> idPresupuesto;
	public static volatile SingularAttribute<Presupuesto, BigDecimal> valor;
	public static volatile SingularAttribute<Presupuesto, BigDecimal> orden;

}

