package com.n26.statistics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.n26.statistics.model.Statistics;
import com.n26.statistics.model.Transactions;

public interface StatisticsController {

	ResponseEntity<Void> registerTransaction(Transactions trans);

	Statistics retriveStatistics();

}