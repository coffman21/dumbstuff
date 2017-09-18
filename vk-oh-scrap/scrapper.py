import requests
import pprint
from time import sleep
import io

pp = pprint.PrettyPrinter(indent=2)
token = '740a7a2a740a7a2a740a7a2a2d745416bf7740a740a7a2a2db55b504350e03ee8132692'


def return_request(name, token, count, domain, offset):
    # 'https://api.vk.com/method/METHOD_NAME?PARAMETERS & access_token = ACCESS_TOKEN'
    addr = 'https://api.vk.com/method/'
    params = {
        'count': count,
        'offset': offset,
        'domain': domain,
        'access_token': token
    }
    return requests.get(addr+name, params)


gap = 100
for i in range(1, 80001, gap):
    status = 0
    resp = return_request('wall.get', token, gap, 'overhear', i)
    res = resp.json()
    for j in range(1, 101):
        with open('1.txt', 'a') as myfile:
            myfile.write(resp.json()['response'][j]['text']+'\n')
    sleep(0.25)
    if (i-1) % 1000 == 0:
        print(str(i-1) + '-th string parsed...')

print('done')

