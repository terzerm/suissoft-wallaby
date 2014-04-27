package com.suissoft.wallaby.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.inject.Inject;

import com.suissoft.wallaby.controller.action.PrintAction;
import com.suissoft.wallaby.controller.action.QuitAction;

public class ToolBarController extends AbstractActionController {
	
	@Inject
	private QuitAction quitAction;
	@Inject
	private PrintAction printAction;
	
	@FXML
	public void onQuit(ActionEvent event) {
		onAction(quitAction, event);
	};
	
	@FXML
	public void onPrint(ActionEvent event) {
		onAction(printAction, event);
	};
	
}
