package com.moguhu.id.creator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {

	public static void main(String[] args) {

		Date date = new Date(1288834974657L);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println(format.format(date));
		
	}

}
