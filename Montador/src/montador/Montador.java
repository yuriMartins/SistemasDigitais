/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package montador;

import java.io.*;
import java.util.HashMap;

/**
 *
 * @author jmalmeida
 */
public class Montador {
    private static HashMap<String, Integer> labelMap, registers;
    private static HashMap<String, String> instrutions;
    private static int codeLineCnt = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {        
        labelMap = new HashMap<>();
        createInstrutions();
        createRegisters();
        File file = new File("saida.txt");          
        if (file.exists()){      
            new File("saida.txt").createNewFile();
            file = new File("saida.txt");
        }        
        PrintWriter gravarArq = new PrintWriter(new FileWriter(file)); 
        
        /*//leitor
        BufferedReader reader = new BufferedReader(new FileReader(new File("entrada.asm")));
        String line;
        while ((line = reader.readLine()) != null){//ler todas linhas
            if (line.contains(".module")){
                
            }
        }*/
        
        indexLabels();//faz uma varredura no arquivo indexando os labels
        
           
        
        //imprimir resultados
        for(String arg: args){
            gravarArq.printf(arg);   
        }
        gravarArq.close();
    }   
    
    private static void indexLabels() throws IOException {
        BufferedReader sourceBr = new BufferedReader(new FileReader(new File("entrada.asm")));
        String line, label;
        String aux[];
        
        boolean incrementLineCount = true;
        
        while ((line = sourceBr.readLine()) != null) {
            line = line.trim();//retira os espacos do fim e comeco, se houver
            aux = line.split("\\s+");

            for (String s : aux) {
                if (s.contains(";")||s.contains("#")) {//comentarios
                    break;
                } else if (s.length() > 0) {
                    if (s.contains(":")) {
                        incrementLineCount = false;
                        label = s.split(":")[0];
                        labelMap.put(label, codeLineCnt);
                        System.out.println("Label encontrada:");
                        System.out.println(label + " " + codeLineCnt + "\n");
                    } else {
                        incrementLineCount = true;
                        if (s.equals(".dseg")) { //nossos assemblys de testes nao tem isso, mas ta ai so por precauçao
                            System.out.println("Segmento de dados encontrado");
                            codeLineCnt = 0;
                            break;
                        } else if (s.equals(".pseg") || s.equals(".module") || s.equals(".data")) {// diretivas
                            break;
                        }

                        if (incrementLineCount) {
                            codeLineCnt++;
                            break;
                        }
                    }
                }
            }
            incrementLineCount = true;
        }
        sourceBr.close();
    }
    
    private static void createRegisters(){
        registers  = new HashMap<>();
        registers.put("$zero", 0);
        registers.put("$at", 1);
        registers.put("$v0", 2);
        registers.put("$v1", 3);
        registers.put("$a0", 4);
        registers.put("$a1", 5);
        registers.put("$a2", 6);
        registers.put("$a3", 7);
        registers.put("$t0", 8);
        registers.put("$t1", 9);
        registers.put("$t2", 10);
        registers.put("$t3", 11);
        registers.put("$t4", 12);
        registers.put("$t5", 13);
        registers.put("$t6", 14);
        registers.put("$t7", 15);
        registers.put("$s0", 16);
        registers.put("$s1", 17);
        registers.put("$s2", 18);
        registers.put("$s3", 19);
        registers.put("$s4", 20);
        registers.put("$s5", 21);
        registers.put("$s6", 22);
        registers.put("$s7", 23);
        registers.put("$t8", 24);
        registers.put("$t9", 25);
        registers.put("$k0", 26);
        registers.put("$k1", 27);
        registers.put("$gp", 28);
        registers.put("$sp", 29);
        registers.put("$fp", 30);
        registers.put("$ra", 31);
    }

    private static void createInstrutions() {
        //o x servira pra dar um split na string e depois concatenar com 
        // os ids dos registadores ou valor imediato
        instrutions = new HashMap<>();
        //aritimeticas
        instrutions.put("add", "000000x00000100000");
        instrutions.put("addi", "001000");
        instrutions.put("addiu", "001001");
        instrutions.put("addu", "000000x00000100001");
        instrutions.put("clz", "011100x00000100000");
        instrutions.put("clo", "011100x00000100001");
        instrutions.put("lui", "00111100000x");
        instrutions.put("sub", "000000x00000100010");
        instrutions.put("subu", "000000x00000100011");
        instrutions.put("seb", "01111100000x10000100000");
        instrutions.put("seh", "01111100000x11000100000");
        //logicas
        instrutions.put(key, value);
    }
}
