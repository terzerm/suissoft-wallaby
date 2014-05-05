package com.suissoft.wallaby.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
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
			tableView.editingCellProperty().addListener((observer, oldValue, newValue) -> {editingCellChanged(tableView, oldValue, newValue);});
			for (TableColumn<E, ?> tableColumn : tableView.getColumns()) {
				setOnEditCommit(tableColumn);
			}
		}

		private <E> void editingCellChanged(TableView<E> tableView, TablePosition<E,?> oldEditingPos, TablePosition<E,?> newEditingPos) {
			System.out.println("editingCellChanged: oldEditingPos=" + oldEditingPos + ", newEditingPos=" + newEditingPos);
			if (oldEditingPos != null && newEditingPos == null) {
				tableView.setUserData(oldEditingPos);
			}
		}
		private <E, T> void setOnEditCommit(TableColumn<E, T> tableColumn) {
			tableColumn.setOnEditCommit((CellEditEvent<E, T> event) -> {
				if (event.getTablePosition() != null && event.getTableColumn() == tableColumn) {
					final TableView<E> tableView = tableColumn.getTableView();
					final int row = event.getTablePosition().getRow();
					setEntityValue(tableView.getItems().get(row), event);
				}
			});
			tableColumn.setOnEditCancel((CellEditEvent<E, T> event) -> {
				if (event != null) {
					final T oldValue = event.getTablePosition() == null ? null : event.getOldValue();
					System.out.println("edit cancelled: oldValue=" + oldValue + ", newValue=" + event.getNewValue() + ", event=" + event);
				} else {
					System.out.println("edit cancelled: event=" + event);
				}
			});
		}

		private <E, T> void setEntityValue(E entity, CellEditEvent<E, T> event) {
			final T oldValue = event.getOldValue();
			final T newValue = event.getNewValue();
			final String id = event.getTableColumn().getId();
			try {
				PropertyUtils.setSimpleProperty(entity, id, newValue);
				System.out.println("property " + id + " set from oldValue=" + oldValue + " to newValue=" + newValue + " in entity " + entity);
			} catch (Exception e) {
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
