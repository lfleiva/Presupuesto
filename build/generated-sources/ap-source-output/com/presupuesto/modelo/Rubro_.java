package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Rubro.class)
public abstract class Rubro_ {

	public static volatile SingularAttribute<Rubro, Vigencia> vigencia;
	public static volatile SingularAttribute<Rubro, String> codigo;
	public static volatile ListAttribute<Rubro, DisponibilidadRubro> disponibilidadRubroList;
	public static volatile SingularAttribute<Rubro, BigDecimal> valor;
	public static volatile ListAttribute<Rubro, Rubro> rubroList1;
	public static volatile SingularAttribute<Rubro, BigDecimal> idRubro;
	public static volatile SingularAttribute<Rubro, String> nombre;
	public static volatile SingularAttribute<Rubro, Rubro> subcuenta;
	public static volatile ListAttribute<Rubro, Ejecucion> ejecucionList;
	public static volatile SingularAttribute<Rubro, TipoRubro> tipoRubro;
	public static volatile ListAttribute<Rubro, Rubro> rubroList;
	public static volatile ListAttribute<Rubro, Presupuesto> presupuestoList;
	public static volatile SingularAttribute<Rubro, Rubro> cuenta;
	public static volatile ListAttribute<Rubro, AdicionRubro> adicionRubroList;
	public static volatile ListAttribute<Rubro, TrasladoRubro> trasladoRubroList;

}

