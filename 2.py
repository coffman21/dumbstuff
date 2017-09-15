from random import randint

l = [randint(1, 500) for i in range(200)]

def bs(t, s):
    res = l[len(l)/2]
    print(res, t, len(l))
    if res > t:
        return bs(l[:len(l)/2], t)
    elif res < t:
        return bs(l[len(l)/2:], t)
    else return res

bs(sorted(l), 100)
