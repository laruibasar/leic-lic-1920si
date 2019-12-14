/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * Classe para obter o acesso a ficheiros do sistema e facilitar
 * a execucao de leituras e escritas
 */
package isel.leic.lic.g2;

import java.io.*;
import java.util.Scanner;

public class FileAccess {
    private final boolean APPEND = true;
    private File file;

    // construtor de instancia
    public FileAccess(String file) {
        this.file = new File(file);
    }

    // devolve um handler para o ficheiro que se pretende ler
    public Scanner read() {
        try {
            InputStream in = new FileInputStream(file);
            return new Scanner(in);
           } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // permite a escrita de texto para o ficheiro guardado na instancia
    public void writeln(String str) {
        try {
            Writer out = new FileWriter(file, APPEND);
            out.write(str + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // coloca o ficheiro vazio para escrita completa
    public void clear() {
        try {
            Writer out = new FileWriter(file);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
