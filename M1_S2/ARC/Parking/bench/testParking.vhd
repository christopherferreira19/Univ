-- TP1 - Test bench for parking
-- Ahmed Zrigui
-- Christopher Ferreira

entity testParking is
end testParking;

architecture test1 of testParking is
	component Parking is port(
		clk, reset,
		require_in, avail, timeout1, sensor_in, 
		insert_ticket, ticketOK, timeout2, sensor_out : in Bit;
          	gate_in_open, car_in, 	
		ticket_inserted, gate_out_open, car_out : out Bit);
  end component;

  signal clk, reset,
	require_in, avail, timeout1, sensor_in,
	insert_ticket, ticketOK, timeout2, sensor_out,
	gate_in_open, car_in,
	ticket_inserted, gate_out_open, car_out : Bit;
begin
	P: Parking port map(clk, reset,
		require_in, avail, timeout1, sensor_in,
		insert_ticket, ticketOK, timeout2, sensor_out,
		gate_in_open, car_in,
		ticket_inserted, gate_out_open, car_out);

	process
	begin
		clk <= '0';
		wait for 18 ns;
		clk <= '1';
		wait for 2 ns;
	end process;

	reset <= '1', '0' after 10 ns;
	avail <= '1';
	require_in <= '0', '1' after 30 ns, '0' after 50 ns;
	sensor_in <= '0', '1' after 90 ns, '0' after 100 ns;
        insert_ticket<='0', '1' after 130 ns, '0' after 150 ns;
	ticketOK<='0','1' after 150 ns, '0' after 162 ns;
	sensor_out<='0','1' after 161 ns, '0' after 178 ns;
end test1;

library LIB_PARKING;
library LIB_PARKING_BENCH;

configuration config1 of LIB_PARKING_BENCH.testParking is 
    for test1 
       for P: Parking use entity LIB_PARKING.Parking(Automaton);
       end for;
    end for; 
end config1; 

