/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */
package isel.leic.lic.g2.LCD;

import isel.leic.lic.g2.HAL;
import isel.leic.lic.g2.SerialEmitter;
import isel.leic.utils.Time;

public class LCD {
    public static final int LINES = 2, COLS = 16; // Dimensão do display

    // Define se a interface com o LCD é série ou paralela
    private static final boolean SERIAL_INTERFACE = false;

    // Define comandos para o LCD
    private static final int LCD_CLEAR = 0x1;

    // Mascara para envio do nibble em parallel
    private static int MASK_ENABLE = 0x20;      // 0010 0000
    private static int MASK_PARALLEL_RS = 0x10; // 000R 0000
    private static int MASK_LOW_DATA = 0x0f;    // 0000 1111
    private static int MASK_HIGH_DATA = 0xf0;   // 1111 0000

    // Escreve um nibble de comando/dados no LCD em paralelo
    // O sinal para a HAL vai ser: 0 0 E (RS) NIBBLE
    // seguindo o diagrama temporal da datasheet do equipamento
    private static void writeNibbleParallel(boolean rs, int data) {
        if (rs == true)
            HAL.setBits(MASK_PARALLEL_RS);
        else
            HAL.clrBits(MASK_PARALLEL_RS);

        HAL.setBits(MASK_ENABLE);
        HAL.writeBits(MASK_LOW_DATA, data);
        HAL.clrBits(MASK_ENABLE);
        HAL.clrBits(MASK_PARALLEL_RS);
    }

    // Escreve um nibble de comando/dados no LCD em série
    // O sinal de dados para o SerialEmitter vai ser:
    // 0 0 0 (D3) (D2) (D1) (D0) (RS) de acordo com a especificao projeto
    private static void writeNibbleSerial(boolean rs, int data) {
        System.out.println("Dados a enviar: " + data);
        data = (data & MASK_LOW_DATA) << 1;
        System.out.println("Shift: " + data);
        if (rs == true)
            data |= 0x1;
        SerialEmitter.send(SerialEmitter.Destination.LCD, data);
    }

    // Escreve um nibble de comando/dados no LCD
    private static void writeNibble(boolean rs, int data) {
        if (SERIAL_INTERFACE)
            writeNibbleSerial(rs, data);
        else
            writeNibbleParallel(rs, data);
    }

    // Escreve um byte de comando/dado no LCD
    private static void writeByte(boolean rs, int data) {
        writeNibble (rs, (MASK_HIGH_DATA & data) >> 4);
        writeNibble(rs, MASK_LOW_DATA & data);
        Time.sleep(5); // espera por execucao comando lcd
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
    public static void init() {
        System.out.print("Initialization LCD...\n");
        Time.sleep(15); // 1 standby
        writeNibble(false, 0x3);  // FS
        Time.sleep(5);  // 2 standby
        writeNibble(false, 0x3);  // FS
        Time.sleep(1);  // 3 timeout
        writeNibble(false, 0x3);  // FS
        writeNibble(false, 0x2);  // FS

        writeCMD(0x28);  // BF can be checked
        // display on/off
        writeCMD(0x8);  // display off
        // entry mode set
        writeCMD(LCD_CLEAR);  // clear
        writeCMD(0x6);  // I/D:1 inc S:0 no shift
        writeCMD(0xf); // display on

        System.out.println("LCD ready");
    }

    // Escreve um caráter na posição corrente
    public static void write(char c) {
        writeDATA(c);
    }

    // Escreve uma string na posição corrente
    public static void write(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            writeDATA(txt.charAt(i));
        }
    }

    // Envia comando para posicionar cursor (‘lin’:0..LINES-1 , ‘col’:0..COLS-1)
    public static void cursor(int lin, int col) { }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0)
    public static void clear() {
        writeCMD(LCD_CLEAR);
    }

    public static void main(String[] args) {
        HAL.init();
        init();

        write("Help me");
        Time.sleep(5000);
        clear();
        write("Working");
        clear();
        Time.sleep(5000);
    }
}
