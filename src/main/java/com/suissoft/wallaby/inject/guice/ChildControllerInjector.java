package com.suissoft.wallaby.inject.guice;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.inject.Injector;
import com.suissoft.wallaby.controller.Controller;
import com.suissoft.wallaby.controller.Parent;

public class ChildControllerInjector {

	public static void injectChildControllers(Injector injector, Controller controller) {
		injectChildControllers(injector, null, controller, new LinkedHashSet<>());
	}

	private static void injectChildControllers(Injector injector, Controller parent, Controller controller, Set<Controller> visited) {
		if (visited.contains(controller)) {
			return;
		}
		visited.add(controller);
		injector.injectMembers(controller);
		Class<?> clazz = controller.getClass();
		do {
			for (final Field field : clazz.getDeclaredFields()) {
				if (Controller.class.isAssignableFrom(field.getType())) {
					try {
						field.setAccessible(true);
						if (field.getAnnotation(Parent.class) != null) {
							//parent controller
							field.set(controller, parent);
						} else {
							//child controller
							final Controller childController = (Controller) field.get(controller);
							if (childController != null) {
								//recurse children of child
								injectChildControllers(injector, controller, childController, visited);
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException("could not access child controller field " + controller.getClass().getName() + "#" + field.getName() + ", e=" + e, e);
					}
				}
			}
			//superclass controller fields
			clazz = clazz.getSuperclass();
		} while (clazz != null);
	}

	//no instances
	private ChildControllerInjector() {
		super();
	}
}
