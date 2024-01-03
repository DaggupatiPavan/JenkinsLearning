// To save all pipeline code into files, run the below script in the Jenkins script console. You will also need to update the file path on line 10 to match your system.

import jenkins.model.*
import java.nio.file.*

def jenkins = Jenkins.instance

jenkins.views.each { view ->
  
  def viewPath = "/var/lib/jenkins/jenkins_files/${view.name}"
  
  new File(viewPath).mkdirs()

  view.items.each { job ->

    if (job instanceof org.jenkinsci.plugins.workflow.job.WorkflowJob) {

      def scriptContent 

      if (job.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition) {
        scriptContent = job.definition.getScriptPath()  
      } else if (job.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition) {
        scriptContent = job.definition.getScript()
      }

      if (scriptContent) {
        def jobPath = "${viewPath}/${job.name}.groovy"
        
        try {
          Files.write(Paths.get(jobPath), scriptContent.getBytes())
          println "Saved job '${job.name}' to ${jobPath}"
        } catch (IOException e) {
          println "Error saving '${job.name}': ${e.message}"
        }
      }
    }
  }
}
