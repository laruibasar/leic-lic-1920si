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
 *   - Dval (VAL):  0x10
 *   - Data (DATA): 0x0f
 *   - ACK (ACK):   0x80
 *
 *       7    6   5    4      3    2    1   0
 * In    0    0   0  (DVAL) (D3) (D2) (D1) (D0)
 * Out (ACK)  0   0    0      0    0    0   0
 *
 *
 * Apos receção do valor do teclado, é necessário mapear o valor para o carater
 * do enviado:
 *   ____________________
 *  |      |      |      |
 *  |  1   |  2   |  3   | r3
 *  | 0000 | 0100 | 1000 |
 *  |--------------------|
 *  |      |      |      |
 *  |  4   |  5   |  6   | r2
 *  | 0001 | 0101 | 1001 |
 *  |--------------------|
 *  |      |      |      |
 *  |  7   |  8   |  9   | r1
 *  | 0010 | 0110 | 1010 |
 *  |--------------------|
 *  |      |      |      |
 *  |  *   |  0   |  #   | r0
 *  | 0011 | 0111 | 1011 |
 *  |______|______|______|
 *     c0     c1     c2
 */
package isel.leic.lic.g2.Keyboard;

import isel.leic.lic.g2.HAL;
import isel.leic.lic.g2.Utils;
import isel.leic.utils.Time;

public class KBD {
    private static int VAL = 0x10;
    private static int DATA = 0xf;
    private static int ACK = 0x80;
    private static boolean simul;
    private static char[] map;

    public static final char NONE = 0;
    // mapeamento mais simple
    public static final char[] MAP_CHAR = {'1', '4', '7', '*', '2', '5', '8', '0', '3', '6', '9', '#'};
    // ajuste ao simulador
    public static final char[] MAP_CHAR_SIMUL = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '0', '#'};

    // inicia a classe
    public static void init() {
        HAL.init();
        String p = Utils.getProperties("simulation");

        // mecanismo para ler do ficheiro de propriedades USB_PORT
        simul = p.equalsIgnoreCase("true") ? true : false;
        map = (simul) ? MAP_CHAR_SIMUL : MAP_CHAR;
    }

    // retorna de imediato a tecla premida ou NONE se não tecla premida.
    public static char getKey() {
        char key = NONE;

        if (HAL.isBit(VAL)) {
            key = map[HAL.readBits(DATA)];
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

        while ((Time.getTimeInMillis() < time) && (key == NONE))
            key = getKey();

        return key;
    }
}
