# Class for converting RISC-V trace log to CSV

from csv import DictWriter
from re import sub

class CSVConv:

    def __init__(self, trace, assembly, hexInst):
        self.assembly = [sub('\n', '', _) if ':' not in _ else sub('\n', '', _[_.find(':') + 2:]) for _ in assembly if ':' not in _ or _ != assembly[-1]]
        self.hexInst = [sub('\n', '', _).lower() for _ in hexInst]

        assert len(self.assembly) == len(self.hexInst),\
        "Assembly and hex instructions are not of equal length"

        self.trace = [[sub('\s', '', __) for __ in _] for _ in trace]
        self.headers = ['pc', 'instr', 'gpr', 'csr', 'binary', 'mode', 'instr_str', 'operand', 'pad']
        self.gpr = [
            'zero', 'ra', 'sp',  'gp',  'tp', 't0', 't1', 't2',
            's0',   's1', 'a0',  'a1',  'a2', 'a3', 'a4', 'a5',
            'a6',   'a7', 's2',  's3',  's4', 's5', 's6', 's7',
            's8',   's9', 's10', 's11', 't3', 't4', 't5', 't6'
        ]
        self.instDict = {
            self.hexInst[i]: {
                self.assembly[i][: self.assembly[i].find(' ')]: self.assembly[i][self.assembly[i].find(' ') + 1:].split(', ')
            } for i in range(len(hexInst))
        }
        self.assemblyDict = {self.hexInst[i]: self.assembly[i] for i in range(len(self.hexInst))}
        self.traceDict = {}
        self.csvDict = []
        self.instr = []

    def getTrace(self):
        return self.trace

    def getAssembly(self):
        return self.assembly

    def getHexInst(self):
        return self.hexInst

    def getHeaders(self):
        return self.headers

    def traceToDict(self):
        traceSplit = [[__.split(':') for __ in _] for _ in self.trace]
        self.traceDict = [{__[0]:__[1] for __ in _} for _ in traceSplit]
        return self.traceDict

    def toCSVDict(self):
        self.csvDict = [
            {
                self.headers[0]: _['pc_rdata'],
                self.headers[1]: [k for k in self.instDict[_['insn']].keys()][0],
                self.headers[2]: f"{self.gpr[int(_['rd_addr'])]}:{_['rd_wdata']}",
                self.headers[3]: '',
                self.headers[4]: _['insn'],
                self.headers[5]: _['mode'],
                self.headers[6]: f"{self.assemblyDict[_['insn']]}",
                self.headers[7]: f"{', '.join(self.instDict[_['insn']][[k for k in self.instDict[_['insn']].keys()][0]])}",
                self.headers[8]: ''
            } for _ in self.traceDict
        ]

    def writeCSV(self, fileName):
        with open(fileName, 'w', encoding = 'UTF-8') as f:
            csv = DictWriter(f, fieldnames = self.headers)
            csv.writeheader()
            csv.writerows(self.csvDict)


class CSVConv2(CSVConv):

    def __init__(self, trace, assembly, hexInst):
        super().__init__(trace, assembly, hexInst)
        self.rvfiHeaders = [
            'pc_rdata',  'pc_wdata',  'rs1_addr', 'rs1_rdata', 'rs2_addr',
            'rs2_rdata', 'rd_addr',   'rd_wdata', 'insn',      'mode',
            'mem_addr',  'mem_rdata', 'mem_wdata'
        ]
        self.csvDict

    def toCSVDict(self):
        self.csvDict = [{
                self.rvfiHeaders[0] : _['pc_rdata'],
                self.rvfiHeaders[1] : _['pc_wdata'],
                self.rvfiHeaders[2] : self.gpr[int(_['rs1_addr'])],
                self.rvfiHeaders[3] : _['rs1_rdata'],
                self.rvfiHeaders[4] : self.gpr[int(_['rs2_addr'])],
                self.rvfiHeaders[5] : _['rs2_rdata'],
                self.rvfiHeaders[6] : self.gpr[int(_['rd_addr'])],
                self.rvfiHeaders[7] : _['rd_wdata'],
                self.rvfiHeaders[8] : _['insn'],
                self.rvfiHeaders[9] : _['mode'],
                self.rvfiHeaders[10]: _['mem_addr'],
                self.rvfiHeaders[11]: _['mem_rdata'],
                self.rvfiHeaders[12]: _['mem_wdata']
            } for _ in self.traceDict
        ]

    def writeCSV(self, fileName):
        with open(fileName, 'w', encoding = 'UTF-8') as f:
            csv = DictWriter(f, fieldnames = self.rvfiHeaders)
            csv.writeheader()
            csv.writerows(self.csvDict)

class CSVConv3(CSVConv2):

    def __init__(self, trace, assembly, hexInst):
        super().__init__(trace, assembly, hexInst)
        self.csvDict = []
    
    def toCSVDict(self):
            for i in range(len(self.traceDict)):
                entry = self.traceDict[i]

                self.csvDict.append({
                    self.rvfiHeaders[0] : entry['pc_rdata'],
                    self.rvfiHeaders[1] : entry['pc_wdata'],
                    self.rvfiHeaders[2] : self.gpr[int(entry['rs1_addr'])],
                    self.rvfiHeaders[3] : entry['rs1_rdata'],
                    self.rvfiHeaders[4] : self.gpr[int(entry['rs2_addr'])],
                    self.rvfiHeaders[5] : entry['rs2_rdata'],
                    self.rvfiHeaders[6] : self.gpr[int(entry['rd_addr'])],
                    self.rvfiHeaders[7] : entry['rd_wdata'],
                    self.rvfiHeaders[8] : entry['insn'],
                    self.rvfiHeaders[9] : entry['mode'],
                    self.rvfiHeaders[10]: entry['mem_addr'],
                    self.rvfiHeaders[11]: entry['mem_rdata'],
                    self.rvfiHeaders[12]: entry['mem_wdata']
                })

                if entry['insn'] == '00008067':
                    break
