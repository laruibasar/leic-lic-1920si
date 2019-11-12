/*-
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

// Envia tramas para os diferentes modulos Serial Receiver
package isel.leic.lic.g8;

import isel.leic.lic.g8.HAL;

public class SerialEmitter {
    private final static int TX_SDX = 0x4;
    public static enum Destination {LCD, DOOR_MECHANISM};

    // inicia a classe
    public static void init() { }

    // envia uma trama para o SerialReceiver identificando o destino em addr e os bits de dados em 'data'
    /*
     * Use HAL to write bits to hardware layer. Because is serial communication, we need to time the sending
     * of information, using a mask to send the signals SS, SCLK and SDX.
     */
    public static void send(Destination addr, int data) {
        // build code to send

    }

    // retorna true se o canal série estiver ocupado
    public static boolean isBusy() {
        return false;
    }
}
