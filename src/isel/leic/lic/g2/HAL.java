/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

// Virtualiza o acesso ao sistema UsbPort
package isel.leic.lic.g2;

import isel.leic.UsbPort;

public class HAL {
    private static int last_write;

    // Inicia a classe
    // Apaga e inicia todos os bits a 1, deixa em active low as saidas
    public static void init() {
        clrBits(0xff);
    }

    // Retorna true se o bit tiver o valor lógico 1
    public static boolean isBit(int mask) {
        return readBits(mask) != 0;
    }

    // Retorna os valores dos bits representados por mask presentes no UsbPort
    public static int readBits(int mask) {
        return UsbPort.in() & mask;
    }

    // Escreve nos bits representados por mask o valor de value
    public static void writeBits(int mask, int value) {
        clrBits(mask);
        setBits(value & mask);
    }

    // Coloca os bits representados por mask no valor lógico '1'
    public static void setBits(int mask) {
        last_write |= mask;
        UsbPort.out(last_write);
    }

    // Coloca os bits representados por mask no valor lógico '0'
    public static void clrBits(int mask) {
        last_write &= (~mask);
        UsbPort.out(last_write);
    }
}
