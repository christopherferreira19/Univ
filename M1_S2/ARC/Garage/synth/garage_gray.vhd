
-- 
-- Definition of  Garage
-- 
--      Wed 12 Apr 2017 02:51:53 PM CEST
--      
--      LeonardoSpectrum Level 3, 2015a.6
-- 

library IEEE;
use IEEE.STD_LOGIC_1164.all;

library c35_CORELIB;
use c35_CORELIB.vcomponents.all;

entity decoder_2 is
   port (
      data : IN std_logic_vector (1 DOWNTO 0) ;
      eq : OUT std_logic_vector (3 DOWNTO 0)) ;
end decoder_2 ;

architecture INTERFACE of decoder_2 is
   component eq_2u_2u
      port (
         a : IN std_logic_vector (1 DOWNTO 0) ;
         b : IN std_logic_vector (1 DOWNTO 0) ;
         d : OUT std_logic) ;
   end component ;
   signal nx2, nx4: std_logic ;

begin
   nx2 <= '0' ;
   nx4 <= '1' ;
   ix6 : eq_2u_2u port map ( a(1)=>data(1), a(0)=>data(0), b(1)=>nx4, b(0)=>
      nx4, d=>eq(3));
   ix8 : eq_2u_2u port map ( a(1)=>data(1), a(0)=>data(0), b(1)=>nx4, b(0)=>
      nx2, d=>eq(2));
   ix10 : eq_2u_2u port map ( a(1)=>data(1), a(0)=>data(0), b(1)=>nx2, b(0)
      =>nx4, d=>eq(1));
   ix12 : eq_2u_2u port map ( a(1)=>data(1), a(0)=>data(0), b(1)=>nx2, b(0)
      =>nx2, d=>eq(0));
end INTERFACE ;

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity decoder_4 is
   port (
      data : IN std_logic_vector (3 DOWNTO 0) ;
      eq : OUT std_logic_vector (15 DOWNTO 0)) ;
end decoder_4 ;

architecture INTERFACE of decoder_4 is
   component eq_4u_4u
      port (
         a : IN std_logic_vector (3 DOWNTO 0) ;
         b : IN std_logic_vector (3 DOWNTO 0) ;
         d : OUT std_logic) ;
   end component ;
   signal nx4, nx6: std_logic ;

begin
   nx4 <= '0' ;
   nx6 <= '1' ;
   ix8 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx6, b(2)=>nx6, b(1)=>nx6, b(0)=>nx6, d=>eq(15));
   ix10 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx6, b(2)=>nx6, b(1)=>nx6, b(0)=>nx4, d=>eq(14));
   ix12 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx6, b(2)=>nx6, b(1)=>nx4, b(0)=>nx6, d=>eq(13));
   ix14 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx6, b(2)=>nx6, b(1)=>nx4, b(0)=>nx4, d=>eq(12));
   ix16 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx6, b(2)=>nx4, b(1)=>nx6, b(0)=>nx6, d=>eq(11));
   ix18 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx6, b(2)=>nx4, b(1)=>nx6, b(0)=>nx4, d=>eq(10));
   ix20 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx6, b(2)=>nx4, b(1)=>nx4, b(0)=>nx6, d=>eq(9));
   ix22 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx6, b(2)=>nx4, b(1)=>nx4, b(0)=>nx4, d=>eq(8));
   ix24 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx4, b(2)=>nx6, b(1)=>nx6, b(0)=>nx6, d=>eq(7));
   ix26 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx4, b(2)=>nx6, b(1)=>nx6, b(0)=>nx4, d=>eq(6));
   ix28 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx4, b(2)=>nx6, b(1)=>nx4, b(0)=>nx6, d=>eq(5));
   ix30 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx4, b(2)=>nx6, b(1)=>nx4, b(0)=>nx4, d=>eq(4));
   ix32 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx4, b(2)=>nx4, b(1)=>nx6, b(0)=>nx6, d=>eq(3));
   ix34 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx4, b(2)=>nx4, b(1)=>nx6, b(0)=>nx4, d=>eq(2));
   ix36 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx4, b(2)=>nx4, b(1)=>nx4, b(0)=>nx6, d=>eq(1));
   ix38 : eq_4u_4u port map ( a(3)=>data(3), a(2)=>data(2), a(1)=>data(1), 
      a(0)=>data(0), b(3)=>nx4, b(2)=>nx4, b(1)=>nx4, b(0)=>nx4, d=>eq(0));

