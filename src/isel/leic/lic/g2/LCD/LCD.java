/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * A classe LCD permite enviar os comandos e carateres para escrever
 * no LCD modelo HD44780U, 16x2 com dimensão de 5x8 dots/caracter
 *
 * O LCD deve ser inicializado e durante a inicialização devemos definir
 * o valor de:
 *      DL:0 interface 4 bits; DL:1 interface 8 bits
 *          use DL:0
 *      N:0 display de 1 linha; N:1 display de 2 linhas
 *          use N:1
 *      F: 0 caracter com 5x8 pontos; F:1 caracter com 5x10 pontos
 *          use F:0
 *
 *
 * Para utilizar o LCD, para apresentar carateres temos que:
 *
 * A coordenada de memoria DDRM associada à posição display (AC)
 *      00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F
 *      40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F
 * instrução: 1 (AC) (AC) (AC) (AC) (AC) (AC) (AC)
 *
 * No display podemos controlar
 *      D:0 entire display off; D:1 display in
 *      C:0 cursor off; C:1 cursor on
 *      B:0 cursor blink off; B:1 cursor blink on
 * instrução: 0 0 0 0 0 1 D C B
 *
 */
package isel.leic.lic.g2.LCD;

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
        data = (data & MASK_LOW_DATA) << 1;
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

        // Function Set, interface a 4 bits
        writeCMD(0x28); // define N:1, F:0
        writeCMD(DISPLAY_OFF);  // display off
        writeCMD(DISPLAY_CLEAR);    // clear
        writeCMD(0x6);  // define I/D:1, S:0
        System.out.println("LCD ready");

        writeCMD(DISPLAY_ON); // display on
    }

    // Escreve um caráter na posição corrente
    public static void write(char c) {
        writeDATA(c);
    }

    // Escreve uma string na posição corrente
    public static void write(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            write(txt.charAt(i));
        }
    }

    // Envia comando para posicionar cursor (‘lin’:0..LINES-1 , ‘col’:0..COLS-1)
    public static void cursor(int lin, int col) {
        writeCMD(0x80 | (lin << 6) | col);
    }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0)
    public static void clear() {
        writeCMD(DISPLAY_CLEAR);
    }

    public static void main(String[] args) {
        SERIAL_INTERFACE = true;

        HAL.init();
        SerialEmitter.init();
        init();

        write('A');
        write('1');
        clear();
        write("Help me");
        clear();
        write("Working");
        clear();

        writeCMD(DISPLAY_OFF);
    }
}
