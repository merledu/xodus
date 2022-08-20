# Class for converting RISC-V trace log to CSV

from csv import DictWriter
from re import sub

class CSVConv:

    def __init__(self, trace, assembly, hexInst):
        self.__assembly = [sub('\n', '', _) if ':' not in _ else sub('\n', '', _[_.find(':') + 2:]) for _ in assembly if ':' not in _ or _ != assembly[-1]]
        self.__hexInst = [sub('\n', '', _).lower() for _ in hexInst]

        assert len(self.__assembly) == len(self.__hexInst),\
        "Assembly and hex instructions are not of equal length"

        self.__trace = [[sub('\s', '', __) for __ in _] for _ in trace]
        self.__headers = ['pc', 'instr', 'gpr', 'csr', 'binary', 'mode', 'instr_str', 'operand', 'pad']
        self.__gpr = [
            'zero', 'ra', 'sp',  'gp',  'tp', 't0', 't1', 't2',
            's0',   's1', 'a0',  'a1',  'a2', 'a3', 'a4', 'a5',
            'a6',   'a7', 's2',  's3',  's4', 's5', 's6', 's7',
            's8',   's9', 's10', 's11', 't3', 't4', 't5', 't6'
        ]
        self.__instDict = {self.__hexInst[i]: {self.__assembly[i][: self.__assembly[i].find(' ')]: self.__assembly[i][self.__assembly[i].find(' ') + 1:].split(', ')} for i in range(len(hexInst))}
        self.__assemblyDict = {self.__hexInst[i]: self.__assembly[i] for i in range(len(self.__hexInst))}
        self.__traceDict = {}
        self.__csvDict = {}
        self.__instr = []

    def getTrace(self):
        return self.__trace

    def getAssembly(self):
        return self.__assembly

    def getHexInst(self):
        return self.__hexInst

    def getHeaders(self):
        return self.__headers

    def traceToDict(self):
        traceSplit = [[__.split(':') for __ in _] for _ in self.__trace]
        self.__traceDict = [{__[0]:__[1] for __ in _} for _ in traceSplit]
        self.__instr = []
        return self.__traceDict

    def toCSVDict(self):
        self.__csvDict = [
            {
                self.__headers[0]: _['PC'],                                            self.__headers[1]: [k for k in self.__instDict[_['Inst']].keys()][0],
                self.__headers[2]: f"{self.__gpr[int(_['rd_addr'])]}:{_['rd_wdata']}", self.__headers[3]: '',
                self.__headers[4]: _['Inst'],                                          self.__headers[5]: _['Mode'],
                self.__headers[6]: f"{self.__assemblyDict[_['Inst']]}",                self.__headers[7]: f"{', '.join(self.__instDict[_['Inst']][[k for k in self.__instDict[_['Inst']].keys()][0]])}",
                self.__headers[8]: ''
            } for _ in self.__traceDict if _['Inst'] != '00000000'
        ]

    def writeCSV(self, fileName):
        with open(fileName, 'w', encoding = 'UTF-8') as f:
            csv = DictWriter(f, fieldnames = self.__headers)
            csv.writeheader()
            csv.writerows(self.__csvDict)
