package main

import (
	"urlshorten"

	"gopkg.in/redis.v3"
)

func main() {

	st := urlshorten.NewRedisStorage(redis.NewClient(&redis.Options{}))
	rs := urlshorten.NewRestService(st)

	rs.Run()
}
