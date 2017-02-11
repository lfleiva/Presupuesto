package com.presupuesto.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ops.class)
public abstract class Ops_ {

	public static volatile SingularAttribute<Ops, BigDecimal> consecutivo;
	public static volatile SingularAttribute<Ops, Vigencia> vigencia;
	public static volatile SingularAttribute<Ops, BigDecimal> idOps;
	public static volatile SingularAttribute<Ops, Date> fecha;
	public static volatile SingularAttribute<Ops, Disponibilidad> disponibilidad;
	public static volatile SingularAttribute<Ops, Date> fechaInicio;
	public static volatile SingularAttribute<Ops, String> plazo;
	public static volatile SingularAttribute<Ops, Date> fechaFirma;
	public static volatile SingularAttribute<Ops, Date> fechaFinal;
	public static volatile SingularAttribute<Ops, String> objeto;

}

