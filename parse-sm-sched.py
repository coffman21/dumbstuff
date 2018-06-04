# coding=utf-8
from bs4 import BeautifulSoup
import requests
from lxml import etree
import re
import ast

URL = "http://www.bmstu.ru/mstu/undergraduate/schedule/table-view/"
for i in range(337,572):
    print("parsed {} page:".format(i))

    init_r = r = requests.get(URL)
    init_r.encoding = 'utf-8'
    r = requests.post(URL, {"id": i, "action": "view"})
    r.encoding = 'utf-8'

    bmstu_rasp = ''.join(r.text.splitlines())

    regexp = '(\$container\.handsontable\(\{\s*data:\s*)(.*)(\,\s*colWidths: )'
    splitted = re.search(regexp, bmstu_rasp).group(2)
    table_list = ast.literal_eval(splitted)
    for tr in table_list:
        for td in tr:
            if "Лабунец" in td:
                print(td)

