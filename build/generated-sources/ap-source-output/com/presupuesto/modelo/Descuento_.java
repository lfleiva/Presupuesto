package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Descuento.class)
public abstract class Descuento_ {

	public static volatile SingularAttribute<Descuento, Vigencia> vigencia;
	public static volatile SingularAttribute<Descuento, BigDecimal> idDescuento;
	public static volatile SingularAttribute<Descuento, BigDecimal> tipoDescuento;
	public static volatile SingularAttribute<Descuento, String> nombre;
	public static volatile ListAttribute<Descuento, EgresoDescuento> egresoDescuentoList;

}

