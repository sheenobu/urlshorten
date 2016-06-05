package net.sheenobu.urlshorten.storage.internal

import net.sheenobu.urlshorten.storage._

import org.osgi.framework._
import org.apache.felix.dm._

class Activator extends DependencyActivatorBase {

	def init(ctx: BundleContext, manager: DependencyManager) = {
		val c = createComponent()
			.setImplementation(classOf[ExceptionCatchingStorage])
			.setInterface(classOf[Storage].getName, null)

		manager.add(c)
	}
}

