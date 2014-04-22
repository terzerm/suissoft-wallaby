package com.suissoft.wallaby.controller;

import javafx.fxml.FXML;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.suissoft.model.util.PersistenceUnit;
import com.suissoft.wallaby.inject.guice.ChildControllerInjector;
import com.suissoft.wallaby.inject.guice.WallabyModule;

public class ApplicationController implements Controller {
	
	private final WallabyModule module = new WallabyModule(PersistenceUnit.H2_MEMORY);
	private final Injector injector = Guice.createInjector(module);
	
	@FXML
	private MenuBarController menuBarController; 
	@FXML
	private ToolBarController toolBarController; 
	
	@FXML
	protected void initialize() {
		injector.injectMembers(this);
		ChildControllerInjector.injectChildControllers(injector, this);
	}
	@Override
	public final ApplicationController getApplicationController() {
		return this;
	}
	
}
