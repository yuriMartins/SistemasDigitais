/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package montador;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author jmalmeida
 */
public class Montador {
    private static HashMap<String, Integer> labelMap;
    private static HashMap<String, String> instrutions, registers;
    private static String arq = "codes/";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {     
        labelMap = new HashMap<>();
        Scanner ler = new Scanner(System.in);
        System.out.println("\nEscolha um codigo para ser traduzido:\n 1 - Bubllesort\n 2 - Fatorial\n 3 - FibonacciRecursive\n "
                + "4 - Potencia\n 5 - Primos\n 6 - Raiz quadrada\n 7 - Outro");
        int opcao = ler.nextInt();
        switch (opcao){
            case 1:
                arq = arq.concat("Bubblesort");
                break;
            case 2:
                arq = arq.concat("Fatorial");
                break;
            case 3:
                arq = arq.concat("FibonacciRecursive");
                break;
            case 4:
                arq = arq.concat("Potencia");
                break;
            case 5:
                arq = arq.concat("Primos");
                break;
            case 6:
                arq = arq.concat("Raiz quadrada");
                break;
            case 7:
                System.out.println("O arquivo deve estar na pasta /codes deste projeto, e com extensao .asm");
                System.out.println("Digite o nome do arquivo (sem a extensao):");
                arq= arq.concat(ler.next());
                break;
            default:
                System.out.println("Opcao invalida!");
                return;
        }
        
        //cria as listas pra comparações
        createInstrutions();
        createRegisters();
        System.out.println("First run - Indexando labels\n");
        indexLabels();//faz uma varredura no arquivo indexando os labels
        System.out.println("\nSecond run - Traduzindo");
        tradutor(); //traduz pra binario e ja grava no arquivo
                    //gera um arquivo pra segemento de dados (.data) e outro pra instrucoes (.pseg)                    
        System.out.println("Traduzido com sucesso!");//ou nao
        //fim
    }   
    
    private static void indexLabels() throws IOException {
        BufferedReader sourceBr = new BufferedReader(new FileReader(new File(arq+".asm")));
        String line, label;
        String aux[];
        int codeLineCnt = 0;
        boolean incrementLineCount = true;
        
        while ((line = sourceBr.readLine()) != null) {
            aux = line.split("#|;"); //separa os comentarios
            line = aux[0]; //pega so a primeira string, pois não será comentario
            line = line.trim();
            aux = line.split("\\s+");// separa por espacos, se houver
            if(!((aux.length==1) && (aux[0].equals("")))){//linha vazia
                for (String s : aux) {
                    if (s.length() > 0) {
                        if (s.contains(":")) {
                            if (aux.length>2)//tem instrucao na linha do label
                                incrementLineCount = true;
                            else
                                incrementLineCount = false;
                            if (s.length()==1){
                                label = aux[0];
                            }else{
                                label = s.split(":")[0];
                            }
                            labelMap.put(label, codeLineCnt);
                            System.out.println("Label encontrada:");
                            System.out.println(label + " " + codeLineCnt);
                        } else {
                            incrementLineCount = true;
                            if (s.equals(".endseg")) { 
                                System.out.println("fim de codigo");
                                codeLineCnt = 0;
                                break;
                            } else if (s.equals(".pseg") || s.equals(".module")||s.equals(".data")) {// diretivas
                                System.out.println(s);
                                break;
                            }

                            if (incrementLineCount) {
                                codeLineCnt++;
                                break;
                            }
                        }
                    }
                }
            }
            incrementLineCount = true;
        }
        sourceBr.close();
    }
    
