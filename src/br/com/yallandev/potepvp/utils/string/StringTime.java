package br.com.yallandev.potepvp.utils.string;

public class StringTime {
	
	public static String formatTime(Integer i) {
		int minutes = i / 60;
		int seconds = i % 60;
		
		String disMinu = (minutes < 10 ? "" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = disMinu + ":" + disSec;
		
		return formattedTime;
	}
}
