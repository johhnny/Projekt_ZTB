package edu.agh.ztb.authorization.transformer;

public interface Transformer<F, T> {
	T transform(F from);
}