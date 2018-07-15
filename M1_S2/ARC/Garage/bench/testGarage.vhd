-- TP1 - Test bench for parking
-- Ahmed Zrigui
-- Christopher Ferreira

library ieee;
use ieee.std_logic_1164.all;

entity testGarage is
end testGarage;

architecture test1 of testGarage is
	component Garage is port(clk, reset,
                                 aes_key, authentication,
                                 reached_bottom, reached_top,
                                 acsc, bad_encryption, timeout,
                                 switch : in std_logic;
                                 command: in std_logic_vector(0 to 1);
                                 key_led, authentication_led,
                                 open_light, close_light: out std_logic);
  end component;

  signal clk, reset,
    aes_key, authentication,
    reached_bottom, reached_top,
    acsc, bad_encryption, timeout,
    switch : std_logic;
  signal command: std_logic_vector(0 to 1);

  signal key_led, authentication_led,
    open_light, close_light: std_logic;
  signal key_led_b, authentication_led_b,
    open_light_b, close_light_b: std_logic;
  signal key_led_g, authentication_led_g,
    open_light_g, close_light_g: std_logic;
  signal key_led_o, authentication_led_o,
    open_light_o, close_light_o: std_logic;

begin
	G: Garage port map(clk, reset,
                           aes_key, authentication,
                           reached_bottom, reached_top,
                           acsc, bad_encryption, timeout,
                           switch,
                           command,
                           key_led, authentication_led,
                           open_light, close_light);

        GSB: Garage port map (clk, reset,
                           aes_key, authentication,
                           reached_bottom, reached_top,
                           acsc, bad_encryption, timeout,
                           switch,
                           command,
                           key_led_b, authentication_led_b,
                           open_light_b, close_light_b);

        GSG: Garage port map (clk, reset,
                           aes_key, authentication,
                           reached_bottom, reached_top,
                           acsc, bad_encryption, timeout,
                           switch,
                           command,
                           key_led_g, authentication_led_g,
                           open_light_g, close_light_g);

        GSO: Garage port map (clk, reset,
                           aes_key, authentication,
                           reached_bottom, reached_top,
                           acsc, bad_encryption, timeout,
                           switch,
                           command,
                           key_led_o, authentication_led_o,
                           open_light_o, close_light_o);

        process
	begin
		clk <= '1';
		wait for 10 ns;
		clk <= '0';
		wait for 10 ns;
	end process;

	reset <= '1', '0' after 10 ns;
        --    NoKey
        switch <= '0', '1' after 15 ns, '0' after 210 ns;
        aes_key <= '0', '1' after 21 ns, '0' after 41 ns;
        --    Deactivated
        authentication <= '0', '1' after 25 ns, '0' after 64 ns;
        --    Activated
        timeout <= '0', '1' after 72 ns, '0' after 86 ns;
        --    Down
        command  <= "00",
                    "10" after 31 ns, "00" after 41 ns,
                    "10" after 87 ns, "01" after 117 ns,
                    "00" after 124 ns, "10" after 158 ns,
                    "00" after 176 ns;
        --    MovingOnUp
        --    MovingOnDown
        acsc <= '0', '1' after 138 ns, '0' after 142 ns;
        --    SafetyError
        --    MovingOnUp
        reached_top <= '0', '1' after 176 ns, '0' after 183 ns;
        --    Up
        bad_encryption <= '0', '1' after 195 ns;
        --    SecurityError
        --    Deactivated

        reached_bottom <= '0';
end test1;

library LIB_GARAGE;
library LIB_GARAGE_BENCH;

configuration config1 of LIB_GARAGE_BENCH.testGarage is
  for test1
       for G: Garage use entity LIB_GARAGE.Garage(Automaton);
       end for;
       for GSB: Garage use entity LIB_GARAGE.Garage(SynthBinary);
       end for;
       for GSG: Garage use entity LIB_GARAGE.Garage(SynthGray2);
       end for;
       for GSO: Garage use entity LIB_GARAGE.Garage(SynthOneHot2);
       end for;
  end for;
end config1;

