import time
​
import redis
from flask import Flask
​
app = Flask(__name__)
# host='redis',其中redis是service的名称，compose中的服务通信可以用服务名来指定
cache = redis.Redis(host='redis', port=6379)
​
def get_hit_count():
    retries = 5
    while True:
        try:
            return cache.incr('hits')
        except redis.exceptions.ConnectionError as exc:
            if retries == 0:
                raise exc
            retries -= 1
            time.sleep(0.5)
​
@app.route('/')
def hello():
    count = get_hit_count()
    return f'Hello World! I have been seen {count} times.\n'