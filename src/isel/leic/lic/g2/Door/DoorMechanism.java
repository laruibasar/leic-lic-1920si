/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */
package isel.leic.lic.g2.Door;

import isel.leic.lic.g2.SerialEmitter;
import isel.leic.utils.Time;

public class DoorMechanism {    // Controla o estado do mecanismo de abertura da porta
    private static final int MASK_DATA_SIZE = 0xf;
    private static final int DOOR_OPEN = 1;
    private static final int DOOR_CLOSE = 0;

    // Inicia a classe, estabelecendo os valores iniciais
    public static void init() {}

    // Envia comando para abrir a porta, com o parametro de velocidade
    // O sinal de dados para o SerialEmitter vai ser:
    // 0 0 0 (V3) (V2) (V1) (V0) (OC) de acordo com a especificao projeto
    public static void open(int velocity) {
        int send = ((MASK_DATA_SIZE & velocity) << 1) | DOOR_OPEN;

        while (!finished()) {
            SerialEmitter.send(SerialEmitter.Destination.DOOR_MECHANISM, send);
        }
    }

    // Envia comando para fechar a porta, com o parameto de velocidade
    // O sinal de dados para o SerialEmitter vai ser:
    // 0 0 0 (D3) (D2) (D1) (D0) (OC) de acordo com a especificao projeto
    public static void close(int velocity) {
        int send = ((MASK_DATA_SIZE & velocity) << 1) | DOOR_CLOSE;

        while (!finished()) {
            SerialEmitter.send(SerialEmitter.Destination.DOOR_MECHANISM, send);
        }
    }

    // Verifica se o comando anterior está concluido
    public static boolean finished() {
        return SerialEmitter.isBusy();
    }

    public static void main(String[] args) {
        init();
        System.out.println("Opening...");
        open(1);
        Time.sleep(1000);
        System.out.println("Closing...");
        close(1);
        Time.sleep(1000);
        System.out.println("Opening...");
        open(5);
        Time.sleep(1000);
        System.out.println("Closing...");
        close(2);
    }
}
