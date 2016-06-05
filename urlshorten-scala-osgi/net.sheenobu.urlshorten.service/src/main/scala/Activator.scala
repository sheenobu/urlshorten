package net.sheenobu.urlshorten.service.internal.osgi;

import org.osgi.framework.{BundleContext}
import org.apache.felix.dm.{DependencyManager, DependencyActivatorBase}

import net.sheenobu.urlshorten.service._
import net.sheenobu.urlshorten.service.internal._

class Activator extends DependencyActivatorBase {
   def init(ctx: BundleContext, manager: DependencyManager) {
      manager.add(createComponent()
	    .setInterface(classOf[URLShortenService].getName(), null)
		.setImplementation(classOf[URLShortener]))
   }
}
