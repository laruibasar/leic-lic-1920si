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
 * bit: 8   7   6   5   4   3   2    1
 *      0   0   0   0 DOOR LCD SCLK SDX
 * Este arranjo permite-nos, apenas com o hardware atual, ter ainda disponivel mais
 * 4 enderecamentos disponiveis para modulos de hardware.
 *
 * Do Input Port da placa verificamos o sinal de Busy do hardware para fazer uma
 * espera do envio dos dados em linha, verificando o bit 1
 */

// Envia tramas para os diferentes modulos Serial Receiver
package isel.leic.lic.g2;

public class SerialEmitter {
    public static enum Destination {LCD, DOOR_MECHANISM};

    private final static int MASK_DATA = 0x01;
    private final static int MASK_SCLK = 0x02;
    private final static int MASK_LCD  = 0x04;
    private final static int MASK_DOOR = 0x08;
    private final static int MASK_BUSY = 0x01;
    private final static int TX_FRAME_SIZE = 5;

    private static int write_addr;

    // inicia a classe
    public static void init() {
        write_addr = 0;
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

        for (int i = 0; i < TX_FRAME_SIZE; i++) {
            HAL.clrBits(MASK_SCLK);
            HAL.writeBits(MASK_DATA, 0x1 & data);

            data >>= 1;
            HAL.setBits(MASK_SCLK);
        }

        HAL.clrBits(MASK_DATA);
        HAL.clrBits(MASK_SCLK);
        HAL.setBits(write_addr);
    }

    // retorna true se o canal série estiver ocupado
    public static boolean isBusy() {
        return HAL.isBit(MASK_BUSY);
    }
}
