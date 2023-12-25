package com.aidan.coursework.bookshop.book;

import java.time.LocalDate;

import com.aidan.coursework.bookshop.enums.*;

public class AudioBook extends BaseBook {
	
	float audio_length;

	public float getAudio_length() {
		return audio_length;
	}

	public AudioBook(
		int barcode,
		String title,
		BookLanguage language,
		BookGenre genre,
		LocalDate date,
		int stock,
		float price,
		float audio_length,
		BookFormat format
	) {
		super(barcode, title, language, genre, date, stock, price, format, BookType.AudioBookType);
		this.audio_length = audio_length;
	}

}
