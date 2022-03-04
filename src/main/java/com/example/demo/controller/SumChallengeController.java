package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.exception.CustomException;
import com.example.demo.model.SumValidationResponse;
import com.example.demo.service.SumChallengeService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("challenge")
@Log4j2
public class SumChallengeController {

	@Autowired
	private SumChallengeService sumChallengeService;

	private static Boolean validateRequest(List<String> headerStrs, List<String> inputStrs) {
		return headerStrs.size() == inputStrs.size() && headerStrs.containsAll(inputStrs);
	}

	@PostMapping(value = "validatesum")
	public ResponseEntity<String> validatesum(@RequestBody @Valid SumValidationResponse response,
			@RequestHeader("X-Request-Id") String dashSeparatedInput) throws CustomException {
		log.info("Validate Response", response);
		if (!dashSeparatedInput.isEmpty()) {
			Boolean isValidRequest = validateRequest(Arrays.asList(dashSeparatedInput.split("-")), response.input);
			if (!isValidRequest) {
				log.error("Error - mismatch in client request/reponse");
			}
		}
		try {
			int sum = response.input.stream().map(Integer::parseInt).reduce(Integer::sum).get();
			if (sum != Integer.parseInt(response.answer)) {
				return ResponseEntity.badRequest().body("Invalid Answer");
			}
		} catch (NumberFormatException e) {
			ResponseEntity.badRequest().body("Invalid Input");
		} catch (Exception e) {
			log.error("Error in validation", e);
			ResponseEntity.badRequest().body("Error Occured");
		}
		return ResponseEntity.ok().body("Good");
	}

	@GetMapping(value = "sumchallenge", produces = "application/json")
	public ResponseEntity<List<Integer>> sumchallenge(
			@RequestParam(value = "count", defaultValue = "2", required = false) Optional<Integer> count,
			@RequestParam(value = "min_val", defaultValue = "0", required = false) Optional<Integer> min_val,
			@RequestParam(value = "max_val", defaultValue = "100", required = false) Optional<Integer> max_val)
			throws CustomException {

		log.info("Get random numbers for ", count);

		if (count.get() < 2 || count.get() > 5) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Count Value must be within range (2,5) both included");
		}

		List<Integer> randInts = sumChallengeService.getRandomNumbers(count.get(), min_val.get(), max_val.get());
		String dashSeparated = randInts.stream().map(i -> i.toString()).collect(Collectors.joining("-"));

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("X-Request-Id", dashSeparated); // This can be encrypted to make it more secure
		return ResponseEntity.ok().headers(responseHeaders).body(randInts);
	}

}
