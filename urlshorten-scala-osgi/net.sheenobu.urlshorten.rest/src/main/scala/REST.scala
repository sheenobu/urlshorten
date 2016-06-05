package net.sheenobu.urlshorten.rest

import javax.ws.rs._
import collection.JavaConverters._
import net.sheenobu.urlshorten.service._
import com.fasterxml.jackson.annotation._

@JsonCreator
case class CreateURL(@JsonProperty("url") url: String, @JsonProperty("ttl") ttl: Int)

@Path("urls")
class URLResource {

	val urlNotFormatted = Map("error" -> "URL not formatted properly").asJava
	def commonError(e: Exception) = Map("error" -> e.toString()).asJava

	var svc: URLShortenService = null

	@POST
	@Produces(Array("application/json"))
	@Consumes(Array("application/json"))
	def createURL(u: CreateURL) = {
		try {
			val url = svc.Create(new java.net.URL(u.url), u.ttl)

			Map(
				"slug" -> url.GetSlug(),
				"url" -> u.url
			).asJava
		}catch{
			case e: java.net.MalformedURLException => urlNotFormatted
		}
	}

	@Path("{slug}")
	@GET
	@Produces(Array("application/json"))
	def getURL(@PathParam("slug") slug: String) = {
		val url = svc.FindBySlug(slug)
		if(url == null) {
			Map(
				"error" -> "slug not found"
			).asJava
		} else {
			Map(
				"slug" -> url.GetSlug(),
				"url" -> url.GetURL().toString()
			).asJava
		}
	}
}
