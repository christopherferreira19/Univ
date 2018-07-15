
-- 
-- Definition of  Garage
-- 
--      Wed 12 Apr 2017 05:04:08 PM CEST
--      
--      LeonardoSpectrum Level 3, 2015a.6
-- 

library IEEE;
use IEEE.STD_LOGIC_1164.all;

library c35_CORELIB;
use c35_CORELIB.vcomponents.all;

architecture SynthOneHot2 of Garage is
   signal key_led_EXMPLR, open_light_EXMPLR, close_light_EXMPLR, nx16, 
      state_7, nx1581, state_5, nx36, state_4, state_3, state_10, state_6, 
      nx54, nx60, nx74, state_2, nx84, state_0, nx100, nx106, nx120, nx136, 
      nx148, nx156, nx1583, nx184, nx200, nx220, nx1591, nx1593, nx1595, 
      nx1597, nx1600, nx1603, nx1606, nx1608, nx1610, nx1614, nx1617, nx1620, 
      nx1623, nx1625, nx1628, nx1630, nx1634, nx1636, nx1639, nx1641, nx1643, 
      nx1645, nx1648, nx1652, nx1654, nx1659, nx1661, nx1665, nx1668, nx1672, 
      nx1675, nx1681, nx1685, nx1688, nx1691: std_logic ;

