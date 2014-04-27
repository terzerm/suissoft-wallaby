package com.suissoft.wallaby.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.inject.Inject;

import com.suissoft.wallaby.controller.action.AboutAction;
import com.suissoft.wallaby.controller.action.PrintAction;
import com.suissoft.wallaby.controller.action.QuitAction;


public class MenuBarController extends AbstractActionController {
	
	@Inject
	private AboutAction aboutAction;
	@Inject
	private QuitAction quitAction;
	@Inject
	private PrintAction printAction;
	
	@FXML
	public void onAbout(ActionEvent event) {
		onAction(aboutAction, event);
	};
	@FXML
	public void onQuit(ActionEvent event) {
		onAction(quitAction, event);
	};
	
	@FXML
	public void onPrint(ActionEvent event) {
		onAction(printAction, event);
	};
	
}
