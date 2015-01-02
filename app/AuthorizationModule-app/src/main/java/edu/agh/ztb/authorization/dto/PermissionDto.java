package edu.agh.ztb.authorization.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class PermissionDto implements Serializable {

	private static final long serialVersionUID = -7079026635001128196L;

	private String name;
}