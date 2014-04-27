package com.suissoft.wallaby.controller.action;

import javafx.application.Platform;
import javafx.event.ActionEvent;

import javax.inject.Singleton;

@Singleton
public class QuitAction extends WallabyAction {
	public QuitAction() {
		super("Quit Wallaby");
	}
	@Override
	public void execute(ActionEvent ae) {
		Platform.exit();
		//FIXME above does not work, hence the hard stop below
		System.exit(0);
	}
}
