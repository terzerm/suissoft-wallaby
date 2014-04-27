package com.suissoft.wallaby.controller.action;

import javafx.event.ActionEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.suissoft.wallaby.controller.ViewController;

@Singleton
public class ViewAction extends WallabyAction {
	@Inject
	private ViewController<?> viewController;

	public ViewAction() {
		super("Load View Data");
	}
	@Override
	public void execute(ActionEvent ae) {
		viewController.initModel();
	}
}
