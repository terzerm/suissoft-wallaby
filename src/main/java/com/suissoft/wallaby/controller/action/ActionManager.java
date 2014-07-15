package com.suissoft.wallaby.controller.action;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.google.inject.Injector;

@Singleton
public class ActionManager {
	
	private final Map<String, WallabyAction> actionsByName = new HashMap<>();
	
	@Inject
	private Injector injector;
	
	@Inject	//we want this before @PostConstruct
	private void init() {
		for (final Object instance : injector.getAllBindings().values().stream().map(b -> b.getProvider().get()).toArray()) {
			if (instance instanceof WallabyAction) {
				final Named named = instance.getClass().getAnnotation(Named.class);
				if (named != null) {
					if (null == actionsByName.put(named.value(), (WallabyAction)instance)) {
						System.out.println("found named action: " + named.value() + "-->" + instance);
					} else {
						throw new IllegalStateException("duplicate named action: " + named.value() + "-->" + instance);
					}
				}
			}
		}
	}
	
	public WallabyAction getActionByName(String name) {
		return actionsByName.get(name);
	}
	public WallabyAction getActionFor(Node node) {
		return getActionByName(node.getId());
	}

}
