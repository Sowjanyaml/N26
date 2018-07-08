package com.n26.statistics.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transactions;
import com.n26.statistics.service.StatisticsServiceImpl;

@RestController
public class StatisticsControllerImpl implements StatisticsController {

	StatisticsServiceImpl stats = new StatisticsServiceImpl();
	
	@PostMapping(value = "/transactions", consumes = "application/json")
	public ResponseEntity<Void> registerTransaction(@RequestBody Transactions transaction) {

		long currentTimeInMilli = Instant.now().toEpochMilli();

		if (transaction == null)
			return ResponseEntity.noContent().build();

		if (transaction.getTimestamp() > (currentTimeInMilli - 60000)) {
			stats.addTransaction(transaction);
			return ResponseEntity.status(201).build();

		} else {
			stats.addTransaction(transaction);
			return ResponseEntity.status(204).build();
		}
	}

	@GetMapping(value = "/statistics")
	public Statistics retriveStatistics() {
		return stats.getStatisticsDetials();
	}
}
