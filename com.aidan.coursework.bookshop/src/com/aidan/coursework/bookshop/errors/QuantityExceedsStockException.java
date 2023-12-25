package com.aidan.coursework.bookshop.errors;

import com.aidan.coursework.bookshop.book.BaseBook;

public class QuantityExceedsStockException extends BaseException {
	
	int stock;
	int requested;

	public QuantityExceedsStockException(BaseBook book, int requested_quantity) {
		super(String.format(
			"The selected quantity (%d) exceeds the quantity in stock for book: %s",
				requested_quantity,
				book.display()
		));
		stock = book.getQuantity();
		requested = requested_quantity;
	}
	
	public int getStockQuantity() {return stock;}
	public int getRequestedQuantity() {return requested;}


}
