package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Vigencia.class)
public abstract class Vigencia_ {

	public static volatile SingularAttribute<Vigencia, String> vigencia;
	public static volatile SingularAttribute<Vigencia, String> descripcion;
	public static volatile ListAttribute<Vigencia, Adicion> adicionList;
	public static volatile ListAttribute<Vigencia, Suministro> suministroList;
	public static volatile ListAttribute<Vigencia, Descuento> descuentoList;
	public static volatile ListAttribute<Vigencia, DisponibilidadRubro> disponibilidadRubroList;
	public static volatile ListAttribute<Vigencia, Traslado> trasladoList;
	public static volatile ListAttribute<Vigencia, Registro> registroList;
	public static volatile SingularAttribute<Vigencia, BigDecimal> idVigencia;
	public static volatile SingularAttribute<Vigencia, Boolean> activa;
	public static volatile ListAttribute<Vigencia, EgresoDescuento> egresoDescuentoList;
	public static volatile ListAttribute<Vigencia, ComprobanteEgreso> comprobanteEgresoList;
	public static volatile ListAttribute<Vigencia, Ejecucion> ejecucionList;
	public static volatile ListAttribute<Vigencia, Rubro> rubroList;
	public static volatile ListAttribute<Vigencia, Presupuesto> presupuestoList;
	public static volatile ListAttribute<Vigencia, OrdenSuministro> ordenSuministroList;
	public static volatile ListAttribute<Vigencia, Disponibilidad> disponibilidadList;
	public static volatile ListAttribute<Vigencia, Ops> opsList;
	public static volatile ListAttribute<Vigencia, AdicionRubro> adicionRubroList;
	public static volatile ListAttribute<Vigencia, TrasladoRubro> trasladoRubroList;

}

