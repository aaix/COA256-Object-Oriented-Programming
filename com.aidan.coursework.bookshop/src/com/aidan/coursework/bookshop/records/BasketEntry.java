package com.aidan.coursework.bookshop.records;

import com.aidan.coursework.bookshop.book.BaseBook;

public record BasketEntry(
	BaseBook book,
	int quantity
) {}
