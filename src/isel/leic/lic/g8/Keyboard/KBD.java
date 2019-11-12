/*-
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 * Copyright (c) 2019 Gonçalo Lourenço
 */

/*
 * Ler teclas. Métodos retornam carateres de '0'..'9', '#', '*' ou NONE.
 *
 * O signal para converter em carater vem da camada HAL e, com base nos bits
 * do sinal pretendido obtemos o valor lido do teclado
 *
 * Para ler do teclado, é enviado para a instancia de HAL a mascara para obter
 * do sinal os indicador pretendido:
 *  Dval: indicação de valor em envio
 *  D0..3: valor da tecla premida
 *
 * Para terminar o envio do sinal do teclado, é necessária comunicar o ACK, para
 * isso também é enviada uma mascara para o bit necessário.
 *
 * O sinal recebido na camada HAL tem a dimensão de 1 byte (8 bits), pelo que
 * definimos para as várias máscaras os seguintes bits:
 *   - Dval (RX_VAL): 8bit - 0x80
 *   - Data (RX_DATA): primeiros 4 bits - 0xf
 *   - ACK (TX_ACK): 6bit - 0x20
 *
 *   RX_VAL     RX_DATA
 *     1 0 0 0  1 1 1 1
 *
 *     0 0 0 0  0 0 0 1
 *        TX_ACK
 *
 * Apos receção do valor do teclado, é necessário mapear o valor para o carater
 * do enviado:
 *   _________________
 *  |     |     |     |
 *  |  1  |  2  |  3  |
 *  |     |     |     |
 *  |-----------------|
 *  |     |     |     |
 *  |  4  |  5  |  6  |
 *  |     |     |     |
 *  | ----------------|
 *  |     |     |     |
 *  |  7  |  8  |  9  |
 *  |     |     |     |
 *  |-----------------|
 *  |     |     |     |
 *  |  *  |  0  |  #  |
 *  |_____|_____|_____|
 *
 */
package isel.leic.lic.g8.Keyboard;

import isel.leic.lic.g8.HAL;
import isel.leic.utils.Time;

public class KBD {
    private static int RX_VAL = 0x80;
    private static int RX_DATA = 0xf;
    private static int TX_ACK = 0x1;

    public static final char NONE = 0;
    public static final char[][] MAP_CHAR = {
            {'1', '4', '7', '*'},
            {'2', '5', '8', '0'},
            {'3', '6', '9', '#'}
    };

    // inicia a classe
    public static void init() {
        HAL.init();
    }

    // retorna de imediato a tecla premida ou NONE se não tecla premida.
    public static char getKey() {
        char key = NONE;

        if (HAL.isBit(RX_VAL)) {
            key = mapKeyToChar(HAL.readBits(RX_DATA));
            HAL.setBits(TX_ACK);
            while (HAL.isBit(RX_VAL)); // espera mudança RX_VAL
            HAL.clrBits(TX_ACK);
        }

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

    public static void main(String[] args) {
        System.out.println("Testes KDB");

        System.out.println("Teste MAPEAR CARATER");
        System.out.println("Teste 1 - mapKeyToChar (1): " + mapKeyToChar(0x0)); // 0000
        System.out.println("Teste 2 - mapKeyToChar (4): " + mapKeyToChar(0x1)); // 0001
        System.out.println("Teste 3 - mapKeyToChar (7): " + mapKeyToChar(0x2)); // 0010
        System.out.println("Teste 4 - mapKeyToChar (*): " + mapKeyToChar(0x3)); // 0011
        System.out.println("Teste 5 - mapKeyToChar (2): " + mapKeyToChar(0x4)); // 0100
        System.out.println("Teste 6 - mapKeyToChar (5): " + mapKeyToChar(0x5)); // 0101
        System.out.println("Teste 7 - mapKeyToChar (8): " + mapKeyToChar(0x6)); // 0110
        System.out.println("Teste 8 - mapKeyToChar (0): " + mapKeyToChar(0x7)); // 0111
        System.out.println("Teste 9 - mapKeyToChar (3): " + mapKeyToChar(0x8)); // 1000
        System.out.println("Teste 10 - mapKeyToChar (6): " + mapKeyToChar(0x9)); // 1001
        System.out.println("Teste 11 - mapKeyToChar (9): " + mapKeyToChar(0xa)); // 1010
        System.out.println("Teste 12 - mapKeyToChar (#): " + mapKeyToChar(0x0b)); // 1011

        System.out.println("Teste waitKey");
        System.out.println("Teste 13 - waitKey (NONE) " + waitKey(1000));
    }
}
