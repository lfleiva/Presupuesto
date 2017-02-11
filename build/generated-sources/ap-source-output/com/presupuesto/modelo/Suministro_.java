package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Suministro.class)
public abstract class Suministro_ {

	public static volatile SingularAttribute<Suministro, Vigencia> vigencia;
	public static volatile SingularAttribute<Suministro, String> unidad;
	public static volatile SingularAttribute<Suministro, BigDecimal> idSuministro;
	public static volatile SingularAttribute<Suministro, OrdenSuministro> ordenSuministro;
	public static volatile SingularAttribute<Suministro, BigDecimal> cantidad;
	public static volatile SingularAttribute<Suministro, String> detalle;

}

