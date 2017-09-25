from multiprocessing.pool import ThreadPool
from time import sleep
import socket
from select import select
from signal import signal, SIGUSR1, SIGUSR2
import subprocess
from sys import exit

import sysv_ipc

keys = [
    ["DESTROY", False],
    ["ERASE",   False],
    ["IMPROVE", False],
]

'''
# 1: UDP listener
'''
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setblocking(0)
s.bind(('0.0.0.0', 7777))
def listen(s):
    data, addr = s.recvfrom(1024)
    return (str(data)[2:-1], addr,)

'''
# 2: IPC Shared Memory
'''
shm = sysv_ipc.SharedMemory(None, flags=sysv_ipc.IPC_CREX, size=1024)
shm.write("***The second flag: " + keys[1][0] + '\n***The next flag can be found when sending one of the SIGUSR signals to this process.')
k = shm.key
# This should handle case when someone reads shared memory segment.
def ipcs():
    ipcs_m = subprocess.Popen(['ipcs', '-m'], stdout=subprocess.PIPE)
    grep_proc = subprocess.Popen(['grep', str(hex(k))[2:]], stdin=ipcs_m.stdout, stdout=subprocess.PIPE)
    awk = subprocess.Popen(['awk', '{print $6}'], stdin=grep_proc.stdout, stdout=subprocess.PIPE)
    ipcs_m.stdout.close()
    grep_proc.stdout.close()
    output = awk.communicate()[0]
    return int(output)

'''
# 3: SIGUSR2 handler
'''
def sigusr2(*_):
    print("***The third flag: " + keys[2][0])
    keys[2][1] = True

def sigusr1(*_):
    print("Hey, I've been told to return the flag by SIGINT2 only!")


pool = ThreadPool(processes=2)
if __name__ == '__main__':
    print("This is entry test for Amonitoring traineeship.\nAuthor: Konstantin Kharitonov")
    while True:
        ready = select([s], [], [], 0.1)
        a = pool.apply_async(listen, (s,))
        b = pool.apply_async(ipcs, )

        if ready[0]:
            got = a.get()
            print(got)
            msg = "Received message: " + got[0] + "\nFrom: " + str(got[1][0]) \
                + "\n***The first flag: " + keys[0][0] \
                + "\n***The next flag is located by the shmkey " \
                + hex(k) + " shared memory segment."
            print(msg)
            s.sendto(bytes(msg+'\n', 'utf-8'), got[1])
            keys[0][1] = True
        # Im not sure for the next 'if' statement. Idk, but number of connected
        # processes to the shm area varies from 1 to 2, even this process is
        # actually the one which is attached to it.
        # However,
        if b.get() > 2 and not keys[1][1]:
            print("Seems you read the next key. I'm not sure if I should pass it to stdout. Well, I wouldn't.")
            keys[1][1] = True
        signal(SIGUSR1, sigusr1)
        signal(SIGUSR2, sigusr2)

        if False not in [_[1] for _ in keys]:
            print("***Seems you found all the keys. Want to listen for it?")
            print ("***https://music.yandex.ru/album/121658")
            exit(0)
