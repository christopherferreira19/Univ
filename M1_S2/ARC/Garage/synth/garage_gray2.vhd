
-- 
-- Definition of  Garage
-- 
--      Wed 12 Apr 2017 04:58:22 PM CEST
--      
--      LeonardoSpectrum Level 3, 2015a.6
-- 

library IEEE;
use IEEE.STD_LOGIC_1164.all;

library c35_CORELIB;
use c35_CORELIB.vcomponents.all;

architecture SynthGray2 of Garage is
   signal state_0, state_3, nx6, state_2, state_1, nx127, nx14, nx28, nx32, 
      nx40, nx44, nx129, nx131, nx58, nx62, nx66, nx84, nx102, nx106, nx118, 
      nx120, nx126, nx134, nx144, nx152, nx156, nx162, nx176, nx182, nx200, 
      nx216, nx242, nx254, nx274, nx280, nx300, nx320, nx145, nx147, nx149, 
      nx153, nx157, nx161, nx163, nx166, nx168, nx172, nx177, nx183, nx189, 
      nx193, nx197, nx199, nx201, nx203, nx205, nx209, nx211, nx215, nx219, 
      nx223, nx227, nx229, nx233, nx235, nx239, nx241, nx245, nx249, nx251, 
      nx255, nx257, nx260, nx262, nx265, nx273, nx277, nx281, nx285, nx291, 
      nx297, nx301, nx305, nx311: std_logic ;

