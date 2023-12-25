package com.aidan.coursework.bookshop.database;

import com.aidan.coursework.bookshop.book.*;
import com.aidan.coursework.bookshop.enums.*;
import com.aidan.coursework.bookshop.account.*;
import com.aidan.coursework.bookshop.errors.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Deserialiser {
	
	
	private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public static BaseBook deserialise_book(String line) throws InvalidDatabaseEntry {
		String[] values = separate_csv_line(line);

		
		if (values.length != 10) {
			throw new InvalidDatabaseEntry("ENTRY HAS INVALID FIELD COUNT");
		};
		
		// BARCODE
		int barcode;
		try {
			barcode = Integer.parseInt(values[0]);
		} catch (NumberFormatException e) {
			throw new InvalidDatabaseEntry("BARCODE NOT AN INTEGER");
		}
		
		// TITLE
		String title = values[2];
		if (title.length() < 1) {
			throw new InvalidDatabaseEntry("TITLE LENGTH IS TOO SHORT");
		}
		
		// LANGUAGE
		BookLanguage language;
		switch (values[3]) {
			case "English":
				language = BookLanguage.English;
				break;
			case "French":
				language = BookLanguage.French;
				break;
			default:
				throw new InvalidDatabaseEntry("UNKNOWN LANGUAGE: >" + values[3]);
		};

		// GENRE
		BookGenre genre;
		switch (values[4]) {
			case "Biography":
				genre = BookGenre.Biography;
				break;
			case "Business":
				genre = BookGenre.Business;
				break;
			case "Computer Science":
				genre = BookGenre.ComputerScience;
				break;
			case "Politics":
				genre = BookGenre.Politics;
				break;
			default:
				throw new InvalidDatabaseEntry("UNKNOWN GENRE: >" + values[4]);
		};
		
		// DATE
		LocalDate date;
		try {
			date = LocalDate.parse(values[5], format);
		} catch (DateTimeParseException e) {
			throw new InvalidDatabaseEntry("FAILED TO PARSE DATE: >" + e.getMessage());
		};
		
		// STOCK QUANTITY
		int stock;
		try {
			stock = Integer.parseInt(values[6]);
		} catch (NumberFormatException e) {
			throw new InvalidDatabaseEntry("STOCK QUANTITY NOT AN INTEGER");
		};
		
		// RETAIL PRICE
		float rrp;
		try {
			rrp = Float.parseFloat(values[7]);
		} catch (NumberFormatException e) {
			throw new InvalidDatabaseEntry("RRP NOT A FLOAT");
		};
		
		// Construct based on format
		return construct_book_with_fields(
				values[1], values,
				barcode,
				title,
				language,
				genre,
				date,
				stock,
				rrp
			);
		
	}
	
	private static BaseBook construct_book_with_fields(
			String book_type,
			String[] values,
			int barcode,
			String title,
			BookLanguage language,
			BookGenre genre,
			LocalDate date,
			int stock,
			float price
		) throws InvalidDatabaseEntry {
		
		String additional1 = values[8];
		String additional2 = values[9];
		
		// this corresponds to the BookFormat enum
		BookFormat format;
		switch (additional2) {

			default:
				throw new InvalidDatabaseEntry("ADDITIONAL BOOK INFORMATION (2) NOT KNOWN: >" + additional2);
			
			// PAPERBACK FORMATS
			case "new":
				format = BookFormat.New;
				break;
			case "used":
				format = BookFormat.Used;
				break;

			// EBOOK FORMATS
			case "EPUB":
				format = BookFormat.EPUB;
				break;
			case "MOBI":
				format = BookFormat.MOBI;
				break;
			case "PDF":
				format = BookFormat.PDF;
				break;

			// AUDIOBOOK FORMATS
			case "MP3":
				format = BookFormat.MP3;
				break;
			case "WMA":
				format = BookFormat.WMA;
				break;
			case "AAC":
				format = BookFormat.AAC;
				break;
			
		}
		
		
		switch (book_type) {
		default:
			throw new InvalidDatabaseEntry("UNKNOWN BOOK FORMAT: >" + book_type);
		
		case "paperback":
			if (!format.isPaperback()) {
				throw new BookTypeMismatchesFormat(book_type, additional2);
			};
			
			// PAGES
			int paper_pages;
			try {
				paper_pages = Integer.parseInt(additional1);
			} catch (NumberFormatException e) {
				throw new InvalidDatabaseEntry("BOOK PAGES NOT AN INTEGER");
			};
			
			return new PaperBook(
				barcode,
				title,
				language,
				genre,
				date,
				stock,
				price,
				paper_pages,
				format
			);

		case "audiobook":
			if (!format.isAudiobook()) {
				throw new BookTypeMismatchesFormat(book_type, additional2);
			};
			
			// DURATION
			
			float audio_length;
			try {
				audio_length = Float.parseFloat(additional1);
			} catch (NumberFormatException e) {
				throw new InvalidDatabaseEntry("BOOK LENGTH NOT A FLOAT");
			};
			
			return new AudioBook(
					barcode,
					title,
					language,
					genre,
					date,
					stock,
					price,
					audio_length,
					format
			);

		case "ebook":
			if (!format.isEbook()) {
				throw new BookTypeMismatchesFormat(book_type, additional2);
			};

			// PAGES
			int ebook_pages;
			try {
				ebook_pages = Integer.parseInt(additional1);
			} catch (NumberFormatException e) {
				throw new InvalidDatabaseEntry("BOOK PAGES NOT AN INTEGER");
			};
			
			return new EBook(
					barcode,
					title,
					language,
					genre,
					date,
					stock,
					price,
					ebook_pages,
					format
		)	;

		}
	}
	
	
	public static BaseAccount deserialise_account(String line) throws InvalidDatabaseEntry {
		String[] values = separate_csv_line(line);

		if (values.length != 8) {
			throw new InvalidDatabaseEntry("ENTRY HAS INVALID FIELD COUNT");
		};
		
		// USER ID
		int uid;
		try {
			uid = Integer.parseInt(values[0]);
		} catch (NumberFormatException e) {
			throw new InvalidDatabaseEntry("USER ID NOT AN INTEGER");
		}
		
		// USERNAME
		String username = values[1];
		
		// SURNAME
		String surname = values[2];
		
		// HOUSE NO
		
		// here we dont treat this as an integer because:
		// 1. there is no need to as we dont do any integer manipulation on it
		// 2. a lot of houses have a name instead of a house number so it wouldnt
		//    be fair to enforce an arbitary restriction on them
		String house_no = values[3];
		
		// POSTCODE
		String postcode = values[4];
		
		// CITY
		String city = values[5];
		
		// ACCOUNT TYPE
		switch (values[7]) {
			default:
				throw new InvalidDatabaseEntry("ACCOUNT TYPE IS NOT VALID: >" + values[7]);
			case "admin":
				return new AdminAccount(
					uid,
					username,
					surname,
					house_no,
					postcode,
					city
				);
			case "customer":

				// CREDIT BAL
				float credit_balance;
				try {
					credit_balance = Float.parseFloat(values[6]);
				} catch (NumberFormatException e) {
					throw new InvalidDatabaseEntry("CREDIT BALANCE NOT A FLOAT");
				};
				
				return new UserAccount(
					uid,
					username,
					surname,
					house_no,
					postcode,
					city,
					credit_balance
				);
		}

	}
	
	private static String[] separate_csv_line(String str) {
		return str.split(", ");
	}

	// SEE COMMENT IN FILE HANDLER READ FROM DB FUNCTION
	// WE SHOULD BE ABLE TO DO T.DESERIALISE BUT java
	public static <T extends DatabaseCompatible> T deseralise(String line) throws InvalidDatabaseEntry {
		if (line.endsWith("admin") || line.endsWith("customer")) {
			return (T) deserialise_account(line);
		} else {
			return (T) deserialise_book(line);
		}
	}
}
