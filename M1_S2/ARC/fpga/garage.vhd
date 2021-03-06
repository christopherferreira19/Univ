-- TP1 - VHDL description for parking
-- Ahmed Zrigui
-- Christopher Ferreira
-- Login: xm1iarc011

library ieee;
use ieee.std_logic_1164.all;

entity Garage is
  port(clk, reset,
       aes_key, authentication,
       reached_bottom, reached_top,
       acsc, bad_encryption, timeout,
       switch : in std_logic;
       command: in std_logic_vector(0 to 1);
       key_led, authentication_led,
       open_light, close_light: out std_logic;

       LEDR: out std_logic_vector(9 downto 0);
       LEDG: out std_logic_vector(7 downto 0);
       seg_val_hex3: out Integer range 0 to 127;
       seg_val_hex0: out Integer range 0 to 127;
       seg_val_hex1: out Integer range 0 to 127);

  constant init_closed: boolean := true;

  constant d_close: std_logic_vector(0 to 1) := "01";
  constant d_open: std_logic_vector(0 to 1) := "10";
  constant deactivate: std_logic_vector(0 to 1) := "11";

  constant segment7_0: integer := 2#0000001#;
  constant segment7_1: integer := 2#1001111#;
  constant segment7_2: integer := 2#0010010#;
  constant segment7_3: integer := 2#0000110#;
  constant segment7_4: integer := 2#1001100#;
  constant segment7_5: integer := 2#0100100#;
  constant segment7_6: integer := 2#0100000#;
  constant segment7_7: integer := 2#0001111#;
  constant segment7_8: integer := 2#0000000#;
  constant segment7_9: integer := 2#0000100#;

end Garage;

architecture Automaton of Garage is
  type States is (PowerUp, PowerDown, NoKey, Deactivated, Activated, Down, Up, MovingOnUp, MovingOnDown, SafetyError, SecurityError);
  signal state, nextstate: States;
  signal clk_1Hz : std_logic;

