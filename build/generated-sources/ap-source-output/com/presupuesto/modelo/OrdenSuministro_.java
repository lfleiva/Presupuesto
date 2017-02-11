package com.presupuesto.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrdenSuministro.class)
public abstract class OrdenSuministro_ {

	public static volatile SingularAttribute<OrdenSuministro, BigDecimal> consecutivo;
	public static volatile SingularAttribute<OrdenSuministro, Vigencia> vigencia;
	public static volatile SingularAttribute<OrdenSuministro, Date> fecha;
	public static volatile SingularAttribute<OrdenSuministro, Disponibilidad> disponibilidad;
	public static volatile ListAttribute<OrdenSuministro, Suministro> suministroList;
	public static volatile SingularAttribute<OrdenSuministro, BigDecimal> idOrdenSuministro;
	public static volatile SingularAttribute<OrdenSuministro, String> objeto;

}

