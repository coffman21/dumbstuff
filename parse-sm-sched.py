# coding=utf-8
import ast
import re
import requests
from time import sleep

URL = "http://www.bmstu.ru/mstu/undergraduate/schedule/table-view/"


def parse(i):
    print("parsed {} page:".format(i))

    r = requests.post(URL, {"id": i, "action": "view"})
    r.encoding = 'utf-8'

    bmstu_rasp = ''.join(r.text.splitlines())

    regexp = '(\$container\.handsontable\(\{\s*data:\s*)(.*)(\,\s*colWidths: )'

    splitted = re.search(regexp, bmstu_rasp).group(2)
    if "Лабунец" in splitted:
        table_list = ast.literal_eval(splitted)
        for tr in table_list:
            for td in tr:
                if "Лабунец" in td:
                    print(td)


for i in range(337,572):
    parse(i)

