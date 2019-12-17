/*-
 * SPDX-License-Identifier: BSD-3-Clause
 * Copyright (c) 2019 Luis Bandarra <luis.bandarra@homestudio.pt>
 */

/*
 * Classe que permite o interface entre o hardware que ativa
 * o modo de manutencao e o software que permite a mudança de estado
 * da aplicacao.
 *
 * O bit a ser lido deve ser o bit maior peso do Input
 *          7 6 5 4  3 2 1 0
 * Input:   M 0 0 0  0 0 0 0
 */
package isel.leic.lic.g2;

public class M {
    private static final int MASK_MAINTENANCE = 0x80;

    // inicializacao da classe
    public static void init() { }

    // verifica se o bit da manutencao esta ativo
    public static boolean isMaintenanceMode() {
        return HAL.isBit(MASK_MAINTENANCE);
    }
}
