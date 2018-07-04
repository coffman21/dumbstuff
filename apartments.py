# coding=utf-8
import ast
import re
import requests
from transliterate import translit, get_available_language_codes
from pprint import pprint
from time import sleep

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
    "comission": '(class="e-price-breakdown">)([^<]+)( &nbsp;&nbsp; Залог)',
    "deposit": '( &nbsp;&nbsp; Залог )([^<]+)(\.<\/span>\s*<span class="e-tile)',
    "type": '(<span class="e-tile-type m-max-width text-overflow">\s*<strong>)([^<]+)(<\/strong>)',
    "address": '(<span itemprop=address class="m-blue-link text-overflow m-max-width">)([^<]+)(<\/span>)',
    "descr": '(div itemprop=description class="e-description-text">)(.*?)(<\/div>)',
    "station": '(<img src="\/shared\/tintmetroicon\?color=%[\w]+">\s*<strong class="text-overflow op-max-width">\s*<span>)([^<]+)(<\/span>)',
    "distance": '(<\/span> )([^<]+)(<\/strong>\s*<\/div>\s*<div class="op-tile-number">)',
}

PARSED = {}

def parsePagination(i):
    print("parsed {} page:".format(i))
    new_par = PARAMS
    new_par["Page"] = i
    r = requests.get(URL, new_par)
    r.encoding = 'utf-8'

    main_page = ''.join(r.text.splitlines())

    cur_len = len(PARSED)
    l = len(re.findall(REGS['link'], main_page))

    for apt_num in range(l):
        PARSED[apt_num+cur_len] = {}

    for key in REGS.keys():
        for idx, prop in enumerate(re.findall(REGS[key], main_page)):
            if key == 'link':
                PARSED[cur_len+idx][key] = SITE + prop[1]
            elif key == 'deposit' or key == 'price':
                PARSED[cur_len+idx][key] = "".join(prop[1].split("&#160;")).rstrip(' РУБ')
            elif key == 'type':
                PARSED[cur_len+idx][key] = "".join(prop[1].split("&#178;"))
            elif key == 'comission':
                PARSED[cur_len+idx][key] = prop[1].lstrip('Комиссия ').rstrip('%')
            else:
                PARSED[cur_len+idx][key] = prop[1]
    print(PARSED)

def parse_all():
    for i in range(1,9):
        parsePagination(i)
        sleep(1)

METRO_PARAMS = {}

GET_NAME_QUERY = 'http://metro.mwmoskva.ru/moscow-map/stList.v2.php?term='
"http://metro.mwmoskva.ru/moscow-map/findWay.v2.php?start=fonvizinskaya_ljublinsko-dmitrovskaja&end=cherkizovskaya_sokolnicheskaya"

def get_metro_stations_cost(name):
    pass


parse_all()
pprint(PARSED)
