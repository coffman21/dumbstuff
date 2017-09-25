FROM alpine:3.5

RUN apk add --no-cache python3 python3-dev linux-headers gcc musl-dev && \
	python3 -m ensurepip && \ 
	rm -r /usr/lib/python*/ensurepip && \ 
	pip3 install --upgrade pip setuptools && \ 
	if [ ! -e /usr/bin/pip ]; then ln -s pip3 /usr/bin/pip ; fi && \ 
	rm -r /root/.cache\
	&& pip3 install http://semanchuk.com/philip/sysv_ipc/sysv_ipc-0.7.0.tar.gz \
	&& mkdir /app

WORKDIR /app

COPY app.py /app/

EXPOSE 7777/udp

CMD ["python3", "app.py"]

