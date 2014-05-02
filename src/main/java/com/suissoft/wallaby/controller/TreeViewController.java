package com.suissoft.wallaby.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import javax.inject.Inject;

import com.suissoft.model.entity.Entity;
import com.suissoft.wallaby.model.ViewSpec;

public class TreeViewController {
	
	@Inject
	private ViewController viewController;

	@FXML
	private TreeView<?> treeView;

	@FXML
	protected void initialize() {
		treeView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<?>> observable, TreeItem<?> oldValue, TreeItem<?> newValue) -> {selectionChanged(oldValue, newValue);});
	}

	private void selectionChanged(TreeItem<?> oldValue, TreeItem<?> newValue) {
		System.out.println("OLD: " + oldValue + ", NEW: " + newValue);
		if (newValue != null && newValue.getValue() instanceof ViewSpec) {
			viewController.replaceView((ViewSpec)newValue.getValue());
		}
	}
	
	public void clearSelection() {
		treeView.getSelectionModel().clearSelection();
	}
	
	public void select(Class<? extends Entity> entityClass) {
		if (select(treeView.getRoot(), entityClass)) {
			System.out.println("tree item selected for " + entityClass.getName());
		} else {
			System.err.println("tree item not found for " + entityClass.getName());
		}
	}
	private boolean select(TreeItem<?> treeItem, Class<? extends Entity> entityClass) {
		if (treeItem.getValue() instanceof ViewSpec) {
			final ViewSpec viewSpec = (ViewSpec)treeItem.getValue(); 
			if (viewSpec.getEntityClass().equals(entityClass)) {
				select(treeItem);
				return true;
			}
		}
		for (TreeItem<?> child : treeItem.getChildren()) {
			if (select(child, entityClass)) {
				return true;
			}
		}
		return false;
	}
	public void select(TreeItem<?> newValue) {
		select(treeView, newValue);
	}
	@SuppressWarnings("unchecked")
	private static <T> void select(TreeView<T> treeView, TreeItem<?> newValue) {
		treeView.getSelectionModel().select((TreeItem<T>)newValue);
	}

}
