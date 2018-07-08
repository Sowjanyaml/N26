package com.n26.statistics.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transactions;
@Configuration
public class StatisticsServiceImpl implements StatisticsService {

	Instant instant;
	long timeNow;
	private final int INTERVAL = 60;
	@Value ("${api-INTERVAL}")
	int myInterval;
	private final int THOUSAND = 1000;

	ConcurrentHashMap<Long, List<Transactions>> repo;

	public StatisticsServiceImpl() {
		repo = new ConcurrentHashMap<>();
	}

	@Override
	public synchronized void addTransaction(Transactions transaction) {

		List<Transactions> transactionList;
		long timestampInSecs = transaction.getTimestamp() / THOUSAND;
		if (repo.containsKey(timestampInSecs)) {
			transactionList = repo.get(timestampInSecs);
			transactionList.add(transaction);
			repo.put(timestampInSecs, transactionList);
		} else {
			transactionList = new ArrayList<Transactions>();
			transactionList.add(transaction);
			repo.put(timestampInSecs, transactionList);
		}
	}

	@Override
	public Statistics getStatisticsDetials() {

		long currentTimeInSeconds = Instant.now().getEpochSecond();
		List<Transactions> transactionsInLast60Sec = new ArrayList<>();

		for (long i = currentTimeInSeconds; i > currentTimeInSeconds - myInterval; i--) {

			if (repo.get(i) != null) {
				transactionsInLast60Sec.addAll(repo.get(i));

			}
		}

		return calculateStatistics(transactionsInLast60Sec);
	}

	public List<Transactions> getTransactionList(long time) { // For Testing

		return repo.get(time);
	}

	private Statistics calculateStatistics(List<Transactions> transactionsInLast60Sec) {

		Statistics results = new Statistics();
		DoubleSummaryStatistics dstats = transactionsInLast60Sec.stream()
				.collect(Collectors.summarizingDouble(Transactions::getAmount));

		results.setSum(dstats.getSum());
		results.setMax(dstats.getMax());
		results.setMin(dstats.getMin());
		results.setCount(dstats.getCount());
		results.setAvg(dstats.getAverage());

		return results;
	}

}
