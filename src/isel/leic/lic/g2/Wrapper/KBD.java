package isel.leic.lic.g2.Wrapper;

import java.util.Scanner;

import isel.leic.utils.Time;

public class KBD {

    private static Scanner in;
    public static final char NONE = 0;
    public static final char[][] MAP_CHAR = {
            {'1', '4', '7', '*'},
            {'2', '5', '8', '0'},
            {'3', '6', '9', '#'}
    };

    // inicia a classe
    public static void init() {
        in = new Scanner(System.in);
        System.out.println("KBD init");
    }

    // retorna de imediato a tecla premida ou NONE se não tecla premida.
    public static char getKey() {
        char key = NONE;

        System.out.println();
        System.out.print("Insert char: 0..9 or * or #    :  ");
        key = in.nextLine().charAt(0);

        return key;
    }

    // retorna quando a tecla for premida ou NONE após
    // decorrido 'timeout' milisegundos
    public static char waitKey(long timeout) {
        char key = NONE;
        long time = Time.getTimeInMillis() + timeout;

        while ((Time.getTimeInMillis() < time) || (key == NONE)) {
            key = getKey();
        }
        return key;
    }

    private static char mapKeyToChar(int k) {
        return MAP_CHAR[k>>2][k & 0x3];
    }
}
