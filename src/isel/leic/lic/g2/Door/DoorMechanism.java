/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * Classe para controlo do mecanismo da porta.
 * Podemos pedir para abrir ou fechar a porta, enviando a velocidade com que queremos
 * que seja a manobra da porta.
 *
 * O sinal de dados para o SerialEmitter vai ser:
 *          7 6 5   4    3    2    1   0
 * Output:  0 0 0 (D3) (D2) (D1) (D0) (OC)
 *
 * Acordo com a especificao projeto
 */
package isel.leic.lic.g2.Door;

import isel.leic.lic.g2.SerialEmitter;

// Controla o estado do mecanismo de abertura da porta
public class DoorMechanism {
    private static final int MASK_DATA_SIZE = 0xf;
    private static final int DOOR_OPEN = 1;
    private static final int DOOR_CLOSE = 0;

    // Inicia a classe, estabelecendo os valores iniciais
    public static void init() {
        SerialEmitter.init();
        close(1);
    }

    // Envia comando para abrir a porta, com o parametro de velocidade
    // O sinal de dados para o SerialEmitter vai ser:
    // 0 0 0 (V3) (V2) (V1) (V0) (OC) de acordo com a especificao projeto
    public static void open(int velocity) {
        send(((MASK_DATA_SIZE & velocity) << 1) | DOOR_OPEN);
    }

    // Envia comando para fechar a porta, com o parameto de velocidade
    public static void close(int velocity) {
        send( ((MASK_DATA_SIZE & velocity) << 1) | DOOR_CLOSE);
        while (!finished());
    }

    // Envia o comando para a porta
    private static void send(int cmd) {
        while (!finished());
        SerialEmitter.send(SerialEmitter.Destination.DOOR_MECHANISM, cmd);
    }

    // Verifica se o comando anterior está concluido
    // valor de isBusy tem de ser 0
    public static boolean finished() {
        return !SerialEmitter.isBusy();
    }

    public static void main(String[] args) {
        init();

        System.out.println("Opening...");
        open(1);
        System.out.println("Closing...");
        close(2);
        System.out.println("Opening...");
        open(5);
        System.out.println("Closing...");
        close(2);
    }
}
