/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * A classe Maintenance disponibiliza todos os metodos para permitir efetuar
 * as operacoes presentes nos requisitos
 *  - Adicionar utilizador
 *  - Apagar utilizador
 *  - Adicionar mensagem a utilizador
 *  - Terminar a aplicacao
 */
package isel.leic.lic.g2;

import isel.leic.lic.g2.TUI.TUI;
import isel.leic.lic.g2.User.User;
import isel.leic.lic.g2.User.Users;

import java.util.Scanner;

public class Maintenance {
    private static Scanner sc;
    private static boolean turnOff;

    // RegExp para validar o uin
    private static final String USER_PATTERN = "^\\d+";
    // RegExp para validar o pin
    private static final String PIN_PATTERN = "^\\d{6}";
    // erro no pin introduzido
    private static final int EPIN = -1;

    // inicializa a classe
    public static void init() {
        sc = new Scanner(System.in);
        turnOff = false;
    }

    // Entrada na classe para obter o menu de opcoes
    public static void enterMaintenance() {
        showMaintenanceMenu();
        while (M.isMaintenanceMode() && !turnOff) {
            System.out.print("Maintenance> ");
            String cmd = sc.nextLine();
            cmd = cmd.toUpperCase();

            switch (cmd) {
                case "NEW":
                    maintenanceNewUser();
                    break;
                case "DEL":
                    maintenanceDelUser();
                    break;
                case "MSG":
                    maintenanceInsertMessage();
                    break;
                case "OFF":
                    turnOff = true;
                    TUI.showCenterMessage("Shutdown...", 1);
                    break;
                case "":
                    continue;
                default:
                    System.out.println("Invalid command: " + cmd);
                    break;
            }
        }

        if (!turnOff)
            System.out.println("Existing maintenance mode...");
    }

    // Indica se a opcao de terminar a aplicacao foi dado
    public static boolean endApp() {
        return turnOff;
    }

    // permite adicionar um novo utilizador ao sistema
    private static void maintenanceNewUser() {
        System.out.print("User name? ");
        String name = sc.nextLine();

        int pin = getPin();
        if (pin < 0) {
            System.out.println("Aborted command.");
            return;
        }

        int uin = Users.getAvailableUIN();
        User u = new User(name, uin, pin, "");
        if (Users.addUser(u))
            System.out.println("Adding user " + u.toString());
        else
            System.out.println("Exceeded maximum number of users available.");
    }

    // permite remover um utilizador do sistema
    private static void maintenanceDelUser() {
        User u = getUser();
        if (u == null) {
            System.out.println("Invalid UIN");
            return;
        }

        System.out.println("Remove user " + u.toString());
        if (confirm()) {
            System.out.println("User " + u.toString() + " removed");
            Users.removeUser(u);
        } else {
            System.out.println("Command aborted");
        }
    }

    // permite adicionar uma mensagem ao utilizador
    private static void maintenanceInsertMessage() {
        User u = getUser();
        if (u == null) {
            System.out.println("Invalid UIN");
            return;
        }

        if (!u.getMessage().equals("")) {
            System.out.println("User has this message: " + u.getMessage());

            if (!confirm("Remove this message")) {
                System.out.println("Command aborted");
                return;
            }
        }

        System.out.print("Message? ");
        String message = sc.nextLine();

        if (message.length() > 32) {
            System.out.println("Message to long for display");
            System.out.println("Command aborted");
            return;
        }

        System.out.println("The message " + message + " as been associated to " + u.toString());
        u.changeMessage(message);
    }

    // adiciona uma mensagem ao metodo de confirmacao
    private static boolean confirm(String msg) {
        System.out.print(msg + " ");
        return confirm();
    }

    // metodo de confirmacao pelo administrador da acao
    private static boolean confirm() {
        System.out.print("Y/N? ");
        String confirm = "Y";
        String answer = sc.nextLine();

        return confirm.equalsIgnoreCase(answer);
    }

    // metodo para pedir ao administrador o uin
    private static User getUser() {
        System.out.print("UIN? ");
        String uin = sc.nextLine();

        if (!uin.matches(USER_PATTERN))
            return null;

        return Users.searchUser(Integer.parseInt(uin));
    }

    // metodo para pedir ao administrador o pin
    private static int getPin() {
        boolean ask = true;
        int newPin = 0;

        while (ask) {
            System.out.print("PIN? ");
            String pin = sc.nextLine();

            if (pin.length() == 0) {
                ask = false;
                return EPIN;
            } else if (pin.matches(PIN_PATTERN)) {
                    newPin = Integer.parseInt(pin);
                    ask = false;
            } else {
                System.out.println("The length of PIN must be of 4 digits.");
            }
        }

        return newPin;
    }

    // metodo para apresentar informacao na entrada do modo manutencao
    private static void showMaintenanceMenu() {
        TUI.clearScreen();
        TUI.showCenterMessage("Out of Service", 0);
        TUI.showCenterMessage("Wait", 1);

        System.out.println("Turn M key off and ENTER, to terminate the maintenance mode.");
        System.out.println("Commands: NEW, DEL, MSG, or OFF");
    }
}
