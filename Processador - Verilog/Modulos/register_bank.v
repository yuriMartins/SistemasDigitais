//Módulo do banco de registradores  (combinacional e sequencial)
module register_bank(CLK, RESET, RA, RB, WC, WPC, WC_Activator, PRA, PRB);

	input 	wire[3:0]	RA, RB, WC;
	input 	wire[31:0]	WPC;
	input 	wire		CLK, RESET, W_RB;
	output	wire [31:0]	PRA, PRB;

	reg[31:0] 	registers[0:31];	//32 registradores de 32 bits


	assign 						PRA = registers[RA];
	assign 						PRB = registers[RB];

	integer i;
	always @(posedge RESET) begin
		for(i = 0; i <= 31; i = i + 1)
			registers[i] = 32'b0;
	end

	/*
		Quando WC_Activator for 1 e o CLK tiver uma borda de subida,
		o valor da entrada WPC é registrado no registrador
		indicado por WC.
	*/
	always @(posedge CLK)
		if(WC_Activator == 1'b1)		registers[WC] <= WPC;

endmodule
