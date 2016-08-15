#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>
#include <string.h>

int maior_end_dados, maior_end_reg;
int HI = 0, LO = 0;
long int acc = 0;
int banco_reg[32];
int pc;
int size_mem;
int *mem;

int carregar(FILE **arq_data, FILE **arq_inst);
int tipo(int op, int funct);

char get_tipo(int op);
void decod_tipoRe(int ist,int op);
void decod_tipoR(int ist, int op);
void decod_tipoJ(int ist, int op);
void decod_tipoI(int ist, int op);


int main() {

    FILE *arq_inst;
    FILE *arq_data;

    int qtd_inst;


    mem = malloc(sizeof(int) * 2048);
    size_mem = 0;
    pc = 0;

    maior_end_dados = 0;
    maior_end_reg = 0;

    arq_inst = fopen("Bubblesort_pseg.txt", "r");
    arq_data = fopen("Bubblesort_data.txt", "r");



    if (arq_inst == NULL)
    {
        printf("Arquivo de entrada de instruções não foi aberto!\n");
        return EXIT_FAILURE;
    }
    else if (arq_data == NULL)
    {
       printf("Arquivo de entrada de dados não foi aberto!\n");
        return EXIT_FAILURE;
    }
    else
        qtd_inst = carregar(&arq_data, &arq_inst);

    for(pc=0; pc<qtd_inst; pc++)
    {
        int inst = mem[pc];
        int opcod  = (((inst))& 4227858432)>>26;
        char tipo = get_tipo(opcod);

        if(tipo == 'r')
            decod_tipoR(inst, opcod);
        else if(tipo == 'i')
            decod_tipoI(inst, opcod);
        else if(tipo == 'x')
            decod_tipoRe(inst,opcod);
        else
            decod_tipoJ(inst, opcod);


        int i = 0;
        printf("BANCO DE REGISTRADOR ATUAL: \n");
        for(i=0; i<maior_end_reg; i++)
            printf("%d:   %d\n", i, banco_reg[i]);

        printf("\n\nMEMORIA DE DADOS ATUAL: \n");
        for(i=qtd_inst; i<maior_end_dados; i++)
            printf("%d:   %d\n", (i-qtd_inst), mem[i]);

        printf("\n\n----------------------------------------\n\n");
    system("PAUSE");
    }
}

int carregar(FILE **arq_data, FILE **arq_inst)
{
    int i=0;
    int j=0;
    int word;

    for (i = 0; 1==1; i++)
    {
        fscanf(*arq_inst, "%32x", &word);
        if(feof(*arq_inst))break;

        mem[i] = word;
        size_mem++;
    }

    for (j = i; 1==1; j++)
    {
        fscanf(*arq_data, "%32x", &word);
        if(feof(*arq_data))break;

        mem[j] = word;

        size_mem++;
        maior_end_dados = j;
    }
    return i;
}

char get_tipo(int op){

    if(op == 0b000010 || op == 0b000011)
        return 'j';
    else if(    op == 0b001000 ||
                op == 0b001001 ||
                op == 0b001111 ||
                op == 0b001100 ||
                op == 0b001101 ||
                op == 0b001110 ||
                op == 0b001010 ||
                op == 0b001011 ||
                op == 0b000100 ||
                op == 0b000111 ||
                op == 0b000101 ||
                op == 0b000001 ||
                op == 0b100000 ||
                op == 0b100011 ||
                op == 0b100001 ||
                op == 0b101000 ||
                op == 0b101001 ||
                op == 0b101011
            )
        return 'i';
    else if ( op == 0b011100)
        return 'x';
    else
        return 'r';
}

