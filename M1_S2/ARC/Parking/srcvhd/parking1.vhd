-- TP1 - VHDL description for parking
-- Ahmed Zrigui
-- Christopher Ferreira

entity Parking is
     port(clk, reset,
		require_in, avail, timeout1, sensor_in, 
		timeout2, insert_ticket, ticketOK, sensor_out : in Bit;
          	gate_in_open, car_in, 	
		ticket_inserted,gate_out_open, car_out : out Bit);
end Parking;

architecture Automaton of Parking is
	type States is (Idle,
			Verif_ticket, Open_out, Close_out,
			Open_in, Close_in);
	signal state, nextstate: States;
begin
    process(state, require_in, avail, timeout1,timeout2,sensor_in,insert_ticket, ticketOK,sensor_out) is
    begin
        case state is
            when Idle =>
                if require_in = '1' and avail = '1' and insert_ticket = '0' then
                    nextstate <= Open_in;
                elsif insert_ticket = '1' then
                    nextstate <= Verif_ticket;
                else
                    nextstate <= Idle;
                end if;
            when Open_in =>
                if timeout1 = '0' and sensor_in = '0' then
                    nextstate <= Open_in;
                else
                    nextstate <= Close_in;
                end if;
            when Close_in =>
                nextstate <= Idle;
            when Verif_ticket =>
                if ticketOK = '1' then
                    nextstate <= Open_out;
                else
                    nextstate <= Idle;
                end if;
            when Open_out =>
                if timeout2 = '0' and sensor_out = '0' then
                    nextstate <= Open_out;
                else
                    nextstate <= Close_out;
                end if;
            when Close_out =>
                nextstate <= Idle;
        end case;
    end process;

    process(reset, clk)
    begin
        --  asynchronous reset (active high)
        if (reset = '1') then
            state <= Idle;
        -- otherwise, update the state if need be
        elsif (clk'event and clk = '1') then
            state <= nextstate;
        end if;
   end process;
    
    process(state,require_in, avail, timeout1,timeout2,sensor_in,insert_ticket, ticketOK,sensor_out)
    begin
        if state = Idle and insert_ticket='1'
        then ticket_inserted<='1';
        else ticket_inserted<='0';
        end if;

        if state = Idle and require_in='1' and avail='1' and 
        insert_ticket='0'
        then gate_in_open<='1';
        else gate_in_open<='0'; 
        end if;

        if state = Verif_ticket and ticketOK='1' 
        then gate_out_open<='1';
        else gate_out_open<='0';
        end if;
        
        if state = Open_out and timeout2='0' and sensor_out='1'
        then car_out<='1';
        else car_out<='0';
        end if;

        if state = Open_in and timeout1='0' and sensor_in='1'
        then car_in<='1';
        else car_in<='0';
        end if;

    end process;

end Automaton;

