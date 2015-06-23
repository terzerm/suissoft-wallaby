package com.suissoft.wallaby.controller.action;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named(AboutAction.NAME)
public class AboutAction extends AbstractDialogAction {
	
	public static final String NAME = "about";

	public AboutAction() {
		super("About Wallaby");
	}
	@Override
	public void handle(ActionEvent ae) {
		showDialog(AlertType.INFORMATION, "About Wallaby", "Wallaby is the most awesome application. Believe it.\n\nIf you don't believe it you better quit the application now immediately.");
	}
}
