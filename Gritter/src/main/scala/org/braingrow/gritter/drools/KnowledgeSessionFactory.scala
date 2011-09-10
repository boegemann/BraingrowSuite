package org.braingrow.gritter.drools

import org.drools.io.ResourceFactory
import org.drools.builder.{ResourceType, KnowledgeBuilderFactory, KnowledgeBuilder}
import org.drools.conf.EventProcessingOption
import org.drools.{KnowledgeBase, KnowledgeBaseFactory}
import org.drools.runtime.StatefulKnowledgeSession
import org.drools.logger.{KnowledgeRuntimeLoggerFactory, KnowledgeRuntimeLogger}

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 12:50
 */

object KnowledgeSessionFactory {
  def createKnowledgeSessionFromClassPathResource(classPath: String, globals: Map[String, Object]) = {
    // Get a new knowledge builder instance
    val kbuilder: KnowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder()

    // Add our ruleset (which will be parsed and compiled) to the knowledge builder
    kbuilder.add(ResourceFactory
      .newClassPathResource(classPath), ResourceType.DRL)

    val config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
    config.setOption(EventProcessingOption.STREAM)

    // Create a new knowledgebase
    val kbase: KnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(config)

    // Add the compiled rule sets and workflows into the knowledgebase
    val packages = kbuilder.getKnowledgePackages();
    if (kbuilder.hasErrors) {
      System.err.println(kbuilder.getErrors());
      System.exit(-1)
    }
    kbase.addKnowledgePackages(packages)

    // Create the knowledge session
    val ksession: StatefulKnowledgeSession = kbase.newStatefulKnowledgeSession()

    // If you want to see what's going on within the Drools Engine just uncomment this line
    //var logger : KnowledgeRuntimeLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession)
    globals.foreach {
      case (key, obj) => {
        ksession.setGlobal("listReceiver", obj)
      }
    }


    // Return the knowledge session
    ksession
  }
}