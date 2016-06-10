package urlshorten

import "net/url"

type Slug string

type URL struct {
	Slug Slug
	URL  url.URL
}
