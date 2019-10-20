/*-
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 * Copyright (c) 2019 Gonçalo Lourenço
 */

// Virtualiza o acesso ao sistema UsbPort
package isel.leic.lic.g8;

import isel.leic.UsbPort;

public class HAL {
    private static UsbPort usbPort;

    // Inicia a classe
    public static void init() {
        usbPort = new UsbPort();
    }

    // Retorna true se o bit tiver o valor lógico 1
    public static boolean isBit(int mask) {
        return (UsbPort.in() & mask) != 0;
    }

    // Retorna os valores dos bits representados por mask presentes no UsbPort
    public static int readBits(int mask) {
        return UsbPort.in() & mask;
    }

    // Escreve nos bits representados por mask o valor de value
    public static void writeBits(int mask, int value) {
        UsbPort.out(value & mask);
    }

    // Coloca os bits representados por mask no valor lógico '1'
    public static void setBits(int mask) {
        UsbPort.out(UsbPort.in() | mask);
    }

    // Coloca os bits representados por mask no valor lógico '0'
    public static void crlBits(int mask) {
        UsbPort.out(UsbPort.in() & (~mask));
    }
}
