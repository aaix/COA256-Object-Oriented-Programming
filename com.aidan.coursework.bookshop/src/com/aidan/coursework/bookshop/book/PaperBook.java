package com.aidan.coursework.bookshop.book;

import java.time.LocalDate;

import com.aidan.coursework.bookshop.enums.*;

public class PaperBook extends BaseBook {
	
	int pages;
	
	public int getPages() {
		return pages;
	}

	public PaperBook(
		int barcode,
		String title,
		BookLanguage language,
		BookGenre genre,
		LocalDate date,
		int stock,
		float price,
		int pages,
		BookFormat format
	) {

		super(barcode, title, language, genre, date, stock, price, format, BookType.PaperBookType);
		this.pages = pages;
	}

}
