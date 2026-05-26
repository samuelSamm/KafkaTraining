package com.tecnosolution.KafkaTraining.controller;

public class TestUno {
    public static boolean test(String word) {
        if (word == null) return false;
        word = word.replace(" ", "");
        int j = word.length() - 1;
        for (int i = 0; i < word.length() / 2; i++) {
            if (Character.toLowerCase(word.charAt(i)) != Character.toLowerCase(word.charAt(j))) {
                return false;
            }
            j--;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(test("anilina"));
        System.out.println(test("paco"));
        System.out.println(test("anita lava la tina"));
    }
}
