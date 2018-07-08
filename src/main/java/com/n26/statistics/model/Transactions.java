package com.n26.statistics.model;

public class Transactions {
	
	double amount;
    long timestamp;
    
    public Transactions() {
    }

    public Transactions(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }
    
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
    
    

}