void decod_tipoR(int inst, int op){
    int rd = (inst >> 11) & 0x1f;
    int rs = (inst >> 21) & 0x1f;
    int rt = (inst >> 16) & 0x1f;
    int sa = (inst>> 6) & 0x1f;
    int func = inst & 0x3F;

    int data_s = banco_reg[rs];
    int data_t = banco_reg[rt];
    int data_a = banco_reg[sa];

    printf("%X\n", inst);
    printf("rd: %d rs: %d rt: %d func: %d \n", rd, rs, rt, func);

    switch(func){
        case 0b100000:
            printf("Inst ADD \n");
            banco_reg[rd] = data_s + data_t;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;

         case 0b100001:
            printf("Inst ADDU \n");
            banco_reg[rd] = data_s + data_t;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;

        case 0b100010:
            printf("Inst SUB \n");
            banco_reg[rd] = data_s - data_t;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;

        case 0b100011:
            printf("Inst SUBU \n");
            banco_reg[rd] = data_s - data_t;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;

        case 0b100100:
            printf("Inst AND \n");
            banco_reg[rd] = data_s & data_t;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
          case 0b100111:
            printf("Inst NOR \n");
            banco_reg[rd] = !(data_s | data_t);

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
        case 0b100101:
            printf("Inst OR \n");
            banco_reg[rd] = data_s | data_t;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
        case 0b100110:
            printf("Inst XNOR \n");
            banco_reg[rd] = data_s ^ data_t;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
         case 0b011010:
            printf("Inst DIV \n");

            LO = data_s / data_t;
            HI = data_s % data_t;

        break;
        case 0b011011:
            printf("Inst DIVU \n");

           unsigned int q = data_s / data_t;
           unsigned int r = data_s % data_t;
           LO = q;
           HI = r;
        break;
        case 0b011000:
            printf("Inst MULT \n");

           acc = data_s * data_t;

        break;
        case 0b011001:
            printf("Inst MULTU \n");

            unsigned long int a = data_s * data_t;
            acc = a;
        break;
         case 0b000000:
            printf("Inst SLL \n");

            banco_reg[rd] = data_t << data_a;

            if(rd>maior_end_reg)
                maior_end_reg = rd;

        break;
        case 0b000100:
            printf("Inst SLLV \n");

            banco_reg[rd] = data_s << data_t;

            if(rd>maior_end_reg)
                maior_end_reg = rd;

        break;
        case 0b000010:
            printf("Inst SRL \n");

            banco_reg[rd] = data_t >> data_a;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
         case 0b000011:
            printf("Inst SRA \n");

            banco_reg[rd] = data_t >> data_a;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
         case 0b000111:
            printf("Inst SRAV \n");

            banco_reg[rd] = data_t >> data_s;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
        case 0b000110:
            printf("Inst SRLV \n");

            banco_reg[rd] = data_t >> data_s;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
        case 0b101010:
            printf("Inst SLT \n");
             if (data_s < data_t)
            banco_reg[rd] = 1;
        else banco_reg[rd] = 0;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
        case 0b101011:
            printf("Inst SLTU \n");
             unsigned int s = data_s;
             unsigned int t = data_t;
             if (s < t)
            banco_reg[rd] = 1;
        else banco_reg[rd] = 0;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
        case 0b001011:
            printf("Inst MOVN \n");
             if ( data_t != 0)
            banco_reg[rd] = data_s;


            if(rd>maior_end_reg)
                maior_end_reg = rd;
        break;
         case 0b001010:
            printf("Inst MOVZ \n");
             if ( data_t == 0)
            banco_reg[rd] = data_s;


            if(rd>maior_end_reg)
                maior_end_reg = rd;
            break;
             case 0b010000:
            printf("Inst MFHI \n");

              banco_reg[rd] = HI;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
            break;
             case 0b010010:
            printf("Inst MFLO \n");

              banco_reg[rd] = LO;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
            break;
            case 0b010001:
            printf("Inst MTHI \n");

              HI = data_s;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
            break;
            case 0b010011:
            printf("Inst MTLO \n");

              LO = data_s;

            if(rd>maior_end_reg)
                maior_end_reg = rd;
            break;
            case 0b001000:
            printf("Inst JR \n");
                pc = pc + banco_reg[31]-1;

            break;
            case 0b001001:
            printf("Inst JALR \n");
                banco_reg[rd] = banco_reg[31];
                pc = pc + banco_reg[31]-1;

                if(rd>maior_end_reg)
                maior_end_reg = rd;
            break;



    }

}
void decod_tipoJ(int inst, int op){
    int instr_index = (inst) & 0x3FFFFFF;

    printf("%X\n", inst);
    printf("address %d\n", instr_index);

    if(op == 0b000010){
        printf("Inst J \n");
        pc = (pc + instr_index)-1; // Por causa do for la em cima, que ele vai acabar incrementando automaticcamente depois do fim do laço
    }
    else{
        printf("Inst JAL \n");
        banco_reg[31] = (pc + instr_index) ;//pc = ( )-1;
        pc = (pc + instr_index)- 1;

    }

}
void decod_tipoI(int inst, int op){
    int rs = (inst >> 21) & 0x1f;
    int rt = (inst >> 16) & 0x1f;
    int immd = (inst & 0xFFFF);

    int data_s = banco_reg[rs];
    int data_t = banco_reg[rt];

    printf("%X\n", inst);
    printf("rt %d rs %d imm %d\n", rt, rs, immd);

    switch(op){
        int end;

        case 0b101000:
            printf("Inst SB \n");
            end = banco_reg[rs] + immd;


            if(end>maior_end_dados)
                maior_end_dados=end;
        break;

        case 0b101001:
            printf("Inst SH \n");

            if(end>maior_end_dados)
                maior_end_dados=end;
        break;

        case 0b101011:
            printf("Inst SW \n");
            end = banco_reg[rs] + immd;
            mem[end] = banco_reg[rt];

            if(end>maior_end_dados)
                maior_end_dados=end;
        break;
        case 0b001000:
            printf("Inst ADDI \n");
            banco_reg[rt] = data_s + immd;

             if(rt>maior_end_reg)
                maior_end_reg = rt;
        break;
        case 0b001001:
            printf("Inst ADDIU \n");
            unsigned int s = data_s;
            unsigned int im = immd;
            banco_reg[rt] = s + im;

             if(rt>maior_end_reg)
                maior_end_reg = rt;
        break;
        case 0b001111:
            printf("Inst LUI \n");

            banco_reg[rt] = (immd << 16);

             if(rt>maior_end_reg)
                maior_end_reg = rt;
        break;
        case 0b001100:
            printf("Inst ANDI \n");
            banco_reg[rt] = data_s & immd;

             if(rt>maior_end_reg)
                maior_end_reg = rt;
        break;
        case 0b001101:
            printf("Inst ORI \n");
            banco_reg[rt] = data_s | immd;

             if(rt>maior_end_reg)
                maior_end_reg = rt;
        break;
        case 0b001110:
            printf("Inst XORI \n");
            banco_reg[rt] = data_s ^ immd;

             if(rt>maior_end_reg)
                maior_end_reg = rt;
        break;
        case 0b001010:
            printf("Inst SLTI \n");
             if (data_s < immd)
            banco_reg[rt] = 1;
        else banco_reg[rt] = 0;

            if(rt>maior_end_reg)
                maior_end_reg = rt;
        break;
         case 0b001011:
            printf("Inst SLTIU \n");
             unsigned int siu = data_s;
             if (siu < immd)
            banco_reg[rt] = 1;
        else banco_reg[rt] = 0;

            if(rt>maior_end_reg)
                maior_end_reg = rt;
        break;
        case 0b000100:
            printf("Inst BEQ \n");
             if (data_s == data_t)
             {
                 pc = (pc + immd) -1;
             }

        break;
        case 0b000111:
            printf("Inst BGTZ \n");
             if (data_s > 0)
             {
                 pc = (pc + immd) -1;
             }

        break;
        case 0b000101:
            printf("Inst BNE \n");
             if (data_s != data_t)
             {
                 pc = (pc + immd) -1;
             }

        break;
        case 0b000001:
            printf("Inst BLTZ \n");
             if (data_s < 0)
             {
                 pc = (pc + immd) -1;
             }

        break;
         case 0b100000:
            printf("Inst LB\n");
            end = mem[rs] + immd;


            if(rt>maior_end_reg)
                maior_end_reg =rt;
        break;
        case 0b100011:
            printf("Inst LW\n");
            banco_reg[rt]  = banco_reg[rs] + immd;


            if(rt>maior_end_reg)
                maior_end_reg =rt;
        break;


    }




}
void decod_tipoRe(int inst,int op){

    int rd = (inst >> 11) & 0x1f;
    int rs = (inst >> 21) & 0x1f;
    int rt = (inst >> 16) & 0x1f;
    int sa = (inst>> 6) & 0x1f;
    int func = inst & 0x3F;

    int data_s = banco_reg[rs];
    int data_t = banco_reg[rt];
    int data_a = banco_reg[sa];

    printf("%X\n", inst);
    printf("rd: %d rs: %d rt: %d func: %d \n", rd, rs, rt, func);

    switch(func){
        case 0b000000:
           printf("Inst MADD \n");

           long int u = data_s * data_t;
           long int aux = u;
           LO = (aux>> 32)& 0xFFFFFFFFFFFFFFFF;
           HI =  u & 0xFFFFFFFFFFFFFFFF;
        break;
        case 0b000001:
           printf("Inst MADDU\n");

           unsigned long int p = data_s * data_t;
           unsigned long int aux1 = p;
           LO += (aux>> 32)& 0xFFFFFFFFFFFFFFFF;
           HI +=  p & 0xFFFFFFFFFFFFFFFF;
        break;
         case 0b000100:
           printf("Inst MSUB\n");

            long int t = data_s * data_t;
            long int aux2 = t;
           LO -= (aux2>> 32)& 0xFFFFFFFFFFFFFFFF;
           HI -=  t & 0xFFFFFFFFFFFFFFFF;
        break;
        case 0b000101:
           printf("Inst MSUB\n");

            unsigned long int b = data_s * data_t;
            unsigned long int aux3 = b;
           LO -= (aux3>> 32)& 0xFFFFFFFFFFFFFFFF;
           HI -=  b & 0xFFFFFFFFFFFFFFFF;
        break;
        case 0b000010:
           printf("Inst MUL\n");

            banco_reg[rd] = data_s * data_t;

             if(rd>maior_end_reg)
                maior_end_reg = rd;


        break;





    }

}












