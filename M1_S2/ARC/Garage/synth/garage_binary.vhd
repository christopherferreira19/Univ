
-- 
-- Definition of  Garage
-- 
--      Wed 12 Apr 2017 02:48:27 PM CEST
--      
--      LeonardoSpectrum Level 3, 2015a.6
-- 

library IEEE;
use IEEE.STD_LOGIC_1164.all;

library c35_CORELIB;
use c35_CORELIB.vcomponents.all;

architecture SynthBinary of Garage is
   signal nx2, state_1, nx8, state_2, nx909, state_0, state_3, nx910, nx26, 
      nx44, nx50, nx54, nx911, nx60, nx82, nx90, nx102, nx118, nx126, nx150, 
      nx162, nx182, nx188, nx190, nx194, nx200, nx206, nx222, nx230, nx232, 
      nx912, nx252, nx266, nx280, nx290, nx298, nx312, nx328, nx344, nx370, 
      nx921, nx923, nx925, nx928, nx931, nx934, nx937, nx939, nx941, nx943, 
      nx946, nx949, nx953, nx957, nx961, nx964, nx966, nx968, nx970, nx973, 
      nx976, nx978, nx980, nx983, nx985, nx987, nx989, nx994, nx997, nx1002, 
      nx1004, nx1007, nx1011, nx1014, nx1018, nx1021, nx1024, nx1026, nx1029, 
      nx1032, nx1035, nx1037, nx1039, nx1041, nx1045, nx1049, nx1052, nx1055
   : std_logic ;

