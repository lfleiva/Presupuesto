package com.presupuesto.modelo;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Beneficiario.class)
public abstract class Beneficiario_ {

	public static volatile SingularAttribute<Beneficiario, BigDecimal> idBeneficiario;
	public static volatile SingularAttribute<Beneficiario, String> direccion;
	public static volatile SingularAttribute<Beneficiario, String> identificacion;
	public static volatile ListAttribute<Beneficiario, Disponibilidad> disponibilidadList;
	public static volatile SingularAttribute<Beneficiario, String> telefono;
	public static volatile SingularAttribute<Beneficiario, String> nombre;
	public static volatile SingularAttribute<Beneficiario, String> email;

}

