package com.suissoft.wallaby.controller.action;

import javafx.stage.Window;

import javax.inject.Inject;

import org.controlsfx.dialog.Dialogs;

abstract public class AbstractDialogAction extends WallabyAction {

	@Inject
	private Window window;

	public AbstractDialogAction(String text) {
		super(text);
	}

	protected Dialogs createDialog(String title, String message) {
		return Dialogs.create().lightweight().owner(window).masthead(null)//
			.title(title)//
			.message(message);
	}
}
