package net.sheenobu.urlshorten.service.internal.osgi;

import org.osgi.framework.{BundleContext}
import org.apache.felix.dm.{DependencyManager, DependencyActivatorBase}

import net.sheenobu.urlshorten.storage._
import net.sheenobu.urlshorten.service._
import net.sheenobu.urlshorten.service.internal._

class Activator extends DependencyActivatorBase {
	def init(ctx: BundleContext, manager: DependencyManager) {
		val d = createServiceDependency()
			.setService(classOf[Storage])
			.setRequired(true)

		val c = createComponent()
		    .setInterface(classOf[URLShortenService].getName(), null)
			.setImplementation(classOf[URLShortener])
			.add(d)

		manager.add(c)
	}
}
