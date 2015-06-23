package com.suissoft.wallaby.controller.action;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

abstract public class AbstractDialogAction extends WallabyAction {

	public AbstractDialogAction(String text) {
		super(text);
	}

	protected void showDialog(AlertType alertType, String title, String message) {
		final Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
