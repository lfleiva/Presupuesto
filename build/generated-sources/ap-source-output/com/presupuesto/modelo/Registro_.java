package com.presupuesto.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Registro.class)
public abstract class Registro_ {

	public static volatile SingularAttribute<Registro, BigDecimal> consecutivo;
	public static volatile SingularAttribute<Registro, Vigencia> vigencia;
	public static volatile SingularAttribute<Registro, Date> fecha;
	public static volatile SingularAttribute<Registro, Disponibilidad> disponibilidad;
	public static volatile SingularAttribute<Registro, String> objeto;
	public static volatile SingularAttribute<Registro, BigDecimal> idRegistro;

}

