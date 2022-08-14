curDir=$PWD
echo "Current Directory: $curDir"
echo "Generating log file..."
sbt "testOnly Simulation.SimulationTest -- -DprogramFile=$curDir/simulation/hex.txt" > simulation/log.txt
echo "Log file successfully generated"
python simulation/instLogger.py

