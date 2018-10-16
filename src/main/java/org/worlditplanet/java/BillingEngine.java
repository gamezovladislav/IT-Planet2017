package org.worlditplanet.java;

import java.nio.file.Path;

/**
 * Интерфейс расчёта и высталения счетов абонентам оператора.
 *
 * @author Sergey Morgunov {@literal <smorgunov@at-consulting.ru>}
 */
public interface BillingEngine {

    /**
     * Выставление счетов абонентам.
     *
     * @param tariffsFile Файл с описанием тарифных планов.
     * @param subscribersFile Файл с данными об абонентах.
     * @param actionsFile Файл с действиями абонентов за отчётный период.
     * @param invoicesFile Файл со счетами абонентов.
     */
    void billing(Path tariffsFile, Path subscribersFile, Path actionsFile, Path invoicesFile);

}
