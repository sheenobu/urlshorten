package net.sheenobu.urlshorten.storage.internal

import redis.clients.jedis.{Jedis, Transaction, MultiKeyPipelineBase, Response}
import redis.clients.jedis.params.Params
import java.time._

import org.osgi.framework._
import org.apache.felix.dm._

import net.sheenobu.urlshorten.storage._

class RedisStorage extends net.sheenobu.urlshorten.storage.Storage {

	var jedis: Jedis = new Jedis()

	def findBySlug(slug: String): URL = jedis.get("urls:" + slug) match {
		case null => null
		case v => new simpleURL(new java.net.URL(v), slug)
	}

	def create(url: java.net.URL, ttl: Int): URL = {

		var seconds = (
			LocalDateTime.now().plusDays(ttl).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() -
			Instant.now().getEpochSecond())

		var sl = generateSlug(url)
		while(jedis.setnx("urls:" + sl,  url.toString()) == 0) {
			sl = generateSlug(url)
		}

		jedis.expire(new String("urls:" + sl), seconds.asInstanceOf[Int])

		new simpleURL(url, sl)
	}

	def delete(slug: String): Boolean = jedis.del("urls:" + slug) != 0

	def generateSlug(url: java.net.URL): String = java.lang.Integer.toString(
		(Math.random() * 500000 + 100000).asInstanceOf[Int] )
}

class simpleURL(url: java.net.URL, slug: String) extends URL {
	def GetSlug() = slug
	def GetURL() = url
}


