#!/bin/zsh
cd burq

# Generating assembly hex
python ./scripts/assemblyParser.py --asm ./asm/$1 --hex ./asm/assembly.hex

# Dump sbt output
cd ../
echo "Dumping sbt output..."
sbt "testOnly Tracer.TracerTopTest -- -DwriteVcd=1" > ./burq/trace/sbtDump.log
echo "sbt output successfully dumped"

# Generating log
cd ./burq
echo "Generating log..."
python ./scripts/sbtToLog.py --asm ./asm/$1 --sbt_dump ./trace/sbtDump.log --log ./trace/xodus.log
echo "Log successfully generated"

# Generating CSV
echo "Generating CSV..."
python ./scripts/log_to_csv.py --log ./trace/xodus.log --csv ./trace/xodus.csv
echo "CSV successfully generated"

