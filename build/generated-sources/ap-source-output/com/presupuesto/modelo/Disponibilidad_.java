package com.presupuesto.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Disponibilidad.class)
public abstract class Disponibilidad_ {

	public static volatile SingularAttribute<Disponibilidad, BigDecimal> consecutivo;
	public static volatile SingularAttribute<Disponibilidad, Vigencia> vigencia;
	public static volatile SingularAttribute<Disponibilidad, Date> fecha;
	public static volatile ListAttribute<Disponibilidad, ComprobanteEgreso> comprobanteEgresoList;
	public static volatile ListAttribute<Disponibilidad, DisponibilidadRubro> disponibilidadRubroList;
	public static volatile ListAttribute<Disponibilidad, Registro> registroList;
	public static volatile SingularAttribute<Disponibilidad, String> objeto;
	public static volatile ListAttribute<Disponibilidad, OrdenSuministro> ordenSuministroList;
	public static volatile ListAttribute<Disponibilidad, Ops> opsList;
	public static volatile SingularAttribute<Disponibilidad, BigDecimal> idDisponibilidad;
	public static volatile SingularAttribute<Disponibilidad, Beneficiario> beneficiario;

}

