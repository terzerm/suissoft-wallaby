package com.suissoft.wallaby.inject.guice;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import javafx.fxml.FXML;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

public class FxmlModule extends AbstractModule {

	private final Class<?> fxmlRootType;
	private final Object fxmlRoot;

	public FxmlModule(Object fxmlRoot) {
		this.fxmlRootType = fxmlRoot.getClass();
		this.fxmlRoot = fxmlRoot;
	}
	public <T> FxmlModule(Class<T> fxmlRootType, T fxmlRoot) {
		this.fxmlRootType = fxmlRootType;
		this.fxmlRoot = fxmlRoot;
	}
	
	@Override
	protected void configure() {
		bind(FxmlModule.class).toInstance(this);
		traverseFxmlTree((Type type, Object instance, Named annotation) -> {bind(type, instance, annotation);}, null);
	}
	
	private static interface TriConsumer {
		void accept(Type type, Object instance, Named annotation);
	}
	
	private <T> void bind(Type type, Object instance, Named namedAnnotation) {
		bind(TypeLiteral.get(type), instance, namedAnnotation);
	}
	@SuppressWarnings("unchecked")
	private <T> void bind(TypeLiteral<T> type, Object instance, Named namedAnnotation) {
		if (namedAnnotation == null) {
			bind(type).toInstance((T)instance);
		} else {
			bind(type).annotatedWith(namedAnnotation).toInstance((T)instance);
		}
		System.out.println("GUICE: bound: " + type + " --> " + instance + (namedAnnotation == null ? "" : " " + namedAnnotation));
	}
	public void injectFxmlMembers(Injector injector) {
		traverseFxmlTree((Type type, Object instance, Named annotation) -> {injector.injectMembers(instance);}, injector);
		PostConstructInvoker.invokePostConstructMethods(injector);
	}

	private <C> void traverseFxmlTree(TriConsumer consumer, C context) {
		traverseFxmlTree(consumer, context, fxmlRootType, fxmlRoot, null, new LinkedHashSet<>());
	}

	private <C> void traverseFxmlTree(TriConsumer consumer, C context, Type type, Object instance, Named annotation, Set<Object> visited) {
		if (visited.contains(instance)) {
			return;
		}
		visited.add(instance);
		consumer.accept(type, instance, annotation);
		Class<?> clazz = instance.getClass();
		do {
			for (final Field field : clazz.getDeclaredFields()) {
				if (null != field.getAnnotation(FXML.class)) {
					try {
						field.setAccessible(true);
						final Object child = field.get(instance);
						if (child != null) {
							//recurse children of child
							traverseFxmlTree(consumer, context, field.getGenericType(), child, field.getAnnotation(Named.class), visited);
						} else {
							System.err.println("GUICE: warning, FXML field is null: " + instance.getClass().getName() + "#" + field.getName());
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException("could not access field value " + instance.getClass().getName() + "#" + field.getName() + ", e=" + e, e);
					}
				}
			}
			//superclass fields
			clazz = clazz.getSuperclass();
		} while (clazz != null);
	}

}
