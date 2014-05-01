package com.suissoft.wallaby.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.inject.Inject;

import com.suissoft.wallaby.controller.action.AboutAction;
import com.suissoft.wallaby.controller.action.PrintAction;
import com.suissoft.wallaby.controller.action.QuitAction;
import com.suissoft.wallaby.controller.action.SelectViewAction;


public class MenuBarController extends AbstractActionController {
	
	@Inject
	private AboutAction aboutAction;
	@Inject
	private QuitAction quitAction;
	@Inject
	private PrintAction printAction;
	@Inject
	private SelectViewAction.NaturalPersonView selectNaturalPersonViewAction;
	@Inject
	private SelectViewAction.JuristicPersonView selectJuristicPersonViewAction;
	
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
	
	@FXML
	public void onSelectNaturalPersonView(ActionEvent event) {
		onAction(selectNaturalPersonViewAction, event);
	}
	
	@FXML
	public void onSelectJuristicPersonView(ActionEvent event) {
		onAction(selectJuristicPersonViewAction, event);
	}
}
