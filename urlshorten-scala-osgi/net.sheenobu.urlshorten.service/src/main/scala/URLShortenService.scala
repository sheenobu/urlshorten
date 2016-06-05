package net.sheenobu.urlshorten.service.internal

import net.sheenobu.urlshorten.service._

class URLShortener extends URLShortenService {
	def Create(url: java.net.URL,ttlInDays: Int): net.sheenobu.urlshorten.service.URL = ???
	def Delete(url: net.sheenobu.urlshorten.service.URL): Boolean = ???
	def FindBySlug(slug: String): net.sheenobu.urlshorten.service.URL = ???
}
