from re import search, sub, split

if __name__ == '__main__':
    # Reading dump file
    with open('../scripts/test.s', 'r', encoding='UTF-8') as f:
        elfLog = f.readlines()
    elfLog = [sub('\n', '', _) for _ in elfLog]

    # Refining the log
    logList = [_ for _ in elfLog if search('^[0-9a-f]+:\s+[0-9a-f]+.+', _)]
    logList = [split('\s+', _) for _ in logList]

    # Writing assembly.hex
    hexList = [_[1] for _ in logList]
    with open('../assembly/assembly.hex', 'w', encoding = 'UTF-8') as f:
        for i in range(len(hexList) - 1):
            f.write(f'{hexList[i]}\n')
        f.write(hexList[-1])

    # Writing assembly.s
    asmList = [_[-1] if (_[-1] == 'ret') or (_[-1] == 'nop') else f'{_[2]} {sub(",", " ", _[3])}' for _ in logList]
    with open('../assembly/assembly.s', 'w', encoding = 'UTF-8') as f:
        for i in range(len(hexList) - 1):
            f.write(f'{asmList[i]}\n')
        f.write(asmList[-1])
