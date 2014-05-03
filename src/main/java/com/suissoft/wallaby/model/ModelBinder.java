package com.suissoft.wallaby.model;

import java.lang.reflect.InvocationTargetException;

import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;

import org.apache.commons.beanutils.PropertyUtils;

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
			final String id = event.getTableColumn().getId();
			System.out.println("property " + id + " set from oldValue=" + oldValue + " to newValue=" + newValue + " in entity " + entity);
			try {
				PropertyUtils.setSimpleProperty(entity, id, newValue);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				System.err.println("could not set property " + id + " to newValue=" + newValue + " in entity " + entity + ", e=" + e);
				e.printStackTrace();
			}
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
