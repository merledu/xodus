# Generating assembly hex
python ./scripts/assemblyParser.py --asm ./asm/$1 --hex ./asm/assembly.hex

# Dump sbt output
cd ../
echo "Dumping sbt output..."
sbt "testOnly nucleusrv.components.TopTest -- -DwriteVcd=1 -DprogramFile=./burq/asm/assembly.hex" > ./burq/trace/sbtDump.log
echo "sbt output successfully dumped"

# Generating log
cd ./burq
echo "Generating log..."
python ./scripts/sbtToLog.py --asm ./asm/$1 --sbt_dump ./trace/sbtDump.log --log ./trace/nrv.log
echo "Log successfully generated"

# Generating CSV
#echo "Generating CSV..."
#python ./scripts/logToCSV.py --log ./trace/nrv.log --asm ./asm/$1 --csv ./trace/trace.csv
#echo "CSV successfully generated"
