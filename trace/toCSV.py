from sys import path
path.insert(0, '../utilities/')
from CSVConverter.CSVConv import CSVConv, CSVConv2, CSVConv3

# Reading from trace file
with open('trace.log', 'r', encoding='UTF-8') as f:
    traceLog = f.readlines()

# Reading from assembly file
with open('../assembly/assembly.s') as f:
    assembly = f.readlines()

# Reading from hex file
with open('../assembly/assembly.hex') as f:
    hexInst = f.readlines()

# Extracting trace
trace = [_.split(', ') for _ in traceLog if _[:8] == 'ClkCycle']
csv = CSVConv3(trace, assembly, hexInst)

# Converting trace to dictionary
csv.traceToDict()
csv.toCSVDict()
 
# Writing to CSV
csv.writeCSV('trace.csv')
