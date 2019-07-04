import datetime
import subprocess

CMD = 'ffmpeg -y -ss XXX -t 600 -i "https://myownconference.net/2778/277842/1238/1238523/080866bef8d2dba373928a7156ec2fb9/index.m3u8?x=1562246875&xx=Vgkptc9q0iyipCPeU1NN7A" -acodec copy -bsf:a aac_adtstoasc -vcodec copy out_XXX.mp4 '


res = ""
dt = datetime.datetime(2019, 1,1,5,10,0)
for i in range(24):
	res += CMD.replace("XXX", dt.strftime("%H:%M:%S")) + "\n"
	dt += datetime.timedelta(minutes=10)

subprocess.call(res.replace('\n', ' & '), shell=True)

