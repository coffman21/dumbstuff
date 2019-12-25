import vk_api

vk_session = vk_api.VkApi('I WOULD NEVER EVER KEEP', 'SENSITIVE DATA IN PLAINTEXT')
vk_session.auth()

vk = vk_session.get_api()

group_ids_list = [
    "podslushano_park_sokolniki", "alekseevskiy_raion_msk_online"
]

groups = vk.groups.getById(group_ids = group_ids_list, fields = ["id", "name", "members_count"])

res = {}
for group in groups:
    members = []
    print("group: " + group)
    for offset in range(0, group.members_count, 1000):
        print("offset = " + offset)
        fields = ["sex", "bdate", "photo_100"]
        members.append(vk.groups.getMembers(offset, fields, group_id=group.id).items)
    res[group.id] = members

for (id, l) in res.items:
    for entry in l:
        link = "https://vk.com/" + entry.id
        yob = int(entry.bdate[-4:]) if len(entry.bdate) > 8 else 0
        is_photo_ok = "deactivated" in entry.photo_100 or "camera" in entry.photo_100
        if (yob < 2001 and yob > 1969 and entry.sex == 1 and is_photo_ok):
            print(id, link, bdate, entry.first_name, entry.last_name)

# print(res)


