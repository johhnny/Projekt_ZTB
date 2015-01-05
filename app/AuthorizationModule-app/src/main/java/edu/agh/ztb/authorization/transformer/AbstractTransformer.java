package edu.agh.ztb.authorization.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractTransformer<F, T> implements Transformer<F, T> {

	public List<T> transformCollection(Collection<F> collection) {
		if (collection == null) {
			throw new NullPointerException("Input must not be null.");
		}
		List<T> result = new ArrayList<T>();
		for (F from : collection) {
			result.add(transform(from));
		}
		return result;
	}
}