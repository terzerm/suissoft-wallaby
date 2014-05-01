package com.suissoft.wallaby.model;

import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import com.suissoft.model.dao.Dao;
import com.suissoft.model.entity.Entity;

/**
 * Provides access to {@link Dao} instances given the entity type.
 */
@Singleton
public class DaoManager {
	
	@Inject 
	private Injector injector;
	
	public <E extends Entity> Dao<E> getDaoFor(Class<E> entityClass) {
		final ParameterizedType daoType = Types.newParameterizedType(Dao.class, entityClass);
		@SuppressWarnings("unchecked") //by construction the correct type
		final TypeLiteral<Dao<E>> daoTypeLiteral = (TypeLiteral<Dao<E>>) TypeLiteral.get(daoType);
		return injector.getInstance(Key.get(daoTypeLiteral));
	}
}
