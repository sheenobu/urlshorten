package net.sheenobu.urlshorten.storage.internal

import redis.clients.jedis.{Jedis, Transaction, MultiKeyPipelineBase, Response}
import redis.clients.jedis.exceptions._
import redis.clients.jedis.params.Params
import java.time._

import org.osgi.framework._
import org.apache.felix.dm._

import net.sheenobu.urlshorten.storage.{Storage, URL, UnavailableException}

class RedisStorage extends Storage {

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
		var i = 0
		while(jedis.setnx("urls:" + sl,  url.toString()) == 0) {
			sl = generateSlug(url)
			i = i + 1
		}

		jedis.incrBy("urladmin:collisions", i)
		jedis.expire(new String("urls:" + sl), seconds.asInstanceOf[Int])

		new simpleURL(url, sl)
	}

	def delete(slug: String): Boolean = jedis.del("urls:" + slug) != 0

	def generateSlug(url: java.net.URL): String = java.lang.Integer.toString(
		(Math.random() * 500000 + 100000).asInstanceOf[Int] )
}

class ExceptionCatchingStorage extends Storage {
	val redisStorage = new RedisStorage()

	def findBySlug(slug: String): URL = try {
		redisStorage.findBySlug(slug)
	} catch {
		case e: JedisConnectionException => throw new UnavailableException()
	}

	def create(url: java.net.URL, ttl: Int): URL = try {
		redisStorage.create(url, ttl)
	} catch {
		case e: JedisConnectionException => throw new UnavailableException()
	}

	def delete(slug: String): Boolean = try {
		redisStorage.delete(slug)
	} catch {
		case e: JedisConnectionException => throw new UnavailableException()
	}

}

class simpleURL(url: java.net.URL, slug: String) extends URL {
	def GetSlug() = slug
	def GetURL() = url
}

