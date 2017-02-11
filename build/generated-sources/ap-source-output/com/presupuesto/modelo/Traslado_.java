package com.presupuesto.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Traslado.class)
public abstract class Traslado_ {

	public static volatile SingularAttribute<Traslado, String> descripcion;
	public static volatile SingularAttribute<Traslado, Vigencia> vigencia;
	public static volatile SingularAttribute<Traslado, Date> fecha;
	public static volatile SingularAttribute<Traslado, String> documento;
	public static volatile SingularAttribute<Traslado, BigDecimal> idTraslado;
	public static volatile ListAttribute<Traslado, TrasladoRubro> trasladoRubroList;

}

