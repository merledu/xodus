# XODUS32-5S

A 5 stage 32 bit RISC-V I(Base) extension core

## Dependencies

- sbt
- openjdk

## Simulation

1. Edit the `assembly.s` file with your own assembly code and place the hex instructions in `hex.txt` in the `simulation` directory. A template file is also given. Follow the pattern for a successful simulation.

2. Before starting the simulation, make sure you're in the root directory of the repository.

```
sbt
```

3. The sbt server will be started. To generate the RTL file,

```
testOnly Top.TopTest -- -DwriteVcd=1
```

4. A `Top.vcd` file is situated in `test_run_dir/XODUS32_5S/`. Use a vcd file viewer like gtkwave to view the RTL.

## ISS Log

A log file in the Spike-ISS format can also be generated.

1. Run the log script

```
./logGenerator.sh
```

2. A CSV file `ISSlog.csv` will be generated.

