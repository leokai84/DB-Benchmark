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
public class Evaluation {

    public static void main(String[] args) {

//      Args needed:
//      Connection parameters:
        String jdbcDriver = args[0];
        String url = args[1];
        String dbName = args[2];
        String username = args[3];
        String password = args[4];
        
//        Number of statements to perform for operations:
        int attemps = Integer.parseInt(args[5]);
        int attempsToCommit = Integer.parseInt(args[6]);

        QueryManager qman = new QueryManager(jdbcDriver, url,
                dbName, username, password);
        EvaluationInsert.insertAttempts(qman, attemps, attempsToCommit);
        EvaluationSelect.selectAttempts(qman, attemps);

    }

}
