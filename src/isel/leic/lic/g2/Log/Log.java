/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

package isel.leic.lic.g2.Log;

import isel.leic.lic.g2.FileAccess;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final String LOG_FILE = "log.txt";
    private static final String FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static FileAccess log;

    // inicializa a classe com uma instancia de FileAccess
    public static void init () {
        log = new FileAccess(LOG_FILE);
    }

    // escreve mensagem para o ficheiro de log, respeitanto o formato
    public static void logger(String msg) {
        String time =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMAT));
        log.writeln(time + " -> " + msg);
    }

    public static void main(String[] args) {
        init();

        System.out.println("Teste Log");
        System.out.println("1\t Logger");
        logger("Hello");

        System.out.println("2\t 1:Luis Bandarra");
        logger("1:Luis Bandarra");

        System.out.println("3\t X: José Manuel");
        logger("X: José Manuel");

        System.out.println("4:\t Libertem Matorras");
        logger("Libertem Matorras");
    }
}
