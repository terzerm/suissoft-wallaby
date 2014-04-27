package com.suissoft.wallaby.scene.control.cell;

import java.lang.reflect.Field;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class TextFieldTableCell<S, T> extends javafx.scene.control.cell.TextFieldTableCell<S, T> {
	
    /**
     * Creates a default TextFieldTableCell with a null converter. Without a 
     * {@link StringConverter} specified, this cell will not be able to accept
     * input from the TextField (as it will not know how to convert this back
     * to the domain object). It is therefore strongly encouraged to not use
     * this constructor unless you intend to set the converter separately.
     */
    public TextFieldTableCell() { 
        super();
    } 
    
    /**
     * Creates a TextFieldTableCell that provides a {@link TextField} when put 
     * into editing mode that allows editing of the cell content. This method 
     * will work on any TableColumn instance, regardless of its generic type. 
     * However, to enable this, a {@link StringConverter} must be provided that 
     * will convert the given String (from what the user typed in) into an 
     * instance of type T. This item will then be passed along to the 
     * {@link TableColumn#onEditCommitProperty()} callback.
     * 
     * @param converter A {@link StringConverter converter} that can convert 
     *      the given String (from what the user typed in) into an instance of 
     *      type T.
     */
    public TextFieldTableCell(StringConverter<T> converter) {
		super(converter);
	}

	/**
     * Provides a {@link TextField} that allows editing of the cell content when
     * the cell is double-clicked, or when 
     * {@link TableView#edit(int, javafx.scene.control.TableColumn)} is called. 
     * This method will only  work on {@link TableColumn} instances which are of
     * type String.
     * 
     * @return A {@link Callback} that can be inserted into the 
     *      {@link TableColumn#cellFactoryProperty() cell factory property} of a 
     *      TableColumn, that enables textual editing of the content.
     */
    public static <S> Callback<TableColumn<S,String>, TableCell<S,String>> forTableColumn() {
        return forTableColumn(new DefaultStringConverter());
    }
    
    /**
     * Provides a {@link TextField} that allows editing of the cell content when
     * the cell is double-clicked, or when 
     * {@link TableView#edit(int, javafx.scene.control.TableColumn) } is called. 
     * This method will work  on any {@link TableColumn} instance, regardless of 
     * its generic type. However, to enable this, a {@link StringConverter} must 
     * be provided that will convert the given String (from what the user typed 
     * in) into an instance of type T. This item will then be passed along to the 
     * {@link TableColumn#onEditCommitProperty()} callback.
     * 
     * @param converter A {@link StringConverter} that can convert the given String 
     *      (from what the user typed in) into an instance of type T.
     * @return A {@link Callback} that can be inserted into the 
     *      {@link TableColumn#cellFactoryProperty() cell factory property} of a 
     *      TableColumn, that enables textual editing of the content.
     */
    public static <S,T> Callback<TableColumn<S,T>, TableCell<S,T>> forTableColumn(
            final StringConverter<T> converter) {
        return list -> new TextFieldTableCell<S,T>(converter);
    }
	
	private boolean focusListenerInstalled = false;
	@Override
	public void startEdit() {
		super.startEdit();
		if (!focusListenerInstalled) {
			installFocusListener();
		}
	}

	private void installFocusListener() {
		try {
			final Field field = javafx.scene.control.cell.TextFieldTableCell.class.getDeclaredField("textField");
			field.setAccessible(true);
			final TextField textField = (TextField)field.get(this);
			if (textField != null) {
				textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
					System.out.println("focus: " + oldValue + "-->" + newValue);
					if (!newValue) {
						commitEdit(getConverter().fromString(textField.getText()));
						System.out.println("edit committed");
					}
				});
				System.out.println("listener added");
				focusListenerInstalled = true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
