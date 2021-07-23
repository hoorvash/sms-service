FROM redis:6.2.4
COPY redis.conf /redis/redis.conf
CMD ["redis-server", "/redis/redis.conf", "--bind", "redis", "--port", "6379"]
