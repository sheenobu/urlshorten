package net.sheenobu.urlshorten.rest

import org.osgi.framework.BundleContext
import org.apache.felix.dm._

import net.sheenobu.urlshorten.service._

class Activator extends DependencyActivatorBase {

	def init(ctx: BundleContext, manager: DependencyManager) = {

		val d = createServiceDependency()
		  .setService(classOf[URLShortenService])
		  .setRequired(true)

		val c = createComponent()
		  .setInterface(classOf[java.lang.Object].getName, null)
		  .setImplementation(classOf[URLResource])
		  .add(d)

		manager.add(c)
	}

}
