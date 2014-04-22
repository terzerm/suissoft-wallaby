package com.suissoft.wallaby.controller;

import javax.inject.Inject;
import javax.inject.Named;

import com.suissoft.wallaby.inject.guice.ChildControllerInjector;

abstract public class ChildController implements Controller {
	@Inject
	@Named(ChildControllerInjector.PARENT_CONTROLLER_NAME)
	private Controller parentController;

	protected Controller getParentController() {
		return parentController;
	}

	public ApplicationController getApplicationController() {
		final Controller parent = getParentController();
		return parent == null ? null : parent.getApplicationController();
	}

}
