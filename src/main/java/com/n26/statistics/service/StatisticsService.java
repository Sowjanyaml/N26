package com.n26.statistics.service;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transactions;

public interface StatisticsService {

	void addTransaction(Transactions transaction);

	Statistics getStatisticsDetials();

}