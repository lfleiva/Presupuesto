package com.presupuesto.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Adicion.class)
public abstract class Adicion_ {

	public static volatile SingularAttribute<Adicion, String> descripcion;
	public static volatile SingularAttribute<Adicion, Vigencia> vigencia;
	public static volatile SingularAttribute<Adicion, Date> fecha;
	public static volatile SingularAttribute<Adicion, BigDecimal> idAdicion;
	public static volatile SingularAttribute<Adicion, String> documento;
	public static volatile ListAttribute<Adicion, AdicionRubro> adicionRubroList;

}

