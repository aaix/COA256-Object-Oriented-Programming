package com.aidan.coursework.bookshop.enums;

import com.aidan.coursework.bookshop.database.Serialiser;

public enum BookType implements SerialisableEnum {
	PaperBookType,
	EBookType,
	AudioBookType;
	
	@Override
	public String serialise() {
		return Serialiser.serialise_enum(this);
	}
}
