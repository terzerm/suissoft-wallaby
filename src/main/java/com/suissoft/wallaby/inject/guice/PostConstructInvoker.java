package com.suissoft.wallaby.inject.guice;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.google.inject.Injector;

public class PostConstructInvoker {

	public static void invokePostConstructMethods(Injector injector) {
		final Set<Object> postConstructedInstances = new HashSet<>();
		for (final Object instance : injector.getAllBindings().values().stream().map(b -> b.getProvider().get()).toArray()) {
			invokePostConstructMethods(instance, postConstructedInstances);
		}
	}
	
	private static void invokePostConstructMethods(Object instance, Set<Object> visited) {
		if (!visited.contains(instance)) {
			invokePostConstructMethods(instance.getClass(), instance);
		}
	}
	private static void invokePostConstructMethods(Class<?> typeInHierarchy, Object instance) {
		if (typeInHierarchy != null) {
			invokePostConstructMethods(typeInHierarchy.getSuperclass(), instance);
			for (final Method method : typeInHierarchy.getDeclaredMethods()) {
				if (method.isAnnotationPresent(PostConstruct.class)) {
					method.setAccessible(true);
					try {
						method.invoke(instance);
					} catch (Exception e) {
						throw new RuntimeException("invocation of post construct method failed: method=" + method + ", e=" + e, e);
					}
				}
			}
		}
	}
	
	//no instances
	private PostConstructInvoker() {
		super();
	}
}