begin
   ix361 : OAI221 port map ( Q=>close_light, A=>nx2, B=>nx923, C=>nx44, D=>
      nx964);
   ix3 : NAND21 port map ( Q=>nx2, A=>command(1), B=>nx921);
   ix922 : CLKIN1 port map ( Q=>nx921, A=>command(0));
   ix924 : AOI211 port map ( Q=>nx923, A=>nx925, B=>nx910, C=>nx344);
   ix926 : CLKIN1 port map ( Q=>nx925, A=>acsc);
   ix105 : NOR40 port map ( Q=>nx910, A=>nx928, B=>state_1, C=>nx957, D=>
      state_2);
   ix207 : NAND41 port map ( Q=>nx206, A=>nx931, B=>nx1032, C=>nx1041, D=>
      nx1045);
   ix932 : NOR31 port map ( Q=>nx931, A=>nx200, B=>nx190, C=>nx182);
   ix201 : NOR40 port map ( Q=>nx200, A=>nx928, B=>nx934, C=>bad_encryption, 
      D=>nx925);
   ix253 : NAND41 port map ( Q=>nx252, A=>nx937, B=>nx941, C=>nx1011, D=>
      nx1014);
   ix938 : NAND31 port map ( Q=>nx937, A=>nx910, B=>nx925, C=>nx939);
   ix940 : NOR21 port map ( Q=>nx939, A=>command(1), B=>nx921);
   ix942 : NAND31 port map ( Q=>nx941, A=>nx2, B=>nx943, C=>nx54);
   ix944 : NOR21 port map ( Q=>nx943, A=>bad_encryption, B=>acsc);
   ix55 : NOR21 port map ( Q=>nx54, A=>nx946, B=>nx50);
   ix329 : NAND41 port map ( Q=>nx328, A=>nx937, B=>nx949, C=>nx985, D=>
      nx994);
   ix950 : AOI311 port map ( Q=>nx949, A=>nx162, B=>command(0), C=>
      command(1), D=>nx188);
   ix163 : NOR31 port map ( Q=>nx162, A=>state_0, B=>nx934, C=>state_1);
   reg_state_0 : DFC1 port map ( Q=>state_0, QN=>nx928, C=>clk, D=>nx206, RN
      =>nx953);
   ix954 : CLKIN1 port map ( Q=>nx953, A=>reset);
   reg_state_1 : DFC1 port map ( Q=>state_1, QN=>nx946, C=>clk, D=>nx328, RN
      =>nx953);
   ix189 : NOR40 port map ( Q=>nx188, A=>nx957, B=>state_2, C=>nx946, D=>
      state_0);
   reg_state_3 : DFC1 port map ( Q=>state_3, QN=>nx957, C=>clk, D=>nx90, RN
      =>nx953);
   ix91 : OAI2111 port map ( Q=>nx90, A=>nx8, B=>nx961, C=>nx964, D=>nx973);
   ix9 : NAND21 port map ( Q=>nx8, A=>switch, B=>bad_encryption);
   ix962 : NAND31 port map ( Q=>nx961, A=>nx946, B=>state_0, C=>state_2);
   reg_state_2 : DFC1 port map ( Q=>state_2, QN=>nx934, C=>clk, D=>nx252, RN
      =>nx953);
   ix965 : AOI221 port map ( Q=>nx964, A=>nx54, B=>nx966, C=>nx970, D=>nx911
   );
   ix967 : NOR21 port map ( Q=>nx966, A=>nx968, B=>command(0));
   ix969 : CLKIN1 port map ( Q=>nx968, A=>command(1));
   ix971 : NOR21 port map ( Q=>nx970, A=>nx939, B=>reached_bottom);
   ix215 : NOR40 port map ( Q=>nx911, A=>state_0, B=>state_1, C=>nx957, D=>
      state_2);
   ix974 : AOI211 port map ( Q=>nx973, A=>nx910, B=>nx937, C=>nx60);
   ix61 : OAI311 port map ( Q=>nx60, A=>nx976, B=>nx934, C=>nx978, D=>nx980
   );
   ix977 : NAND21 port map ( Q=>nx976, A=>state_1, B=>nx928);
   ix979 : OAI211 port map ( Q=>nx978, A=>nx966, B=>bad_encryption, C=>
      switch);
   ix981 : OAI211 port map ( Q=>nx980, A=>nx911, B=>nx54, C=>nx44);
   ix45 : NAND21 port map ( Q=>nx44, A=>nx983, B=>nx925);
   ix984 : CLKIN1 port map ( Q=>nx983, A=>bad_encryption);
   ix986 : AOI221 port map ( Q=>nx985, A=>nx987, B=>nx82, C=>nx2, D=>nx909);
   ix988 : NOR21 port map ( Q=>nx987, A=>nx989, B=>nx983);
   ix990 : CLKIN1 port map ( Q=>nx989, A=>switch);
   ix83 : NOR21 port map ( Q=>nx82, A=>state_1, B=>nx50);
   ix51 : NAND21 port map ( Q=>nx50, A=>state_0, B=>state_2);
   ix339 : NOR21 port map ( Q=>nx909, A=>nx989, B=>nx976);
   ix995 : NOR31 port map ( Q=>nx994, A=>nx312, B=>nx280, C=>nx266);
   ix313 : OAI221 port map ( Q=>nx312, A=>state_0, B=>nx997, C=>nx26, D=>
      nx1004);
   ix998 : AOI311 port map ( Q=>nx997, A=>state_3, B=>nx934, C=>nx298, D=>
      nx290);
   ix299 : OAI211 port map ( Q=>nx298, A=>acsc, B=>nx26, C=>nx983);
   ix27 : NAND21 port map ( Q=>nx26, A=>nx968, B=>command(0));
   ix291 : NOR31 port map ( Q=>nx290, A=>nx1002, B=>state_3, C=>state_1);
   ix1003 : NAND21 port map ( Q=>nx1002, A=>switch, B=>nx934);
   ix1005 : NAND21 port map ( Q=>nx1004, A=>switch, B=>nx82);
   ix281 : OAI311 port map ( Q=>nx280, A=>nx1002, B=>authentication, C=>
      nx946, D=>nx1007);
   ix1008 : OAI2111 port map ( Q=>nx1007, A=>nx2, B=>nx44, C=>nx54, D=>nx194
   );
   ix195 : NAND21 port map ( Q=>nx194, A=>nx983, B=>acsc);
   ix267 : AOI211 port map ( Q=>nx266, A=>nx1002, B=>nx8, C=>nx976);
   ix1012 : NAND41 port map ( Q=>nx1011, A=>authentication, B=>state_1, C=>
      state_0, D=>nx912);
   ix261 : NOR21 port map ( Q=>nx912, A=>nx989, B=>state_2);
   ix1015 : AOI2111 port map ( Q=>nx1014, A=>nx983, B=>nx232, C=>nx230, D=>
      nx222);
   ix233 : NOR40 port map ( Q=>nx232, A=>nx989, B=>state_1, C=>nx928, D=>
      nx934);
   ix231 : AOI211 port map ( Q=>nx230, A=>command(1), B=>command(0), C=>
      nx1018);
   ix1019 : NAND31 port map ( Q=>nx1018, A=>nx928, B=>state_2, C=>nx946);
   ix223 : OAI311 port map ( Q=>nx222, A=>nx1021, B=>nx44, C=>nx970, D=>
      nx1026);
   ix1022 : NAND21 port map ( Q=>nx1021, A=>nx928, B=>nx102);
   ix103 : NOR21 port map ( Q=>nx102, A=>state_1, B=>nx1024);
   ix1025 : NAND21 port map ( Q=>nx1024, A=>state_3, B=>nx934);
   ix1027 : NAND31 port map ( Q=>nx1026, A=>nx909, B=>state_2, C=>nx978);
   ix191 : OAI211 port map ( Q=>nx190, A=>nx1024, B=>nx976, C=>nx1029);
   ix1030 : NAND31 port map ( Q=>nx1029, A=>nx162, B=>command(0), C=>
      command(1));
   ix183 : OAI221 port map ( Q=>nx182, A=>reached_top, B=>nx941, C=>nx987, D
      =>nx961);
   ix1033 : AOI221 port map ( Q=>nx1032, A=>timeout, B=>nx162, C=>nx934, D=>
      nx150);
   ix151 : OAI211 port map ( Q=>nx150, A=>state_3, B=>nx1035, C=>nx1037);
   ix1036 : AOI211 port map ( Q=>nx1035, A=>aes_key, B=>nx928, C=>nx989);
   ix1038 : NAND31 port map ( Q=>nx1037, A=>state_0, B=>nx1039, C=>state_1);
   ix1040 : CLKIN1 port map ( Q=>nx1039, A=>authentication);
   ix1042 : AOI311 port map ( Q=>nx1041, A=>nx983, B=>nx928, C=>nx118, D=>
      nx126);
   ix119 : AOI211 port map ( Q=>nx118, A=>nx970, B=>nx943, C=>nx1024);
   ix127 : AOI2111 port map ( Q=>nx126, A=>state_0, B=>state_2, C=>switch, D
      =>nx946);
   ix1046 : OAI211 port map ( Q=>nx1045, A=>nx2, B=>acsc, C=>nx910);
   ix345 : NOR40 port map ( Q=>nx344, A=>nx989, B=>nx976, C=>bad_encryption, 
      D=>nx934);
   ix383 : OAI2111 port map ( Q=>open_light, A=>reached_top, B=>nx941, C=>
      nx1049, D=>nx937);
   ix1050 : NAND21 port map ( Q=>nx1049, A=>nx939, B=>nx370);
   ix371 : OAI221 port map ( Q=>nx370, A=>bad_encryption, B=>nx1004, C=>nx44, 
      D=>nx1052);
   ix1053 : NAND31 port map ( Q=>nx1052, A=>nx946, B=>state_3, C=>nx934);
   ix393 : NOR40 port map ( Q=>key_led, A=>nx1055, B=>state_3, C=>state_0, D
      =>nx1002);
   ix1056 : CLKIN1 port map ( Q=>nx1055, A=>aes_key);
   ix247 : NOR40 port map ( Q=>authentication_led, A=>nx1039, B=>nx946, C=>
      nx928, D=>nx1002);
end SynthBinary ;

