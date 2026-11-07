```bash
function sendsim {
    adb -s $device shell am broadcast -a com.dentex.troller.SIM @args
}
```
```bash
sendsim --es cmd position --es value "44.2167,14.7934"
```
```bash
sendsim --es cmd position --es value "44.2166,14.7935"
```
```bash
sendsim --es cmd position --es value "44.2165,14.7936"
```
```bash
sendsim --es cmd position --es value "44.2164,14.7937"
```
```bash
sendsim --es cmd position --es value "44.2180,14.7959"
```
```bash
sendsim --es cmd mode --es value manual    # as the remote button: manual|anchor|navigation|cruise|record
```
```bash
sendsim --es cmd mode --es value anchor    # as the remote button: manual|anchor|navigation|cruise|record
```
```bash
sendsim --es cmd mode --es value navigation    # as the remote button: manual|anchor|navigation|cruise|record
```
```bash
sendsim --es cmd mode --es value cruise    # as the remote button: manual|anchor|navigation|cruise|record
```
```bash
sendsim --es cmd status
```
```bash
sendsim --es cmd position --es value "44.2167,14.7934"
sendsim --es cmd heading --es value 90
sendsim --es cmd gps --es value nofix         # or fix
sendsim --es cmd arrival --es value manual    # anchor, manual or circle
sendsim --es cmd current --es value "0.5,90"  # knots, flowing towards
sendsim --es cmd timescale --es value 10      # simulated seconds per second
sendsim --es cmd fail --es value navigation   # none, upload, navigation or anchor
sendsim --es cmd battery --es value 20
sendsim --es cmd link --es value "drop,8"     # motor silent, the watchdog fires
sendsim --es cmd link --es value "lose,15"    # remote out of range, then reconnect
sendsim --es cmd reset
```
