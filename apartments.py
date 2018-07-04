# coding=utf-8
import ast
import re
import requests
from pprint import pprint
from time import sleep
import csv

SITE = "https://www.domofond.ru"
URL = "https://www.domofond.ru/arenda-kvartiry-moskva-c3584"

PARAMS = {
    "PriceTo": "40000",
    "RentalRate": "Month",
    "Rooms": "Two%2CThree",
    "DistanceFromMetro": "UpTo1000m",
    "SearchText": "изолированные"
    #"Page": "2",
}

REGS = {
    "link": '(<object>\s*<a itemprop=sameAs href=")(.*?)(\" aria-label=\")',
    "price": '(itemprop=price class="e-tile-price m-blue-link">)([^<]+)(\.<\/h2>\s*<span class="e-price-breakdown")',
    "comission": '(class="e-price-breakdown">)([^<]+)( &nbsp;&nbsp;)',
    "deposit": '(&nbsp;&nbsp; )([^<]+)(\<\/span>\s*<span class="e-tile)',
    "type": '(<span class="e-tile-type m-max-width text-overflow">\s*<strong>)([^<]+)(<\/strong>)',
    "address": '(<span itemprop=address class="m-blue-link text-overflow m-max-width">)([^<]+)(<\/span>)',
    "descr": '(div itemprop=description class="e-description-text">)(.*?)(<\/div>)',
    "station": '(<img src="\/shared\/tintmetroicon\?color=%[\w]+">\s*<strong class="text-overflow op-max-width">\s*<span>)([^<]+)(<\/span>)',
    "distance": '(<\/span> )([^<]+)(<\/strong>\s*<\/div>\s*<div class="op-tile-number">)',
}

# param: term: Комсомольская
# reply format: [{'label': 'komsomolskaya_sokolnicheskaya', ...}]
GET_NAME_QUERY = 'http://metro.mwmoskva.ru/moscow-map/stList.v2.php'

# params:
# start: fonvizinskaya_ljublinsko-dmitrovskaja
# end: cherkizovskaya_sokolnicheskaya
# reply format: {'time_count': 33, ...}
GET_DISTANCE_QUERY = "http://metro.mwmoskva.ru/moscow-map/findWay.v2.php"

# feel free to edit
FAV_STATIONS = {
    'Комсомольская': 'komsomolskaya_sokolnicheskaya',
    'Университет': 'universitet_sokolnicheskaya',
    'Павелецкая': 'paveletskaya_kolcevaya',
    'Курская': 'kurskaya_arbatsko-pokrovskaya',
    'Пролетарская': 'proletarskaya_tagansko-krasnopresnenskaya',
}

PARSED = []

def parsePagination(i):
    print("parsed {} page:".format(i))
    new_par = PARAMS
    new_par["Page"] = i
    r = send_request(URL, new_par)

    main_page = ''.join(r.text.splitlines())

    cur_len = len(PARSED)
    l = len(re.findall(REGS['link'], main_page))

    for apt_num in range(l):
        PARSED.append({})

    for key in REGS.keys():
        for idx, prop in enumerate(re.findall(REGS[key], main_page)):
            if key == 'link':
                PARSED[cur_len+idx][key] = SITE + prop[1]
            elif key == 'price':
                PARSED[cur_len+idx][key] = "".join(prop[1].split("&#160;")).rstrip(' РУБ')
            elif key == 'deposit':
                if prop[1] == 'Без залога':
                    PARSED[cur_len+idx][key] = '0'
                else:
                    PARSED[cur_len+idx][key] = "".join(prop[1].split("&#160;")).lstrip('Залог ').rstrip(' РУБ.')
            elif key == 'comission':
                if prop[1] == 'Без комиссии':
                    PARSED[cur_len+idx][key] = '0'
                else:
                    PARSED[cur_len+idx][key] = "".join(prop[1].split("&#160;")).lstrip('Комиссия ').rstrip('%')
            elif key == 'type':
                PARSED[cur_len+idx][key] = "".join(prop[1].split("&#178;"))
            elif key == 'comission':
                PARSED[cur_len+idx][key] = prop[1].lstrip('Комиссия ').rstrip('%')
            else:
                PARSED[cur_len+idx][key] = prop[1]

    for apt_num in range(l):
        metro = PARSED[apt_num+cur_len]['station']
        dir_costs = get_metro_stations_cost(metro)
        for k, v in dir_costs.items():
            PARSED[apt_num+cur_len][k] = v
        print(dir_costs.values())
        PARSED[apt_num+cur_len]['sum_time'] = sum((int(_) if type(_) == str else 0 for _ in dir_costs.values()))


def parse_all():
    for i in range(1,9):
        parsePagination(i)
        sleep(1)


def get_metro_stations_cost(name):
    r = send_request(GET_NAME_QUERY, {
        'term': name
    })
    trans_name = r.json()[0]['label']
    etas = {}
    print(name)
    for k, v in FAV_STATIONS.items():
        r = send_request(GET_DISTANCE_QUERY, {
            'start': trans_name,
            'end': v
        })
        eta = r.json()['time_count']
        etas[k] = eta
        sleep(0.1)
    return etas

def send_request(url, params):
    r = requests.get(url, params)
    if r.status_code != 200:
        sleep(1)
        return send_request(url, params)

    else:
        r.encoding = 'utf-8'
        return r


def convert_to_csv(l):
    keys = l[0].keys()
    with open('parsed.csv', 'w') as output_file:
        dict_writer = csv.DictWriter(output_file, keys)
        dict_writer.writeheader()
        dict_writer.writerows(l)

parse_all()
pprint(PARSED)

convert_to_csv(PARSED)

