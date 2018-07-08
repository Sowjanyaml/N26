package com.n26.statistics.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.n26.statistics.model.Transactions;

import net.minidev.json.JSONValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new StatisticsControllerImpl()).setHandlerExceptionResolvers()
				.build();
	}

	@Test
	public void oldTimestampRequestReturns204() throws Exception {

		long timeNow = Instant.now().toEpochMilli();
		Transactions transaction = new Transactions(100, timeNow - 60001);

		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(JSONValue.toJSONString(transaction))).andExpect(status().is(204));
	}

	@Test
	public void validTimestampRequestReturns201() throws Exception {
		long timeNow = Instant.now().toEpochMilli();
		Transactions transaction = new Transactions(10, timeNow + 1);
		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(JSONValue.toJSONString(transaction))).andExpect(status().is(201));

	}

	@Test
	public void transactionStatisticsReturnEmptyResult() throws Exception {

		this.mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(jsonPath("$.count", is(0)));

	}

	@Test
	public void transactionStatisticsReturnNotEmptyResult() throws Exception {

		long timeNow = Instant.now().toEpochMilli();
		Transactions transaction = new Transactions(100, timeNow);

		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(JSONValue.toJSONString(transaction)));

		this.mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(jsonPath("$.count", is(1)));
	}

}
