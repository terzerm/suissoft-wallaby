package com.suissoft.wallaby.controller.action;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Base class of all Wallaby actions.
 */
abstract public class WallabyAction implements EventHandler<ActionEvent> {
	
	private final StringProperty text = new SimpleStringProperty();
	private final BooleanProperty enabled = new SimpleBooleanProperty(true);
	
	public WallabyAction(String text) {
		setText(text); 
	}
	public String getText() {
		return text.get();
	}
	public void setText(String text) {
		this.text.set(text);
	}
	public StringProperty textProperty() {
		return text;
	}
	public boolean isEnabled() {
		return enabled.get();
	}
	public void setEnabled(boolean enabled) {
		this.enabled.set(enabled);
	}
	public BooleanProperty enabledProperty() {
		return enabled;
	}
	

}
