package com.aidan.coursework.bookshop.gui;

import java.util.Comparator;

import com.aidan.coursework.bookshop.enums.AccountType;

public class StringCasterComparator implements Comparator<String[]>{
	
	AccountType type;
	
	public StringCasterComparator(AccountType type) {
		this.type = type;
	}
	
    @Override
    public int compare(String[] row1, String[] row2) {

		return switch (type) {
			// Sort by stock
			case AdminAccountType -> {
				Integer int1 = Integer.valueOf(row1[5]);
				Integer int2 = Integer.valueOf(row2[5]);
				yield int1.compareTo(int2);
			}
			// Sort by price
			case UserAccountType -> {
				// we need to remove the '£' sign before we can sort the column
				Float float1 = Float.valueOf(row1[6].substring(1));
				Float float2 = Float.valueOf(row2[6].substring(1));
				yield float1.compareTo(float2);
			}
		};
		
    }
}
