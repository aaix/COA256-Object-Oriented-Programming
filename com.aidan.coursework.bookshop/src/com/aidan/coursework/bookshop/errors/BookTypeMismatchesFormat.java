package com.aidan.coursework.bookshop.errors;

public class BookTypeMismatchesFormat extends InvalidDatabaseEntry {
	public BookTypeMismatchesFormat(String book_type, String book_format) {
		super("BOOK TYPE '" + book_type + "' DOES NOT SUPPORT THE FORMAT: " + book_format);
	}
}
