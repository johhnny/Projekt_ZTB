package edu.agh.ztb.authorization.dto;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class ErrorWrapper {
	private int errorCode;
	private String errorMessage;
}