end INTERFACE ;

library IEEE;
use IEEE.STD_LOGIC_1164.all;

architecture SynthGray of Garage is
   component mux_16u_16u
      port (
         a : IN std_logic_vector (15 DOWNTO 0) ;
         b : IN std_logic_vector (15 DOWNTO 0) ;
         d : OUT std_logic) ;
   end component ;
   procedure DFFPC (
      constant data   : in std_logic;
      constant preset : in std_logic;
      constant clear  : in std_logic;
      signal clk      : in std_logic;
      signal q        : out std_logic)
   is begin
       if (preset = '1') then
           q <= '1' ;
       elsif (clear = '1') then
           q <= '0' ;
       elsif (clk'event and clk'last_value = '0' and clk = '1') then
           q <= data and data ;    -- takes care of q<='X' if data='Z'
       end if ;
       if ((clear/='1' or preset/='1') and clk/='0' and clk/='1') then
           q <= 'X' ;
       end if ;
   end DFFPC ;
   component select_3_3
      port (
         a : IN std_logic_vector (2 DOWNTO 0) ;
         b : IN std_logic_vector (2 DOWNTO 0) ;
         d : OUT std_logic) ;
   end component ;
   component select_4_4
      port (
         a : IN std_logic_vector (3 DOWNTO 0) ;
         b : IN std_logic_vector (3 DOWNTO 0) ;
         d : OUT std_logic) ;
   end component ;
   component decoder_2
      port (
         data : IN std_logic_vector (1 DOWNTO 0) ;
         eq : OUT std_logic_vector (3 DOWNTO 0)) ;
   end component ;
   component decoder_4
      port (
         data : IN std_logic_vector (3 DOWNTO 0) ;
         eq : OUT std_logic_vector (15 DOWNTO 0)) ;
   end component ;
   signal state_3, state_2, state_1, state_0, nextstate_3, nextstate_2, 
      nextstate_1, nextstate_0, GND, PWR, NOT_aes_key, nx29, NOT_switch, 
      nx57, nx70, nx77, nx92, nx116, nx118, nx138, nx142, nx144, nx147, 
      nx150, nx151, nx153, NOT_nx153, nx171, NOT_nx171, nx182, nx185, nx186, 
      nx188, NOT_nx188, nx198, nx208, NOT_nx208, nx237, nx243, nx274, nx275, 
      nx277, nx279, nx280, nx282, nx288, nx289, nx295, nx296, 
      NOT_bad_encryption, nx300, nx303, nx305, nx306, NOT_acsc, nx310, nx315, 
      NOT_reached_top, nx319, nx327, nx333, nx348, nx349, nx351, nx354, 
      nx356, nx357, nx359, nx364, NOT_reached_bottom, nx368, nx376, nx378, 
      nx379, nx382, nx401, nx403, nx404, nx451, nx468, nx475, nx476, nx487, 
      nx541, NOT_nx541, nx547, NOT_nx547, nx553, NOT_nx553, nx565, NOT_nx565, 
      nx571, NOT_nx571, nx577, nx579, nx587, nx589, nx650, nx702, nx704, 
      NOT_nx704, NOT_nx77, NOT_nx95, NOT_nx121, NOT_nx487: std_logic ;
   
   signal DANGLING : std_logic_vector (8 downto 0 );

