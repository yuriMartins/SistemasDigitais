#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>
#include <string.h>

int maior_end_dados, maior_end_reg;

int banco_reg[32];
int pc;
int size_mem;
int *mem;

int carregar(FILE **arq_data, FILE **arq_inst);
int tipo(int op, int funct);

char get_tipo(int op);

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

    arq_inst = fopen("arq_inst.txt", "r");
    arq_data = fopen("arq_data.txt", "r");



    if (arq_inst == NULL)
    {
        printf("Arquivo de entrada de dados não foi aberto!\n");
        return EXIT_FAILURE;
    }
    else if (arq_data == NULL)
    {
        printf("Arquivo de entrada de instruções não foi aberto!\n");
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
    else
        return 'r';
}

void decod_tipoR(int inst, int op){
    int rd = (inst >> 11) & 0x1f;

    int rs = (inst >> 21) & 0x1f;
    int rt = (inst >> 16) & 0x1f;
    int func = inst & 0x3F;

    int data_s = banco_reg[rs];
    int data_t = banco_reg[rt];


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




    }

}
void decod_tipoJ(int inst, int op){
    int instr_index = (inst) & 0x3FFFFFF;

    printf("%X\n", inst);
    printf("address %d\n", instr_index);

    if(op == 0b000010){
        printf("Inst J \n");
        pc = pc + instr_index;
    }

    if(op == 0b000010){
        printf("Inst J \n");
        pc = (pc + instr_index)-1; // Por causa do for la em cima, que ele vai acabar incrementando automaticcamente depois do fim do laço
    }
    else{
        printf("Inst JAL \n");
        //pc = ( )-1;
    }

}
void decod_tipoI(int inst, int op){
    int rs = (inst >> 21) & 0x1f;
    int rt = (inst >> 16) & 0x1f;
    int immd = (inst & 0xFFFF);

    int data_s = banco_reg[rs];

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
    }




}












