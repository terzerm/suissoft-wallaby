package com.suissoft.wallaby.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

public class ApplicationController {
	
	@FXML
	private Pane applicationPane;
	@FXML
	private MenuBar menuBar;
	@FXML
	private ToolBar toolBar;
	@FXML
	private Pane viewPane;

	@FXML
	private MenuBarController menuBarController; 
	@FXML
	private ToolBarController toolBarController; 
	@FXML
	private ViewController<?> viewController;
	
}
