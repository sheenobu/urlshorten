package urlshorten

import (
	"math/rand"
	"net/url"
	"strconv"
	"time"

	"gopkg.in/redis.v3"
)

type RedisStorage struct {
	rc *redis.Client
}

func NewRedisStorage(rc *redis.Client) *RedisStorage {
	return &RedisStorage{
		rc: rc,
	}
}

func (rs *RedisStorage) Load(slug Slug) (*URL, error) {
	b, err := rs.rc.Get("urls:" + string(slug)).Result()
	if err != nil {
		return nil, err
	}

	u, err := url.Parse(b)
	if err != nil {
		return nil, err
	}

	return &URL{
		Slug: slug,
		URL:  *u,
	}, nil
}

func (rs *RedisStorage) Save(u url.URL, ttl time.Duration) (ret *URL, err error) {

	i := int64(0)

	defer func() {
		if err == nil {
			if i > 0 {
				err = rs.rc.IncrBy("urladmin:collisions", i).Err()
			}
		}
	}()

	for {
		slug := rs.generateSlug()

		var ok bool
		if ok, err = rs.rc.SetNX("urls:"+slug, u.String(), 0).Result(); err != nil {
			return
		} else if ok {
			if err = rs.rc.Expire("urls:"+slug, ttl).Err(); err != nil {
				return
			}
			ret = &URL{
				Slug: Slug(slug),
				URL:  u,
			}
			return
		}

		i++
	}
}

func (rs *RedisStorage) generateSlug() string {
	return strconv.Itoa(int(rand.Float64()*500000 + 100000))
}
