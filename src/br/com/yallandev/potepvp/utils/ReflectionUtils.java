package br.com.yallandev.potepvp.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionUtils {

	public static void setValue(String field, Class<?> clazz, Object instance, Object value) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static Object getValue(String field, Class<?> clazz, Object instance) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static void setValue(String field, Object instance, Object value) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static Object getValue(String field, Object instance) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) throws Exception {
		return getConstructor(clazz, args).newInstance(initargs);
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception {
		Constructor<?> c = clazz.getConstructor(args);
		c.setAccessible(true);
		return c;
	}
}
