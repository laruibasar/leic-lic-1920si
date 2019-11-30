/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * A classe TUI introduz os métodos necessários para abstrair a interação com o LCD e o KBD pela aplicação.
 *
 * Os métodos permitem responder às necessidades da aplicação para apresentar informação no LCD
 * bem como obter valores ou comandos dados pelo utilizador através do KBD.
 *
 */
package isel.leic.lic.g2.TUI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import isel.leic.lic.g2.LCD.LCD;
import isel.leic.lic.g2.Keyboard.KBD;

public class TUI {
    private static final String USER = "UIN:";
    private static final int USER_DIGITS = 3;
    private static final String PIN = "PIN:";
    private static final int PIN_DIGITS = 4;

    // Inicia a classe, estabelecendo os valores iniciais
    public static void init() {
        KBD.init();
        LCD.init();
        LCD.setSerialInterface(true);
    }

    // obtencao de input, preparando mensagem de introducao
    public static int readInput(String txt, int ndigits, boolean obsfucate, char obs, long timeout, int lin, int col) {
        showMessage(txt, lin, col);
        int input_col = col + txt.length() + 1;

        return readInteger(ndigits, obsfucate, obs, timeout, lin, input_col);
    }

    // apresentacao do tempo
    public static void showCurrentDateTime(int lin, int col) {
        LocalDateTime currentLocal = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        showMessage(currentLocal.format(format), lin, col);
    }

    // leitura de um numero, controlando a apresentacao do input do utilizador
    // retorno -1 significa utilizador ultrapassou o timeout e sistema deve voltar ao estado idle
    public static int readInteger(int ndigits, boolean obsfucate, char obs, long timeout, int lin, int col) {
        int value = 0;
        if (obsfucate) {
            String msg = "";
            for (int i = 0; i < ndigits; i++) {
                msg = msg + obs;
            }
            showMessage(msg, lin, col);
        }

        for (int i = 0; i < ndigits; i++) {
            char c = KBD.waitKey(timeout);
            if (c == 0) {
                value = -1;
                break;
            } else if (c == '*') {
                // there'll be dragons (the danger of recursivity)
                value = readInteger(ndigits, obsfucate, obs, timeout, lin, col);
                break;
            } else if (c == '#') {
                continue;
            }
            value = (value * 10) + c - '0';
            showCharacter(c, lin, col + i);
        }

        return value;
    }

    // leitura de um carater
    public static char readInputKeyboard() {
        return KBD.getKey();
    }

    // apresentacao de mensagem
    public static void showMessage(String msg, int lin, int col) {
        LCD.cursor(lin, col);
        LCD.write(msg);
    }

    // apresentacao de caracter
    public static void showCharacter(char c, int lin, int col) {
        LCD.cursor(lin, col);
        LCD.write(c);
    }

    // limpar ecra
    public static void clearScreen() {
        LCD.clear();
    }

    public static void main(String[] args) {
        init();

        System.out.println("Testing TUI Class");

        System.out.println("Teste readInteger: ");
        System.out.println("1 - Ler input: lin 1, col 1");
        showCharacter('>', 1, 0);
        int input1 = readInteger(USER_DIGITS, true, '?', 5000, 1, 2);
        System.out.println("Resultado: " + Integer.toString(input1));

        clearScreen();

        System.out.println("2 - Ler input: lin 0, col 1");
        showCharacter('>', 0, 0);
        int input2 = readInteger(PIN_DIGITS, true, '*', 5000, 0, 2);
        System.out.println("Resultado: " + Integer.toString(input2));

        clearScreen();

        System.out.println("3 - Teste ler User");
        System.out.println("Pedir utilizador (introduzir 123)");
        int user_test = readInput(USER, USER_DIGITS, true, '?', 5000, 1, 0);
        System.out.println("Resultado: " + user_test);

        clearScreen();

        System.out.println("4 - Teste ler passwd");
        System.out.println("pedir password: (introduzir 1234)");
        int pin_test = readInput(PIN, PIN_DIGITS, true, '?', 5000, 1, 0);
        System.out.println("Resultado: " + pin_test);

        clearScreen();

        System.out.println("5 - Teste apresentacao tempo");
        showCurrentDateTime(0, 0);

        System.out.println();
        System.out.println("6 - Teste limpar ecra");
        clearScreen();
    }
}
