package com.n26.statistics.controller;

import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transactions;
import com.n26.statistics.service.StatisticsServiceImpl;

@RestController
@RequestMapping(value = "/api")
public class StatisticsControllerImpl implements StatisticsController {

	StatisticsServiceImpl stats = new StatisticsServiceImpl();

	@Override
	@PostMapping(value = "/transactions", consumes = "application/json")
	public ResponseEntity<Void> registerTransaction(@RequestBody Transactions trans) {

		// TODO Validate of Transaction
		Instant instant = Instant.now();
		long timeNow = instant.toEpochMilli();

		if (trans == null)
			return ResponseEntity.noContent().build();

		if (trans.getTimestamp() > (timeNow - 60000)) {// || trans.getTimestamp()==0L

			stats.addTransaction(trans);
			return ResponseEntity.status(201).build();

		} else {
			stats.addTransaction(trans);
			return ResponseEntity.status(204).build();
		}

	}

	@Override
	@GetMapping(value = "/statistics")
	public Statistics retriveStatistics() {
		return stats.getStatisticsDetials();
	}

	@GetMapping(value = "/transactionList/{time}") // For Testing
	public List<Transactions> retriveTransactionList(@PathVariable("time") long time) {
		return stats.getTransactionList(time / 1000);
	}

}
