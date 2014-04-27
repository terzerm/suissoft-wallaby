package com.suissoft.wallaby.model;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;

import javax.inject.Singleton;

/**
 * Stores model data.
 */
@Singleton
public class ApplicationModel {
	
	private final Map<Class<?>, ObservableList<?>> listByElementType = new HashMap<>();
	
	@SuppressWarnings("unchecked")//safe cast, we know that the  types match in the map
	public <E> ObservableList<E> getList(Class<E> elementType) {
		return (ObservableList<E>)listByElementType.get(elementType);
	}
	public <E> void addList(Class<E> elementType, ObservableList<E> list) {
		final Object old = listByElementType.put(elementType, list);
		if (old != null) {
			throw new IllegalStateException("model already exists: " + old);
		}
	}
}
