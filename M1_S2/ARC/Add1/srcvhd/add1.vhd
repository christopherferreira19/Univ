entity full_adder is
     port(X,Y,Cin : in Bit;   -- ports d'entree
          Sum,Cout : out Bit);   -- ports de sortie
end full_adder;

architecture Dataflow_view of full_adder is
begin
    Sum <= (X xor Y) xor Cin;
    Cout <= (X and Y) or ((X xor Y) and Cin);
end Dataflow_view;

