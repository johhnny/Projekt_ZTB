package edu.agh.ztb.authorization.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractTransformer<F, T> implements Transformer<F, T> {

	public List<T> transformCollection(Collection<F> list) {
		if (list == null) {
			return null;
		}
		List<T> result = new ArrayList<T>();
		for (F from : list) {
			result.add(transform(from));
		}
		return result;
	}
}