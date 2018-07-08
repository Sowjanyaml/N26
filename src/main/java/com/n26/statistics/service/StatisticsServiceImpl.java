package com.n26.statistics.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transactions;

public class StatisticsServiceImpl implements StatisticsService {

	private final int INTERVAL_IN_SECS = 60;
	private final int THOUSAND = 1000;

	private ConcurrentHashMap<Long, List<Transactions>> transactionMap;

	public StatisticsServiceImpl() {
		transactionMap = new ConcurrentHashMap<>();
	}

	@Override
	public void addTransaction(Transactions transaction) {

		List<Transactions> transactionList;
		long timestampInSecs = transaction.getTimestamp() / THOUSAND;
		if (transactionMap.containsKey(timestampInSecs)) {
			transactionList = transactionMap.get(timestampInSecs);
		} else {
			transactionList = new ArrayList<Transactions>();
		}
		transactionList.add(transaction);
		transactionMap.put(timestampInSecs, transactionList);
	}

	@Override
	public Statistics getStatisticsDetials() {

		long currentTimeInSeconds = Instant.now().getEpochSecond();
		List<Transactions> transactionsInLast60Sec = new ArrayList<>();
		IntStream.range(0, INTERVAL_IN_SECS)
		  .forEach(index -> {
			  if (transactionMap.get(currentTimeInSeconds-index) != null) {
					transactionsInLast60Sec.addAll(transactionMap.get(currentTimeInSeconds-index));
			  }});
		return calculateStatistics(transactionsInLast60Sec);
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
