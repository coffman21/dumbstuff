class Table:
    arr = []
    length = 0

    def __init__(self):
        self.arr = [None for _ in range(256)]
        self.length = 256  # in binaries

    # using length of hash in hexes
    def h(self, elem):
        # len(str(length)) used to trim hash sequence by e.g. 2 characters
        # hsh = int(str(hash(elem))[-len(str(self.length)):])
        hsh = abs(hash(elem))
        # e.g. if length of array is 256, so we choose hashes from 512 to 256, then reduce it
        while hsh >= (self.length << 1):
            hsh = hsh >> 1
        print(elem, hsh - self.length)
        return hsh - self.length

    def add(self, elem):
        hsh = self.h(elem)
        # check for overflow
        if (self.arr.count(None))/self.length <= 0.3:
            self.increase()
        if self.arr[hsh] is None:
            self.arr[hsh] = elem
        else:
            # define behavior with lists
            print('collision')

    def rem(self, elem):
        hsh = self.h(elem)
        if isinstance(self.arr[hsh], str):
            self.arr[hsh] = None
        else:
            # define behavior with lists
            print('collision')

    def get(self, elem):
        hsh = self.h(elem)
        if isinstance(self.arr[hsh], str):
            return self.arr[hsh]
        else:
            # define behavior with lists
            print('collision')

    def increase(self):
        self.length = self.length << 1
        new = [None for _ in range(self.length)]
        for i, elem in enumerate(self.arr):
            if isinstance(elem, int):
                hsh = h(elem)
                new[hsh] = elem
            # else: define behavior with lists
        self.arr = new


t = Table()
t.add("qwe")
t.add("asd")
print(t.get("asd"))
t.rem("asd")
print(t.get("asd"))
[t.add(str(i)) for i in range(1, 360)]
print(t.get('321'))
