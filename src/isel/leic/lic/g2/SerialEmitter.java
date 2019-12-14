/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * A classe SerialEmitter envia para os varios modulos de hardware modules
 * os dados que recebe do controlo de software.
 *
 * O mapeamento para o Output Port da placa vai ser mapeado de acordo com o seguinte
 * esquema de acordo com os bits disponiveis:
 *       7   6   5  4       3   2    1   0
 * In  : 0  BSY  0  0     0   0    0   0
 * Out : 0   0   0  0   DOOR LCD SCLK SDX
 *
 *
 * Este arranjo permite-nos, apenas com o hardware atual, ter ainda disponivel mais
 * 4 enderecamentos disponiveis para modulos de hardware.
 *
 * Do Input Port da placa verificamos o sinal de Busy do hardware para fazer uma
 * espera do envio dos dados em linha
 */

// Envia tramas para os diferentes modulos Serial Receiver
package isel.leic.lic.g2;

public class SerialEmitter {
    public enum Destination {LCD, DOOR_MECHANISM};

    private final static int MASK_DATA = 0x01;
    private final static int MASK_SCLK = 0x02;
    private final static int MASK_LCD  = 0x04;
    private final static int MASK_DOOR = 0x08;
    private final static int MASK_BUSY = 0x40;
    private final static int TX_FRAME_SIZE = 5;

    private static int write_addr;
    private static int parity;

    // inicia a classe
    public static void init() {
        HAL.init();
        write_addr = 0;
        parity = 0;
        HAL.setBits(MASK_LCD);
        HAL.setBits(MASK_DOOR);
    }

    // envia uma trama para o SerialReceiver identificando o destino em addr
    // e os bits de dados em 'data'
    // o método faz o envio, sem validar se está busy, para isso esta disponivel
    // o método isBusy que deve ser utilizado pelas classes antes de fazer a chamada
    public static void send(Destination addr, int data) {
        switch (addr) {
            case LCD:
                write_addr = MASK_LCD;
                break;
            case DOOR_MECHANISM:
                write_addr = MASK_DOOR;
                break;
            default:
                return;
        }
        HAL.clrBits(write_addr);

        // simples verificacao de controlo da transmissao
        // dependente do valor da transmissao de par ou impar
        parity = 0;
        for (int i = 0; i < TX_FRAME_SIZE; i++) {
            HAL.clrBits(MASK_SCLK);
            int tx_data = MASK_DATA & data;
            parity += (tx_data);
            HAL.writeBits(MASK_DATA, tx_data);
            data >>= 1;
            HAL.setBits(MASK_SCLK);
        }

        // enviar a paridade do sinal
        HAL.clrBits(MASK_SCLK);
        parity %= 2;
        HAL.writeBits(MASK_DATA, parity);
        HAL.setBits(MASK_SCLK);

        HAL.clrBits(MASK_SCLK);
        HAL.setBits(MASK_SCLK);

        HAL.clrBits(MASK_DATA);
        HAL.clrBits(MASK_SCLK);
        HAL.setBits(write_addr);
    }

    // retorna true se o canal série estiver ocupado
    // se ocupado, valor 1
    public static boolean isBusy() {
        return HAL.isBit(MASK_BUSY);
    }

    public static void main(String[] args) {
        init();

        send(Destination.LCD, 0xf);  // 0000 1111
        send(Destination.LCD, 0x01); // 0000 0001
        send(Destination.LCD, 0x02); // 0000 0010
        send(Destination.LCD, 0x04); // 0000 0100
        send(Destination.LCD, 0x08); // 0000 1000
        send(Destination.LCD, 0xf);  // 0000 1111
        send(Destination.LCD, 0x11);
        send(Destination.LCD, 0x12);
        send(Destination.LCD, 0x14);
        send(Destination.LCD, 0x18);
        send(Destination.LCD, 0x1f);
        send(Destination.DOOR_MECHANISM, 0x01);
        send(Destination.DOOR_MECHANISM, 0x02);
        send(Destination.DOOR_MECHANISM, 0x04);
        send(Destination.DOOR_MECHANISM, 0x08);
        send(Destination.DOOR_MECHANISM, 0xf);
        send(Destination.DOOR_MECHANISM, 0x11);
        send(Destination.DOOR_MECHANISM, 0x12);
        send(Destination.DOOR_MECHANISM, 0x14);
        send(Destination.DOOR_MECHANISM, 0x18);
        send(Destination.DOOR_MECHANISM, 0x1f);
    }
}
