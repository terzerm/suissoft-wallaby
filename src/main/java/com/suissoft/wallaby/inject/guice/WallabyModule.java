package com.suissoft.wallaby.inject.guice;

import javax.persistence.EntityManagerFactory;

import com.google.inject.AbstractModule;
import com.suissoft.model.inject.guice.EntityManagerModule;
import com.suissoft.model.util.PersistenceUnit;

public class WallabyModule extends AbstractModule {
	
	private final EntityManagerModule entityManagerModule;

	public WallabyModule(PersistenceUnit persistenceUnit) {
		this(new EntityManagerModule(persistenceUnit));
	}
	public WallabyModule(EntityManagerFactory entityManagerFactory) {
		this(new EntityManagerModule(entityManagerFactory));
	}
	public WallabyModule(EntityManagerModule entityManagerModule) {
		this.entityManagerModule = entityManagerModule;
	}

	@Override
	protected void configure() {
		install(entityManagerModule);
//		install(new ActionModule());
//		install(new ControllerModule());
	}

}
