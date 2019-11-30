package isel.leic.lic.g2.Wrapper;

import isel.leic.lic.g2.HAL;
import isel.leic.lic.g2.SerialEmitter;
import isel.leic.utils.Time;

public class LCD {
    public static final int LINES = 2, COLS = 16; // Dimensão do display

    // Define se a interface com o LCD é série ou paralela
    private static boolean SERIAL_INTERFACE = false;

    // Define comandos para o LCD
    private static final int DISPLAY_CLEAR = 0x1;
    private static final int DISPLAY_ON = 0xf;
    private static final int DISPLAY_OFF = 0x8;

    // Mascara para envio do nibble em parallel
    private static int MASK_ENABLE = 0x20;      // 0010 0000
    private static int MASK_PARALLEL_RS = 0x10; // 000R 0000
    private static int MASK_LOW_DATA = 0x0f;    // 0000 1111
    private static int MASK_HIGH_DATA = 0xf0;   // 1111 0000

    // Envia a sequência de iniciação para a comunicação a 4 bits
    public static void init() {
        System.out.print("Initialization LCD...\n");
        Time.sleep(15); // 1 standby
        Time.sleep(5);  // 2 standby
        Time.sleep(1);  // 3 timeout
        System.out.println("LCD ready");
    }

    // Escreve um caráter na posição corrente
    public static void write(char c) {
        System.out.print(c);
    }

    // Escreve uma string na posição corrente
    public static void write(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            write(txt.charAt(i));
        }
    }

    // Envia comando para posicionar cursor (‘lin’:0..LINES-1 , ‘col’:0..COLS-1)
    public static void cursor(int lin, int col) {
        System.out.println("Set cursor at: " + (0x80 | (lin << 6) | col));
    }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0)
    public static void clear() {
        System.out.println("Clear LCD");
    }

    // Define o valor do interface
    public static void setSerialInterface(boolean set) {
        SERIAL_INTERFACE = set;
    }
}