-- PSL default clock is (clk'event and clk = '1');
-- PSL property require_encryption_key is
--     always(state = PowerUp or state = PowerDown -> state /= Activated until! aes_key = '1');
-- PSL assert require_encryption_key;
-- PSL property maintaining_open_light is always(state = Down ->
--      ((command = d_open  and bad_encryption = '0' and acsc ='0') =
--      (open_light = '1')) until! state = Up);
-- PSL assert maintaining_open_light;
-- PSL property bad_encryption_management is always(
--      { state = Down or state = Up;
--      (command = d_open or command = d_close) and bad_encryption = '0' and acsc ='0';
--      (bad_encryption = '0' and acsc = '0')[*];
--      bad_encryption = '1' } |=> state = SecurityError before! state=Deactivated);
-- PSL assert bad_encryption_management;

  attribute chip_pin: string;
  attribute chip_pin of reset: signal is "R22";
  attribute chip_pin of clk: signal is "L1";

  attribute chip_pin of command:        signal is "L2,M1";
  attribute chip_pin of aes_key:        signal is "M2";
  attribute chip_pin of authentication: signal is "U11";
  attribute chip_pin of reached_bottom: signal is "U12";
  attribute chip_pin of reached_top:    signal is "W12";
  attribute chip_pin of acsc:           signal is "V12";
  attribute chip_pin of bad_encryption: signal is "M22";
  attribute chip_pin of timeout:        signal is "L21";
  attribute chip_pin of switch:         signal is "L22";

  attribute chip_pin of seg_val_hex0: signal is "J2,J1,H2,H1,F2,F1,E2";
  attribute chip_pin of seg_val_hex1: signal is "E1,H6,H5,H4,G3,D2,D1";
  attribute chip_pin of seg_val_hex3: signal is "F4,D5,D6,J4,L8,F3,D4";

  attribute chip_pin of LEDR: signal is "R17,R18,U18,Y18,V19,T18,Y19,U19,R19,R20";
  attribute chip_pin of LEDG: signal is "Y21,Y22,W21,W22,V21,V22,U21,U22";

begin
  process(state, aes_key, authentication, reached_bottom, reached_top, acsc, bad_encryption, timeout, switch, command) is
  begin
    case state is
      when PowerUp =>
        if switch='1' and aes_key='1' then
          nextstate <= Deactivated;
        elsif switch='1' and aes_key='0' then
          nextstate <= NoKey;
        else
          nextstate <= PowerDown;
        end if;
      when PowerDown =>
        if switch = '1' then
          nextstate <= PowerUp;
        else
          nextstate <= PowerDown;
        end if;
      when NoKey =>
        if switch = '0' then
          nextstate <= PowerDown;
        elsif aes_key = '1' then
          nextstate <= Deactivated;
        else
          nextstate <= NoKey;
        end if;
      when Deactivated =>
        if switch = '0' then
          nextstate <= PowerDown;
        elsif authentication = '1' then
          nextstate <= Activated;
        else
          nextstate <= Deactivated;
        end if;
      when Activated =>
        if command = deactivate then
          nextstate <= Deactivated;
        elsif timeout = '1' and not init_closed then
          nextstate <= Up;
        elsif timeout = '1' and init_closed then
          nextstate <= Down;
        else
          nextstate <= Activated;
        end if;
      when Down =>
        if switch = '0' then
          nextstate <= PowerDown;
        elsif bad_encryption = '1' then
          nextstate <= SecurityError;
        elsif command = d_open then
          nextstate <= MovingOnUp;
        else
          nextstate <= Down;
        end if;
      when Up =>
        if switch = '0' then
          nextstate <= PowerDown;
        elsif bad_encryption = '1' then
          nextstate <= SecurityError;
        elsif command = d_close then
          nextstate <= MovingOnDown;
        else
          nextstate <= Up;
        end if;
      when MovingOnUp =>
        if bad_encryption = '1' then
          nextstate <= SecurityError;
        elsif acsc = '1' then
          nextstate <= SafetyError;
        elsif command = d_close then
          nextstate <= MovingOnDown;
        elsif reached_top = '1' then
          nextstate <= Up;
        else
          nextstate <= MovingOnUp;
        end if;
      when MovingOnDown =>
        if bad_encryption = '1' then
          nextstate <= SecurityError;
        elsif acsc = '1' then
          nextstate <= SafetyError;
        elsif command = d_open then
          nextstate <= MovingOnUp;
        elsif reached_bottom = '1' then
          nextstate <= Down;
        else
          nextstate <= MovingOnDown;
        end if;
      when SafetyError =>
        if acsc = '1' then
          nextstate <= SafetyError;
        elsif command = d_open then
          nextstate <= MovingOnUp;
        elsif command = d_close then
          nextstate <= MovingOnDown;
        else
          nextstate <= SafetyError;
        end if;
      when SecurityError =>
        nextstate <= Deactivated;
    end case;
  end process;

  process(reset, clk_1Hz)
  begin
    --  asynchronous reset (active low)
    if (reset = '0') then
      state <= PowerUp;
    -- otherwise, update the state if need be
    elsif (clk_1Hz'event and clk_1Hz = '1') then
      state <= nextstate;
    end if;
  end process;

  process(state, switch, aes_key)
  begin
    if state = PowerUp and switch = '1' and aes_key = '1' then
      key_led <= '1';
      LEDG(7) <= '1';
    elsif state = NoKey then
      key_led <= '1';
      LEDG(7) <= '1';
    else
      key_led <= '0';
      LEDG(7) <= '0';
    end if;
  end process;

  process(state, switch, authentication)
  begin
    if state = Deactivated then
      authentication_led <= '1';
      LEDG(6) <= '1';
    else
      authentication_led <= '0';
      LEDG(6) <= '0';
    end if;
  end process;

  process(state, switch, bad_encryption, command, reached_top, acsc)
  begin
   if state = Down and switch = '1' and bad_encryption = '0' and command = d_open then
     open_light <= '1';
     LEDG(5) <= '1';
   elsif state = MovingOnUp and bad_encryption = '0' and acsc = '0' and command /= d_close and reached_top = '0' then
     open_light <= '1';
     LEDG(5) <= '1';
   elsif state = MovingOnDown and bad_encryption = '0' and acsc = '0' and command = d_open then
     open_light <= '1';
     LEDG(5) <= '1';
   elsif state = SafetyError and acsc = '0' and command = d_open then
     open_light <= '1';
     LEDG(5) <= '1';
   else
     open_light <= '0';
     LEDG(5) <= '0';
   end if;
  end process;

  process(state, switch, bad_encryption, command, reached_bottom, acsc)
  begin
    if state = Up and switch = '1' and bad_encryption = '0' and command = d_close then
      close_light <= '1';
      LEDG(4) <= '1';
    elsif state = MovingOnDown and bad_encryption = '0' and acsc = '0' and command /= d_open and reached_bottom = '0' then
      close_light <= '1';
      LEDG(4) <= '1';
    elsif state = MovingOnUp and bad_encryption = '0' and acsc = '0' and command = d_close then
      close_light <= '1';
      LEDG(4) <= '1';
    elsif state = SafetyError and acsc = '0' and command = d_close then
      close_light <= '1';
      LEDG(4) <= '1';
    else
      close_light <= '0';
      LEDG(4) <= '0';
    end if;
  end process;


  process(state) is
  begin
     case state is
       when PowerDown =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_0;
       when PowerUp =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_1;
       when NoKey =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_2;
       when Deactivated =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_3;
       when Activated =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_4;
       when Down =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_5;
       when Up =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_6;
       when MovingOnUp =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_7;
       when MovingOnDown =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_8;
       when SafetyError =>
         seg_val_hex1 <= segment7_0;
         seg_val_hex0 <= segment7_9;
       when SecurityError =>
         seg_val_hex1 <= segment7_1;
         seg_val_hex0 <= segment7_0;
     end case;
  end process;

  process(command) is
  begin
    case command is
      when "00"   => seg_val_hex3 <= segment7_0;
      when "01"   => seg_val_hex3 <= segment7_3;
      when "10"   => seg_val_hex3 <= segment7_2;
      when "11"   => seg_val_hex3 <= segment7_1;
      when others => seg_val_hex3 <= segment7_0;
    end case;
  end process;

  LEDR(9) <= command(1);
  LEDR(8) <= command(0);
  LEDR(7) <= aes_key;
  LEDR(6) <= authentication;
  LEDR(5) <= reached_bottom;
  LEDR(4) <= reached_top;
  LEDR(3) <= acsc;
  LEDR(2) <= bad_encryption;
  LEDR(1) <= timeout;
  LEDR(0) <= switch;

  LEDG(3) <= '0';
  LEDG(2) <= '0';
  LEDG(1) <= '0';
  LEDG(0) <= '0';

  process(clk)
    variable cnt : integer range 0 to 67108863;
    constant verrou_t : integer := 50000000;
  begin
    if clk'event and clk = '1' then
      if (reset = '0') or (cnt = verrou_t) then
        cnt := 0;
      else
        cnt := cnt + 1;
      end if;
    end if;
    if (cnt = verrou_t) then
      clk_1Hz <= '1';
    else
      clk_1Hz <= '0';
    end if;
  end process;

end Automaton;
