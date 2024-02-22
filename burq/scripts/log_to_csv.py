import re, csv

from argparse import ArgumentParser


def log_to_csv(log_path, csv_path):
    with open(log_path, 'r', encoding='UTF-8') as f:
        instr = f.readlines()
    instr = [re.split('\s+', _.replace('\n', '')) for _ in instr]

    csv_header = [
        'pc',
        'instr',
        'gpr',
        'csr',
        'binary',
        'mode',
        'instr_str',
        'operand',
        'pad'
    ]
    gpr = {
        'x': [
            'zero',
            'ra',
            'sp',
            'gp',
            'tp',
            't0',
            't1',
            't2',
            's0',
            's1',
            'a0',
            'a1',
            'a2',
            'a3',
            'a4',
            'a5',
            'a6',
            'a7',
            's2',
            's3',
            's4',
            's5',
            's6',
            's7',
            's8',
            's9',
            's10',
            's11',
            't3',
            't4',
            't5',
            't6'
        ],
        'f': [
            'ft0',
            'ft1',
            'ft2',
            'ft3',
            'ft4',
            'ft5',
            'ft6',
            'ft7',
            'fs0',
            'fs1',
            'fa0',
            'fa1',
            'fa2',
            'fa3',
            'fa4',
            'fa5',
            'fa6',
            'fa7',
            'fs2',
            'fs3',
            'fs4',
            'fs5',
            'fs6',
            'fs7',
            'fs8',
            'fs9',
            'fs10',
            'fs11',
            'ft8',
            'ft9',
            'ft10',
            'ft11'
        ]
    }
    instr_csv = []
    for i in range(1, len(instr)):
        inst = instr[i]
        instr_csv.append({
            'pc': inst[1],
            'instr': '',
            'gpr': f'{gpr[inst[9][0]][int(inst[9][1:])]}:{inst[10]}',
            'csr': '',
            'binary': inst[3],
            'mode': inst[4],
            'instr_str': inst[14] if len(inst[14:]) == 1 else '{:<8}'.format(inst[14]) + ' '.join(inst[15:]),
            'operand': '',
            'pad': ''
        })

    with open(csv_path, 'w', encoding='UTF-8') as f:
        writer = csv.DictWriter(f, fieldnames=csv_header)
        writer.writeheader()
        for i in range(len(instr_csv)):
            writer.writerow(instr_csv[i])


if __name__ == '__main__':
    parser = ArgumentParser()
    parser.add_argument('--log', type=str, help='Input log file')
    parser.add_argument('--csv', type=str, help='Output CSV file')
    args = parser.parse_args()

    log_to_csv(args.log, args.csv)

