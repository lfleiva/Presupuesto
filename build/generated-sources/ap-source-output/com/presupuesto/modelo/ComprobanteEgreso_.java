package com.presupuesto.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ComprobanteEgreso.class)
public abstract class ComprobanteEgreso_ {

	public static volatile SingularAttribute<ComprobanteEgreso, String> descripcion;
	public static volatile SingularAttribute<ComprobanteEgreso, Vigencia> vigencia;
	public static volatile SingularAttribute<ComprobanteEgreso, BigDecimal> idComprobanteEgreso;
	public static volatile SingularAttribute<ComprobanteEgreso, BigDecimal> valorDescuentos;
	public static volatile ListAttribute<ComprobanteEgreso, EgresoDescuento> egresoDescuentoList;
	public static volatile SingularAttribute<ComprobanteEgreso, BigDecimal> consecutivo;
	public static volatile SingularAttribute<ComprobanteEgreso, Date> fecha;
	public static volatile SingularAttribute<ComprobanteEgreso, Disponibilidad> disponibilidad;
	public static volatile SingularAttribute<ComprobanteEgreso, BigDecimal> valorCuenta;
	public static volatile SingularAttribute<ComprobanteEgreso, String> valorLetras;
	public static volatile SingularAttribute<ComprobanteEgreso, String> cuentaBanco;
	public static volatile SingularAttribute<ComprobanteEgreso, String> cheque;
	public static volatile SingularAttribute<ComprobanteEgreso, BigDecimal> valorPagar;

}

