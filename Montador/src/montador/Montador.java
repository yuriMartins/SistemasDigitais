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
    private static String arq = "entrada.asm";
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
        tradutor();
           
        
        //imprimir resultados
        for(String arg: args){
            gravarArq.printf(arg);   
        }
        gravarArq.close();
    }   
    
    private static void indexLabels() throws IOException {
        BufferedReader sourceBr = new BufferedReader(new FileReader(new File(arq)));
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
                        } else if (s.equals(".pseg") || s.equals(".module")||s.equals(".endseg") || s.equals(".data")) {// diretivas
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
    
    private static void tradutor() throws IOException {
        BufferedReader sourceBr = new BufferedReader(new FileReader(new File(arq)));
        String line, label;
        String aux[];
        boolean data =false;
        String instrution = null;
        
         while ((line = sourceBr.readLine()) != null) {
            line = line.trim();//retira os espacos do fim e comeco, se houver
            aux = line.split("\\s+");
            if(line.contains(".data")){
                data =true;
            }
            if (line.contains(".pseg")){
                data = false;
            }
            if(!data){
                for (String s : aux) {//linha
                    if (s.contains(";")||s.contains("#")) {//comentarios
                        break;
                    } else if (s.length() > 0) {
                        if (s.contains(":")) {
                            break;
                        }else if (s.equals(".pseg")||s.equals(".dseg")||s.equals(".endseg") || s.equals(".module") || s.equals(".data")){
                            break;
                        }else{
                            instrution = instrutions.get(aux[0]);
                            String aux2[] = instrution.split("x");
                            String regs[] = aux[1].split(",");
                            //verificar se as instrucoes de cada tipo se comportam da mesma forma
                            //se nao agrupar as que se comportam igual neste switch case
                            switch (aux[0]){
                                case "add":
                                    
                                    break;
                            }
                            for(int i=0; i<regs.length;i++){
                                regs[i] = registers.get(regs[i]);
                                
                            } 
                        }
                    }
                }
            }
         }
    }
    
    //mudar de int pra binario(string)
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
    //adicionar o tipo no inicio de cada instrucao
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
        instrutions.put("lui", "00111100000");
        instrutions.put("sub", "000000x00000100010");
        instrutions.put("subu", "000000x00000100011");
        instrutions.put("seb", "01111100000x10000100000");
        instrutions.put("seh", "01111100000x11000100000");
        instrutions.put("div", "000000x0000000000011010");
        instrutions.put("divu", "000000x0000000000011011");
        instrutions.put("madd", "011100x0000000000000000");
        instrutions.put("maddu", "011100x0000000000000001");
        instrutions.put("msub", "011100x0000000000000100");
        instrutions.put("msubu", "011100x0000000000000101");
        instrutions.put("mul", "011100x00000000010");
        instrutions.put("mult", "000000x0000000000011000");
        instrutions.put("multu", "000000x0000000000011001");
        //logicas
        instrutions.put("and", "00000x00000100100");
        instrutions.put("andi", "001101");
        instrutions.put("nor", "00000x00010100000");
        instrutions.put("wsbh", "01111100000x00010100000");
        instrutions.put("or", "000000x00000100101");
        instrutions.put("ori", "001101");
        instrutions.put("xor", "000000x00000100110");
        instrutions.put("xori", "001110");
        instrutions.put("ext", "011111x000000");//x = rs rt (pos+size-1) (pos)
        instrutions.put("ins", "011111x000100");//x = rs rt (pos+size-1) (pos)
        //deslocamento
        instrutions.put("sll", "00000000000x000000");// rt rd sa//sa = constante
        instrutions.put("sllv", "000000x00000000100");
        instrutions.put("srl", "00000000000x000010");
        instrutions.put("sra", "00000000000x000011");
        instrutions.put("srav", "0000");
        //saltos 
        instrutions.put("slt", "000000x00000101010");
        instrutions.put("slti", "001010");
        instrutions.put("sltiu", "001011");
        instrutions.put("sltu", "000000x00000101011");
        instrutions.put("movn", "000000x00000001011");
        instrutions.put("movz", "000000x00000001010");
        //acesso ao acumulador
        instrutions.put("mfhi", "0000000000000000x00000010000");
        instrutions.put("mflo", "0000000000000000x00000010010");
        instrutions.put("mthi", "000000x000000000000000010001");
        instrutions.put("mtlo", "000000x000000000000000010011");
        //saltos e branchs
        instrutions.put("beq", "000100");//usa offset, e eu nao sei o que é
        instrutions.put("bgtz", "000111x00000");//+offset
        instrutions.put("bne", "000101");//+rs rt offset
        instrutions.put("bltz", "000001x00000");
        instrutions.put("j", "000010");//+instrution id
        instrutions.put("jr", "000000x0000000000x001000");// 2° x = hint, que nao sei o que é
        instrutions.put("jal", "000011");//+instrution index
    }

    
}
