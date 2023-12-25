package com.aidan.coursework.bookshop.enums;

import com.aidan.coursework.bookshop.database.Serialiser;

public enum BookFormat implements SerialisableEnum {
	// Paper book
	New,
	Used,

	// Ebook formats
	EPUB,
	MOBI,
	PDF,

	// Audiobook formats
	MP3,
	WMA,
	AAC;

	public boolean isPaperback() {
		switch (this) {
		case New:
		case Used:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isEbook() {
		switch (this) {
		case EPUB:
		case MOBI:
		case PDF:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isAudiobook() {
		switch (this) {
		case MP3:
		case WMA:
		case AAC:
			return true;
		default:
			return false;
		}
	}

	@Override
	public String serialise() {
		return Serialiser.serialise_enum(this);
	}
	
	public static BookFormat[] getPaperbackFormats() {
		return new BookFormat[] {New, Used};
	}
	
	public static BookFormat[] getEbookFormats() {
		return new BookFormat[] {EPUB, MOBI, PDF};
	}
	
	public static BookFormat[] getAudiobookFormats() {
		return new BookFormat[] {MP3, WMA, AAC};
	}
}
