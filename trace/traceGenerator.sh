#! /usr/bin/zsh

# Generating trace file
echo "Generating trace..."
cd ../
sbt "testOnly Tracer.TracerTopTest -- -DwriteVcd=1" > ./trace/trace.log
cd trace
echo "Trace successfully generated"

# Generating CSV file
echo "Generating CSV..."
python toCSV.py
echo "CSV generated successfully"

