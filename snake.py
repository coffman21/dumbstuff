import random
import time
import os
from sys import exit
from itertools import count
from curtsies import Input

clear=lambda: os.system('clear')

class Snake:
	snake = []
	def __init__(self):
		self.snake = [(20, 20), (21, 20), (22, 20)]
		self.prev_move = 'init'
		self.e = False

	def eat(self, food):
		self.e = True


	def move(self, key):
		head = self.snake[0]
		# print(self.snake)
		if key == None and self.prev_move != 'init':
			key = self.prev_move
		if key != None:
			if str(key) == 'KEY_UP':
				self.snake.insert(0, (head[0]-1, head[1]))
			elif str(key) == 'KEY_DOWN':
				self.snake.insert(0, (head[0]+1, head[1]))
			elif str(key) == 'KEY_LEFT':
				self.snake.insert(0, (head[0], head[1]-1))
			elif str(key) == 'KEY_RIGHT':
				self.snake.insert(0, (head[0], head[1]+1))
			elif key == 'init':
				return
			elif str(key) == 'q':
				exit(0)
			self.prev_move = key
			if not self.e:
				self.snake.pop(-1)
			else:
				self.e = False
			

class App:

	def __init__(self):
		self.chars = [
		'\x1b[2m'+'▉'+'\x1b[0m',
		'\x1b[2m'+'▓'+'\x1b[0m',
		'\x1b[2m'+'▒'+'\x1b[0m',
		'\x1b[2m'+'░'+'\x1b[0m',
		'\x1b[2m'+' '+'\x1b[0m',
		'\x1b[14m'+'@'+'\x1b[0m',
		'\x1b[14m'+'a'+'\x1b[0m',
		'\x1b[13m'+'✦'+'\x1b[0m',
		'\x1b[5m'+'✧'+'\x1b[0m',
		]
		self.size = (80,24)
		self.screen = [[self.chars[4] for i in range(self.size[0])] for j in range(self.size[1])]
		self.snake = Snake()
		self.blink = False
		self.speed = 0.15
		self.food = (random.randint(0,23), random.randint(0, 79))
		self.walls = [(0, i) for i in range(self.size[0])]
		self.walls += [(23, i) for i in range(self.size[0])]
		self.walls += [(i, 0) for i in range(self.size[1])]
		self.walls += [(i, 79) for i in range(self.size[1])]
		self.game_over = False
	def start(self):
		with Input(keynames='curses') as input_generator:
			t = time.time()
			prev = 'init'
			length = 3
			for i in count(0, 1):
				#print(t)
				e = input_generator.send(timeout=self.speed)
				if e == None:
					e = prev
				else:
					prev = e
				# print(self.walls)
				if time.time() - t >= self.speed:
					if self.snake.snake[0] == self.food:
						self.snake.eat(self.food)
						self.food = (random.randint(1,22), random.randint(1, 78))
					self.snake.move(e)
					if len(set(self.snake.snake)) < len(self.snake.snake):
						self.game_over = True
					if self.snake.snake[0] in self.walls:
						self.game_over = True
					self.render()
					t = time.time()
				# if i % 10 == 0 and self.speed > 0.05:
				# 	self.speed -=0.01



	def render(self):
		clear()
		self.blink = not self.blink
		screen = [[self.chars[4] for i in range(self.size[0])] for j in range(self.size[1])]
		f = self.food
		# print(f)
		screen[f[0]][f[1]] = self.chars[7] if self.blink else self.chars[8]
		s = self.snake.snake
		for i, j in s:
			# print(i, j)
			screen[i][j] = self.chars[5] if self.blink else self.chars[6]
		for i, j in self.walls:
			# print(i, j)
			screen[i][j] = self.chars[0]
		screen[-1] = self.chars[0] + "score: " + str(len(s)).zfill(4) + self.chars[0]*58 + 'q to exit' + self.chars[0]
		if self.game_over:
			#screen = [[self.chars[1] for i in range(self.size[0])] for j in range(self.size[1])]
			screen[self.size[1]//2] = ''.join([self.chars[1]*36, 'GAME OVER', self.chars[1]*35])
		#for i, line in enumerate(self.screen):
		#	for j, char in enumerate(line):
		#		self.screen[i][j] = (self.chars[5] if self.blink else 'a') if (i,j) in self.snake.snake else self.chars[3]
		for i in range(len(screen)):
			print(''.join([_ for _ in screen[i]]))
		if self.game_over:
			exit(0)



def main():
    a = App()
    a.start()

if __name__ == '__main__':
    main()
