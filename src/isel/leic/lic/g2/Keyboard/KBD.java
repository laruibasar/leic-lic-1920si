/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
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
 *   - Dval (VAL): 8bit - 0x80
 *   - Data (DATA): primeiros 4 bits - 0xf
 *   - ACK (ACK): 6bit - 0x20
 *
 *   VAL     DATA
 *     1 0 0 0  1 1 1 1
 *
 *     0 0 0 0  0 0 0 1
 *        ACK
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
package isel.leic.lic.g2.Keyboard;

import isel.leic.lic.g2.HAL;
import isel.leic.utils.Time;

public class KBD {
    private static int VAL = 0x80;
    private static int DATA = 0xf;
    private static int ACK = 0x1;

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

        if (HAL.isBit(VAL)) {
            key = mapKeyToChar(HAL.readBits(DATA));
            HAL.setBits(ACK);
            while (HAL.isBit(VAL));

            HAL.clrBits(ACK);
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
}
