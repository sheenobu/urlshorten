package net.sheenobu.urlshorten.command;

import org.osgi.framework.{BundleContext}
import org.apache.felix.dm.{DependencyManager, DependencyActivatorBase}

import net.sheenobu.urlshorten.service._
import net.sheenobu.urlshorten.service.internal._

import org.apache.felix.service.command.CommandProcessor

import collection.JavaConverters._

class Activator extends DependencyActivatorBase {
   def init(ctx: BundleContext, manager: DependencyManager) {

      val props = new java.util.Properties()
	  props.putAll(Map(
		CommandProcessor.COMMAND_SCOPE -> "urlshorten",
		CommandProcessor.COMMAND_FUNCTION -> Array[String]("create", "delete")).asJava)

	  val dep = createServiceDependency()
		.setService(classOf[URLShortenService])
		.setRequired(true)

	  val comp = createComponent()
	    .setInterface(classOf[java.lang.Object].getName(), props)
		.setImplementation(classOf[Commands])
		.add(dep)

      manager.add(comp)
   }

}
