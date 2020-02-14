import vk_api

vk_session = vk_api.VkApi('I WOULD NEVER EVER KEEP', 'SENSITIVE DATA IN PLAINTEXT')
vk_session.auth()

vk = vk_session.get_api()

group_ids_list = [
    'club186583116' , 'podslushano_vdnkh' , 'podslushano_park_sokolniki' , 'alekseevskiy_raion_msk_online' ,
    'vdnh_online' , 'club32300384' , 'newnogtiru' , 'cvao_msk' , 'best_svao' 
]

groups = vk.groups.getById(group_ids = group_ids_list, fields = ["id", "name", "members_count"])

res = {}
for group in groups:
    members = []
    print("groupId: " + str(group["id"]))
    for offset in range(0, group["members_count"], 1000):
        print("offset = " + str(offset))
        fields = ["sex", "bdate", "photo_100"]
        members.extend(vk.groups.getMembers(offset=offset, fields=fields, group_id=group["id"])["items"])
    res[group["id"]] = members



i = 0
page_ids = []
for (id, l) in res.items():
    for entry in filter(lambda x: "bdate" in x, l):
        page_id = str(entry["id"])
        yob = int(entry["bdate"][-4:]) if len(entry["bdate"]) >= 8 else 0
        is_photo_ok = isinstance(entry["photo_100"], str) and "deactivated" not in entry["photo_100"] and "camera" not in entry["photo_100"]
        if (yob < 2001 and yob > 1969 and "sex"in entry and entry["sex"] == 1 and is_photo_ok):
            page_ids.append(page_id)
            i += 1

with open('users.txt', "w") as f:
    for page_id in set(page_ids):
        f.write(page_id + '\n')

print(len(page_ids))
print(len(set(page_ids)))
