package com.aidan.coursework.bookshop.database;

import java.time.format.DateTimeFormatter;

import com.aidan.coursework.bookshop.account.*;
import com.aidan.coursework.bookshop.book.*;
import com.aidan.coursework.bookshop.enums.*;

public class Serialiser {

	private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	
	public static String serialise_book(BaseBook book) {
		return switch (book.getType()) {
			case AudioBookType -> serialise_book((AudioBook) book);
			case PaperBookType -> serialise_book((PaperBook) book);
			case EBookType -> serialise_book((EBook) book);
		};
	}
	
	// these all seralise their respective fields and add on to the base
	public static String serialise_book(PaperBook book) {
		String[] values = partially_serialise_book(book);
		values[8] = Integer.toString(book.getPages());
		values[1] = "paperback";
		return serialise_array(values);
	}

	public static String serialise_book(EBook book) {
		String[] values = partially_serialise_book(book);
		values[8] = Integer.toString(book.getPages());
		values[1] = "ebook";
		return serialise_array(values);
	}

	public static String serialise_book(AudioBook book) {
		String[] values = partially_serialise_book(book);
		values[8] = Float.toString(book.getAudio_length());
		values[1] = "audiobook";
		return serialise_array(values);
	}
	
	private static String[] partially_serialise_book(BaseBook book) {
		String[] values = {
			Integer.toString(book.getBarcode()),
			"book type",
			book.getTitle(),
			serialise_enum(book.getLanguage()),
			serialise_enum(book.getGenre()),
			format.format(book.getRelease_date()),
			Integer.toString(book.getQuantity()),
			String.format("%.2f", book.getPrice()), // use format here to keep trailing 0s for the penny
			"additional1",
			serialise_enum(book.getFormat())
		};
		return values;
	}

	
	
	public static String serialise_account(BaseAccount account) {
		return switch (account.getType()) {
			case UserAccountType -> serialise_account((UserAccount) account);
			case AdminAccountType -> serialise_account((AdminAccount) account);
		};
	}
	
	public static String serialise_account(AdminAccount account) {
		String[] values = partially_serialise_account(account);
		values[7] = "admin";
		return serialise_array(values);
	}

	public static String serialise_account(UserAccount account) {
		String[] values = partially_serialise_account(account);
		values[6] = Float.toString(account.getCredit_balance());
		values[7] = "customer";
		return serialise_array(values);
	}
	
	private static String[] partially_serialise_account(BaseAccount account) {
		String[] values = {
			Integer.toString(account.id()),
			account.getUsername(),
			account.getSurname(),
			account.getAddress().house_no(),
			account.getAddress().postcode(),
			account.getAddress().city(),
			"", // Balance goes here
			"" // account type goes here
		};
		
		return values;
	}
	

	private static String serialise_array(String[] values) {
		return String.join(", ", values);
	}
	
	public static String serialise_enum(BookFormat format) {
		return switch (format) {
			case AAC -> "AAC";
			case EPUB -> "EPUB";
			case MOBI -> "MOBI";
			case MP3 -> "MP3";
			case New -> "new";
			case PDF -> "PDF";
			case Used -> "used";
			case WMA -> "WMA";
		};
	};

	public static String serialise_enum(BookGenre genre) {
		return switch (genre) {
			case Biography -> "Biography";
			case Business -> "Business";
			case ComputerScience -> "Computer Science";
			case Politics -> "Politics";
		};
	};
	public static String serialise_enum(BookLanguage language) {
		return switch (language) {
			case English -> "English";
			case French -> "French";
		};
	}

	public static String serialise_enum(BookType type) {
		return switch (type) {
			case AudioBookType -> "audiobook";
			case EBookType -> "ebook";
			case PaperBookType -> "paperback";
		};
	}

	public static String serialise_enum(AccountType type) {
		return switch (type) {
			case AdminAccountType -> "admin";
			case UserAccountType -> "customer";
		};
	}

}
