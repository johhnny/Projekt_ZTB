package edu.agh.ztb.authorization.transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.agh.ztb.authorization.dto.PermissionDto;
import edu.agh.ztb.authorization.model.Permission;

public class Permission2PermissionDtoTransformerTest {
	private final static String PERM1_NAME = "perm1_name";
	private final static String PERM1_DESC = "perm1_desc";
	private final static String PERM2_NAME = "perm2_name";
	private final static String PERM2_DESC = "perm2_desc";

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private Permission2PermissionDtoTransformer transformer;

	@Before
	public void setUp() {
		transformer = new Permission2PermissionDtoTransformer();
	}

	@Test
	public void transform_shouldThrowNPE() {
		exception.expect(NullPointerException.class);
		exception.expectMessage("Input must not be null.");

		transformer.transform(null);
	}

	@Test
	public void transformCollection_shouldThrowNPE() {
		exception.expect(NullPointerException.class);
		exception.expectMessage("Input must not be null.");

		transformer.transformCollection(null);
	}

	@Test
	public void transform_shouldReturnCorrectPermissionDto() {
		// given
		Permission permission = new Permission();
		permission.setName(PERM1_NAME);
		permission.setDescription(PERM1_DESC);

		// when
		PermissionDto permissionDto = transformer.transform(permission);

		// then
		assertThat(permissionDto, is(not(nullValue())));
		assertThat(permissionDto.getName(), equalTo(PERM1_NAME));
	}

	@Test
	public void transform_shouldReturnPermissionDtoCollection() {
		// given
		Permission permission1 = new Permission();
		permission1.setName(PERM1_NAME);
		permission1.setDescription(PERM1_DESC);

		Permission permission2 = new Permission();
		permission2.setName(PERM2_NAME);
		permission2.setDescription(PERM2_DESC);

		// when
		List<PermissionDto> permissionDtos = transformer.transformCollection(Arrays.asList(permission1, permission2));

		// then
		assertThat(permissionDtos, is(not(nullValue())));
		assertThat(permissionDtos, hasSize(2));
		assertThat(permissionDtos, containsInAnyOrder(PermissionDto.builder().name(PERM1_NAME).build(), PermissionDto.builder().name(PERM2_NAME).build()));
	}
}