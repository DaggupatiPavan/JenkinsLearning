// To save all pipeline code into files, run the below script in the Jenkins script console. You will also need to update the file path on line 16 to match your system.

import jenkins.model.Jenkins
import java.nio.file.*

def savePipelineJob(job) {
    def scriptContent

    if (job.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition) {
        scriptContent = job.definition.getScriptPath()
    } else if (job.definition instanceof org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition) {
        scriptContent = job.definition.getScript()
    }

    if (scriptContent) {
        def filePath = "/var/lib/jenkins/jenkins_files/${job.name}.groovy"

        try {
            Files.write(Paths.get(filePath), scriptContent.getBytes())
            println("Job '${job.name}' saved successfully at $filePath")
        } catch (IOException e) {
            println("Error saving job '${job.name}': $e.message")
        }
    } else {
        println("Job '${job.name}' does not have a valid pipeline script.")
    }
}

Jenkins.instance.getAllItems(org.jenkinsci.plugins.workflow.job.WorkflowJob.class).each { job ->
    savePipelineJob(job)
}
