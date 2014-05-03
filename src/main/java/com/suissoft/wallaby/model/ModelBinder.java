package com.suissoft.wallaby.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;

public enum ModelBinder {
	TABLE(TableView.class) {
		@Override
		@SuppressWarnings("unchecked")
		// not safe but what can we do?
		public <E> void castAndBind(Control control, ObservableList<E> data) {
			final TableView<E> tableView = (TableView<E>) control;
			tableView.setItems(data);
			for (TableColumn<E, ?> tableColumn : tableView.getColumns()) {
				setOnEditCommit(tableColumn);
			}
		}

		private <E, T> void setOnEditCommit(TableColumn<E, T> tableColumn) {
			tableColumn.setOnEditCommit((CellEditEvent<E, T> event) -> {
				setEntityValue(event.getTableView().getItems().get(event.getTablePosition().getRow()), event);
			});
		}

		private <E, T> void setEntityValue(E entity, CellEditEvent<E, T> event) {
			final T oldValue = event.getOldValue();
			final T newValue = event.getNewValue();
			System.out.println("setEntityValue: entity=" + entity + ", oldValue=" + oldValue + ", newValue=" + newValue);
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
