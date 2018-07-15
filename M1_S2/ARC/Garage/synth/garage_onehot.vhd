
-- 
-- Definition of  Garage
-- 
--      Wed 12 Apr 2017 02:51:04 PM CEST
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

architecture SynthOneHot of Garage is
   component select_11_11
      port (
         a : IN std_logic_vector (10 DOWNTO 0) ;
         b : IN std_logic_vector (10 DOWNTO 0) ;
         d : OUT std_logic) ;
   end component ;
   component select_10_10
      port (
         a : IN std_logic_vector (9 DOWNTO 0) ;
         b : IN std_logic_vector (9 DOWNTO 0) ;
         d : OUT std_logic) ;
   end component ;
   component select_9_9
      port (
         a : IN std_logic_vector (8 DOWNTO 0) ;
         b : IN std_logic_vector (8 DOWNTO 0) ;
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
   signal state_10, state_9, state_8, state_7, state_6, state_5, state_4, 
      state_3, state_2, state_1, state_0, nextstate_10, nextstate_9, 
      nextstate_8, nextstate_7, nextstate_6, nextstate_5, nextstate_4, 
      nextstate_3, nextstate_2, nextstate_1, nextstate_0, GND0, PWR, 
      NOT_aes_key, nx50, nx52, NOT_switch, nx160, nx170, nx203, nx266, nx327, 
      nx331, nx352, nx353, nx397, nx398, nx399, nx441, nx442, nx489, nx491, 
      nx493, nx513, nx516, nx519, nx521, nx610, nx612, nx614, nx616, nx622, 
      nx628, NOT_bad_encryption, nx632, nx635, nx637, NOT_acsc, nx641, 
      NOT_nx643, nx646, NOT_reached_top, nx650, nx657, nx662, nx677, nx679, 
      nx682, nx684, nx686, NOT_nx688, nx691, NOT_reached_bottom, nx695, 
      nx702, nx704, nx707, nx736, nx737, nx741, nx763, nx784, nx805, nx839, 
      nx872, nx884, nx916, nx929, nx930, NOT_nx741, NOT_nx763, NOT_nx784, 
      NOT_nx805, NOT_nx839, NOT_nx884, nx1231, NOT_nx1231, nx1237, 
      NOT_nx1237, nx1243, NOT_nx1243, nx1257, NOT_nx1257, nx1263, NOT_nx1263, 
      nx1269, nx1271, nx1279, nx1281, nx1385, nx1387, NOT_nx1387, NOT_nx170, 
      NOT_nx872, NOT_nx916, NOT_nx922: std_logic ;
   
   signal DANGLING : std_logic_vector (0 downto 0 );

begin
   PWR <= '1' ;
   GND0 <= '0' ;
   nx736 <= switch AND aes_key ;
   NOT_aes_key <= NOT aes_key ;
   nx737 <= switch AND NOT_aes_key ;
   NOT_switch <= NOT switch ;
   modgen_select_9 : select_11_11 port map ( a(10)=>state_0, a(9)=>state_1, 
      a(8)=>state_2, a(7)=>state_3, a(6)=>state_4, a(5)=>state_5, a(4)=>
      state_6, a(3)=>state_7, a(2)=>state_8, a(1)=>state_9, a(0)=>state_10, 
      b(10)=>GND0, b(9)=>GND0, b(8)=>GND0, b(7)=>GND0, b(6)=>GND0, b(5)=>
      nx327, b(4)=>nx327, b(3)=>bad_encryption, b(2)=>bad_encryption, b(1)=>
      GND0, b(0)=>GND0, d=>nextstate_10);
   modgen_select_10 : select_11_11 port map ( a(10)=>state_0, a(9)=>state_1, 
      a(8)=>state_2, a(7)=>state_3, a(6)=>state_4, a(5)=>state_5, a(4)=>
      state_6, a(3)=>state_7, a(2)=>state_8, a(1)=>state_9, a(0)=>state_10, 
      b(10)=>GND0, b(9)=>GND0, b(8)=>GND0, b(7)=>GND0, b(6)=>GND0, b(5)=>
      GND0, b(4)=>GND0, b(3)=>nx397, b(2)=>nx397, b(1)=>nx489, b(0)=>GND0, d
      =>nextstate_9);
   modgen_select_11 : select_11_11 port map ( a(10)=>state_0, a(9)=>state_1, 
      a(8)=>state_2, a(7)=>state_3, a(6)=>state_4, a(5)=>state_5, a(4)=>
      state_6, a(3)=>state_7, a(2)=>state_8, a(1)=>state_9, a(0)=>state_10, 
      b(10)=>GND0, b(9)=>GND0, b(8)=>GND0, b(7)=>GND0, b(6)=>GND0, b(5)=>
      GND0, b(4)=>nx331, b(3)=>nx352, b(2)=>NOT_nx1263, b(1)=>nx491, b(0)=>
      GND0, d=>nextstate_8);
   modgen_select_12 : select_11_11 port map ( a(10)=>state_0, a(9)=>state_1, 
      a(8)=>state_2, a(7)=>state_3, a(6)=>state_4, a(5)=>state_5, a(4)=>
      state_6, a(3)=>state_7, a(2)=>state_8, a(1)=>state_9, a(0)=>state_10, 
      b(10)=>GND0, b(9)=>GND0, b(8)=>GND0, b(7)=>GND0, b(6)=>GND0, b(5)=>
      nx266, b(4)=>GND0, b(3)=>NOT_nx1243, b(2)=>nx398, b(1)=>nx493, b(0)=>
      GND0, d=>nextstate_7);
   nx513 <= state_8 OR state_10 ;
   modgen_select_13 : select_10_10 port map ( a(9)=>state_0, a(8)=>state_1, 
      a(7)=>state_2, a(6)=>state_3, a(5)=>state_4, a(4)=>state_5, a(3)=>
      state_6, a(2)=>state_7, a(1)=>state_9, a(0)=>nx513, b(9)=>GND0, b(8)=>
      GND0, b(7)=>GND0, b(6)=>GND0, b(5)=>GND0, b(4)=>GND0, b(3)=>NOT_nx884, 
      b(2)=>nx353, b(1)=>GND0, b(0)=>GND0, d=>nextstate_6);
   nx516 <= state_7 OR state_10 ;
   modgen_select_14 : select_10_10 port map ( a(9)=>state_0, a(8)=>state_1, 
      a(7)=>state_2, a(6)=>state_3, a(5)=>state_4, a(4)=>state_5, a(3)=>
      state_6, a(2)=>state_8, a(1)=>state_9, a(0)=>nx516, b(9)=>GND0, b(8)=>
      GND0, b(7)=>GND0, b(6)=>GND0, b(5)=>nx203, b(4)=>NOT_nx839, b(3)=>GND0, 
      b(2)=>nx399, b(1)=>GND0, b(0)=>GND0, d=>nextstate_5);
   nx519 <= state_7 OR state_8 ;
   nx521 <= nx519 OR state_10 ;
   modgen_select_15 : select_9_9 port map ( a(8)=>state_0, a(7)=>state_1, 
      a(6)=>state_2, a(5)=>state_3, a(4)=>state_4, a(3)=>state_5, a(2)=>
      state_6, a(1)=>state_9, a(0)=>nx521, b(8)=>GND0, b(7)=>GND0, b(6)=>
      GND0, b(5)=>nx160, b(4)=>NOT_nx805, b(3)=>GND0, b(2)=>GND0, b(1)=>GND0, 
      b(0)=>GND0, d=>nextstate_4);
   modgen_select_16 : select_10_10 port map ( a(9)=>state_0, a(8)=>state_1, 
      a(7)=>state_2, a(6)=>state_3, a(5)=>state_4, a(4)=>state_5, a(3)=>
      state_6, a(2)=>state_9, a(1)=>state_10, a(0)=>nx519, b(9)=>nx50, b(8)
      =>GND0, b(7)=>nx736, b(6)=>NOT_nx784, b(5)=>nx170, b(4)=>GND0, b(3)=>
      GND0, b(2)=>GND0, b(1)=>PWR, b(0)=>GND0, d=>nextstate_3);
   modgen_select_17 : select_9_9 port map ( a(8)=>state_0, a(7)=>state_1, 
      a(6)=>state_2, a(5)=>state_3, a(4)=>state_4, a(3)=>state_5, a(2)=>
      state_6, a(1)=>state_9, a(0)=>nx521, b(8)=>nx52, b(7)=>GND0, b(6)=>
      NOT_nx763, b(5)=>GND0, b(4)=>GND0, b(3)=>GND0, b(2)=>GND0, b(1)=>GND0, 
      b(0)=>GND0, d=>nextstate_2);
   modgen_select_18 : select_9_9 port map ( a(8)=>state_0, a(7)=>state_1, 
      a(6)=>state_2, a(5)=>state_3, a(4)=>state_4, a(3)=>state_5, a(2)=>
      state_6, a(1)=>state_9, a(0)=>nx521, b(8)=>NOT_nx741, b(7)=>NOT_switch, 
      b(6)=>NOT_switch, b(5)=>NOT_switch, b(4)=>GND0, b(3)=>NOT_switch, b(2)
      =>NOT_switch, b(1)=>GND0, b(0)=>GND0, d=>nextstate_1);
   modgen_select_19 : select_9_9 port map ( a(8)=>state_0, a(7)=>state_1, 
      a(6)=>state_2, a(5)=>state_3, a(4)=>state_4, a(3)=>state_5, a(2)=>
      state_6, a(1)=>state_9, a(0)=>nx521, b(8)=>GND0, b(7)=>switch, b(6)=>
      GND0, b(5)=>GND0, b(4)=>GND0, b(3)=>GND0, b(2)=>GND0, b(1)=>GND0, b(0)
      =>GND0, d=>nextstate_0);
   DFFPC (nextstate_10,GND0,reset,clk,state_10) ;
   DFFPC (nextstate_9,GND0,reset,clk,state_9) ;
   DFFPC (nextstate_8,GND0,reset,clk,state_8) ;
   DFFPC (nextstate_7,GND0,reset,clk,state_7) ;
   DFFPC (nextstate_6,GND0,reset,clk,state_6) ;
   DFFPC (nextstate_5,GND0,reset,clk,state_5) ;
   DFFPC (nextstate_4,GND0,reset,clk,state_4) ;
   DFFPC (nextstate_3,GND0,reset,clk,state_3) ;
   DFFPC (nextstate_2,GND0,reset,clk,state_2) ;
   DFFPC (nextstate_1,GND0,reset,clk,state_1) ;
   DFFPC (nextstate_0,reset,GND0,clk,state_0) ;
   nx610 <= state_0 AND switch ;
   nx612 <= nx610 AND aes_key ;
   nx614 <= state_2 AND switch ;
   nx616 <= nx614 AND aes_key ;
   nx622 <= state_3 AND switch ;
   authentication_led <= nx622 AND authentication ;
   nx628 <= state_5 AND switch ;
   NOT_bad_encryption <= NOT bad_encryption ;
   nx632 <= nx628 AND NOT_bad_encryption ;
   nx635 <= nx632 AND nx441 ;
   nx637 <= state_7 AND NOT_bad_encryption ;
   NOT_acsc <= NOT acsc ;
   nx641 <= nx637 AND NOT_acsc ;
   NOT_nx643 <= NOT nx442 ;
   nx646 <= nx641 AND NOT_nx643 ;
   NOT_reached_top <= NOT reached_top ;
   nx650 <= nx646 AND NOT_reached_top ;
   nx657 <= nx686 AND nx441 ;
   nx662 <= nx704 AND nx441 ;
   nx677 <= state_6 AND switch ;
   nx679 <= nx677 AND NOT_bad_encryption ;
   nx682 <= nx679 AND nx442 ;
   nx684 <= state_8 AND NOT_bad_encryption ;
   nx686 <= nx684 AND NOT_acsc ;
   NOT_nx688 <= NOT nx441 ;
   nx691 <= nx686 AND NOT_nx688 ;
   NOT_reached_bottom <= NOT reached_bottom ;
   nx695 <= nx691 AND NOT_reached_bottom ;
   nx702 <= nx641 AND nx442 ;
   nx704 <= state_9 AND NOT_acsc ;
   nx707 <= nx704 AND nx442 ;
   modgen_select_31 : select_3_3 port map ( a(2)=>nx736, a(1)=>nx737, a(0)=>
      NOT_switch, b(2)=>PWR, b(1)=>GND0, b(0)=>GND0, d=>nx50);
   modgen_select_32 : select_3_3 port map ( a(2)=>nx736, a(1)=>nx737, a(0)=>
      NOT_switch, b(2)=>GND0, b(1)=>PWR, b(0)=>GND0, d=>nx52);
   nx741 <= nx736 OR nx737 ;
   nx763 <= NOT_switch OR aes_key ;
   nx784 <= NOT_switch OR authentication ;
   nx805 <= nx170 OR timeout ;
   nx839 <= nx872 OR nx441 ;
   nx872 <= NOT_switch OR bad_encryption ;
   nx884 <= nx872 OR nx442 ;
   nx916 <= nx441 OR nx442 ;
   modgen_select_59 : select_4_4 port map ( a(3)=>acsc, a(2)=>nx929, a(1)=>
      nx930, a(0)=>NOT_nx1387, b(3)=>GND0, b(2)=>PWR, b(1)=>GND0, b(0)=>GND0, 
      d=>nx493);
   key_led <= nx612 OR nx616 ;
   NOT_nx741 <= NOT nx741 ;
   NOT_nx763 <= NOT nx763 ;
   NOT_nx784 <= NOT nx784 ;
   NOT_nx805 <= NOT nx805 ;
   NOT_nx839 <= NOT nx839 ;
   NOT_nx884 <= NOT nx884 ;
   nx1231 <= bad_encryption OR acsc ;
   NOT_nx1231 <= NOT nx1231 ;
   nx352 <= NOT_nx1231 AND nx442 ;
   nx1237 <= nx1231 OR nx442 ;
   NOT_nx1237 <= NOT nx1237 ;
   nx353 <= NOT_nx1237 AND reached_top ;
   nx1243 <= nx1237 OR reached_top ;
   NOT_nx1243 <= NOT nx1243 ;
   nx397 <= NOT_bad_encryption AND acsc ;
   nx398 <= NOT_nx1231 AND nx441 ;
   nx1257 <= nx1231 OR nx441 ;
   NOT_nx1257 <= NOT nx1257 ;
   nx399 <= NOT_nx1257 AND reached_bottom ;
   nx1263 <= nx1257 OR reached_bottom ;
   NOT_nx1263 <= NOT nx1263 ;
   nx1269 <= nx635 OR nx650 ;
   nx1271 <= nx1269 OR nx657 ;
   open_light <= nx1271 OR nx662 ;
   nx1279 <= nx682 OR nx695 ;
   nx1281 <= nx1279 OR nx702 ;
   close_light <= nx1281 OR nx707 ;
   nx929 <= NOT_acsc AND nx441 ;
   nx930 <= NOT_acsc AND nx442 ;
   nx1385 <= acsc OR nx441 ;
   nx1387 <= nx1385 OR nx442 ;
   NOT_nx1387 <= NOT nx1387 ;
   NOT_nx170 <= NOT nx170 ;
   nx203 <= timeout AND NOT_nx170 ;
   nx266 <= nx441 AND NOT_nx872 ;
   NOT_nx872 <= NOT nx872 ;
   nx331 <= nx442 AND NOT_nx872 ;
   NOT_nx916 <= NOT nx916 ;
   NOT_nx922 <= NOT nx1385 ;
   nx491 <= nx442 AND NOT_nx922 ;
   nx160 <= authentication AND switch ;
   nx327 <= bad_encryption AND switch ;
   nx489 <= NOT_nx916 OR acsc ;
   ix1491 : decoder_2 port map ( data(1)=>command(0), data(0)=>command(1), 
      eq(3)=>nx170, eq(2)=>nx441, eq(1)=>nx442, eq(0)=>DANGLING(0));
end SynthOneHot ;

