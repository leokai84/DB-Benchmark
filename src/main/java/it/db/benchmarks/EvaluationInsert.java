/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.db.benchmarks;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leonardo Venezia
 */
public class EvaluationInsert {

    public static void insertAttempts(QueryManager qman, int attempts, int attToCommit) {

//        Execution time for each statement
        long statementTime[] = new long[attempts];

        long min = (long) Math.pow(2, 63);
        long max = (long) -Math.pow(2, 63);
        double avg = 0;

//        Table columns
        String[] cols = {"id", "name", "surname", "role", "company"};
        
        qman.connect();
        Connection conn = qman.getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(Evaluation.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < attempts; i++) {
            
//            Creating query statement with values such as: 1, name1, surname, ...
            String query = "INSERT INTO candidates (";
            for (String c : cols) {
                query = query + c + ",";
            }
            query = query.substring(0, query.length() - 1) + ") values (";
            for (String c : cols) {
                if (c.equals("id")) {
                    query = query + (i + 1) + ",";
                } else {
                    query = query + "\'" + c + (i + 1) + "\',";
                }
            }
            query = query.substring(0, query.length() - 1) + ");";

            long start = System.currentTimeMillis();
            qman.performUpdate(query);
            long end = System.currentTimeMillis();

            statementTime[i] = end - start;
            if (statementTime[i] > max) {
                max = statementTime[i];
            }
            if (statementTime[i] < min) {
                min = statementTime[i];
            }

//            this commits queries every attToCommit times
            if ((i + 1) % attToCommit == 0) {
                try {
                    conn.commit();
                } catch (SQLException ex) {
                    Logger.getLogger(Evaluation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        try {
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Evaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        qman.disconnect();

        for (int i = 0; i < attempts; i++) {
            avg = avg + statementTime[i];
        }
        avg = avg / attempts;

        System.out.println("FOR INSERT STATEMENTS:");
        
        System.out.println("Min time: " + min + " milliseconds");
        System.out.println("Max time: " + max + " milliseconds");
        System.out.println("Average time: " + avg + " milliseconds");
    }

}