begin
   ix295 : OAI211 port map ( Q=>close_light, A=>nx118, B=>nx147, C=>nx297);
   ix146 : CLKIN1 port map ( Q=>nx145, A=>command(0));
   ix148 : AOI2111 port map ( Q=>nx147, A=>nx149, B=>nx144, C=>nx280, D=>
      nx274);
   ix150 : CLKIN1 port map ( Q=>nx149, A=>acsc);
   ix145 : NOR21 port map ( Q=>nx144, A=>nx153, B=>nx168);
   ix255 : NAND31 port map ( Q=>nx254, A=>nx157, B=>nx265, C=>nx273);
   ix158 : AOI221 port map ( Q=>nx157, A=>reached_bottom, B=>nx134, C=>nx203, 
      D=>nx242);
   ix135 : AOI2111 port map ( Q=>nx134, A=>nx161, B=>nx163, C=>state_0, D=>
      nx168);
   ix162 : NOR21 port map ( Q=>nx161, A=>command(1), B=>nx145);
   ix164 : NOR21 port map ( Q=>nx163, A=>bad_encryption, B=>acsc);
   reg_state_0 : DFC1 port map ( Q=>state_0, QN=>nx153, C=>clk, D=>nx254, RN
      =>nx166);
   ix167 : CLKIN1 port map ( Q=>nx166, A=>reset);
   ix169 : NAND21 port map ( Q=>nx168, A=>state_3, B=>nx129);
   ix163 : OAI2111 port map ( Q=>nx162, A=>nx172, B=>nx177, C=>nx235, D=>
      nx249);
   ix174 : NAND21 port map ( Q=>nx172, A=>switch, B=>nx6);
   ix7 : NOR21 port map ( Q=>nx6, A=>state_3, B=>nx153);
   ix178 : NAND21 port map ( Q=>nx177, A=>bad_encryption, B=>state_2);
   ix107 : NAND41 port map ( Q=>nx106, A=>nx168, B=>nx183, C=>nx229, D=>
      nx233);
   ix184 : NAND41 port map ( Q=>nx183, A=>authentication, B=>state_1, C=>
      nx131, D=>nx58);
   ix67 : NAND31 port map ( Q=>nx66, A=>nx189, B=>nx205, C=>nx219);
   ix190 : AOI311 port map ( Q=>nx189, A=>nx129, B=>bad_encryption, C=>nx153, 
      D=>nx62);
   ix75 : NOR21 port map ( Q=>nx129, A=>nx193, B=>state_1);
   reg_state_2 : DFC1 port map ( Q=>state_2, QN=>nx193, C=>clk, D=>nx106, RN
      =>nx166);
   ix63 : AOI211 port map ( Q=>nx62, A=>nx197, B=>nx199, C=>nx201);
   ix198 : NAND21 port map ( Q=>nx197, A=>switch, B=>nx193);
   ix200 : NAND21 port map ( Q=>nx199, A=>state_2, B=>state_1);
   reg_state_3 : DFC1 port map ( Q=>state_3, QN=>nx203, C=>clk, D=>nx162, RN
      =>nx166);
   ix206 : AOI311 port map ( Q=>nx205, A=>nx32, B=>switch, C=>nx203, D=>nx44
   );
   ix33 : OAI211 port map ( Q=>nx32, A=>nx209, B=>nx211, C=>nx177);
   reg_state_1 : DFC1 port map ( Q=>state_1, QN=>nx209, C=>clk, D=>nx66, RN
      =>nx166);
   ix216 : CLKIN1 port map ( Q=>nx215, A=>command(1));
   ix45 : NOR40 port map ( Q=>nx44, A=>nx193, B=>nx209, C=>nx203, D=>nx153);
   ix220 : NAND31 port map ( Q=>nx219, A=>nx127, B=>reached_bottom, C=>nx223
   );
   ix263 : NOR21 port map ( Q=>nx127, A=>state_0, B=>nx168);
   ix224 : NOR31 port map ( Q=>nx223, A=>nx161, B=>bad_encryption, C=>acsc);
   ix173 : NOR21 port map ( Q=>nx131, A=>state_3, B=>state_0);
   ix228 : CLKIN1 port map ( Q=>nx227, A=>switch);
   ix230 : AOI221 port map ( Q=>nx229, A=>nx131, B=>nx211, C=>state_2, D=>
      nx84);
   ix85 : OAI211 port map ( Q=>nx84, A=>command(0), B=>nx201, C=>nx172);
   ix234 : NAND21 port map ( Q=>nx233, A=>nx153, B=>nx129);
   ix236 : AOI2111 port map ( Q=>nx235, A=>nx14, B=>nx144, C=>nx156, D=>
      nx152);
   ix157 : NOR21 port map ( Q=>nx156, A=>nx239, B=>nx241);
   ix240 : CLKIN1 port map ( Q=>nx239, A=>bad_encryption);
   ix242 : NAND21 port map ( Q=>nx241, A=>nx129, B=>nx131);
   ix153 : OAI311 port map ( Q=>nx152, A=>nx163, B=>state_0, C=>nx168, D=>
      nx245);
   ix246 : OAI211 port map ( Q=>nx245, A=>nx144, B=>nx120, C=>acsc);
   ix250 : AOI221 port map ( Q=>nx249, A=>nx251, B=>nx126, C=>nx257, D=>
      nx134);
   ix252 : NOR21 port map ( Q=>nx251, A=>nx215, B=>command(0));
   ix127 : OAI311 port map ( Q=>nx126, A=>nx255, B=>nx227, C=>nx153, D=>
      nx241);
   ix258 : CLKIN1 port map ( Q=>nx257, A=>reached_bottom);
   ix261 : AOI211 port map ( Q=>nx260, A=>reached_top, B=>nx118, C=>
      bad_encryption);
   ix264 : NAND21 port map ( Q=>nx262, A=>nx145, B=>timeout);
   ix274 : NOR40 port map ( Q=>nx273, A=>nx216, B=>nx200, C=>nx182, D=>nx176
   );
   ix217 : AOI311 port map ( Q=>nx216, A=>nx177, B=>switch, C=>nx277, D=>
      nx281);
   ix278 : AOI221 port map ( Q=>nx277, A=>nx215, B=>nx129, C=>nx14, D=>nx40
   );
   ix201 : NOR31 port map ( Q=>nx200, A=>nx285, B=>aes_key, C=>state_2);
   ix183 : NOR40 port map ( Q=>nx182, A=>nx153, B=>nx255, C=>nx251, D=>nx161
   );
   ix177 : NOR40 port map ( Q=>nx176, A=>command(1), B=>nx291, C=>nx201, D=>
      nx199);
   ix292 : CLKIN1 port map ( Q=>nx291, A=>timeout);
   ix281 : NOR40 port map ( Q=>nx280, A=>nx172, B=>bad_encryption, C=>nx193, 
      D=>state_1);
   ix275 : NOR31 port map ( Q=>nx274, A=>bad_encryption, B=>acsc, C=>nx241);
   ix298 : NAND31 port map ( Q=>nx297, A=>nx127, B=>nx257, C=>nx223);
   ix333 : OAI311 port map ( Q=>open_light, A=>bad_encryption, B=>acsc, C=>
      nx301, D=>nx305);
   ix302 : AOI211 port map ( Q=>nx301, A=>nx102, B=>nx161, C=>nx300);
   ix301 : NOR31 port map ( Q=>nx300, A=>nx241, B=>reached_top, C=>nx251);
   ix306 : AOI311 port map ( Q=>nx305, A=>nx144, B=>nx149, C=>nx161, D=>
      nx320);
   ix321 : NOR40 port map ( Q=>nx320, A=>bad_encryption, B=>nx209, C=>nx28, 
      D=>nx172);
   ix29 : NAND21 port map ( Q=>nx28, A=>nx161, B=>state_2);
   ix337 : NOR40 port map ( Q=>key_led, A=>nx227, B=>state_2, C=>nx311, D=>
      nx285);
   ix312 : CLKIN1 port map ( Q=>nx311, A=>aes_key);
   ix266 : CLKIN1 port map ( Q=>nx265, A=>nx152);
   ix121 : CLKIN1 port map ( Q=>nx120, A=>nx241);
   ix119 : CLKIN1 port map ( Q=>nx118, A=>nx251);
   ix103 : CLKIN1 port map ( Q=>nx102, A=>nx168);
   ix59 : CLKIN1 port map ( Q=>nx58, A=>nx197);
   ix202 : CLKIN1 port map ( Q=>nx201, A=>nx131);
   ix256 : CLKIN1 port map ( Q=>nx255, A=>nx129);
   ix41 : CLKIN1 port map ( Q=>nx40, A=>nx199);
   ix212 : CLKIN1 port map ( Q=>nx211, A=>nx28);
   ix15 : CLKIN1 port map ( Q=>nx14, A=>nx161);
   ix282 : CLKIN1 port map ( Q=>nx281, A=>nx6);
   ix101 : CLKIN1 port map ( Q=>authentication_led, A=>nx183);
   ix243 : IMUX30 port map ( Q=>nx242, A=>nx262, B=>nx260, C=>switch, S0=>
      nx209, S1=>nx193);
   ix286 : IMUX21 port map ( Q=>nx285, A=>nx6, B=>nx131, S=>nx209);
end SynthGray2 ;