    private static void tradutor() throws IOException {
        //gravadores
        File file1 = new File(arq+"_pseg.txt");          
        if (file1.exists()){      
            new File(arq+"_pseg.txt").createNewFile();
            file1 = new File(arq+"_pseg.txt");
        }        
        PrintWriter gravarPseg = new PrintWriter(new FileWriter(file1)); 
        File file2 = new File(arq+"_data.txt");          
        if (file2.exists()){      
            new File(arq+"_data.txt").createNewFile();
            file2 = new File(arq+"_data.txt");
        }    
        //leitor
        PrintWriter gravarData = new PrintWriter(new FileWriter(file2));
        BufferedReader sourceBr = new BufferedReader(new FileReader(new File(arq+".asm")));
        //auxiliares
        String line;
        String aux[];
        boolean data =false;
        String instrution = null;
        String labelIndex =null;//variavel auxiliar pra escrever o index com o tamnho correto
        int auxInt;//variavel auxiliar pra conversoes de texto pra inteiro
        boolean erroSintax;//se for erro nao grava na saida
        
         while ((line = sourceBr.readLine()) != null) {
             
            erroSintax = false;
             //divisoes de elementos por espacos e virgulas e retirada de labels e comentarios
            aux = line.split("#|;"); //separa os comentarios
            line = aux[0]; //pega so a primeira string, pois não será comentario
            line = line.trim();
            if (line.contains(":")){//tira os labels
                //aproveitar a instrucao se houver, ex: loop: add $,$,$
                String temp[] = line.split(":");
                if(temp.length>1)
                    line = temp[1];
                else
                    line = "";
            }
            if (line.equals(""))
                System.out.println("void line"); 
            else
                System.out.println(line);         
            line = line.trim();//retira os espacos do inicio e fim
            aux = line.split("\\s+,*|,+\\s*");// separa por espacos e virgulas            
            
            if(!((aux.length==1) && (aux[0].equals("")))){//se nao for uma linha vazia, faca
                
                if (line.contains(".pseg")){
                    System.out.println("Segmento de codigo:");
                    data = false;
                }
                if(data){
                    auxInt = Integer.parseInt(aux[1]);
                    String dataBin32 = String.format("%32s", Integer.toBinaryString(auxInt)).replace(' ', '0'); 
                    System.out.println(dataBin32+" - "+dataBin32.length()+"bits");
                    gravarData.printf(dataBin32+"%n");
                }else{
                    if(line.contains(".data")){
                        System.out.println("Segmento de dados:");
                        data =true;
                    }
                    //verificações a partir do primeiro elemento da linha
                    if (!(aux[0].contains(".data")||aux[0].contains(".pseg")||aux[0].contains(".endseg") || aux[0].contains(".module"))){
                        //considerando que so existe uma instrução por linha
                        
                        instrution = instrutions.get(aux[0]);
                        if (instrution == null)
                            instrution = "";
                        String codesInst[] = instrution.split("x");
                        instrution = codesInst[0]; //ja coloca o a primeira parte (opcode), todas instrucoes fazem isso
                        //verificar se as instrucoes de cada tipo se comportam da mesma forma
                        //se nao agrupar as que se comportam igual neste switch case
                        
                        switch (aux[0]){
                            case "addi":case "addiu":case "addu":case "clz":case "clo":case "lui":case "sub":case "subu":
                                case "seb":case "seh":case "div":case "divu":case "madd":case "maddu":case "msub":case "msubu":
                                case "mul":case "mult":case "multu": 
                                    //aritimeticas
                                case "and":case "andi":case "nor":case "wsbh":case "or":case "ori":case "xor":case "xori":
                                    //logicas, exceto ins e ext
                                case "sll":case "sllv":case "srl":case "sra":case "srav":case "srlv":case "rotr":case "rotrv":
                                    //deslocamento
                                case "slt":case "slti":case "sltiu":case "sltu":case "movn":case "movz":
                                    //condicionais e de troca
                                case "mfhi":case "mflo":case "mthi":case "mtlo":
                                    //acumulador
                                case "jr":
                                    //caso especifico de jump (so esse)
                                    //todos casos acima tem: $d,$s,$t |$d,$s| $d |$d,$s,c| $d,c
                                    // formato da concatenação 01std01
                                    boolean imediate =false;
                                    for(int i=2; i<aux.length; i++){
                                        if (!aux[i].contains("$")){//imediato
                                            imediate = true;
                                            //se for uma cosntante, converter pra binario
                                            instrution = instrution.concat(registers.get(aux[1]));//d
                                            auxInt = Integer.parseInt(aux[i]);
                                            aux[i] = String.format("%16s", Integer.toBinaryString(auxInt)).replace(' ', '0');   
                                            instrution = instrution.concat(aux[i]);                                             
                                        }else{ //registrador                                            
                                            instrution = instrution.concat(registers.get(aux[i]));//s & t                                            
                                        }                                                                              
                                    }
                                    if (!imediate){
                                        instrution = instrution.concat(registers.get(aux[1]));//d
                                        instrution = instrution.concat(codesInst[1]);
                                    }
                                break;
                            case "lb":case "lw":case "lh":case "sb":case "sh":case "sw":
                                //load e store  $d, c($s)
                                // formato da concatenação 01sdc
                                String aux2[] = aux[2].split("\\s+\\(*|\\(+\\s*|\\s+\\)*|\\)+\\s*");//separa c de $s
                                instrution = instrution.concat(registers.get(aux2[1])); //s
                                instrution = instrution.concat(registers.get(aux[1])); //d
                                //converte a constante pra binario
                                auxInt = Integer.parseInt(aux2[0]);
                                aux2[0] = Integer.toBinaryString(auxInt);
                                auxInt = Integer.parseInt(aux2[0]);//c
                                labelIndex = String.format("%16s", Integer.toBinaryString(auxInt)).replace(' ', '0');
                                instrution = instrution.concat(labelIndex);
                                break;

                            //casos especificos
                                
                            case "add":
                                //$d, $s, $t ou c
                                //01std01 ou 01sdc                                
                                if (aux[3].contains("$")){//add
                                    instrution = instrution.concat(registers.get(aux[2]));//s
                                    instrution = instrution.concat(registers.get(aux[3]));//t
                                    instrution = instrution.concat(registers.get(aux[1]));//d
                                    instrution = instrution.concat(codesInst[1]); //fuction
                                }else{//addi
                                    instrution ="";
                                    instrution = instrution.concat(instrutions.get("addi")); //troca o opcode pro addi
                                    instrution = instrution.concat(registers.get(aux[2]));//s
                                    instrution = instrution.concat(registers.get(aux[1]));//d
                                    auxInt = Integer.parseInt(aux[3]);
                                    labelIndex = String.format("%16s", Integer.toBinaryString(auxInt)).replace(' ', '0');
                                    instrution = instrution.concat(labelIndex);
                                }
                                break;
                            case "li":
                                //$d, c
                                //na verdade é o ori $d, $zero, c
                                //formato: 01dc                                
                                instrution = instrution.concat(registers.get(aux[1]));//d
                                auxInt = Integer.parseInt(aux[2]);
                                labelIndex = String.format("%16s", Integer.toBinaryString(auxInt)).replace(' ', '0');                                    
                                instrution = instrution.concat(labelIndex);                                
                                break;
                            case "j":case "jal":
                                // label
                                //formato: 01Labelindex
                                labelIndex = String.format("%26s", Integer.toBinaryString(labelMap.get(aux[1]))).replace(' ', '0');                                    
                                instrution = instrution.concat(labelIndex);
                                break;
                            case "jalr":
                                //$d, $s
                                //formato: 01s01d01
                                instrution = instrution.concat(registers.get(aux[2]));//s
                                instrution = instrution.concat(codesInst[1]);
                                instrution = instrution.concat(registers.get(aux[1]));//d
                                instrution = instrution.concat(codesInst[2]);
                                break;
                            case "ins":
                                //$d $s, pos, size
                                //ins: 01sd pos+size-1 pos 01
                                instrution = instrution.concat(registers.get(aux[2]));//s
                                instrution = instrution.concat(registers.get(aux[1]));//d
                                auxInt = Integer.parseInt(aux[3])+Integer.parseInt(aux[4])-1;//pos+size-1
                                instrution = instrution.concat(String.format("%5s", Integer.toBinaryString(auxInt)).replace(' ', '0'));
                                auxInt = Integer.parseInt(aux[3]);//pos
                                instrution = instrution.concat(String.format("%5s", Integer.toBinaryString(auxInt)).replace(' ', '0'));
                                break;                                
                            case "ext":
                                // $d $s, pos, size
                                //formato:  ext 01sd size-1 pos 01
                                instrution = instrution.concat(registers.get(aux[2]));//s
                                instrution = instrution.concat(registers.get(aux[1]));//d
                                auxInt = Integer.parseInt(aux[4])-1;//size-1
                                instrution = instrution.concat(String.format("%5s", Integer.toBinaryString(auxInt)).replace(' ', '0'));
                                auxInt = Integer.parseInt(aux[3]);//pos
                                instrution = instrution.concat(String.format("%5s", Integer.toBinaryString(auxInt)).replace(' ', '0'));
                                instrution = instrution.concat(codesInst[1]);
                                break;
                            case "beq":case "bne":
                                //$t, $s, label
                                //formato: 01st labelIndex
                                instrution = instrution.concat(registers.get(aux[2]));//s
                                instrution = instrution.concat(registers.get(aux[1]));//t
                                labelIndex = String.format("%16s", Integer.toBinaryString(labelMap.get(aux[3]))).replace(' ', '0');                                    
                                instrution = instrution.concat(labelIndex);
                                break;
                            case "bgtz":case "bltz":
                                //$s, label
                                //formato: 01s01 labelIndex
                                instrution = instrution.concat(registers.get(aux[1]));//s
                                instrution = instrution.concat(codesInst[1]);
                                labelIndex = String.format("%16s", Integer.toBinaryString(labelMap.get(aux[2]))).replace(' ', '0');                                    
                                instrution = instrution.concat(labelIndex);
                                break;
                            default:
                                System.out.println(aux[0]+" - Erro de sintaxe");
                                erroSintax = true;
                        }
                        if(!erroSintax){
                            gravarPseg.printf(instrution+"%n");
                            System.out.println(instrution+" - "+instrution.length()+"bits");
                        }
                    }                    
                }
            }
        }
         gravarData.close();
         gravarPseg.close();
    }
    
