package com.suissoft.wallaby.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.suissoft.model.entity.Entity;

public class ViewSpec {
	private String text;
	private Class<? extends Entity> entityClass;
	private String listView;
	private String detailView;
	
	public String getText() {
		return text;
	}
	public void setText(String value) {
		this.text = value;
	}
	public Class<? extends Entity> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<? extends Entity> entityClass) {
		this.entityClass = entityClass;
	}
	public void setEntityClassName(String entityClassName) {
		try {
			final Class<?> clazz = Class.forName(entityClassName);
			setEntityClass(clazz.asSubclass(Entity.class));
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(entityClassName + " is not a valid entity class, e=" + e, e);
		}
	}
	public String getEntityClassName() {
		return entityClass == null ? null : entityClass.getName();
	}
	public String getListView() {
		return listView;
	}
	public void setListView(String listView) {
		this.listView = listView;
	}
	public String getDetailView() {
		return detailView;
	}
	public void setDetailView(String detailView) {
		this.detailView = detailView;
	}
	@Override
	public String toString() {
		return text != null ? text : super.toString();
	}
	public String toStringExtended() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
