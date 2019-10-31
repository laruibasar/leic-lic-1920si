/*-
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

// Virtualiza o acesso ao sistema UsbPort
package isel.leic.lic.g8;

import isel.leic.UsbPort;

public class HAL {
    private static int input_value;

    // Inicia a classe
    public static void init() {
        input_value = 0;
    }

    // Retorna true se o bit tiver o valor lógico 1
    // we don't update current value (we can check for okay to read signal)
    public static boolean isBit(int mask) {
        return (UsbPort.in() & mask) != 0;
    }

    // Retorna os valores dos bits representados por mask presentes no UsbPort
    // we update current value for register in memory
    public static int readBits(int mask) {
        input_value = UsbPort.in();
        return input_value & mask;
    }

    // Escreve nos bits representados por mask o valor de value
    public static void writeBits(int mask, int value) {
        UsbPort.out(value & mask);
    }

    // Coloca os bits representados por mask no valor lógico '1'
    public static void setBits(int mask) {
        input_value |= mask;
        UsbPort.out(input_value);
    }

    // Coloca os bits representados por mask no valor lógico '0'
    public static void clrBits(int mask) {
        input_value &= (~mask);
        UsbPort.out(input_value);
    }

    public static void main(String[] args) {
        System.out.println("Testes HAL");

        init();
        clrBits(0xff);
        System.out.print("Teste 1: setBits (0000 1111)");
        setBits(0xf);
        System.out.println(" | " + readBits(0xf));
        System.out.print("Teste 2: setBits (1111 0000)");
        setBits(0xf0);
        System.out.println(" | " + readBits(0xf));
    }
}
