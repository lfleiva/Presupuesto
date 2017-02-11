package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Acceso.class)
public abstract class Acceso_ {

	public static volatile SingularAttribute<Acceso, String> password;
	public static volatile SingularAttribute<Acceso, String> usuario;
	public static volatile SingularAttribute<Acceso, BigDecimal> idAcceso;

}

