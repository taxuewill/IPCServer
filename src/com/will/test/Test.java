package com.will.test;

import com.will.common.Multipart;

public class Test {
	public static void main(String[] args) {
		String str = new String("Hello");
		Multipart multipart = new Multipart().a(str);
		System.out.println(multipart);
		System.out.println("Hello".hashCode());
	}
}
