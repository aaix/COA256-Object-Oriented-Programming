package com.aidan.coursework.bookshop.account;

import com.aidan.coursework.bookshop.book.BaseBook;
import com.aidan.coursework.bookshop.enums.AccountType;
import com.aidan.coursework.bookshop.errors.*;
import com.aidan.coursework.bookshop.records.BasketEntry;
import com.aidan.coursework.bookshop.database.FileHandler;

import java.util.ArrayList;
import java.util.HashMap;


public class UserAccount extends BaseAccount {
	
	float credit_balance;
	HashMap<Integer, BasketEntry> basket;
	
	public UserAccount(
		int account_id,
		String username,
		String surname,
		String house_no,
		String postcode,
		String city,
		float credit_balance
	) {

		super(account_id, username, surname, house_no, postcode, city, AccountType.UserAccountType);
		this.credit_balance = credit_balance;
		this.elevated_permissions = false;
		this.basket = new HashMap<Integer, BasketEntry>();
	}
	
	
	public void cancel_basket() {
		this.basket = new HashMap<Integer, BasketEntry>();
	};
	
	public float getCredit_balance() {
		return credit_balance;
	}
	

	public String display() {
		return "[USER]  " + super.display();
	}


	public void add_book_to_basket(BaseBook book) throws QuantityExceedsStockException {
		int id = book.id();
		
		if (book.getQuantity() < 1) {
			throw new QuantityExceedsStockException(book, 1);
		};
		
		if (basket.containsKey(id)) {
			int old_quantity = basket.get(id).quantity();
			
			update_quantity(book, old_quantity+1);
			
		} else { // add new book to basket
			basket.put(id, new BasketEntry(
				book,
				1
			));
		}
	}
	
	public ArrayList<BasketEntry> get_basket() {
		return new ArrayList<BasketEntry>(this.basket.values());
	}
	
	public void update_quantity(BaseBook book, int quantity) throws QuantityExceedsStockException {
		if (book.getQuantity() < quantity) {
			throw new QuantityExceedsStockException(book, quantity);
		};
		basket.put(book.id(), new BasketEntry(
			book,
			quantity
		));
	}


	public int basket_size() {
		return basket.size();
	}
	
	public float basket_cost() {
		float cost = 0;
		for (BasketEntry entry: this.get_basket()) {
			cost += (entry.book().getPrice() * entry.quantity());
		};
		return cost;
	}
	
	public float checkout (FileHandler<BaseBook> books, FileHandler<BaseAccount> accounts) throws InsufficientFundsException {
		
		float cost = basket_cost();
		
		if (cost > credit_balance) {
			throw new InsufficientFundsException(credit_balance, cost, cost - credit_balance);
		};
		
		for (BasketEntry entry: get_basket()) {
			entry.book().decrease_quantity(entry.quantity());
		}
		
		this.credit_balance = this.credit_balance - cost;
		
		books.update();
		accounts.update();
		cancel_basket();
		return this.credit_balance;
	};
}
