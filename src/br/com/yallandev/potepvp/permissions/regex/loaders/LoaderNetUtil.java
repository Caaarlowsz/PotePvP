package br.com.yallandev.potepvp.permissions.regex.loaders;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.minecraft.util.com.google.common.cache.CacheLoader;

public class LoaderNetUtil extends CacheLoader<String, Pattern> {

	public static final String RAW_REGEX_CHAR = "$";

	public Pattern load(String arg0) throws Exception {
		return createPattern(arg0);
	}

	protected static Pattern createPattern(String expression) {
		try {
			return Pattern.compile(prepareRegexp(expression), 2);
		} catch (PatternSyntaxException e) {
		}
		return Pattern.compile(Pattern.quote(expression), 2);
	}

	public static String prepareRegexp(String expression) {
		if (expression.startsWith("-")) {
			expression = expression.substring(1);
		}
		if (expression.startsWith("#")) {
			expression = expression.substring(1);
		}
		boolean rawRegexp = expression.startsWith("$");
		if (rawRegexp) {
			expression = expression.substring(1);
		}
		String regexp = rawRegexp ? expression : expression.replace(".", "\\.").replace("*", "(.*)");
		return regexp;
	}
}
