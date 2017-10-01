import random
import time
import os
from itertools import count
from curtsies import Input

clear=lambda: os.system('clear')

class Snake:
	snake = []
	def __init__(self):
		self.snake = [(20, 20), (21, 20), (22, 20)]
		self.prev_move = 'init'

	def eat(self, food):
		self.snake.append(food)


	def move(self, key):
		head = self.snake[0]
		print(self.snake)
		if key == None and self.prev_move != 'init':
			key = self.prev_move
		if key != None:
			if str(key) == 'KEY_UP':
				self.snake.append((head[0]-1, head[1]))
			elif str(key) == 'KEY_DOWN':
				self.snake.append((head[0]+1, head[1]))
			elif str(key) == 'KEY_LEFT':
				self.snake.append((head[0], head[1]-1))
			elif str(key) == 'KEY_RIGHT':
				self.snake.append((head[0], head[1]+1))
			self.prev_move = key
			self.snake.pop(0)

	def pos():
		return [(10,10)]


class App:

	def __init__(self):
		self.chars = ['▉', '▓', '▒', '░', ' ', '@', '✦', '✧']
		self.size = (80,24)
		self.screen = [[self.chars[4] for i in range(self.size[0])] for j in range(self.size[1])]
		self.snake = Snake()
		self.blink = False
		self.speed = 1.5
		self.food = (random.randint(0,23), random.randint(0, 79))
	def start(self):
		with Input(keynames='curses') as input_generator:
			t = time.time()
			for i in count(0, 1):
				#print(t)
				e = input_generator.send(timeout=self.speed)
				if time.time() - t >= self.speed:
					self.snake.move(e)
					if self.snake.snake[-1] == self.food:
						self.snake.eat(self.food)
					self.render()
				# t = time.time()
				if i % 10 == 0 and self.speed > 0.05:
					self.speed -=0.01



	def render(self):
		# clear()
		# self.blink = not self.blink
		for i, line in enumerate(self.screen):
			for j, char in enumerate(line):
				self.screen[i][j] = (self.chars[5] if self.blink else 'a') if (i,j) in self.snake.snake else self.chars[3]
		f = self.food
		print(f)
		self.screen[f[0]][f[1]] = self.chars[6] if self.blink else self.chars[7]
		for i in range(len(self.screen)):
			print(''.join([_ for _ in self.screen[i]]))




def main():
    a = App()
    a.start()

if __name__ == '__main__':
    main()
