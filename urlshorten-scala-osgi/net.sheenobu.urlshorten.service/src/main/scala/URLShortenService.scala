package net.sheenobu.urlshorten.service.internal

import net.sheenobu.urlshorten.service._
import net.sheenobu.urlshorten.storage._

class URLShortener extends URLShortenService {
	var storage: Storage = null

	def Create(url: java.net.URL,ttlInDays: Int) = {
		if(ttlInDays == 0) {
			storage.create(url, 5)
		} else {
			storage.create(url, ttlInDays)
		}
	}

	def Delete(url: URL): Boolean = {
		val slug = url.GetSlug()
		storage.delete(slug)
	}

	def FindBySlug(slug: String): URL = {
		return storage.findBySlug(slug)
	}

}

