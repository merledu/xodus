from re import sub

# Reading the log file
with open('log.txt', 'r', encoding='UTF-8') as f:
    log = f.readlines()

# Reading the assembly
# with open('assembly.s', 'r', encoding='UTF-8') as f:
#     assembly = f.readlines()

# Refining the log output
regVal = [{__.split(': ')[0]: __.split(': ')[1] for __ in _[:-1].split(', ')} for _ in log if _[:2] == 'PC']
keyList = [k for k in regVal[0]]

# Refining the assembly
# refAssembly = [sub('[\n\t]', '', _) for _ in assembly]
# print(refAssembly)

with open('ISSlog.csv', 'w', encoding='UTF-8') as f:
    f.write(f'{keyList[0]}, {keyList[1]}, {keyList[2]}, {keyList[3]}\n')
    for i in range(len(regVal)):
        f.write(f'{regVal[i][keyList[0]]}, {regVal[i][keyList[1]]}, {regVal[i][keyList[2]]}, {regVal[i][keyList[3]]}\n')

