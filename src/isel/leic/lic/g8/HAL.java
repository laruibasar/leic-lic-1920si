/*-
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * setBits write bits to OUT PORT, don't clear
 */

// Virtualiza o acesso ao sistema UsbPort
package isel.leic.lic.g8;

import isel.leic.UsbPort;
import isel.leic.utils.Time;

public class HAL {
    private static int last_write;

    // Inicia a classe
    public static void init() {
        last_write = 0x0;
    }

    // Retorna true se o bit tiver o valor lógico 1
    // we don't update current value (we can check for okay to read signal)
    public static boolean isBit(int mask) {
        return (UsbPort.in() & mask) != 0;
    }

    // Retorna os valores dos bits representados por mask presentes no UsbPort
    // we update current value for register in memory
    public static int readBits(int mask) {
        return UsbPort.in() & mask;
    }

    // Escreve nos bits representados por mask o valor de value
    public static void writeBits(int mask, int value) {
        last_write = value & mask;
        UsbPort.out(last_write);
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

    public static void main(String[] args) {
        System.out.println("Testes HAL");

        System.out.print("Init...");
        init();
        System.out.print(last_write + " ");
        clrBits(0xff);
        System.out.println("clrBits 0xff");
        Time.sleep(4000);

        System.out.println("Testes setBits");
        System.out.println("Teste 1: setBits (0000 1111)");
        setBits(0xf);

        Time.sleep(1000);
        clrBits(0xff);
        System.out.println("Teste 2: setBits (1111 0000)");
        setBits(0xf0);

        Time.sleep(1000);
        clrBits(0xff);
        System.out.println("Teste 3: setBits (1111 1111)");
        setBits(0xff);

        Time.sleep(1000);
        clrBits(0xff);
        System.out.println("Teste 4: setBits (0111 1110)");
        setBits(0x7e);

        Time.sleep(1000);
        clrBits(0xff);
        System.out.println("Teste 5: setBits (0001 1000)");
        setBits(0x18);

        Time.sleep(1000);
        clrBits(0xff);
        System.out.println("Teste 6: setBits (0110 0110)");
        setBits(0x66);

        Time.sleep(1000);
        clrBits(0xff);
        System.out.println("Teste 7: setBits (0100 0000)");
        setBits(0x40);

        Time.sleep(1000);
        clrBits(0xff);
        System.out.println("Fim teste: setBits");

        Time.sleep(3000);
        System.out.println("Testes clrBits");
        System.out.print("Teste 8: setBits (1110 0111)");
        setBits(0xe7);
        Time.sleep(1000);
        clrBits(0xe7);
        System.out.print("... clear (0000 0000)");

        System.out.println("Testes writeBits");
        System.out.println("Teste 9: writeBits mask: 0xf int: 0xff");
        writeBits(0xf, 0xff);
        Time.sleep(3000);
        clrBits(0xff);

        System.out.println("Testes writeBits");
        System.out.println("Teste 10: writeBits mask: 0x1 int: 0xff ");
        writeBits(0x1, 0xff);
        Time.sleep(1000);
        clrBits(0xff);

        System.out.println("Teste readBits");
        System.out.println("Teste 11: readBits ");
        while (true) {
            int tmp = readBits(0x0f);
            System.out.println("readed: " + tmp);
            System.out.println("input_value: " + last_write);
        }
    }
}
