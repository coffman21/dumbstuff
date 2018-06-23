from itertools import zip_longest
import re

def calc(s1, s2, oper):
	if oper == '+':
		return s1 + s2
	elif oper == '-':
		return s1[:len(s2)] if s1[-len(s2):] == s2 else s1
	elif oper == '*':
		return ''.join([(i if i else '') + (j if j else '') for i, j in zip_longest(s1, s2)])
	elif oper == '/':
		t = [(i, j) for i, j in zip(s[1::2], s2) if i == j]
		return s[:len(t)*2:2], s[len(t)*2:]

def evaluate(expr):
	expr = re.findall('[+\-*/()]|[^+\-*/()]+', expr.replace(' ', ''))
	opers = []
	vals = []
	for token in expr:
		if token not in "+-*/()":
			vals.append(token)
		if token in "+-*/":
			while opers and opers[-1] in "*/":
				vals.append(opers.pop())
			opers.append(token)
		if token == '(':
			opers.append(token)
		if token == ')':
			try:
				while opers[-1] != '(':
					vals.append(opers.pop())
				opers.pop()
			except:
				raise IndexError("Mismatched parentheses found.")
	vals += opers
	print(vals)
	out = []
	for token in vals:
		if token not in "+-*/":
			out.append(token)
		else:
			s1 = out.pop()
			s2 = out.pop()
			out.append(calc(s1, s2, token))
			print(out)
	return out

if __name__ == '__main__':
	e = input('insert an expression: ')
	print(evaluate(e))
