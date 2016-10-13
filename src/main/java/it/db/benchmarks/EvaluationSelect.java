/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.db.benchmarks;

/**
 *
 * @author Leonardo Venezia
 */
public class EvaluationSelect {

    public static void selectAttempts(QueryManager qman, int attempts) {

//        Execution time for each statement
        long statementTime[] = new long[attempts];

        long min = (long) Math.pow(2, 63);
        long max = (long) -Math.pow(2, 63);
        double avg = 0;

        qman.connect();

        for (int i = 0; i < attempts; i++) {
            String query = "SELECT * FROM candidates WHERE id=" + (i + 1) + ";";

            long start = System.currentTimeMillis();
            qman.performQuery(query);
            long end = System.currentTimeMillis();

            statementTime[i] = end - start;
            if (statementTime[i] > max) {
                max = statementTime[i];
            }
            if (statementTime[i] < min) {
                min = statementTime[i];
            }

        }
        qman.disconnect();

        for (int i = 0; i < attempts; i++) {
            avg = avg + statementTime[i];
        }
        avg = avg / attempts;

        System.out.println("FOR SELECT STATEMENTS:");
        
        System.out.println("Min time: " + min + " milliseconds");
        System.out.println("Max time: " + max + " milliseconds");
        System.out.println("Average time: " + avg + " milliseconds");
    }

}
