package com.suissoft.wallaby.controller;


abstract public class ChildController implements Controller {
	
	@Parent
	private Controller parentController;

	protected Controller getParentController() {
		return parentController;
	}

	public ApplicationController getApplicationController() {
		final Controller parent = getParentController();
		return parent == null ? null : parent.getApplicationController();
	}

}
