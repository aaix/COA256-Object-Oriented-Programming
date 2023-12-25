package com.aidan.coursework.bookshop.errors;

public class InsufficientFundsException extends BaseException {
	
	float current_bal;
	float required_bal;
	float bal_mismatch;

	public InsufficientFundsException(float current_bal, float required_bal, float bal_mismatch) {
		super(String.format("The account funds of £%.2f do not cover the cost of £%.2f (missing £%.2f)", current_bal, required_bal, bal_mismatch));
	}

	public float getCurrent_bal() {
		return current_bal;
	}

	public float getRequired_bal() {
		return required_bal;
	}

	public float getBal_mismatch() {
		return bal_mismatch;
	}

}
