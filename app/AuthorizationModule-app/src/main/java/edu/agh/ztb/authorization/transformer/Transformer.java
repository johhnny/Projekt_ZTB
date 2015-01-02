package edu.agh.ztb.authorization.transformer;

import java.util.Collection;
import java.util.List;

public interface Transformer<F, T> {
	T transform(F from);
	List<T> transformCollection(Collection<F> from);
}