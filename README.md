# urlshorten

Simple URL shortening service written
in different platforms.

## REST API

All implementations should have the same REST API

 * GET /urls/$slug -> `{ "url": "", "slug": "$slug" }`
 * POST /urls `{ "url": "...", "ttl": $time-to-live-in-days }`
 * GET /u/$slug -> 301 redirect to URL or 404 not found

Errors:

 * 400, `{"error":"URL not formatted properly"}`
 * 404, `{"error":"Slug not found"}`
 * 500, `{"error": "Unkown Error"}`
 * 503, `{"error": "Service Unavailable"}`
 * 429, `{"error":"Too Many Requests"}`

## Redis Schema

All implementations should have the same redis schema and operations.

### Slug Generation

Psuedocode for generating a slug:

```
base64urlencode(int(random(0,1) * 500000 + 100000))
```

base64urlencode is a URL friendly base64 encoding scheme.

NOTE: slug generation will most likely change

### Create URL

Create URL psuedocode for transactionally safe insertion while avoiding collisions:

```
i = 0
for {
	slug = generateSlug()
	if SETNX(urls:$slug, $url) == 1 {
	   EXPIRE(urls:$slug, $ttlInSeconds)
	   break
	}
	i++
}
INCRBY("urladmin:collisions", i)
```
