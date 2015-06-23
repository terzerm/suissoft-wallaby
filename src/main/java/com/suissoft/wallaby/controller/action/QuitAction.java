package com.suissoft.wallaby.controller.action;

import javafx.application.Platform;
import javafx.event.ActionEvent;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named(QuitAction.NAME)
public class QuitAction extends WallabyAction {
	
	public static final String NAME = "quit";

	public QuitAction() {
		super("Quit Wallaby");
	}
	@Override
	public void handle(ActionEvent ae) {
		Platform.exit();
		//FIXME above does not work, hence the hard stop below
		System.exit(0);
	}
}
