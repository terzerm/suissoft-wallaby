package com.suissoft.wallaby.controller;

import javafx.collections.ObservableList;

import javax.inject.Inject;

import com.suissoft.wallaby.model.ApplicationModel;

abstract public class ViewController<E> {
	
	@Inject
	private ApplicationModel model;
	
	public void initModel() {
		ObservableList<E> list = model.getList(elementType());
		if (list == null) {
			list = load();
			model.addList(elementType(), list);
		}
		bind(list);
	}
	
	abstract protected Class<E> elementType();
	abstract protected void bind(ObservableList<E> list);
	abstract protected ObservableList<E> load();
}
