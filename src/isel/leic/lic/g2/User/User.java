/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * Classe para modelar a informacao de User a utilizar pelo sistema,
 * apresentando diversos metodos para facilitar a sua gestao
 */

package isel.leic.lic.g2.User;

import java.util.Scanner;

public class User {
    private final char separator = ';';
    private String name;
    private int uin;
    private String pin;
    private String message;

    // construtor passando todos os campos
    public User(String name, int uin, int pin, String message) {
        this.name = name;
        this.uin = uin;
        this.pin = generatePassword(pin);
        this.message = message;
    }

    // construtor passando uma string, fazendo a sua separacao
    public User(String user) {
        String[] outStr = user.split(";",0);
        uin = Integer.parseInt(outStr[0]);
        pin = outStr[1];
        name = outStr[2];
        message = (outStr.length == 4) ? outStr[3] : "";
    }

    // permite codificar o pin do utilizador
    private String generatePassword(int value) {
        return Integer.toString((value - 32) / 2);
    }

    // permite comparar o pin passado com o pin guardado
    // nao e feita uma desencriptacao
    public boolean checkPin(int pinCheck) {
        String codedPin = generatePassword(pinCheck);
        return pin.equals(codedPin);
    }

    // permite mudar o pin do utilizador
    public void changePin(int newPin) {
        pin = generatePassword(newPin);
    }

    public String getMessage() {
        return message;
    }

    public void removeMessage() {
        message = "";
    }

    public void changeMessage(String str) {
        message = str;
    }

    public int getUID() {
        return uin;
    }

    public String save() {
        String msg = (message.length() > 0) ? message + separator : "";
        return String.valueOf(uin) + separator + String.valueOf(pin) + separator + name + separator + msg;
    }

    @Override
    public String toString() {
        return String.valueOf(uin) + separator + name;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Teste User");
        System.out.println("1 - Adicionar novo utilizador");
        System.out.println("Insira nome e carregue enter:");
        String name = sc.nextLine();
        System.out.println("Insira uin e carregue enter:");
        int uin = Integer.parseInt(sc.nextLine());
        System.out.println("Insira pin e carregue enter:");
        int pin = Integer.parseInt(sc.nextLine());
        System.out.println("Insira msg e carregue enter:");
        String msg = sc.nextLine();
        User u = new User(name, uin, pin, msg);
        System.out.println(u.toString());

        System.out.println("2 - Comparar pin com pin errado");
        System.out.println("Insira o pin incorreto e carregue enter:");
        int checkPin = Integer.parseInt(sc.nextLine());
        String check = u.checkPin(checkPin) == true ? "correto" : "errado";
        System.out.println("pin = " + check);

        System.out.println("3 - Comparar pin com pin correto");
        System.out.println("Insira o pin incorreto e carregue enter:");
        checkPin = Integer.parseInt(sc.nextLine());
        check = u.checkPin(checkPin) == true ? "correto" : "errado";
        System.out.println("pin = " + check);

        System.out.println("4 - Alterar o pin");
        System.out.println("Insira novo pin");
        int newPin = Integer.parseInt(sc.nextLine());
        u.changePin(newPin);
        System.out.println(u.toString());
    }
}
