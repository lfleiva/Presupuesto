package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TrasladoRubro.class)
public abstract class TrasladoRubro_ {

	public static volatile SingularAttribute<TrasladoRubro, Vigencia> vigencia;
	public static volatile SingularAttribute<TrasladoRubro, Rubro> rubro;
	public static volatile SingularAttribute<TrasladoRubro, String> tipo;
	public static volatile SingularAttribute<TrasladoRubro, BigDecimal> idTrasladoRubro;
	public static volatile SingularAttribute<TrasladoRubro, BigDecimal> valor;
	public static volatile SingularAttribute<TrasladoRubro, Traslado> traslado;

}

