package com.suissoft.wallaby.controller;

import javafx.event.ActionEvent;

import com.suissoft.wallaby.controller.action.WallabyAction;

abstract public class AbstractActionController {
	
	protected void onAction(WallabyAction action, ActionEvent event) {
		if (action.isEnabled()) {
			action.handle(event);
		} else {
			System.err.println("Action is disabled: action=" + action + ", event=" + event);
		}
	}
}
