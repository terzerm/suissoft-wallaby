package com.suissoft.wallaby.controller;

import javafx.event.ActionEvent;

import org.controlsfx.control.action.Action;

abstract public class AbstractActionController {
	
	protected void onAction(Action action, ActionEvent event) {
		if (!action.disabledProperty().get()) {
			action.execute(event);
		} else {
			System.err.println("Action is disabled: action=" + action + ", event=" + event);
		}
	}
}
