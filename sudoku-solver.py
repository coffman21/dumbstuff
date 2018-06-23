from itertools import chain
import pprint
pp = pprint.PrettyPrinter(indent=4)
puzzle = [[5,3,0,0,7,0,0,0,0],
          [6,0,0,1,9,5,0,0,0],
          [0,9,8,0,0,0,0,6,0],
          [8,0,0,0,6,0,0,0,3],
          [4,0,0,8,0,3,0,0,1],
          [7,0,0,0,2,0,0,0,6],
          [0,6,0,0,0,0,2,8,0],
          [0,0,0,4,1,9,0,0,5],
          [0,0,0,0,8,0,0,7,9]]

solution = [[5,3,4,6,7,8,9,1,2],
            [6,7,2,1,9,5,3,4,8],
            [1,9,8,3,4,2,5,6,7],
            [8,5,9,7,6,1,4,2,3],
            [4,2,6,8,5,3,7,9,1],
            [7,1,3,9,2,4,8,5,6],
            [9,6,1,5,3,7,2,8,4],
            [2,8,7,4,1,9,6,3,5],
            [3,4,5,2,8,6,1,7,9]]

quadrants = [[0,0,0,1,1,1,2,2,2],
             [0,0,0,1,1,1,2,2,2],
             [0,0,0,1,1,1,2,2,2],
             [3,3,3,4,4,4,5,5,5],
             [3,3,3,4,4,4,5,5,5],
             [3,3,3,4,4,4,5,5,5],
             [6,6,6,7,7,7,8,8,8],
             [6,6,6,7,7,7,8,8,8],
             [6,6,6,7,7,7,8,8,8]]

def get_quad(puzzle, mask, q):
    res = []
    for i, row in enumerate(puzzle):
        if mask in q[i]:
            res.append([])
            for j, elem in enumerate(row):
                if q[i][j] == mask:
                    res[-1].append(elem)
    return chain.from_iterable(res)


def gen_mask(puzzle):
    mask = [[set(range(1,10)) for j in range(9)] for i in range(9)]
    for i, (row, m_row) in enumerate(zip(puzzle, mask)):
        for j, (elem, m) in enumerate(zip(row, m_row)):
            if elem != 0:
                m = set()
            else:
                q = quadrants[i][j]
                mask[i][j] = m - (set(row) | set([_[j] for _ in puzzle]) \
                    | set(get_quad(puzzle, q, quadrants)))
    return mask


def sudoku(puzzle):
    while 0 in chain.from_iterable(puzzle):
        mask = gen_mask(puzzle)
        for i, row in enumerate(puzzle):
            for j, elem in enumerate(row):
                if len(mask[i][j]) == 1:
                    puzzle[i][j] = min(mask[i][j]) # what the actual fuck
    return puzzle

print "True" if  sudoku(puzzle) == solution else "False"
