package urlshorten

import (
	"net/url"
	"time"
)

type Storage interface {
	Save(u url.URL, ttl time.Duration) (*URL, error)

	Load(slug Slug) (*URL, error)
}
