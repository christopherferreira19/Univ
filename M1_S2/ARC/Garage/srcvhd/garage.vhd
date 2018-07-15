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
       open_light, close_light: out std_logic);

  constant init_closed: boolean := true;

  constant d_close: std_logic_vector(0 to 1) := "01";
  constant d_open: std_logic_vector(0 to 1) := "10";
  constant deactivate: std_logic_vector(0 to 1) := "11";
end Garage;

architecture Automaton of Garage is
  type States is (PowerUp, PowerDown, NoKey, Deactivated, Activated, Down, Up, MovingOnUp, MovingOnDown, SafetyError, SecurityError);
  signal state, nextstate: States;

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

  process(reset, clk)
  begin
    --  asynchronous reset (active high)
    if (reset = '1') then
      state <= PowerUp;
    -- otherwise, update the state if need be
    elsif (clk'event and clk = '1') then
      state <= nextstate;
    end if;
  end process;

  key_led <= '1' when state = PowerUp and switch = '1' and aes_key = '1' else
             '1' when state = NoKey and switch = '1' and aes_key = '1' else
             '0';

  authentication_led <= '1' when state = Deactivated and switch = '1' and authentication = '1' else
                        '0';

  open_light <= '1' when state = Down and switch = '1' and bad_encryption = '0' and command = d_open else
                '1' when state = MovingOnUp and bad_encryption = '0' and acsc = '0' and command /= d_close and reached_top = '0' else
                '1' when state = MovingOnDown and bad_encryption = '0' and acsc = '0' and command = d_open else
                '1' when state = SafetyError and acsc = '0' and command = d_open else
                '0';

  close_light <= '1' when state = Up and switch = '1' and bad_encryption = '0' and command = d_close else
                 '1' when state = MovingOnDown and bad_encryption = '0' and acsc = '0' and command /= d_open and reached_bottom = '0' else
                 '1' when state = MovingOnUp and bad_encryption = '0' and acsc = '0' and command = d_close else
                 '1' when state = SafetyError and acsc = '0' and command = d_close else
                 '0';

end Automaton;
