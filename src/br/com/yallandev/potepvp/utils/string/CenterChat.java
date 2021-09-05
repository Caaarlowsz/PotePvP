package br.com.yallandev.potepvp.utils.string;

import org.bukkit.ChatColor;

public class CenterChat {

	private final static int CENTER_PX = 154;

	public static String centered(String string) {
		if (string == null || string.equals("")) {
			return "";
		}
		string = ChatColor.translateAlternateColorCodes('&', string);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : string.toCharArray()) {
			if (c == '�') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else
					isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder stringBuilder = new StringBuilder();
		while (compensated < toCompensate) {
			stringBuilder.append(" ");
			compensated += spaceLength;
		}
		return stringBuilder.toString() + string;
	}

	public static String centered(String string, int center) {
		if (string == null || string.equals("")) {
			return "";
		}
		string = ChatColor.translateAlternateColorCodes('&', string);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : string.toCharArray()) {
			if (c == '�') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else
					isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = center - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder stringBuilder = new StringBuilder();
		while (compensated < toCompensate) {
			stringBuilder.append(" ");
			compensated += spaceLength;
		}
		return stringBuilder.toString() + string;
	}
}
