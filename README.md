# XODUS32-5S

A 5 stage 32 bit RISC-V I(Base) extension core

## Dependencies

- sbt
- openjdk 18

## RTL Generation

1. Edit the `assembly.s` file with your own assembly code and place the hex instructions in `hex.txt` in the `assembly` directory. A template file is also given. Follow the pattern for a successful simulation.

2. Make sure you're in the root directory of the repository.

```bash
sbt
```

3. The sbt server will be started. To generate the RTL file,

```bash
testOnly Top.TopTest -- -DwriteVcd=1
```

4. A `Top.vcd` file is situated in `test_run_dir/XODUS32_5S/`. Use a vcd file viewer like `gtkwave` to view the RTL.

## RVFI Log

A log file in the RVFI format can also be generated.
Place a disassembly file, named `test.s` in the `scripts` directory.
From the root directory, cd into the `trace` directory and execute `traceGenerator.sh` file.

```bash
cd trace
./traceGenerator.sh
```

A CSV file `trace.csv` along with a log file `trace.log` will be generated.

