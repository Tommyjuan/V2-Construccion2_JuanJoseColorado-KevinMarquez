/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import java.util.Scanner;

/**
 *
 * @author Camilo
 */
public abstract class Utils {

    private static Scanner reader = new Scanner(System.in);

    public static Scanner getReader() {
        return reader;
    }
}