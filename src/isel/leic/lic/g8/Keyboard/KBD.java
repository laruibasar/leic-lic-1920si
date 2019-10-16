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
    private static HAL hal;

    private static int RX_VAL = 0x80;
    private static int RX_DATA = 0xf;
    private static int TX_ACK = 0x20;

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
            while (HAL.isBit(RX_VAL)) {
                HAL.setBits(TX_ACK);
            }
            HAL.crlBits(TX_ACK);
        }

        return key;
    }

    // retorna quando a tecla for premida ou NONE após
    // decorrido 'timeout' milisegundos
    public static char waitKey(long timeout) {
        char key = NONE;
        long now = Time.getTimeInMillis();

        while ((now < (now + timeout)) || (key == NONE)) {
            key = getKey();
        }

        return key;
    }

    private static char mapKeyToChar(int k) {
        return MAP_CHAR[k>>2][k & 0x2];
    }
}
