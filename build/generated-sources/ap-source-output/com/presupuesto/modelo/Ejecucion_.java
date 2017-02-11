package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ejecucion.class)
public abstract class Ejecucion_ {

	public static volatile SingularAttribute<Ejecucion, Vigencia> vigencia;
	public static volatile SingularAttribute<Ejecucion, Rubro> rubro;
	public static volatile SingularAttribute<Ejecucion, BigDecimal> presupuestoFinal;
	public static volatile SingularAttribute<Ejecucion, BigDecimal> gastoTotal;
	public static volatile SingularAttribute<Ejecucion, BigDecimal> adiciones;
	public static volatile SingularAttribute<Ejecucion, BigDecimal> presupuestoInicial;
	public static volatile SingularAttribute<Ejecucion, BigDecimal> contracreditos;
	public static volatile SingularAttribute<Ejecucion, BigDecimal> creditos;
	public static volatile SingularAttribute<Ejecucion, BigDecimal> saldo;
	public static volatile SingularAttribute<Ejecucion, BigDecimal> idEjecucion;

}