begin
   GND <= '0' ;
   PWR <= '1' ;
   nx403 <= switch AND aes_key ;
   NOT_aes_key <= NOT aes_key ;
   nx404 <= switch AND NOT_aes_key ;
   NOT_switch <= NOT switch ;
   nx153 <= nx151 OR NOT_nx553 ;
   NOT_nx153 <= NOT nx153 ;
   nx171 <= nx150 OR NOT_nx553 ;
   NOT_nx171 <= NOT nx171 ;
   nx188 <= nx185 OR nx186 ;
   NOT_nx188 <= NOT nx188 ;
   nx198 <= bad_encryption OR nx186 ;
   nx208 <= nx185 OR NOT_nx571 ;
   NOT_nx208 <= NOT nx208 ;
   modgen_mux_20 : mux_16u_16u port map ( a(15)=>GND, a(14)=>GND, a(13)=>
      nx237, a(12)=>NOT_nx188, a(11)=>GND, a(10)=>GND, a(9)=>GND, a(8)=>GND, 
      a(7)=>nx142, a(6)=>GND, a(5)=>nx138, a(4)=>NOT_nx153, a(3)=>GND, a(2)
      =>GND, a(1)=>GND, a(0)=>GND, b(15)=>GND, b(14)=>GND, b(13)=>GND, b(12)
      =>GND, b(11)=>GND, b(10)=>GND, b(9)=>GND, b(8)=>GND, b(7)=>GND, b(6)=>
      GND, b(5)=>GND, b(4)=>GND, b(3)=>state_3, b(2)=>state_2, b(1)=>state_1, 
      b(0)=>state_0, d=>nextstate_3);
   modgen_mux_21 : mux_16u_16u port map ( a(15)=>GND, a(14)=>GND, a(13)=>PWR, 
      a(12)=>PWR, a(11)=>GND, a(10)=>GND, a(9)=>GND, a(8)=>GND, a(7)=>switch, 
      a(6)=>NOT_nx77, a(5)=>switch, a(4)=>PWR, a(3)=>GND, a(2)=>nx70, a(1)=>
      GND, a(0)=>GND, b(15)=>GND, b(14)=>GND, b(13)=>GND, b(12)=>GND, b(11)
      =>GND, b(10)=>GND, b(9)=>GND, b(8)=>GND, b(7)=>GND, b(6)=>GND, b(5)=>
      GND, b(4)=>GND, b(3)=>state_3, b(2)=>state_2, b(1)=>state_1, b(0)=>
      state_0, d=>nextstate_2);
   modgen_mux_22 : mux_16u_16u port map ( a(15)=>PWR, a(14)=>GND, a(13)=>GND, 
      a(12)=>nx198, a(11)=>GND, a(10)=>GND, a(9)=>GND, a(8)=>GND, a(7)=>
      nx116, a(6)=>PWR, a(5)=>nx142, a(4)=>bad_encryption, a(3)=>switch, 
      a(2)=>switch, a(1)=>GND, a(0)=>nx401, b(15)=>GND, b(14)=>GND, b(13)=>
      GND, b(12)=>GND, b(11)=>GND, b(10)=>GND, b(9)=>GND, b(8)=>GND, b(7)=>
      GND, b(6)=>GND, b(5)=>GND, b(4)=>GND, b(3)=>state_3, b(2)=>state_2, 
      b(1)=>state_1, b(0)=>state_0, d=>nextstate_1);
   modgen_mux_23 : mux_16u_16u port map ( a(15)=>GND, a(14)=>GND, a(13)=>
      nx243, a(12)=>NOT_nx208, a(11)=>GND, a(10)=>GND, a(9)=>GND, a(8)=>GND, 
      a(7)=>nx118, a(6)=>nx92, a(5)=>nx144, a(4)=>NOT_nx171, a(3)=>nx57, 
      a(2)=>NOT_switch, a(1)=>NOT_switch, a(0)=>nx29, b(15)=>GND, b(14)=>GND, 
      b(13)=>GND, b(12)=>GND, b(11)=>GND, b(10)=>GND, b(9)=>GND, b(8)=>GND, 
      b(7)=>GND, b(6)=>GND, b(5)=>GND, b(4)=>GND, b(3)=>state_3, b(2)=>
      state_2, b(1)=>state_1, b(0)=>state_0, d=>nextstate_0);
   DFFPC (nextstate_3,GND,reset,clk,state_3) ;
   DFFPC (nextstate_2,GND,reset,clk,state_2) ;
   DFFPC (nextstate_1,GND,reset,clk,state_1) ;
   DFFPC (nextstate_0,GND,reset,clk,state_0) ;
   nx275 <= nx274 AND switch ;
   nx277 <= nx275 AND aes_key ;
   nx280 <= nx279 AND switch ;
   nx282 <= nx280 AND aes_key ;
   nx289 <= nx288 AND switch ;
   authentication_led <= nx289 AND authentication ;
   nx296 <= nx295 AND switch ;
   NOT_bad_encryption <= NOT bad_encryption ;
   nx300 <= nx296 AND NOT_bad_encryption ;
   nx303 <= nx300 AND nx182 ;
   nx306 <= nx305 AND NOT_bad_encryption ;
   NOT_acsc <= NOT acsc ;
   nx310 <= nx306 AND NOT_acsc ;
   nx315 <= nx310 AND NOT_nx121 ;
   NOT_reached_top <= NOT reached_top ;
   nx319 <= nx315 AND NOT_reached_top ;
   nx327 <= nx359 AND nx182 ;
   nx333 <= nx379 AND nx182 ;
   nx349 <= nx348 AND switch ;
   nx351 <= nx349 AND NOT_bad_encryption ;
   nx354 <= nx351 AND nx147 ;
   nx357 <= nx356 AND NOT_bad_encryption ;
   nx359 <= nx357 AND NOT_acsc ;
   nx364 <= nx359 AND NOT_nx95 ;
   NOT_reached_bottom <= NOT reached_bottom ;
   nx368 <= nx364 AND NOT_reached_bottom ;
   nx376 <= nx310 AND nx147 ;
   nx379 <= nx378 AND NOT_acsc ;
   nx382 <= nx379 AND nx147 ;
   nx401 <= nx403 OR nx404 ;
   modgen_select_46 : select_3_3 port map ( a(2)=>nx403, a(1)=>nx404, a(0)=>
      NOT_switch, b(2)=>GND, b(1)=>PWR, b(0)=>PWR, d=>nx29);
   nx451 <= bad_encryption OR nx147 ;
   nx468 <= NOT_switch OR bad_encryption ;
   modgen_select_73 : select_4_4 port map ( a(3)=>acsc, a(2)=>nx475, a(1)=>
      nx476, a(0)=>NOT_nx704, b(3)=>PWR, b(2)=>GND, b(1)=>PWR, b(0)=>PWR, d
      =>nx237);
   nx487 <= nx182 OR nx147 ;
   key_led <= nx277 OR nx282 ;
   nx541 <= bad_encryption OR acsc ;
   NOT_nx541 <= NOT nx541 ;
   nx150 <= NOT_nx541 AND nx147 ;
   nx547 <= nx541 OR nx147 ;
   NOT_nx547 <= NOT nx547 ;
   nx151 <= NOT_nx547 AND reached_top ;
   nx553 <= nx547 OR reached_top ;
   NOT_nx553 <= NOT nx553 ;
   nx185 <= NOT_nx541 AND nx182 ;
   nx565 <= nx541 OR nx182 ;
   NOT_nx565 <= NOT nx565 ;
   nx186 <= NOT_nx565 AND reached_bottom ;
   nx571 <= nx565 OR reached_bottom ;
   NOT_nx571 <= NOT nx571 ;
   nx577 <= nx303 OR nx319 ;
   nx579 <= nx577 OR nx327 ;
   open_light <= nx579 OR nx333 ;
   nx587 <= nx354 OR nx368 ;
   nx589 <= nx587 OR nx376 ;
   close_light <= nx589 OR nx382 ;
   nx475 <= NOT_acsc AND nx182 ;
   nx476 <= NOT_acsc AND nx147 ;
   nx702 <= acsc OR nx182 ;
   nx704 <= nx702 OR nx147 ;
   NOT_nx704 <= NOT nx704 ;
   NOT_nx77 <= NOT nx77 ;
   nx92 <= timeout AND NOT_nx77 ;
   NOT_nx95 <= NOT nx182 ;
   NOT_nx121 <= NOT nx147 ;
   NOT_nx487 <= NOT nx487 ;
   nx70 <= authentication AND switch ;
   nx116 <= nx650 AND switch ;
   nx138 <= nx451 AND switch ;
   nx142 <= bad_encryption AND switch ;
   nx650 <= NOT_nx95 OR bad_encryption ;
   nx118 <= NOT_nx95 OR nx468 ;
   nx144 <= NOT_nx121 OR nx468 ;
   nx243 <= NOT_nx487 OR acsc ;
   nx57 <= NOT_aes_key OR NOT_switch ;
   ix818 : decoder_2 port map ( data(1)=>command(0), data(0)=>command(1), 
      eq(3)=>nx77, eq(2)=>nx182, eq(1)=>nx147, eq(0)=>DANGLING(0));
   ix819 : decoder_4 port map ( data(3)=>state_3, data(2)=>state_2, data(1)
      =>state_1, data(0)=>state_0, eq(15)=>DANGLING(1), eq(14)=>DANGLING(2), 
      eq(13)=>nx378, eq(12)=>nx356, eq(11)=>DANGLING(3), eq(10)=>DANGLING(4), 
      eq(9)=>DANGLING(5), eq(8)=>DANGLING(6), eq(7)=>nx295, eq(6)=>DANGLING(
      7), eq(5)=>nx348, eq(4)=>nx305, eq(3)=>nx279, eq(2)=>nx288, eq(1)=>
      DANGLING(8), eq(0)=>nx274);
end SynthGray ;

