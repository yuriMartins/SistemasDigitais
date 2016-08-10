/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package montador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 *
 * @author jmalmeida
 */
public class Montador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {        
        
        
        //leitor
        BufferedReader reader = new BufferedReader(new FileReader(new File("entrada.asm")));
        String line;
        while ((line = reader.readLine()) != null){//ler todas linhas
            if (line.contains(".module")){
                
            }
        }
        
        
        //gravar no txt
        File file = new File("saida.txt");  
        
        if (file.exists()){      
            new File("saida.txt").createNewFile();
            file = new File("saida.txt");
        }        
        PrintWriter gravarArq = new PrintWriter(new FileWriter(file));    
        
        //imprimir resultados
        for(String arg: args){
            gravarArq.printf(arg);   
        }
        gravarArq.close();
    }   
    
}
