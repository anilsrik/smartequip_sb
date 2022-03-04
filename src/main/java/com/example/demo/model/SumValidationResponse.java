package com.example.demo.model;


import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class SumValidationResponse {
	
	@NotEmpty
	public List<String> input;
	@NotBlank
	public String answer;	
}
