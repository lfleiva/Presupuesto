package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EgresoDescuento.class)
public abstract class EgresoDescuento_ {

	public static volatile SingularAttribute<EgresoDescuento, Vigencia> vigencia;
	public static volatile SingularAttribute<EgresoDescuento, BigDecimal> idEgresoDescuento;
	public static volatile SingularAttribute<EgresoDescuento, Descuento> descuento;
	public static volatile SingularAttribute<EgresoDescuento, BigDecimal> valor;
	public static volatile SingularAttribute<EgresoDescuento, BigDecimal> porcentaje;
	public static volatile SingularAttribute<EgresoDescuento, ComprobanteEgreso> comprobanteEgreso;

}

