package com.n26.statistics.service;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transactions;

@RunWith(SpringRunner.class)
public class StatisticsServiceTests {

	StatisticsService statisticsService = new StatisticsServiceImpl();

	@Test
	public void testStatisticsDetails() throws Exception {

		long timeNow = Instant.now().toEpochMilli();

		statisticsService.addTransaction(new Transactions(50.0, timeNow)); // Min
		for (int i = 0; i < 8; i++) {
			statisticsService.addTransaction(new Transactions(100.0, timeNow + 1 + i));
		}

		statisticsService.addTransaction(new Transactions(150.0, timeNow + 20)); // Max

		Statistics results = statisticsService.getStatisticsDetials();

		assertEquals(150.0, results.getMax(), 0);
		assertEquals(50.0, results.getMin(), 0);
		assertEquals(100.0, results.getAvg(), 0);
		assertEquals(10.0, results.getCount(), 0);
		assertEquals(1000.0, results.getSum(), 0);

	}

	@Test
	public void testStatisticsDetailsWithEmptyData() throws Exception {
		Statistics results = statisticsService.getStatisticsDetials();

		assertEquals(0, results.getCount(), 0);
		assertEquals(0, results.getSum(), 0);

	}

}
