from multiprocessing.pool import ThreadPool
from time import sleep
import socket
import select

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setblocking(0)
s.bind(('0.0.0.0', 5555))

def foo(s):
    data, addr = s.recvfrom(1024)
    return data

def bar(x):
    return x*x

pool = ThreadPool(processes=2)
pool.apply_async(foo, (s,))
pool.apply_async(bar, (3,))

if __name__ == '__main__':

    while True:
        ready = select.select([s], [], [], 0.01)
        a = pool.apply_async(foo, (s,))
        b = pool.apply_async(bar, (12,))

        if ready[0]:
            print(a.get())
            print(b.get())
