package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TipoRubro.class)
public abstract class TipoRubro_ {

	public static volatile SingularAttribute<TipoRubro, String> tipoRubro;
	public static volatile ListAttribute<TipoRubro, Rubro> rubroList;
	public static volatile SingularAttribute<TipoRubro, BigDecimal> idTipoRubro;

}