    //mudar de int pra binario(string)
    private static void createRegisters(){
        registers  = new HashMap<>();
        registers.put("$zero", "00000");
        registers.put("$at", "00001");
        registers.put("$v0", "00010");
        registers.put("$v1", "00011");
        registers.put("$a0", "00100");
        registers.put("$a1", "00101");
        registers.put("$a2", "00110");
        registers.put("$a3", "00111");
        registers.put("$t0", "01000");
        registers.put("$t1", "01001");
        registers.put("$t2", "01010");
        registers.put("$t3", "01011");
        registers.put("$t4", "01100");
        registers.put("$t5", "01101");
        registers.put("$t6", "01110");
        registers.put("$t7", "01111");
        registers.put("$s0", "10000");
        registers.put("$s1", "10001");
        registers.put("$s2", "10010");
        registers.put("$s3", "10011");
        registers.put("$s4", "10100");
        registers.put("$s5", "10101");
        registers.put("$s6", "10110");
        registers.put("$s7", "10111");
        registers.put("$t8", "11000");
        registers.put("$t9", "11001");
        registers.put("$k0", "11010");
        registers.put("$k1", "11011");
        registers.put("$gp", "11100");
        registers.put("$sp", "11101");
        registers.put("$fp", "11110");
        registers.put("$ra", "11111");
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
        instrutions.put("srav", "000000x00000000111");
        instrutions.put("srlv", "000000x00000000110");
        instrutions.put("rotr", "00000000001x000010");
        instrutions.put("rotrv", "000000x00001000110");
        //condicionais e trocas
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
        instrutions.put("beq", "000100");//usa offset, é a constante que vem an instrucao
        instrutions.put("bgtz", "000111x00000");//+offset
        instrutions.put("bne", "000101");//+rs rt offset
        instrutions.put("bltz", "000001x00000");
        instrutions.put("j", "000010");//+instrution id
        instrutions.put("jr", "000000x000000000000000001000");//hint, é usado em pipeline, set pra 0(no momento)
        instrutions.put("jal", "000011");//+instrution index
        instrutions.put("jalr", "000000x00000x00000001001");//hint, é usado em pipeline, set pra 0(no momento)
        //load e store
        instrutions.put("lb", "100000");
        instrutions.put("lw", "100011");
        instrutions.put("lh", "100011");
        instrutions.put("sb", "101000");
        instrutions.put("sh", "101001");
        instrutions.put("sw", "101011");
        //pseudoInstrucoes
        instrutions.put("li", "00110100000");//na verdade é a ori
        
    }    
}
