package net.sheenobu.urlshorten.command

import net.sheenobu.urlshorten.service._

class Commands {

	var svc: URLShortenService = null

	def create(url: String, ttl: Int): String = {
		val u = new java.net.URL(url)
		val n = svc.Create(u, ttl)
		return n.GetSlug()
	}

	def delete(slug: String):Unit = {
		var u = svc.FindBySlug(slug)
		if(u == null) {
			System.out.println("Slug not found")
		} else {
			svc.Delete(u)
		}
	}
}
