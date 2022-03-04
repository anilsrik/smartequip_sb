package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.demo.exception.CustomException;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SumChallengeService {

	public List<Integer> getRandomNumbers(Integer count, Integer min_value, Integer max_value) throws CustomException {
		
		log.info("Generating random numbers");
		List<Integer> randInts = new ArrayList<Integer>();
		
		Random rand = new Random();
		int i = 0;

		while (i < count) {
			randInts.add(rand.nextInt((max_value - min_value) + 1) + min_value);
			i++;
		}
		return randInts;
	}

}
