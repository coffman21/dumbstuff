import requests as r

URL = 'https://msk.tele2.ru/api/shop/products/numbers/bundles/1/groups/prod1500853'
params = {
    'exclude': '',
    'siteId': 'siteMSK',
}
numbers = []
for i in range(1, 10000, 24):
    params['bundleIndex'] = i
    print(params)
    res = r.get(url=URL, params=params)
    numbers.extend([x['numbers'][0]['number'] for x in res.json()['data'][0]['bundleGroups'][0]['bundles']])

print(*filter(lambda n: len(set(n)) < 5, numbers), sep='\n')

for i in range(0, 100, 11):
    for j in range(0, 10, 1):
        a = ''.join(map(lambda x: str(x), [i, j, i]))
        print(a)
        print(*list(filter(lambda x: a in x, numbers)), sep='\n')

for i in range(0, 100, 11):
    for j in range(0, 10, 1):
        a = ''.join(map(lambda x: str(x), [j, i, j, i]))
        print(a)
        print(*list(filter(lambda x: a in x, numbers)), sep='\n')

for i in range(0, 100, 11):
    for j in range(0, 10000, 1100):
        a = str(i + j).zfill(4)
        print(a)
        print(*list(filter(lambda x: a in x, numbers)), sep='\n')

for n in numbers:
    a = n[4:7]
    if set(n[8:]) == set(a):
        print(n)
