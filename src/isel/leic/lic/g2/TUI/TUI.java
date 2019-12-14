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
import isel.leic.utils.Time;

public class TUI {
    // retorno ETIMEOUT significa utilizador ultrapassou o timeout e sistema deve voltar ao estado idle
    private static final int ETIMEOUT = -1;
    // retorno EABORT significa utilizador cancelou o comando
    private static final int EABORT = -2;

    private static final boolean CURSOR_SET = true;
    private static final boolean CURSOR_OFF = false;

    // Inicia a classe, estabelecendo os valores iniciais
    public static void init() {
        KBD.init();
        LCD.setSerialInterface(true);
        LCD.init();
        cursorSet(CURSOR_OFF);
    }

    // obtencao de input, preparando mensagem de introducao
    public static int readInput(String txt, int ndigits, boolean obsfucate, char obs, long timeout, int lin) {
        showMessage(txt, lin, 0);
        int input_col = txt.length();

        return readInteger(ndigits, obsfucate, obs, timeout, lin, input_col);
    }

    // apresentacao do tempo
    public static void showCurrentDateTime(int lin) {
        LocalDateTime currentLocal = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        showMessage(currentLocal.format(format), lin, 0);
    }

    // leitura de um carater
    public static char readInputKeyboard() {
        return KBD.getKey();
    }

    // apresentacao de mensagem, no ultimo local de cursor
    public static void showMessage(String msg) {
        LCD.write(msg);
    }

    // apresentacao de mensagem, na posicao pretendida
    public static void showMessage(String msg, int lin, int col) {
        LCD.cursor(lin, col);
        cursorSet(CURSOR_OFF);
        showMessage(msg);
    }

    // apresentacao de caracter
    public static void showCharacter(char c, int lin, int col) {
        showMessage(String.valueOf(c), lin, col);
    }

    // apresentacao de mensagem, com indicacao de centrada
    public static void showMessage(String msg, int lin, boolean center) {
        int col = 0;
        if (center)
            col = (LCD.COLS - msg.length()) / 2;

        showMessage(msg, lin, col);
    }

    // limpar ecra
    public static void clearScreen() {
        LCD.clear();
        cursorSet(CURSOR_OFF);
    }

    // limpar linha, verificar se o flicker é demasiado visivel com o eng.
    public static void clearLine(int lin) {
        char[] clear = new char[LCD.COLS];
        for (int i = 0; i < LCD.COLS; i++) {
            clear[i] = ' ';
        }
        showMessage(String.copyValueOf(clear), 1, 0);
        cursorSet(CURSOR_OFF);
    }

    public static void cursorSet(boolean set) {
        LCD.cursorSet(set);
    }

    // leitura de um numero, controlando a apresentacao do input do utilizador
    private static int readInteger(int ndigits, boolean obsfucate, char obs, long timeout, int lin, int col) {
        int value = 0;
        int i = 0;
        showInputRequest(ndigits, obsfucate, obs, lin, col);

        do {
            char c = KBD.waitKey(timeout);

            switch (c) {
                case 0:
                    value = ETIMEOUT;
                    i = ndigits;
                    break;
                case '*':
                    if (i > 0) {
                        value = 0;
                        i = 0;
                        showInputRequest(ndigits, obsfucate, obs, lin, col);
                    } else {
                        value = EABORT;
                        i = ndigits;
                    }
                    break;
                case '#':
                    continue;
                default:
                    value = (value * 10) + c - '0';
                    showCharacter(c, lin, col + i);
                    i++;
                    break;
            }
        } while (i < ndigits);

        return value;
    }

    private static void showInputRequest(int ndigits, boolean obsfucate, char obs, int lin, int col) {
        if (obsfucate) {
            String msg = "";
            for (int i = 0; i < ndigits; i++) {
                msg = msg + obs;
            }
            showMessage(msg, lin, col);
            LCD.cursor(lin, col);
            cursorSet(CURSOR_SET);
        }
    }

    public static void main(String[] args) {
        init();

        System.out.println("Testing TUI Class");
        String USER = "UIN:";
        int USER_DIGITS = 3;
        String PIN = "PIN:";
        int PIN_DIGITS = 4;

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
        int user_test = readInput(USER, USER_DIGITS, true, '?', 5000, 1);
        System.out.println("Resultado: " + user_test);

        clearScreen();

        System.out.println("4 - Teste ler passwd");
        System.out.println("pedir password: (introduzir 1234)");
        int pin_test = readInput(PIN, PIN_DIGITS, true, '?', 5000, 1);
        System.out.println("Resultado: " + pin_test);

        clearScreen();

        System.out.println("5 - Teste apresentacao tempo");
        showCurrentDateTime(0);
        Time.sleep(5000);

        System.out.println();
        System.out.println("6 - Teste limpar ecra");
        clearScreen();
    }
}
