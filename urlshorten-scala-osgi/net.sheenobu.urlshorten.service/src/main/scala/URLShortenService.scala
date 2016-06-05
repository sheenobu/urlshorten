package net.sheenobu.urlshorten.service.internal

import net.sheenobu.urlshorten.service._

import scala.collection.mutable.{Map, SynchronizedMap, HashMap}

import java.util.concurrent.locks._

class URLShortener extends URLShortenService {
	var data = new HashMap[String, URL]

	def Create(url: java.net.URL,ttlInDays: Int): net.sheenobu.urlshorten.service.URL = {
		return data.synchronized {
			val slug = generateSlug(url)
			val o = new simpleURL(url, slug, ttlInDays)
			data(slug) = o
			return o
		}
	}

	def Delete(url: net.sheenobu.urlshorten.service.URL): Boolean = {
		val slug = url.GetSlug()
		return data.synchronized {
			if(data contains slug){
				data remove slug
				return true
			}
			return false
		}
	}

	def FindBySlug(slug: String): net.sheenobu.urlshorten.service.URL = {
		return data.synchronized {
			return data getOrElse (slug, null)
		}
	}

	def generateSlug(url: java.net.URL): String = {
		var slugNumber = java.lang.Integer.toString( (Math.random() * 500000 + 100000).asInstanceOf[Int] )

		while (data contains slugNumber) {
			slugNumber = java.lang.Integer.toString( (Math.random() * 500000 + 100000).asInstanceOf[Int] )
		}

		return slugNumber
	}
}

class simpleURL(url: java.net.URL, slug: String, ttl: Int) extends URL {
	def GetSlug() = slug
	def GetURL() = url
}


