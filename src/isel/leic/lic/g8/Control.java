/*-
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 * Copyright (c) 2019 Gonçalo Lourenço
 */

package isel.leic.lic.g8;

import isel.leic.lic.g8.Keyboard.KBD;

public class Control {

    private static boolean run;
    public static void main(String[] args) {
        run = true;
        char key;

        /* setup keyboard */
        KBD.init();

        while (run) {
            key = KBD.getKey();
            System.out.println("Tecla: " + key);
        }
    }
}
