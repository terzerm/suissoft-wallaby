package com.suissoft.wallaby.controller.action;

import javafx.event.ActionEvent;

import javax.inject.Singleton;

@Singleton
public class AboutAction extends AbstractDialogAction {
	
	public AboutAction() {
		super("About Wallaby");
	}
	@Override
	public void execute(ActionEvent ae) {
		createDialog("About Wallaby", "Wallaby is the most awesome application. Believe it.\n\nIf you don't believe it you better quit the application now immediately.")//
			.showInformation();
	}
}
