entity testAdd1 is
end testAdd1;

architecture test1 of testAdd1 is
  component full_adder is
     port(X,Y,Cin : in Bit; Sum,Cout : out Bit);  
  end component;
  signal x,y,c1,s,c2: bit;
begin
     A: full_adder port map(x,y,c1,s,c2);
     x <= '0', '1' after 60 ns;
     y <= '0', '1' after 30 ns,
               '0' after 90 ns;
     c1 <= '0', '1' after 15 ns,
               '0' after 45 ns,
               '1' after 75 ns,
               '0' after 105 ns;
end test1;

library LIB_ADD1;
library LIB_ADD1_BENCH;

configuration config1 of LIB_ADD1_BENCH.testAdd1 is 
    for test1 
       for A:full_adder use entity LIB_ADD1.full_adder(Dataflow_view);
       end for;
    end for; 
end config1; 