begin
   key_led <= key_led_EXMPLR ;
   open_light <= open_light_EXMPLR ;
   close_light <= close_light_EXMPLR ;
   ix229 : OAI311 port map ( Q=>close_light_EXMPLR, A=>reached_bottom, B=>
      nx1591, C=>nx1595, D=>nx1600);
   reg_state_8 : DFC1 port map ( Q=>OPEN, QN=>nx1591, C=>clk, D=>
      close_light_EXMPLR, RN=>nx1593);
   ix1594 : CLKIN1 port map ( Q=>nx1593, A=>reset);
   ix1596 : OAI211 port map ( Q=>nx1595, A=>command(1), B=>nx1597, C=>nx16);
   ix1598 : CLKIN1 port map ( Q=>nx1597, A=>command(0));
   ix17 : NOR21 port map ( Q=>nx16, A=>acsc, B=>bad_encryption);
   ix1601 : OAI211 port map ( Q=>nx1600, A=>nx220, B=>nx1581, C=>nx1606);
   ix221 : OAI221 port map ( Q=>nx220, A=>nx1603, B=>nx1691, C=>acsc, D=>
      nx1639);
   ix61 : NOR21 port map ( Q=>nx60, A=>nx1606, B=>nx1610);
   ix1607 : NOR21 port map ( Q=>nx1606, A=>nx1608, B=>command(0));
   ix1609 : CLKIN1 port map ( Q=>nx1608, A=>command(1));
   ix1611 : AOI221 port map ( Q=>nx1610, A=>state_6, B=>nx54, C=>reached_top, 
      D=>nx1581);
   reg_state_6 : DFC1 port map ( Q=>state_6, QN=>nx1603, C=>clk, D=>nx60, RN
      =>nx1593);
   ix55 : NOR21 port map ( Q=>nx54, A=>bad_encryption, B=>nx1614);
   ix1615 : CLKIN1 port map ( Q=>nx1614, A=>switch);
   ix207 : OAI311 port map ( Q=>open_light_EXMPLR, A=>nx1620, B=>reached_top, 
      C=>nx1606, D=>nx1623);
   ix1621 : NAND21 port map ( Q=>nx1620, A=>state_7, B=>nx16);
   reg_state_7 : DFC1 port map ( Q=>state_7, QN=>nx1617, C=>clk, D=>
      open_light_EXMPLR, RN=>nx1593);
   ix1624 : NAND21 port map ( Q=>nx1623, A=>nx1625, B=>nx200);
   ix1626 : NOR21 port map ( Q=>nx1625, A=>command(1), B=>nx1597);
   ix201 : OAI221 port map ( Q=>nx200, A=>acsc, B=>nx1628, C=>nx1645, D=>
      nx1691);
   ix1631 : CLKIN1 port map ( Q=>nx1630, A=>bad_encryption);
   ix185 : OAI221 port map ( Q=>nx184, A=>nx1634, B=>nx1636, C=>nx1639, D=>
      nx1641);
   ix1635 : CLKIN1 port map ( Q=>nx1634, A=>acsc);
   ix1637 : AOI211 port map ( Q=>nx1636, A=>nx1630, B=>state_7, C=>nx1583);
   ix195 : OAI211 port map ( Q=>nx1583, A=>bad_encryption, B=>nx1591, C=>
      nx1639);
   reg_state_9 : DFC1 port map ( Q=>OPEN, QN=>nx1639, C=>clk, D=>nx184, RN=>
      nx1593);
   ix1642 : AOI211 port map ( Q=>nx1641, A=>nx1597, B=>nx1608, C=>nx1643);
   ix157 : OAI211 port map ( Q=>nx156, A=>nx1625, B=>nx1648, C=>nx1654);
   ix1649 : AOI211 port map ( Q=>nx1648, A=>state_5, B=>nx54, C=>nx148);
   reg_state_5 : DFC1 port map ( Q=>state_5, QN=>nx1645, C=>clk, D=>nx156, 
      RN=>nx1593);
   ix149 : NOR40 port map ( Q=>nx148, A=>acsc, B=>bad_encryption, C=>nx1652, 
      D=>nx1591);
   ix1653 : CLKIN1 port map ( Q=>nx1652, A=>reached_bottom);
   ix1655 : NAND31 port map ( Q=>nx1654, A=>nx36, B=>timeout, C=>state_4);
   ix37 : NAND21 port map ( Q=>nx36, A=>command(1), B=>command(0));
   ix137 : OAI311 port map ( Q=>nx136, A=>nx1643, B=>timeout, C=>nx1659, D=>
      nx1661);
   reg_state_4 : DFC1 port map ( Q=>state_4, QN=>nx1659, C=>clk, D=>nx136, 
      RN=>nx1593);
   ix1662 : NAND31 port map ( Q=>nx1661, A=>state_3, B=>authentication, C=>
      switch);
   reg_state_3 : DFC1 port map ( Q=>state_3, QN=>OPEN, C=>clk, D=>nx120, RN
      =>nx1593);
   ix121 : OAI2111 port map ( Q=>nx120, A=>nx1659, B=>nx36, C=>nx1665, D=>
      nx1688);
   ix1666 : NOR21 port map ( Q=>nx1665, A=>key_led_EXMPLR, B=>state_10);
   ix117 : NOR31 port map ( Q=>key_led_EXMPLR, A=>nx1668, B=>nx1681, C=>
      nx1614);
   ix1669 : NOR21 port map ( Q=>nx1668, A=>state_0, B=>state_2);
   reg_state_0 : DFP1 port map ( Q=>state_0, QN=>OPEN, C=>clk, D=>nx106, SN
      =>nx1593);
   ix107 : NOR21 port map ( Q=>nx106, A=>nx1614, B=>nx1672);
   reg_state_1 : DFC1 port map ( Q=>OPEN, QN=>nx1672, C=>clk, D=>nx100, RN=>
      nx1593);
   ix101 : AOI311 port map ( Q=>nx100, A=>nx1668, B=>nx1672, C=>nx1675, D=>
      switch);
   ix1676 : NOR31 port map ( Q=>nx1675, A=>state_5, B=>state_6, C=>state_3);
   reg_state_2 : DFC1 port map ( Q=>state_2, QN=>OPEN, C=>clk, D=>nx84, RN=>
      nx1593);
   ix85 : NOR31 port map ( Q=>nx84, A=>nx1668, B=>aes_key, C=>nx1614);
   ix1682 : CLKIN1 port map ( Q=>nx1681, A=>aes_key);
   reg_state_10 : DFC1 port map ( Q=>state_10, QN=>OPEN, C=>clk, D=>nx74, RN
      =>nx1593);
   ix75 : AOI311 port map ( Q=>nx74, A=>nx1685, B=>nx1617, C=>nx1591, D=>
      nx1630);
   ix1686 : OAI211 port map ( Q=>nx1685, A=>state_5, B=>state_6, C=>switch);
   ix1689 : NAND31 port map ( Q=>nx1688, A=>nx1661, B=>switch, C=>state_3);
   ix129 : CLKIN1 port map ( Q=>authentication_led, A=>nx1661);
   ix1629 : CLKIN1 port map ( Q=>nx1628, A=>nx1583);
   ix1692 : CLKIN1 port map ( Q=>nx1691, A=>nx54);
   ix1644 : CLKIN1 port map ( Q=>nx1643, A=>nx36);
   ix213 : CLKIN1 port map ( Q=>nx1581, A=>nx1620);
end SynthOneHot2 ;

