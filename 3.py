def max(l):
    if len(l) == 2:
        return l[0] if l[0] > l[1] else l[1]
    else:
        return max(l[1:])

l = [2,3,4,5,6, 2, 3]
print max(l)
