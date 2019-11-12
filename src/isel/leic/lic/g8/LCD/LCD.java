package isel.leic.lic.g8.LCD;

import isel.leic.lic.g8.HAL;

public class LCD {
    public static final int LINES = 2, COLS = 16; // Dimensão do display

    // Define se a interface com o LCD é série ou paralela
    private static final boolean SERIAL_INTERFACE = false;

    // MASK for sending nibble (parallel)
    private static int PARALLEL_MASK = 0x3f; // 0011 1111
    private static int ENABLE_BIT = 5;
    private static int RS_BIT = 4;

    private static boolean DEBUG = false;

    // Escreve um nibble de comando/dados no LCD em paralelo
    // the HAL signal will be: 0 0 E (RS) NIBBLE
    //
    private static void writeNibbleParallel(boolean rs, int data) {
        int value = 0;

        value = (1 << ENABLE_BIT); // enable : 1 << 6
        if (rs)
            value |= (1 << RS_BIT); // rs : rs << 5

        // value : 0 0 E (RS) D A T A
        if (DEBUG)
            System.out.println(value | data);

        HAL.writeBits(PARALLEL_MASK, value | data);
    }

    // Escreve um nibble de comando/dados no LCD em série
    private static void writeNibbleSerial(boolean rs, int data) { }

    // Escreve um nibble de comando/dados no LCD
    private static void writeNibble(boolean rs, int data) {
        if (SERIAL_INTERFACE)
            writeNibbleSerial(rs, data);
        else
            writeNibbleParallel(rs, data);
    }

    // Escreve um byte de comando/dado no LCD
    private static void writeByte(boolean rs, int data) {
        // write higher nibble first
        if (DEBUG)
            System.out.println("RS: " + rs + "\thigh nibble: " + Integer.toString(((0xf0 & data) >> 4)));
        writeNibble (rs, (0xf0 & data) >> 4);
        // write lower nibble second
        if (DEBUG)
            System.out.println("RS: " + rs + "\tlow nibble: " + Integer.toString(0x0f & data));
        writeNibble(rs, 0x0f & data);
    }

    // Escreve um comando no LCD
    private static void writeCMD(int data) {
        writeByte(false, data);
    }

    // Escreve um dado no LCD
    private static void writeDATA(int data) {
        writeByte(true, data);
    }

    // Envia a sequência de iniciação para a comunicação a 4 bits
    public static void init() {}

    public static void main(String[] args) {
        System.out.println("Testes");
        DEBUG = true;

        System.out.println("Testes writeNibbleParallel:");
        System.out.println("Teste 1: write RS: 0 data: 1 Result: 0010 0001 (33)");
        writeNibbleParallel(false, 1);
        System.out.println("Teste 2: write RS: 0 data: 3 Result: 0010 0011 (35)");
        writeNibbleParallel(false, 3);
        System.out.println("Teste 3: write RS: 1 data: 15 Result: 0011 1111 (63)");
        writeNibbleParallel(true, 15);
        System.out.println("Teste 4: write RS: 1 data: 0 Result: 0011 0000 (48)");
        writeNibbleParallel(true, 0);

        System.out.println("Testes writeByte:");
        System.out.println("Teste 5: write RS: 0 data: 10 (0000 1010) Result 1: 0010 0000 (32) + 2: 0010 1010 (42)");
        writeByte(false, 10);
        System.out.println("Teste 6: write RS: 1 data: 00 (0000 0000) Result 1: 0011 0000 (48) + 2: 0011 0000 (48)");
        writeByte(true, 0);
    }
}
