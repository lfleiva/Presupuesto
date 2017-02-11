package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AdicionRubro.class)
public abstract class AdicionRubro_ {

	public static volatile SingularAttribute<AdicionRubro, Vigencia> vigencia;
	public static volatile SingularAttribute<AdicionRubro, Rubro> rubro;
	public static volatile SingularAttribute<AdicionRubro, BigDecimal> idAdicionRubro;
	public static volatile SingularAttribute<AdicionRubro, BigDecimal> valor;
	public static volatile SingularAttribute<AdicionRubro, Adicion> adicion;

}

