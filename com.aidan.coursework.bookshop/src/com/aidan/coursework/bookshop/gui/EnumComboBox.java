package com.aidan.coursework.bookshop.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.aidan.coursework.bookshop.enums.SerialisableEnum;

public class EnumComboBox<T extends SerialisableEnum> extends JComboBox {
	
	T[] values;
	

	public EnumComboBox() {
		super();
	}
	
	public EnumComboBox(T[] values) {
		super(get_enum_names(values));
		this.values = values;
		
	};
	
	private static String[] get_enum_names(SerialisableEnum[] values) {
		String[] names = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			names[i] = values[i].serialise();
		};
		return names;
	}
	
	public T get_value() {
		return values[this.getSelectedIndex()];
	}

	public void update_values(T[] values) {
		this.values = values;
		String[] items = get_enum_names(values);
        setModel(new DefaultComboBoxModel(items));
		
	}
}
