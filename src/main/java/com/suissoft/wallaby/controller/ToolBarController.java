package com.suissoft.wallaby.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.suissoft.wallaby.controller.action.ActionManager;
import com.suissoft.wallaby.controller.action.WallabyAction;

public class ToolBarController extends AbstractActionController {
	
	private static final String BUTTON_IMAGE_PREFIX = "/image/suissoft_button_";

	private static enum ButtonStyle {
		ACTIVE("_active.png"),
		INACTIVE("_inactive.png"),
		MOUSE_OVER("_mouse_over.png"),
		PUSHED("_pushed.png");
		
		private final String urlPostfix;

		private ButtonStyle(String urlPostfix) {
			this.urlPostfix = urlPostfix;
		}

		public StringProperty getStylePropertyFor(Node node) {
			final String url = BUTTON_IMAGE_PREFIX + node.getId() + urlPostfix;
			return new SimpleStringProperty("-fx-graphic:url('" + url + "'); -fx-padding:0; -fx-background-color:transparent;");
		}
	}
	
	@Inject
	private ActionManager actionManager;

	@FXML
	private ToolBar toolBar;

	@FXML
	protected void initialize() {
		for (final Node node : toolBar.getItems()) {
			if (node instanceof Button) {
				bindStyleProperty(node);
			}
		}
	}
	
	@PostConstruct
	private void bindActions() {
		for (final Node node : toolBar.getItems()) {
			if (node instanceof Button) {
				bindOnActionProperty((Button)node);
			}
		}
	}

	private void bindStyleProperty(Node node) {
		node.styleProperty().bind(//
				Bindings//
					.when(node.disabledProperty())//
					.then(ButtonStyle.INACTIVE.getStylePropertyFor(node))//
					.otherwise(Bindings//
							.when(node.hoverProperty())//
							.then(Bindings//
									.when(node.pressedProperty())//
									.then(ButtonStyle.PUSHED.getStylePropertyFor(node))//
									.otherwise(ButtonStyle.MOUSE_OVER.getStylePropertyFor(node)))//
							.otherwise(ButtonStyle.ACTIVE.getStylePropertyFor(node)//
		)));
		System.out.println("bound style property for button: " + node.getId());
	}

	private void bindOnActionProperty(Button button) {
		final WallabyAction action = actionManager.getActionFor(button);
		if (action != null) {
			button.onActionProperty().setValue(e -> action.execute(e));
			System.out.println("bound action for button: " + button.getId());
		} else {
			System.err.println("action for button " + button.getId() + " not found");
		}
			
	}
}
