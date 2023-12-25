package com.aidan.coursework.bookshop.book;

import com.aidan.coursework.bookshop.database.*;
import com.aidan.coursework.bookshop.enums.*;
import com.aidan.coursework.bookshop.errors.InvalidDatabaseEntry;

import java.time.LocalDate;


public class BaseBook extends DatabaseCompatible {
	
	public BookType getType() {
		return type;
	}

	public int getBarcode() {
		return barcode;
	}

	public String getTitle() {
		return title;
	}

	public BookLanguage getLanguage() {
		return language;
	}

	public BookGenre getGenre() {
		return genre;
	}

	public LocalDate getRelease_date() {
		return release_date;
	}

	public int getQuantity() {
		return quantity;
	}

	public float getPrice() {
		return price;
	}

	public BookFormat getFormat() {
		return format;
	}
	
	
	public String display() {
		return String.format("[%d] %s (%d in stock)", this.barcode, this.title, this.quantity);
	}

	int barcode;
	String title;
	BookLanguage language;
	BookGenre genre;
	LocalDate release_date;
	int quantity;
	float price;
	BookType type;
	
	BookFormat format;
	
	BaseBook(
		int barcode,
		String title,
		BookLanguage language,
		BookGenre genre,
		LocalDate date,
		int stock,
		float price,
		BookFormat format,
		BookType type
	) {
		this.barcode = barcode;
		this.title = title;
		this.genre = genre;
		this.language = language;
		this.release_date = date;
		this.quantity = stock;
		this.price = price;
		this.format = format;
		this.type = type;
		
	}
	
	public static BaseBook deserialise(String line) throws InvalidDatabaseEntry {
		return Deserialiser.deserialise_book(line);
	}

	public String serialise() {
		return Serialiser.serialise_book(this);
	}
	
	
	public int id() {
		return this.barcode;
	}
	
	public int decrease_quantity(int decrement_value) {
		this.quantity -= decrement_value;
		return this.quantity;
	}
}
