package com.suissoft.wallaby.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;

public enum ModelBinder {
	TABLE(TableView.class) {
		@Override
		@SuppressWarnings("unchecked") // not safe but what can we do?
		public <E> void castAndBind(Control control, ObservableList<E> data) {
			((TableView<E>)control).setItems((ObservableList<E>)data);
		}
	};
	private final Class<? extends Control> controllClass;
	private static final ModelBinder[] VALUES = values();
	private ModelBinder(Class<? extends Control> controllClass) {
		this.controllClass = controllClass;
	}
	
	
	abstract protected <E> void castAndBind(Control control, ObservableList<E> data);
	
	public static <E> void bind(Control control, ObservableList<E> data) {
		for (final ModelBinder binder : VALUES) {
			if (binder.controllClass.isInstance(control)) {
				binder.castAndBind(control, data);
				return;
			}
		}
		throw new IllegalArgumentException("no binder found for control: " + control);
	}
}
