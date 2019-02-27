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
        self.chars = {
                    'wall':    '\033[107m'+'▉'+'\033[00m',
                    'wall_75': '\033[107m'+'▓'+'\033[00m',
                    'wall_50': '\033[107m'+'▒'+'\033[00m',
                    'wall_25': '\033[107m'+'░'+'\033[00m',
                    'empty':   '\033[00m' +' '+'\033[00m',
                    's_bl':    '\033[92m' +'@'+'\033[00m',
                    's':       '\033[92m' +'a'+'\033[00m',
                    'trg_bl':  '\033[94m' +'✦'+'\033[00m',
                    'trg':     '\033[94m' +'✧'+'\033[00m',
        }
        self.size = (80,24)
        self.screen = [[self.chars['wall'] for i in range(self.size[0])] for j in range(self.size[1])]
        self.snake = Snake()
        self.blink = False
        self.speed = 0.15
        self.food = (random.randint(1,22), random.randint(1, 78))
        self.walls = [(0, i) for i in range(self.size[0])]
        self.walls += [(23, i) for i in range(self.size[0])]
        self.walls += [(i, 0) for i in range(self.size[1])]
        self.walls += [(i, 79) for i in range(self.size[1])]
        self.game_over = False

# TODO: use curses instead of curtsies
    def start(self):
        current_time = time.time()
        with Input(keynames='curses') as input_generator:
            t = time.time()
            prev = 'init'
            length = 3
            # TODO: remove count
            while(True):
                e = input_generator.send(timeout=self.speed)
                if e == None:
                    e = prev
                else:
                    if e != prev:
                        prev = e
                if time.time() - t >= self.speed:
                    if self.snake.snake[0] == self.food:
                        self.snake.eat(self.food)
                        self.food = (random.randint(1,22), random.randint(1, 78))
                    self.snake.move(e)
                    if len(set(self.snake.snake)) < len(self.snake.snake):
                        self.game_over = True
                    if self.snake.snake[0] in self.walls:
                        self.game_over = True
                        # TODO: blink as parameter
                    self.render()
                    t = time.time()
                #if i % 100 == 0 and self.speed > 0.1:
                    #self.speed -=0.01



    def render(self):
        clear()
        self.blink = not self.blink
        screen = [[self.chars['empty'] for i in range(self.size[0])] \
                                       for j in range(self.size[1])]
        f = self.food
        screen[f[0]][f[1]] = self.chars['trg'] if self.blink \
                                               else self.chars['trg_bl']
        s = self.snake.snake
        for i, j in s:
            screen[i][j] = self.chars['s'] if self.blink \
                                           else self.chars['s_bl']
        for i, j in self.walls:
            screen[i][j] = self.chars['wall']
        screen[-1] = self.chars['wall'] + "score: " \
                                        + str(len(s)-3).zfill(4) \
                                        + self.chars['wall']*58 \
                                        + 'q to exit' + self.chars['wall']
        if self.game_over:
            screen[self.size[1]//2] = ''.join([self.chars['wall_75']*36, \
                                               'GAME OVER', \
                                               self.chars['wall_75']*35])
        for i in range(len(screen)):
            print(''.join([_ for _ in screen[i]]))
        if self.game_over:
            exit(0)


def main():
    a = App()
    a.start()

if __name__ == '__main__':
    main()
